package br.com.deolhonacamara.scheduler;

import br.com.deolhonacamara.api.dto.ExpenseBodyDto;
import br.com.deolhonacamara.api.model.ExpenseEntity;
import br.com.deolhonacamara.api.model.PoliticianEntity;
import br.com.deolhonacamara.api.model.SyncProgressEntity;
import br.com.deolhonacamara.api.repository.ExpenseRepository;
import br.com.deolhonacamara.api.repository.PoliticianRepository;
import br.com.deolhonacamara.api.service.CamaraDeputadosService;
import br.com.deolhonacamara.api.service.SyncExecutionGate;
import br.com.deolhonacamara.api.service.SyncFlows;
import br.com.deolhonacamara.api.service.SyncHalt;
import br.com.deolhonacamara.api.service.SyncProgressService;
import br.com.deolhonacamara.api.service.SyncRunControl;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
@Log4j2
public class ExpenseSyncJob {

    private final PoliticianRepository politicianRepository;
    private final ExpenseRepository expenseRepository;
    private final CamaraDeputadosService camaraDeputadosService;
    private final SyncProgressService syncProgressService;
    private final SyncExecutionGate syncExecutionGate;
    private final SyncRunControl syncRunControl;

    public void syncExpenses() {
        if (!syncExecutionGate.enter(SyncFlows.EXPENSES)) {
            return;
        }
        log.info("Starting expense synchronization from Câmara API...");

        try {
            // Get or create current execution for Expenses flow
            SyncProgressEntity currentExecution = syncProgressService.getOrCreateCurrentExecution("Expenses");

            // Get all politicians
            var pageable = PageRequest.of(0, 1000);
            var politiciansPage = politicianRepository.findAll(pageable, java.util.Map.of());
            List<PoliticianEntity> politicians = politiciansPage.getContent();

            log.info("Found {} politicians to sync expenses", politicians.size());

            int totalExpenses = currentExecution.getProcessedCount() == null ? 0 : currentExecution.getProcessedCount();
            int errors = currentExecution.getErrorCount() == null ? 0 : currentExecution.getErrorCount();
            int alreadyDone = currentExecution.getCurrentPage() == null ? 0 : currentExecution.getCurrentPage();
            int processedPoliticians = 0;
            int totalPoliticians = politicians.size();

            for (PoliticianEntity politician : politicians) {
                processedPoliticians++;
                if (processedPoliticians <= alreadyDone) {
                    continue;
                }
                syncRunControl.ensureRunning(SyncFlows.EXPENSES);
                try {
                    log.info("Processing politician {}/{}: {} (ID: {})", processedPoliticians, totalPoliticians, politician.getName(), politician.getId());

                    var expensesResponse = camaraDeputadosService.getExpenses(politician.getId(), null, null);

                    if (expensesResponse != null && expensesResponse.getData() != null) {
                        for (ExpenseBodyDto expenseDto : expensesResponse.getData()) {
                            ExpenseEntity expense = ExpenseEntity.builder()
                                    .politicianId(politician.getId())
                                    .year(expenseDto.getYear())
                                    .month(expenseDto.getMonth())
                                    .expenseType(expenseDto.getExpenseType())
                                    .supplier(expenseDto.getSupplier())
                                    .supplierCnpjCpf(expenseDto.getSupplierCnpjCpf())
                                    .documentValue(expenseDto.getDocumentValue())
                                    .netValue(expenseDto.getNetValue())
                                    .glosaValue(expenseDto.getGlosaValue())
                                    .documentDate(expenseDto.getDocumentDate())
                                    .documentUrl(expenseDto.getDocumentUrl())
                                    .documentCode(expenseDto.getDocumentCode())
                                    .batchCode(expenseDto.getBatchCode())
                                    .documentTypeCode(expenseDto.getDocumentTypeCode())
                                    .documentNumber(expenseDto.getDocumentNumber())
                                    .reimbursementNumber(expenseDto.getReimbursementNumber())
                                    .installment(expenseDto.getInstallment())
                                    .documentType(expenseDto.getDocumentType())
                                    .build();
                            expenseRepository.upsertExpense(expense);
                            totalExpenses++;
                        }
                    }

                    syncProgressService.checkpoint(currentExecution, processedPoliticians, totalPoliticians,
                            totalExpenses, errors, String.valueOf(politician.getId()), null, null);

                    if (processedPoliticians % 10 == 0) {
                        log.info("Processed {} politicians, {} expenses synced so far", processedPoliticians, totalExpenses);
                    }
                } catch (Exception e) {
                    errors++;
                    syncProgressService.checkpoint(currentExecution, processedPoliticians, totalPoliticians,
                            totalExpenses, errors, String.valueOf(politician.getId()), null, null);
                    log.error("Error syncing expenses for politician {} (ID: {}): ", politician.getName(), politician.getId(), e);
                }
            }

            syncRunControl.ensureRunning(SyncFlows.EXPENSES);
            syncProgressService.markExecutionCompleted("Expenses", currentExecution.getExecutionId());
            log.info("Expense synchronization completed: {} expenses synced for {} politicians.", totalExpenses, processedPoliticians);

        } catch (SyncHalt halt) {
            syncProgressService.getCurrentProgress("Expenses")
                    .filter(SyncProgressEntity::isRunning)
                    .ifPresent(exec -> syncProgressService.markExecutionHalted("Expenses", exec.getExecutionId(), halt.getStatus()));
            log.info("Expense synchronization {}", halt.getStatus());
        } catch (Exception e) {
            log.error("Error syncing expenses: ", e);
            // Mark execution as failed if something goes wrong
            try {
                SyncProgressEntity currentExecution = syncProgressService.getCurrentProgress("Expenses")
                    .filter(SyncProgressEntity::isRunning)
                    .orElse(null);
                if (currentExecution != null) {
                    syncProgressService.markExecutionFailed("Expenses", currentExecution.getExecutionId());
                }
            } catch (Exception e2) {
                log.error("Error marking execution as failed: ", e2);
            }
        } finally {
            syncExecutionGate.release(SyncFlows.EXPENSES);
        }
    }
}

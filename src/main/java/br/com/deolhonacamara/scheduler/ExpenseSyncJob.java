package br.com.deolhonacamara.scheduler;

import br.com.deolhonacamara.api.dto.ExpenseBodyDto;
import br.com.deolhonacamara.api.model.ExpenseEntity;
import br.com.deolhonacamara.api.model.PoliticianEntity;
import br.com.deolhonacamara.api.model.SyncProgressEntity;
import br.com.deolhonacamara.api.repository.ExpenseRepository;
import br.com.deolhonacamara.api.repository.PoliticianRepository;
import br.com.deolhonacamara.api.service.CamaraDeputadosService;
import br.com.deolhonacamara.api.service.SyncProgressService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.PageRequest;
import org.springframework.scheduling.annotation.Scheduled;
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

    // Runs daily at 01:00 (Brasília time) - after politician sync
    @Scheduled(cron = "0 0 1 * * *", zone = "America/Sao_Paulo")
    public void syncExpenses() {
        log.info("Starting expense synchronization from Câmara API...");

        try {
            // Get or create current execution for Expenses flow
            SyncProgressEntity currentExecution = syncProgressService.getOrCreateCurrentExecution("Expenses");

            // Get all politicians
            var pageable = PageRequest.of(0, 1000);
            var politiciansPage = politicianRepository.findAll(pageable, java.util.Map.of());
            List<PoliticianEntity> politicians = politiciansPage.getContent();

            log.info("Found {} politicians to sync expenses", politicians.size());

            int totalExpenses = 0;
            int processedPoliticians = 0;
            int totalPoliticians = politicians.size();

            // Set total pages (total politicians) on first response for this execution
            if (currentExecution.getTotalPages() == null) {
                syncProgressService.updateTotalPages("Expenses", currentExecution.getExecutionId(), totalPoliticians);
                log.info("Set total pages to {} for expenses execution {}", totalPoliticians, currentExecution.getExecutionId());
            }

            for (PoliticianEntity politician : politicians) {
                try {
                    // Update progress for current politician
                    processedPoliticians++;
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

                    // Persist current page (politician processed)
                    syncProgressService.updateCurrentPage("Expenses", currentExecution.getExecutionId(), processedPoliticians);

                    if (processedPoliticians % 10 == 0) {
                        log.info("Processed {} politicians, {} expenses synced so far", processedPoliticians, totalExpenses);
                    }
                } catch (Exception e) {
                    log.error("Error syncing expenses for politician {} (ID: {}): ", politician.getName(), politician.getId(), e);
                }
            }

            // Mark execution as completed
            syncProgressService.markExecutionCompleted("Expenses", currentExecution.getExecutionId());
            log.info("Expense synchronization completed: {} expenses synced for {} politicians.", totalExpenses, processedPoliticians);

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
        }
    }
}

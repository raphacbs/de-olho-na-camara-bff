package br.com.deolhonacamara.scheduler;

import br.com.deolhonacamara.api.dto.PropositionBodyDto;
import br.com.deolhonacamara.api.dto.PropositionListResponseBodyDto;
import br.com.deolhonacamara.api.dto.PropositionResponseBodyDto;
import br.com.deolhonacamara.api.mapper.Mapper;
import br.com.deolhonacamara.api.model.PoliticianEntity;
import br.com.deolhonacamara.api.model.PropositionEntity;
import br.com.deolhonacamara.api.model.SyncProgressEntity;
import br.com.deolhonacamara.api.repository.PoliticianRepository;
import br.com.deolhonacamara.api.repository.PropositionRepository;
import br.com.deolhonacamara.api.service.CamaraDeputadosService;
import br.com.deolhonacamara.api.service.SyncProgressService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.PageRequest;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.List;
import java.util.LinkedHashMap;
import java.util.Map;

@Component
@RequiredArgsConstructor
@Log4j2
public class PropositionSyncJob {

    private static final String YEARS_TO_SYNC = String.join(
            ",",
            java.util.stream.IntStream.rangeClosed(0, 1)
                    .map(i -> LocalDate.now().getYear() - i)
                    .mapToObj(String::valueOf)
                    .toArray(String[]::new)
    );


    private final PoliticianRepository politicianRepository;
    private final PropositionRepository propositionRepository;
    private final CamaraDeputadosService camaraDeputadosService;
    private final SyncProgressService syncProgressService;
    @Qualifier("syncExecutor")
    private final Executor syncExecutor;

    // configurable period (months) used to compute dataApresentacaoInicio as first day of the month 'periodMonths' months ago
    @Value("${sync.propositions.period.months:1}")
    private int syncPropositionsPeriodMonths;

    // Runs daily at 04:00 (Brasília time)
    @Scheduled(cron = "0 0 4 * * *", zone = "America/Recife")
    public void syncPropositions() {
        log.info("Starting proposition synchronization from Câmara API...");

        try {
            // Get or create current execution for Propositions flow
            SyncProgressEntity currentExecution = syncProgressService.getOrCreateCurrentExecution("Propositions");

            var pageable = PageRequest.of(0, 1000);
            var politiciansPage = politicianRepository.findAll(pageable, java.util.Map.of());
            List<PoliticianEntity> politicians = politiciansPage.getContent();

            log.info("Found {} politicians to sync propositions", politicians.size());

            int processedPoliticians = 0;
            int totalPoliticians = politicians.size();

            for (PoliticianEntity politician : politicians) {
                try {
                    // Update progress for current politician
                    processedPoliticians++;
                    log.info("Processing politician {}/{}: {} (ID: {})", processedPoliticians, totalPoliticians, politician.getName(), politician.getId());

                    processPropositions(politician, currentExecution, 1);

                    // Update current page (politician processed)
                    syncProgressService.updateCurrentPage("Propositions", currentExecution.getExecutionId(), processedPoliticians);

                } catch (Exception e) {
                    log.error("Error syncing propositions for politician {} (ID: {}): ", politician.getName(), politician.getId(), e);
                }
            }

            // Mark execution as completed
            syncProgressService.markExecutionCompleted("Propositions", currentExecution.getExecutionId());
            log.info("Proposition synchronization completed successfully");

        } catch (Exception e) {
            log.error("Error syncing propositions: ", e);
            // Mark execution as failed if something goes wrong
            try {
                syncProgressService.getCurrentProgress("Propositions")
                    .filter(SyncProgressEntity::isRunning)
                    .ifPresent(currentExecution -> syncProgressService.markExecutionFailed("Propositions", currentExecution.getExecutionId()));
            } catch (Exception e2) {
                log.error("Error marking execution as failed: ", e2);
            }
        }
    }

    private PropositionListResponseBodyDto getPropositions(Integer politicianId, Integer page) {

        // calcula o período: primeiro dia do mês (syncPropositionsPeriodMonths) meses atrás até hoje
        LocalDate hoje = LocalDate.now();
        LocalDate inicioPeriodo = hoje.withDayOfMonth(1).minusMonths(Math.max(1, syncPropositionsPeriodMonths));
        String dataInicio = inicioPeriodo.toString(); // formato YYYY-MM-DD
        String dataFim = hoje.toString(); // hoje

        // monta parâmetros em um map e delega ao serviço que aceita Map
        try {
            Map<String, Object> queryParams = new LinkedHashMap<>();
            queryParams.put("ano", YEARS_TO_SYNC);
            queryParams.put("idDeputadoAutor", politicianId);
            queryParams.put("dataApresentacaoInicio", dataInicio);
            queryParams.put("dataApresentacaoFim", dataFim);
            queryParams.put("ordem", "DESC");
            queryParams.put("ordenarPor", "ano");
            queryParams.put("pagina", page);

            return camaraDeputadosService.getPropositions(queryParams);
        } catch (Exception e) {
            log.warn("Erro ao montar/obter proposições para o deputado ID {} na página {} com filtro {}: {}", politicianId, page, "dataApresentacaoInicio=" + dataInicio + "&dataApresentacaoFim=" + dataFim, e.getMessage());
            return null;
        }
    }

    private Integer getLastPageNumber(PropositionListResponseBodyDto response) {
        for (var link : response.getLinks()) {
            Integer lastPage = link.getNumberLastPage();
            if (lastPage != null) {
                return lastPage;
            }
        }
        return 1;
    }

    private void processPropositions(PoliticianEntity politician, SyncProgressEntity currentExecution, Integer page) {
        PropositionListResponseBodyDto propositionsResponse = getPropositions(politician.getId(), page);

        Integer lastPage = propositionsResponse != null && propositionsResponse.getLinks() != null ?
                getLastPageNumber(propositionsResponse) : 1;

        log.info("Processing page {}/{} for politician {} (ID: {})", page, lastPage, politician.getName(), politician.getId());

        // Set total pages on first response for this politician
        if (currentExecution.getTotalPages() == null) {
            syncProgressService.updateTotalPages("Propositions", currentExecution.getExecutionId(), lastPage);
            log.info("Set total pages to {} for propositions execution {}", lastPage, currentExecution.getExecutionId());
        }

        if (propositionsResponse != null && propositionsResponse.getData() != null) {
            log.info("Found {} propositions for politician {} (ID: {}) on page {}", propositionsResponse.getData().size(), politician.getName(), politician.getId(), page);

            // Repository methods are stateless/JdbcTemplate-backed and safe for concurrent usage with the limited executor
            List<CompletableFuture<Void>> tasks = propositionsResponse.getData().stream()
                    .map(propositionDto -> CompletableFuture.runAsync(() ->
                            savePropositionForPolitician(politician, page, propositionDto), syncExecutor))
                    .toList();

            // Wait for all parallel requests to finish before moving to the next page
            CompletableFuture.allOf(tasks.toArray(new CompletableFuture[0])).join();

            if (page < lastPage) {
                log.info("Starting processing of next page {} for politician {} (ID: {})", page + 1, politician.getName(), politician.getId());
                processPropositions(politician, currentExecution, page + 1);
            }
        } else {
            log.info("No proposition data found for politician {} (ID: {}) on page {}, this likely means we've reached the end", politician.getName(), politician.getId(), page);
        }
    }

    private void savePropositionForPolitician(PoliticianEntity politician, Integer page, PropositionBodyDto propositionDto) {
        try {
            PropositionResponseBodyDto propositionFullResponse =
                    camaraDeputadosService.getPropositionsById(propositionDto.getId());

            if (propositionFullResponse == null || propositionFullResponse.getData() == null) {
                log.warn("Empty response when fetching proposition {} for politician {} (ID: {}) on page {}", propositionDto.getId(), politician.getName(), politician.getId(), page);
                return;
            }

            PropositionBodyDto fullPropositionDto = propositionFullResponse.getData();
            PropositionEntity entity = Mapper.INSTANCE.toEntity(fullPropositionDto);

            log.info("Saving proposition ID {} for politician {} (ID: {}) on page {}",
                    entity.getId(), politician.getName(), politician.getId(), page);

            propositionRepository.upsertProposition(entity);
            propositionRepository.linkPoliticianToProposition(politician.getId(), entity.getId());

        } catch (Exception e) {
            log.error("Error saving proposition {} for politician {} (ID: {}): ", propositionDto.getId(), politician.getName(), politician.getId(), e);
        }
    }
}

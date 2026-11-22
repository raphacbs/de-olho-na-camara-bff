package br.com.deolhonacamara.scheduler;

import br.com.deolhonacamara.api.dto.DeputadosBodyDto;
import br.com.deolhonacamara.api.model.PoliticianEntity;
import br.com.deolhonacamara.api.repository.PoliticianRepository;
import br.com.deolhonacamara.api.service.CamaraDeputadosService;
import br.com.deolhonacamara.api.service.SyncExecutionGate;
import br.com.deolhonacamara.api.service.SyncFlows;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
@RequiredArgsConstructor
@Log4j2
public class PoliticianSyncJob {

    private final CamaraDeputadosService camaraDeputadosService;

    private final PoliticianRepository politicianRepository;

    private final SyncExecutionGate syncExecutionGate;

    // Runs daily at 23:00 (Brasília time)
    @Scheduled(cron = "0 0 23 * * *", zone = "America/Recife")
    public void syncPoliticians() {
        if (!syncExecutionGate.enter(SyncFlows.POLITICIANS)) {
            return;
        }
        log.info("Starting politician synchronization from Câmara API...");

        try {
            int page = 1;
            int count = 0;
            while (true) {
                Map<String, Object> params = new HashMap<>();
                params.put("itens", 100);
                params.put("pagina", page);
                var response = camaraDeputadosService.getDeputados(params);
                if (response == null || response.getData() == null || response.getData().isEmpty()) {
                    break;
                }
                for (DeputadosBodyDto deputadosBodyDto : response.getData()) {
                    PoliticianEntity p = PoliticianEntity.builder()
                            .id((Integer) deputadosBodyDto.getId())
                            .name(deputadosBodyDto.getNome())
                            .party(deputadosBodyDto.getSiglaPartido())
                            .partyUri(deputadosBodyDto.getUriPartido())
                            .state(deputadosBodyDto.getSiglaUf())
                            .legislatureId(deputadosBodyDto.getIdLegislatura())
                            .email(deputadosBodyDto.getEmail())
                            .uri(deputadosBodyDto.getUri())
                            .photoUrl(deputadosBodyDto.getUrlFoto())
                            .build();
                    politicianRepository.upsertPolitician(p);
                    count++;
                }
                boolean hasNext = response.getLinks() != null
                        && response.getLinks().stream().anyMatch(link -> "next".equals(link.getRel()));
                if (!hasNext) {
                    break;
                }
                page++;
            }

            log.info("Synchronization completed: {} politicians updated.", count);

        } catch (Exception e) {
            log.error("Error syncing politicians: ", e);
        } finally {
            syncExecutionGate.release(SyncFlows.POLITICIANS);
        }
    }
}

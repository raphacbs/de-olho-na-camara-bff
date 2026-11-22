package br.com.deolhonacamara.scheduler;

import br.com.deolhonacamara.api.dto.DeputadosBodyDto;
import br.com.deolhonacamara.api.model.PoliticianEntity;
import br.com.deolhonacamara.api.model.SyncProgressEntity;
import br.com.deolhonacamara.api.repository.PoliticianRepository;
import br.com.deolhonacamara.api.service.CamaraDeputadosService;
import br.com.deolhonacamara.api.service.SyncExecutionGate;
import br.com.deolhonacamara.api.service.SyncFlows;
import br.com.deolhonacamara.api.service.SyncHalt;
import br.com.deolhonacamara.api.service.SyncProgressService;
import br.com.deolhonacamara.api.service.SyncRunControl;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
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
    private final SyncProgressService syncProgressService;
    private final SyncRunControl syncRunControl;

    public void syncPoliticians() {
        if (!syncExecutionGate.enter(SyncFlows.POLITICIANS)) {
            return;
        }
        log.info("Starting politician synchronization from Câmara API...");
        SyncProgressEntity execution = null;
        try {
            execution = syncProgressService.getOrCreateCurrentExecution(SyncFlows.POLITICIANS);
            int page = execution.getCurrentPage() == null || execution.getCurrentPage() < 1
                    ? 1
                    : execution.getCurrentPage() + 1;
            int count = execution.getProcessedCount() == null ? 0 : execution.getProcessedCount();
            while (true) {
                syncRunControl.ensureRunning(SyncFlows.POLITICIANS);
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
                syncProgressService.checkpoint(execution, page, null, count, null, String.valueOf(page), null, null);
                boolean hasNext = response.getLinks() != null
                        && response.getLinks().stream().anyMatch(link -> "next".equals(link.getRel()));
                if (!hasNext) {
                    break;
                }
                page++;
            }
            syncRunControl.ensureRunning(SyncFlows.POLITICIANS);
            syncProgressService.markExecutionCompleted(SyncFlows.POLITICIANS, execution.getExecutionId());
            log.info("Synchronization completed: {} politicians updated.", count);
        } catch (SyncHalt halt) {
            if (execution != null) {
                syncProgressService.markExecutionHalted(SyncFlows.POLITICIANS, execution.getExecutionId(), halt.getStatus());
            }
            log.info("Politician synchronization {}", halt.getStatus());
        } catch (Exception e) {
            log.error("Error syncing politicians: ", e);
            if (execution != null) {
                syncProgressService.markExecutionFailed(SyncFlows.POLITICIANS, execution.getExecutionId());
            }
        } finally {
            syncExecutionGate.release(SyncFlows.POLITICIANS);
        }
    }
}

package br.com.deolhonacamara.scheduler;

import br.com.deolhonacamara.api.dto.DeputadosBodyDto;
import br.com.deolhonacamara.api.model.PoliticianEntity;
import br.com.deolhonacamara.api.repository.PoliticianRepository;
import br.com.deolhonacamara.api.service.CamaraDeputadosService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Log4j2
public class PoliticianSyncJob {

    private final CamaraDeputadosService camaraDeputadosService;

    private final PoliticianRepository politicianRepository;

    // Runs daily at 23:00 (Brasília time)
    @Scheduled(cron = "0 0 23 * * *", zone = "America/Sao_Paulo")
    public void syncPoliticians() {
        log.info("Starting politician synchronization from Câmara API...");

        try {
            var response = camaraDeputadosService.getDeputados();
            int count = 0;
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
                log.info("Synchronized politician: {} (ID: {})", p.getName(), p.getId());
                count++;
            }

            log.info("Synchronization completed: {} politicians updated.", count);

        } catch (Exception e) {
            log.error("Error syncing politicians: ", e);
        }
    }
}

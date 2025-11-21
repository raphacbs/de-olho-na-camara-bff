package br.com.deolhonacamara.scheduler;

import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;

@Component
@Log4j2
public class PresenceSyncJob {

    /**
     * ATENÇÃO: Job desabilitado porque o endpoint /deputados/{id}/presencas não existe na API REST v2.
     * As presenças estão disponíveis apenas via web service SOAP da Câmara dos Deputados.
     * 
     * Para implementar sincronização de presenças, seria necessário:
     * - Integrar com o web service SOAP: ListarPresencasParlamentar ou ListarPresencasDia
     * - Ou consultar eventos: /eventos/{id}/presencas (mas requer ID do evento, não do deputado)
     */
    // @Scheduled(cron = "0 0 5 * * *", zone = "America/Sao_Paulo")
    public void syncPresence() {
        log.warn("Presence synchronization is disabled: endpoint /deputados/{id}/presencas does not exist in API REST v2");
        log.warn("Presences are only available via SOAP web service. See: https://www2.camara.leg.br/transparencia/dados-abertos/dados-abertos-legislativo/webservices/deputados");
        
        // Job desabilitado - endpoint não existe na API REST v2
        // throw new UnsupportedOperationException("Presence sync is disabled: endpoint does not exist in API REST v2");
    }
}


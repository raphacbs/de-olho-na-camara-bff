package br.com.deolhonacamara.api.service;

import br.com.deolhonacamara.api.config.PropertiesConfig;
import br.com.deolhonacamara.api.dto.*;
import br.com.deolhonacamara.request.HTTPShepherd;
import br.com.deolhonacamara.request.repository.HTTPShepherdRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import java.net.http.HttpClient;
import java.util.HashMap;
import java.util.Map;

@Service
public class CamaraDeputadosService {
    private final HTTPShepherd<Void, DeputadosResponseBodyDto> getDeputadosShepherd;
    private final HttpClient httpClient;
    private final Environment environment;
    private final HTTPShepherdRepository httpShepherdRepository;
    private final PropertiesConfig config;
    private final ObjectMapper objectMapper;

    public CamaraDeputadosService(HttpClient httpClient, Environment environment,
                                  HTTPShepherdRepository httpShepherdRepository,
                                  ObjectMapper objectMapper,
                                  PropertiesConfig config){
        this.httpClient = httpClient;
        this.environment = environment;
        this.httpShepherdRepository = httpShepherdRepository;
        this.config = config;
        this.objectMapper = objectMapper;
        
        this.getDeputadosShepherd = HTTPShepherd.<Void, DeputadosResponseBodyDto>builder(httpClient, environment, DeputadosResponseBodyDto.class, objectMapper)
                .url(config.getApiCamaraBaseUrl())
                .endpoint("/deputados")
                .timeout(config.getTimeout())
                .contentType("application/json")
                .repository(httpShepherdRepository)
                .build();
    }

    public DeputadosResponseBodyDto getDeputados(){
        return getDeputadosShepherd.request();
    }

    public ExpenseResponseBodyDto getExpenses(Integer politicianId, Integer year, Integer month) {
        Map<String, Object> params = new HashMap<>();
        if (year != null) {
            params.put("ano", year);
        }
        if (month != null) {
            params.put("mes", month);
        }
        
        HTTPShepherd<Void, ExpenseResponseBodyDto> shepherd = HTTPShepherd
                .<Void, ExpenseResponseBodyDto>builder(httpClient, environment, ExpenseResponseBodyDto.class, objectMapper)
                .url(config.getApiCamaraBaseUrl())
                .endpoint("/deputados/" + politicianId + "/despesas")
                .timeout(config.getTimeout())
                .contentType("application/json")
                .repository(httpShepherdRepository)
                .build();
        
        return params.isEmpty() ? shepherd.request() : shepherd.request(params);
    }

    public VoteResponseBodyDto getVotes(Integer politicianId) {
        HTTPShepherd<Void, VoteResponseBodyDto> shepherd = HTTPShepherd
                .<Void, VoteResponseBodyDto>builder(httpClient, environment, VoteResponseBodyDto.class, objectMapper)
                .url(config.getApiCamaraBaseUrl())
                .endpoint("/deputados/" + politicianId + "/votacoes")
                .timeout(config.getTimeout())
                .contentType("application/json")
                .repository(httpShepherdRepository)
                .build();
        
        return shepherd.request();
    }


    public SpeechResponseBodyDto getSpeeches(Integer politicianId) {
        HTTPShepherd<Void, SpeechResponseBodyDto> shepherd = HTTPShepherd
                .<Void, SpeechResponseBodyDto>builder(httpClient, environment, SpeechResponseBodyDto.class, objectMapper)
                .url(config.getApiCamaraBaseUrl())
                .endpoint("/deputados/" + politicianId + "/discursos")
                .timeout(config.getTimeout())
                .contentType("application/json")
                .repository(httpShepherdRepository)
                .build();
        
        return shepherd.request();
    }

    public PropositionResponseBodyDto getPropositions(Integer politicianId) {
        HTTPShepherd<Void, PropositionResponseBodyDto> shepherd = HTTPShepherd
                .<Void, PropositionResponseBodyDto>builder(httpClient, environment, PropositionResponseBodyDto.class, objectMapper)
                .url(config.getApiCamaraBaseUrl())
                .endpoint("/deputados/" + politicianId + "/proposicoes")
                .timeout(config.getTimeout())
                .contentType("application/json")
                .repository(httpShepherdRepository)
                .build();
        
        return shepherd.request();
    }

    /**
     * ATENÇÃO: Este endpoint não existe na API REST v2 da Câmara dos Deputados.
     * As presenças estão disponíveis apenas via web service SOAP, não via API REST.
     * 
     * Para obter presenças, seria necessário:
     * - Usar o web service SOAP: ListarPresencasParlamentar ou ListarPresencasDia
     * - Ou consultar eventos: /eventos/{id}/presencas (mas requer ID do evento, não do deputado)
     * 
     * @deprecated Este método não funciona pois o endpoint não existe na API REST
     */
    @Deprecated
    public PresenceResponseBodyDto getPresence(Integer politicianId) {
        // Endpoint não existe na API REST v2
        // As presenças estão disponíveis apenas via web service SOAP
        throw new UnsupportedOperationException(
            "O endpoint /deputados/{id}/presencas não existe na API REST v2 da Câmara dos Deputados. " +
            "As presenças estão disponíveis apenas via web service SOAP."
        );
    }
}

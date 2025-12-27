package br.com.deolhonacamara.api.service;

import br.com.deolhonacamara.api.config.PropertiesConfig;
import br.com.deolhonacamara.api.dto.*;
import br.com.deolhonacamara.request.HTTPShepherd;
import br.com.deolhonacamara.request.repository.HTTPShepherdRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import java.net.http.HttpClient;
import java.time.LocalDate;
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
                                  PropertiesConfig config) {
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

    public DeputadosResponseBodyDto getDeputados() {
        return getDeputadosShepherd.request();
    }

    public DeputadoResponseBodyDto getDeputadoById(Integer politicianId) {
        HTTPShepherd<Void, DeputadoResponseBodyDto> shepherd = HTTPShepherd
                .<Void, DeputadoResponseBodyDto>builder(httpClient, environment, DeputadoResponseBodyDto.class, objectMapper)
                .url(config.getApiCamaraBaseUrl())
                .endpoint("/deputados/" + politicianId)
                .timeout(config.getTimeout())
                .contentType("application/json")
                .repository(httpShepherdRepository)
                .build();

        return shepherd.request();
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

    public VotingResponseBodyDto getVotesByPoliticianId(Integer politicianId) {
        HTTPShepherd<Void, VotingResponseBodyDto> shepherd = HTTPShepherd
                .<Void, VotingResponseBodyDto>builder(httpClient, environment, VotingResponseBodyDto.class, objectMapper)
                .url(config.getApiCamaraBaseUrl())
                .endpoint("/deputados/" + politicianId + "/votacoes")
                .timeout(config.getTimeout())
                .contentType("application/json")
                .repository(httpShepherdRepository)
                .build();

        return shepherd.request();
    }

    /**
     * @param startDate - Data de início (formato: yyyy-MM-dd)
     * @param endDate - Data de fim (formato: yyyy-MM-dd)
     * @return Lista de votações
     */
    public VotingResponseBodyDto getVotingLastMonths(LocalDate startDate, LocalDate endDate) {
        return getVotingLastMonthsWithPage(startDate, endDate, 1);
    }

    /**
     * @param startDate - Data de início (formato: yyyy-MM-dd)
     * @param endDate - Data de fim (formato: yyyy-MM-dd)
     * @param page - Página a ser consultada
     * @return Lista de votações paginada
     */
    public VotingResponseBodyDto getVotingLastMonthsWithPage(LocalDate startDate, LocalDate endDate, Integer page) {
        // validar se as datas são válidas
        if (startDate == null || endDate == null) {
            throw new IllegalArgumentException("As datas de início e fim são obrigatórias");
        }
        if (startDate.isAfter(endDate)) {
            throw new IllegalArgumentException("A data de início não pode ser posterior à data de fim");
        }
        if (page == null || page < 1) {
            page = 1;
        }

        HTTPShepherd<Void, VotingResponseBodyDto> shepherd = HTTPShepherd
                .<Void, VotingResponseBodyDto>builder(httpClient, environment, VotingResponseBodyDto.class, objectMapper)
                .url(config.getApiCamaraBaseUrl())
                .endpoint("/votacoes?dataInicio=" + startDate + "&dataFim=" + endDate + "&ordem=DESC&ordenarPor=dataHoraRegistro&pagina=" + page)
                .timeout(config.getTimeout())
                .contentType("application/json")
                .repository(httpShepherdRepository)
                .build();

        return shepherd.request();
    }

    public VotingByIdResponseBodyDto getVotingById(String voteId) {
        HTTPShepherd<Void, VotingByIdResponseBodyDto> shepherd = HTTPShepherd
                .<Void, VotingByIdResponseBodyDto>builder(httpClient, environment, VotingByIdResponseBodyDto.class, objectMapper)
                .url(config.getApiCamaraBaseUrl())
                .endpoint("/votacoes/" + voteId)
                .timeout(config.getTimeout())
                .contentType("application/json")
                .repository(httpShepherdRepository)
                .build();

        return shepherd.request();
    }

    public VoteResponseBodyDto getVotesInVoting(String votingId) {
        HTTPShepherd<Void, VoteResponseBodyDto> shepherd = HTTPShepherd
                .<Void, VoteResponseBodyDto>builder(httpClient, environment, VoteResponseBodyDto.class, objectMapper)
                .url(config.getApiCamaraBaseUrl())
                .endpoint("/votacoes/" + votingId + "/votos")
                .timeout(config.getTimeout())
                .contentType("application/json")
                .repository(httpShepherdRepository)
                .build();

        return shepherd.request();
    }

    @Deprecated
    public VoteResponseBodyDto getVotesInVotingWithPage(String votingId, Integer page) {
        // Método mantido por compatibilidade, mas não usa paginação pois o endpoint não suporta
        return getVotesInVoting(votingId);
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

    public PropositionListResponseBodyDto getPropositionsWithParams(String params) {
        HTTPShepherd<Void, PropositionListResponseBodyDto> shepherd = HTTPShepherd
                .<Void, PropositionListResponseBodyDto>builder(httpClient, environment, PropositionListResponseBodyDto.class, objectMapper)
                .url(config.getApiCamaraBaseUrl())
                .endpoint("proposicoes?" + params)
                .timeout(config.getTimeout())
                .contentType("application/json")
                .repository(httpShepherdRepository)
                .build();

        return shepherd.request();
    }

    public PropositionResponseBodyDto getPropositionsById(Integer id) {
        HTTPShepherd<Void, PropositionResponseBodyDto> shepherd = HTTPShepherd
                .<Void, PropositionResponseBodyDto>builder(httpClient, environment, PropositionResponseBodyDto.class, objectMapper)
                .url(config.getApiCamaraBaseUrl())
                .endpoint("/proposicoes/+" + id)
                .timeout(config.getTimeout())
                .contentType("application/json")
                .repository(httpShepherdRepository)
                .build();

        return shepherd.request();
    }


    /**
     * ATENÇÃO: Este endpoint não existe na API REST v2 da Câmara dos Deputados.
     * As presenças estão disponíveis apenas via web service SOAP, não via API REST.
     * <p>
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

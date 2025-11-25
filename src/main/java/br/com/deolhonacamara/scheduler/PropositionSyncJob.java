package br.com.deolhonacamara.scheduler;

import br.com.deolhonacamara.api.dto.PropositionBodyDto;
import br.com.deolhonacamara.api.dto.PropositionListResponseBodyDto;
import br.com.deolhonacamara.api.dto.PropositionResponseBodyDto;
import br.com.deolhonacamara.api.mapper.Mapper;
import br.com.deolhonacamara.api.model.PoliticianEntity;
import br.com.deolhonacamara.api.model.PropositionEntity;
import br.com.deolhonacamara.api.repository.PoliticianRepository;
import br.com.deolhonacamara.api.repository.PropositionRepository;
import br.com.deolhonacamara.api.service.CamaraDeputadosService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.PageRequest;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Component
@RequiredArgsConstructor
@Log4j2
public class PropositionSyncJob {

    private static final String YEARS_TO_SYNC = java.util.stream.IntStream.rangeClosed(0, 2)
            .map(i -> LocalDate.now().getYear() - i)
            .boxed()
            .toArray(Integer[]::new)
            .toString().replaceAll("[\\[\\] ]", "");

    private final PoliticianRepository politicianRepository;
    private final PropositionRepository propositionRepository;
    private final CamaraDeputadosService camaraDeputadosService;

    // Runs daily at 04:00 (Brasília time)
    @Scheduled(cron = "0 0 4 * * *", zone = "America/Sao_Paulo")
    public void syncPropositions() {
        log.info("Starting proposition synchronization from Câmara API...");

        try {
            var pageable = PageRequest.of(0, 1000);
            var politiciansPage = politicianRepository.findAll(pageable, java.util.Map.of());
            List<PoliticianEntity> politicians = politiciansPage.getContent();

            log.info("Found {} politicians to sync propositions", politicians.size());

            for (PoliticianEntity politician : politicians) {
                try {
                    processPropositions(politician, 1);
                } catch (Exception e) {
                    log.error("Error syncing propositions for politician {} (ID: {}): ", politician.getName(), politician.getId(), e);
                }
            }

        } catch (Exception e) {
            log.error("Error syncing propositions: ", e);
        }
    }

    private PropositionListResponseBodyDto getPropositions(Integer politicianId, Integer page) {
        String params = "ano=" + YEARS_TO_SYNC + "&idDeputadoAutor=" + politicianId + "&ordem=DESC&ordenarPor=ano&pagina=" + page;
        try {
            return camaraDeputadosService.getPropositionsWithParams(params);
        } catch (Exception e) {
            log.warn("Erro ao obter proposições para o deputado ID {} na página {}: {}", politicianId, page, e.getMessage());
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

    private void processPropositions(PoliticianEntity politician, Integer page) {
        PropositionListResponseBodyDto propositionsResponse = getPropositions(politician.getId(), page);
        log.info("Processando página {}/{}", page,
                propositionsResponse != null && propositionsResponse.getLinks() != null ?
                        getLastPageNumber(propositionsResponse) : 1);
        log.info("Total de proposições encontradas para o deputado {} (ID: {}): {} na página {}",
                politician.getName(),
                politician.getId(),
                propositionsResponse != null && propositionsResponse.getData() != null ? propositionsResponse.getData().size() : 0,
                page
        );
        log.info("Processando proposições para o deputado {} (ID: {}) na página {}",
                politician.getName(),
                politician.getId(),
                page
        );
        if (propositionsResponse != null && propositionsResponse.getData() != null) {
            for (PropositionBodyDto propositionDto : propositionsResponse.getData()) {

                PropositionResponseBodyDto propositionFullResponse =
                        camaraDeputadosService.getPropositionsById(propositionDto.getId());

                PropositionBodyDto fullPropositionDto = propositionFullResponse.getData();
                PropositionEntity entity = Mapper.INSTANCE.toEntity(fullPropositionDto);

                propositionRepository.upsertProposition(entity);
                propositionRepository.linkPoliticianToProposition(politician.getId(), entity.getId());
            }
            Integer lastPage = getLastPageNumber(propositionsResponse);
            if (page < lastPage) {
                processPropositions(politician, page + 1);
            }
        }
    }
}


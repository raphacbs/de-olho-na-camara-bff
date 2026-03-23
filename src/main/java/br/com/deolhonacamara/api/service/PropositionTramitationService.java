package br.com.deolhonacamara.api.service;

import br.com.deolhonacamara.api.dto.PropositionTramitationDto;
import br.com.deolhonacamara.api.model.PropositionTramitationEntity;
import br.com.deolhonacamara.api.repository.PropositionTramitationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PropositionTramitationService {

    private final PropositionTramitationRepository repository;

    public List<PropositionTramitationDto> getLatestTramitation(Integer propositionId, int limit) {
        return repository.findLatestByPropositionId(propositionId, limit)
                .stream()
                .map(this::fromEntity)
                .collect(Collectors.toList());
    }

    public List<PropositionTramitationDto> getTramitation(Integer propositionId, LocalDateTime start, LocalDateTime end, int page, int size) {
        return repository.findByPropositionIdWithDateFilter(propositionId, start, end, page * size, size)
                .stream()
                .map(this::fromEntity)
                .collect(Collectors.toList());
    }

    public Integer countTramitation(Integer propositionId, LocalDateTime start, LocalDateTime end) {
        return repository.countByPropositionIdWithDateFilter(propositionId, start, end);
    }

    public void upsertTramitationEntities(List<PropositionTramitationEntity> list) {
        if (list == null || list.isEmpty()) return;
        repository.upsertTramitation(list);
    }

    private PropositionTramitationDto fromEntity(PropositionTramitationEntity e) {
        return new PropositionTramitationDto(
                e.getDateTime(),
                e.getSequence(),
                e.getOrgAcronym(),
                e.getOrgUri(),
                e.getLastReporterUri(),
                e.getRegime(),
                e.getTramitationDescription(),
                e.getTramitationTypeCode(),
                e.getSituationDescription(),
                e.getSituationCode(),
                e.getDispatch(),
                e.getUrl(),
                e.getScope(),
                e.getAppreciation()
        );
    }
}

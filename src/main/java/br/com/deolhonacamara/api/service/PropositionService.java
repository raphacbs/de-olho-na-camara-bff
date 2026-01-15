package br.com.deolhonacamara.api.service;

import br.com.deolhonacamara.api.model.PageResponse;
import br.com.deolhonacamara.api.model.PoliticianDTO;
import br.com.deolhonacamara.api.model.PropositionDTO;
import br.com.deolhonacamara.api.model.PropositionEntity;
import br.com.deolhonacamara.api.model.input.PropositionInput;
import br.com.deolhonacamara.api.model.screen.PropositionScreen;
import br.com.deolhonacamara.api.repository.PropositionRepository;
import lombok.RequiredArgsConstructor;
import net.coelho.deolhonacamara.api.model.PropositionDto;
import net.coelho.deolhonacamara.api.model.PropositionResponseDTO;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PropositionService {

    private final PropositionRepository repository;

    public PropositionResponseDTO getByPoliticianId(PropositionInput input) {
        var pageable = PageRequest.of(input.getPage(), input.getSizePage());
        PageResponse<PropositionEntity> pageRes = repository.findByPoliticianId(input, pageable);
        List<PropositionDto> list = pageRes.getContent().stream()
                .map(this::fromEntityToApiDto)
                .collect(Collectors.toList());

        var responseDto = new PropositionResponseDTO();
        responseDto.data(list);
        responseDto.total((int) pageRes.getTotalElements());
        responseDto.page(pageRes.getNumber());
        responseDto.totalPages(pageRes.getTotalPages());
        responseDto.sizePage(pageRes.getSize());

        return responseDto;
    }

    public List<PropositionEntity> getLatestPropositions(int limit) {
        return repository.findLatestPropositions(limit);
    }

    public List<PropositionScreen> getLatestPropositionsScreen(int limit) {
        return repository.findLatestPropositionsScreen(limit);
    }

    public List<PropositionScreen> getFilteredPropositionsScreen(String politico, List<String> tipos, List<String> statuses, LocalDate dataInicio, LocalDate dataFim, int limit) {
        return repository.findFilteredPropositionsScreen(politico, tipos, statuses, dataInicio, dataFim, limit);
    }

    public PropositionResponseDTO getFilteredPropositions(String politico, List<String> tipos, List<String> statuses,
                                                          LocalDate dataInicio, LocalDate dataFim, int page, int size) {
        var pageable = PageRequest.of(page, size);
        PageResponse<PropositionEntity> pageRes = repository.findFilteredPropositions(
                politico, tipos, statuses, dataInicio, dataFim, pageable);
        List<PropositionDto> list = pageRes.getContent().stream()
                .map(this::fromEntityToApiDto)
                .collect(Collectors.toList());

        var responseDto = new PropositionResponseDTO();
        responseDto.data(list);
        responseDto.total((int) pageRes.getTotalElements());
        responseDto.page(pageRes.getNumber());
        responseDto.totalPages(pageRes.getTotalPages());
        responseDto.sizePage(pageRes.getSize());

        return responseDto;
    }

    public PropositionResponseDTO getFilteredPropositions(Map<String, Object> filters) {
        PageResponse<PropositionDTO> pageRes = repository.findFilteredPropositionsByMap(filters);
        List<PropositionDto> list = pageRes.getContent().stream()
                .map(this::toApiDto)
                .collect(Collectors.toList());

        var responseDto = new PropositionResponseDTO();
        responseDto.data(list);
        responseDto.total((int) pageRes.getTotalElements());
        responseDto.page(pageRes.getNumber());
        responseDto.totalPages(pageRes.getTotalPages());
        responseDto.sizePage(pageRes.getSize());

        return responseDto;
    }

    private PropositionDto fromEntityToApiDto(PropositionEntity p) {
        if (p == null) {
            return null;
        }
        var dto = new PropositionDto();
        dto.setId(p.getId());
        dto.setUri(p.getUri());
        dto.setType(p.getType());
        dto.setCodeType(p.getCodeType());
        dto.setNumber(p.getNumber());
        dto.setYear(p.getYear());
        dto.setSummary(p.getSummary());
        dto.setDetailedSummary(p.getDetailedSummary());
        if (p.getPresentationDate() != null) {
            dto.setPresentationDate(p.getPresentationDate().toLocalDate());
        }
        if (p.getStatusDateTime() != null) {
            dto.setStatusDateTime(p.getStatusDateTime().toString());
        }
        dto.setStatusLastReporterUri(p.getStatusLastReporterUri());
        dto.setStatusTramitationDescription(p.getStatusTramitationDescription());
        dto.setStatusTramitationTypeCode(p.getStatusTramitationTypeCode());
        dto.setStatusSituationDescription(p.getStatusSituationDescription());
        dto.setStatusSituationCode(p.getStatusSituationCode());
        dto.setStatusDispatch(p.getStatusDispatch());
        dto.setStatusUrl(p.getStatusUrl());
        dto.setStatusScope(p.getStatusScope());
        dto.setStatusAppreciation(p.getStatusAppreciation());
        dto.setUriOrgaoNumerador(p.getUriOrgaoNumerador());
        dto.setUriAutores(p.getUriAutores());
        dto.setTypeDescription(p.getTypeDescription());
        dto.setKeywords(p.getKeywords());
        dto.setUriPropPrincipal(p.getUriPropPrincipal());
        dto.setUriPropAnterior(p.getUriPropAnterior());
        dto.setUriPropPosterior(p.getUriPropPosterior());
        dto.setUrlInteiroTeor(p.getUrlInteiroTeor());
        dto.setUrnFinal(p.getUrnFinal());
        dto.setText(p.getText());
        dto.setJustification(p.getJustification());
        if (p.getCreatedAt() != null) {
            dto.setCreatedAt(p.getCreatedAt().toString());
        }
        if (p.getUpdatedAt() != null) {
            dto.setUpdatedAt(p.getUpdatedAt().toString());
        }
        // Politician is not available in PropositionEntity, so it will be null
        return dto;
    }

    private PropositionDto toApiDto(PropositionDTO p) {
        if (p == null) {
            return null;
        }
        var dto = new PropositionDto();
        dto.setId(p.getId().intValue());
        dto.setUri(p.getUri());
        dto.setType(p.getType());
        dto.setCodeType(p.getCodeType());
        dto.setNumber(p.getNumber());
        dto.setYear(p.getYear());
        dto.setSummary(p.getSummary());
        dto.setDetailedSummary(p.getDetailedSummary());
        if (p.getPresentationDate() != null) {
            dto.setPresentationDate(p.getPresentationDate());
        }
        if (p.getStatusDateTime() != null) {
            dto.setStatusDateTime(p.getStatusDateTime().toString());
        }
        dto.setStatusLastReporterUri(p.getStatusLastReporterUri());
        dto.setStatusTramitationDescription(p.getStatusTramitationDescription());
        dto.setStatusTramitationTypeCode(p.getStatusTramitationTypeCode());
        dto.setStatusSituationDescription(p.getStatusSituationDescription());
        dto.setStatusSituationCode(p.getStatusSituationCode());
        dto.setStatusDispatch(p.getStatusDispatch());
        dto.setStatusUrl(p.getStatusUrl());
        dto.setStatusScope(p.getStatusScope());
        dto.setStatusAppreciation(p.getStatusAppreciation());
        dto.setUriOrgaoNumerador(p.getUriOrgaoNumerador());
        dto.setUriAutores(p.getUriAutores());
        dto.setTypeDescription(p.getTypeDescription());
        dto.setKeywords(p.getKeywords());
        dto.setUriPropPrincipal(p.getUriPropPrincipal());
        dto.setUriPropAnterior(p.getUriPropAnterior());
        dto.setUriPropPosterior(p.getUriPropPosterior());
        dto.setUrlInteiroTeor(p.getUrlInteiroTeor());
        dto.setUrnFinal(p.getUrnFinal());
        dto.setText(p.getText());
        dto.setJustification(p.getJustification());
        if (p.getCreatedAt() != null) {
            dto.setCreatedAt(p.getCreatedAt().toString());
        }
        if (p.getUpdatedAt() != null) {
            dto.setUpdatedAt(p.getUpdatedAt().toString());
        }
        dto.setStatus(p.getStatus());
        if (p.getPoliticians() != null) {
            dto.setPoliticians(p.getPoliticians().stream().map(this::toApiDto).collect(Collectors.toList()));
        }
        return dto;
    }

    private net.coelho.deolhonacamara.api.model.PoliticianDto toApiDto(PoliticianDTO p) {
        if (p == null) {
            return null;
        }
        var dto = new net.coelho.deolhonacamara.api.model.PoliticianDto();
        dto.setId(p.getId().intValue());
        dto.setName(p.getName());
        dto.setParty(p.getParty());
        dto.setPhotoUrl(p.getPhotoUrl());
        dto.setState(p.getState());
        return dto;
    }
}

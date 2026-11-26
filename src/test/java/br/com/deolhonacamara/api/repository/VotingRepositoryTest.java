package br.com.deolhonacamara.api.repository;

import br.com.deolhonacamara.api.service.CamaraDeputadosService;
import net.coelho.deolhonacamara.api.model.VotingWithVotesResponseDTO;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class VotingRepositoryTest {

    @Mock
    private NamedParameterJdbcTemplate jdbcTemplate;

    @Mock
    private PoliticianRepository politicianRepository;

    @Mock
    private CamaraDeputadosService camaraDeputadosService;

    @Test
    void emptyListingReturnsTotalZero() {
        when(jdbcTemplate.query(anyString(), anyMap(), any(RowMapper.class))).thenReturn(List.of());

        VotingRepository repository = new VotingRepository(jdbcTemplate, politicianRepository, camaraDeputadosService);
        VotingWithVotesResponseDTO response = repository.findVotingsWithVotes(PageRequest.of(0, 20), null, null, false);

        assertTrue(response.getData().isEmpty());
        assertEquals(0, response.getTotal());
        assertEquals(0, response.getTotalPages());
        assertEquals(0, response.getPage());
    }
}

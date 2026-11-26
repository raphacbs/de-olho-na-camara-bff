package br.com.deolhonacamara.api.service;

import br.com.deolhonacamara.api.repository.PoliticianVoteRepository;
import br.com.deolhonacamara.api.repository.VotingRepository;
import net.coelho.deolhonacamara.api.model.PoliticianVoteWithPropositionResponseDTO;
import net.coelho.deolhonacamara.api.model.VotingWithVotesResponseDTO;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Pageable;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class VotingServiceTest {

    @Mock
    private VotingRepository votingRepository;

    @Mock
    private PoliticianVoteRepository politicianVoteRepository;

    @InjectMocks
    private VotingService votingService;

    @Test
    void pageZeroAndPageOneQueryDistinctOffsets() {
        when(votingRepository.findVotingsWithVotes(any(), any(), any(), anyBoolean()))
                .thenReturn(new VotingWithVotesResponseDTO());
        when(politicianVoteRepository.findVotesWithPropositionByPoliticianId(any(), any()))
                .thenReturn(new PoliticianVoteWithPropositionResponseDTO());

        votingService.getVotingsWithVotes(0, 20, null, null, false);
        votingService.getVotingsWithVotes(1, 20, null, null, false);
        votingService.getPoliticianVotesWithProposition(10, 0, 20);
        votingService.getPoliticianVotesWithProposition(10, 1, 20);

        ArgumentCaptor<Pageable> votingsPage = ArgumentCaptor.forClass(Pageable.class);
        verify(votingRepository, times(2)).findVotingsWithVotes(votingsPage.capture(), isNull(), isNull(), eq(false));
        assertEquals(0, votingsPage.getAllValues().get(0).getOffset());
        assertEquals(20, votingsPage.getAllValues().get(1).getOffset());
        assertNotEquals(votingsPage.getAllValues().get(0).getOffset(), votingsPage.getAllValues().get(1).getOffset());

        ArgumentCaptor<Pageable> votesPage = ArgumentCaptor.forClass(Pageable.class);
        verify(politicianVoteRepository, times(2)).findVotesWithPropositionByPoliticianId(eq(10), votesPage.capture());
        assertEquals(0, votesPage.getAllValues().get(0).getOffset());
        assertEquals(20, votesPage.getAllValues().get(1).getOffset());
    }
}

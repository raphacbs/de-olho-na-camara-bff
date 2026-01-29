package br.com.deolhonacamara.api.service;

import br.com.deolhonacamara.api.repository.DashboardRepository;
import lombok.RequiredArgsConstructor;
import net.coelho.deolhonacamara.api.model.DashboardStatsDto;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DashboardService {

    private final DashboardRepository repository;

    public DashboardStatsDto getStats(UUID userId) {
        var stats = new DashboardStatsDto();
        stats.setTotalPropositions(repository.countTotalPropositions().intValue());
        stats.setTotalExpenses(repository.countTotalExpenses().intValue());
        stats.setTotalVotes(repository.countTotalVotes().intValue());
        stats.setTotalPoliticians(repository.countTotalPoliticians().intValue());
        stats.setTotalFollowing(repository.countTotalFollowing(userId).intValue());
        return stats;
    }

    public DashboardStatsDto getStats(UUID userId, int year) {
        var stats = new DashboardStatsDto();
        stats.setTotalPropositions(repository.countTotalPropositions(year).intValue());
        stats.setTotalExpenses(repository.countTotalExpenses(year).intValue());
        stats.setTotalVotes(repository.countTotalVotes().intValue());
        stats.setTotalPoliticians(repository.countTotalPoliticians().intValue());
        stats.setTotalFollowing(repository.countTotalFollowing(userId).intValue());
        return stats;
    }
}

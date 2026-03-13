package com.worldcupstats.api.teams;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class TeamsServiceTest {

    @Test
    void returnsTeamsFromRepository() {
        TeamsRepository teamsRepository = mock(TeamsRepository.class);

        when(teamsRepository.findAll()).thenReturn(List.of(
                new TeamDto("bra", "Brazil", "BRA"),
                new TeamDto("ger", "Germany", "GER")
        ));

        TeamsService teamsService = new TeamsService(teamsRepository);

        List<TeamDto> result = teamsService.getTeams();

        assertThat(result).hasSize(2);
        assertThat(result.get(0).id()).isEqualTo("bra");
        assertThat(result.get(0).name()).isEqualTo("Brazil");
        assertThat(result.get(0).code()).isEqualTo("BRA");
    }
}
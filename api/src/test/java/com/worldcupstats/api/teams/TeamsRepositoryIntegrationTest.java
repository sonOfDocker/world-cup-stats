package com.worldcupstats.api.teams;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("local")
@Tag("integration")
class TeamsRepositoryIntegrationTest {

    @Autowired
    private TeamsRepository teamsRepository;

    @Test
    void shouldRetrieveTeamsFromDatabase() {
        // Given initial data from V2 migration exists
        
        // When
        List<TeamDto> teams = teamsRepository.findAll();
        
        // Then
        assertThat(teamsRepository).isInstanceOf(DbTeamsRepository.class);
        assertThat(teams).isNotEmpty();
        assertThat(teams).extracting(TeamDto::code)
                .contains("BRA", "GER", "ARG", "FRA");
    }
}

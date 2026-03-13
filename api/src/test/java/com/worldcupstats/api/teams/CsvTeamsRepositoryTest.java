package com.worldcupstats.api.teams;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class CsvTeamsRepositoryTest {

    @Test
    void readsTeamsFromWorldCupCsv() {
        String csvPath = "../../data/raw/kaggle/fifa_world_cup_1930_2022_all_matches.csv";
        CsvTeamsRepository repository = new CsvTeamsRepository(csvPath);

        List<TeamDto> teams = repository.findAll();

        assertThat(teams).isNotEmpty();
        assertThat(teams).anyMatch(team -> 
            team.code().equals("BRA") && team.name().equals("Brazil")
        );
        assertThat(teams).anyMatch(team -> 
            team.code().equals("ARG") && team.name().equals("Argentina")
        );
        assertThat(teams).anyMatch(team -> 
            team.code().equals("GER") && team.name().equals("Germany")
        );
        
        // Teams should be unique (no duplicates)
        List<String> codes = teams.stream().map(TeamDto::code).toList();
        assertThat(codes).doesNotHaveDuplicates();
        
        // Teams should be sorted by code
        assertThat(codes).isSorted();
    }
}

package com.worldcupstats.api.teams;

import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
class InMemoryTeamsRepository implements TeamsRepository {

    @Override
    public List<TeamDto> findAll() {
        return List.of(
                new TeamDto("bra", "Brazil", "BRA"),
                new TeamDto("ger", "Germany", "GER"),
                new TeamDto("arg", "Argentina", "ARG"),
                new TeamDto("fra", "France", "FRA")
        );
    }
}
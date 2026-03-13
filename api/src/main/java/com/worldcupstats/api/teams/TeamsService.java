package com.worldcupstats.api.teams;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
class TeamsService {

    private final TeamsRepository teamsRepository;

    TeamsService(TeamsRepository teamsRepository) {
        this.teamsRepository = teamsRepository;
    }

    List<TeamDto> getTeams() {
        return teamsRepository.findAll();
    }
}
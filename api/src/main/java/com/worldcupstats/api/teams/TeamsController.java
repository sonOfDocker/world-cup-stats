package com.worldcupstats.api.teams;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class TeamsController {

    private final TeamsService teamsService;

    TeamsController(TeamsService teamsService) {
        this.teamsService = teamsService;
    }

    @GetMapping(value = "/api/v1/teams", produces = MediaType.APPLICATION_JSON_VALUE)
    List<TeamDto> teams() {
        return teamsService.getTeams();
    }

}

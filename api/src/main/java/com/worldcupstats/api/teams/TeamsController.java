package com.worldcupstats.api.teams;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class TeamsController {

    @GetMapping(value = "/api/v1/teams", produces = MediaType.APPLICATION_JSON_VALUE)
    List<TeamDto> teams() {
        return List.of(
                new TeamDto("bra", "Brazil", "BRA"),
                new TeamDto("eng", "England", "ENG"),
                new TeamDto("ger", "Germany", "GER"),
                new TeamDto("ita", "Italy", "ITA"),
                new TeamDto("esp", "Spain", "ESP"),
                new TeamDto("uru", "Uruguay", "URU"),
                new TeamDto("fra", "France", "FRA")
        );
    }

}

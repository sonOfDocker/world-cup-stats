package com.worldcupstats.api.teams;

import java.util.List;

public interface TeamsRepository {

    List<TeamDto> findAll();

}
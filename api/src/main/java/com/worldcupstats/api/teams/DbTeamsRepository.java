package com.worldcupstats.api.teams;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import java.util.Comparator;
import java.util.List;

@Repository
@Profile("local")
class DbTeamsRepository implements TeamsRepository {

    private final JpaTeamRepository jpaTeamRepository;

    DbTeamsRepository(JpaTeamRepository jpaTeamRepository) {
        this.jpaTeamRepository = jpaTeamRepository;
    }

    @Override
    public List<TeamDto> findAll() {
        return jpaTeamRepository.findAll().stream()
                .map(entity -> new TeamDto(entity.getId(), entity.getName(), entity.getCode()))
                .sorted(Comparator.comparing(TeamDto::code))
                .toList();
    }
}

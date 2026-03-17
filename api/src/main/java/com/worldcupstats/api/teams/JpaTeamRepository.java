package com.worldcupstats.api.teams;

import org.springframework.data.jpa.repository.JpaRepository;

interface JpaTeamRepository extends JpaRepository<TeamEntity, String> {
}

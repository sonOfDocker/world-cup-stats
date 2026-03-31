package com.worldcupstats.api.ingestion.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface JpaMatchRepository extends JpaRepository<MatchEntity, Long> {
    Optional<MatchEntity> findBySourceId(String sourceId);
    boolean existsBySourceId(String sourceId);
}

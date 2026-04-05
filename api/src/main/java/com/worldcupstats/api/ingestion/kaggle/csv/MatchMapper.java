package com.worldcupstats.api.ingestion.kaggle.csv;

import com.worldcupstats.api.canonical.Match;

/**
 * Service to map raw CSV rows to canonical Match entities.
 */
public interface MatchMapper {
    /**
     * Maps a raw CSV row to a canonical Match entity.
     * @param row the raw CSV row
     * @return the canonical Match entity
     */
    Match mapToMatch(KaggleMatchCsvRow row);
}

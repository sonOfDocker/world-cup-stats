package com.worldcupstats.api.canonical;

import java.time.LocalDate;
import java.time.LocalTime;

public record Match(
        String id,
        Tournament tournament,
        Stage stage,
        Stadium stadium,
        LocalDate matchDate,
        LocalTime matchTime,
        Team homeTeam,
        Team awayTeam,
        Integer homeScore,
        Integer awayScore,
        MatchResult result,
        boolean draw,
        boolean extraTime,
        boolean penaltyShootout,
        Integer homePenaltyScore,
        Integer awayPenaltyScore
) {
}
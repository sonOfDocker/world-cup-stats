package com.worldcupstats.api.canonical;

public record Match(
        String matchId,
        String sourceId,
        Integer tournamentYear,
        String kickoffDatetime,
        String tournamentRound,
        Venue venue,
        Team homeTeam,
        Team awayTeam,
        Integer homeGoals,
        Integer awayGoals,
        MatchResult result,
        String scoreDisplay,
        boolean draw,
        boolean extraTimePlayed,
        boolean decidedByPenalties,
        Integer homePenaltyScore,
        Integer awayPenaltyScore,
        String penaltyScoreDisplay,
        Integer attendance
) {
}
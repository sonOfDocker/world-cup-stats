package com.worldcupstats.api.ingestion.csv;

/**
 * Represents a single raw row from the World Cup matches CSV.
 * Column names are preserved from the source file for traceability.
 */
public record WorldCupMatchCsvRow(
    String keyId,
    String tournamentId,
    String tournamentName,
    String matchId,
    String matchName,
    String stageName,
    String groupName,
    String groupStage,
    String knockoutStage,
    String replayed,
    String replay,
    String matchDate,
    String matchTime,
    String stadiumId,
    String stadiumName,
    String cityName,
    String countryName,
    String homeTeamId,
    String homeTeamName,
    String homeTeamCode,
    String awayTeamId,
    String awayTeamName,
    String awayTeamCode,
    String score,
    String homeTeamScore,
    String awayTeamScore,
    String homeTeamScoreMargin,
    String awayTeamScoreMargin,
    String extraTime,
    String penaltyShootout,
    String scorePenalties,
    String homeTeamScorePenalties,
    String awayTeamScorePenalties,
    String result,
    String homeTeamWin,
    String awayTeamWin,
    String draw
) {
}

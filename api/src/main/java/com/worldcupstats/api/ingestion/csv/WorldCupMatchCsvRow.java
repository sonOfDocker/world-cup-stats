package com.worldcupstats.api.ingestion.csv;

/**
 * Represents a single raw row from the World Cup matches CSV.
 * Column names are preserved from the source file for traceability.
 * Mapping from CSV header to record component:
 * - "Key Id" -> keyId
 * - "Tournament Id" -> tournamentId
 * - "tournament Name" -> tournamentName
 * - "Match Id" -> matchId
 * - "Match Name" -> matchName
 * - "Stage Name" -> stageName
 * - "Group Name" -> groupName
 * - "Group Stage" -> groupStage
 * - "Knockout Stage" -> knockoutStage
 * - "Replayed" -> replayed
 * - "Replay" -> replay
 * - "Match Date" -> matchDate
 * - "Match Time" -> matchTime
 * - "Stadium Id" -> stadiumId
 * - "Stadium Name" -> stadiumName
 * - "City Name" -> cityName
 * - "Country Name" -> countryName
 * - "Home Team Id" -> homeTeamId
 * - "Home Team Name" -> homeTeamName
 * - "Home Team Code" -> homeTeamCode
 * - "Away Team Id" -> awayTeamId
 * - "Away Team Name" -> awayTeamName
 * - "Away Team Code" -> awayTeamCode
 * - "Score" -> score
 * - "Home Team Score" -> homeTeamScore
 * - "Away Team Score" -> awayTeamScore
 * - "Home Team Score Margin" -> homeTeamScoreMargin
 * - "Away Team Score Margin" -> awayTeamScoreMargin
 * - "Extra Time" -> extraTime
 * - "Penalty Shootout" -> penaltyShootout
 * - "Score Penalties" -> scorePenalties
 * - "Home Team Score Penalties" -> homeTeamScorePenalties
 * - "Away Team Score Penalties" -> awayTeamScorePenalties
 * - "Result" -> result
 * - "Home Team Win" -> homeTeamWin
 * - "Away Team Win" -> awayTeamWin
 * - "Draw" -> draw
 * - "Attendance" -> attendance
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
    String draw,
    String attendance
) {
}

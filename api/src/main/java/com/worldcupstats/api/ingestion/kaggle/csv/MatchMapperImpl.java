package com.worldcupstats.api.ingestion.kaggle.csv;

import com.worldcupstats.api.canonical.Match;
import com.worldcupstats.api.canonical.MatchResult;
import com.worldcupstats.api.canonical.Team;
import com.worldcupstats.api.canonical.Venue;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class MatchMapperImpl implements MatchMapper {

    private static final DateTimeFormatter CSV_DATE_FORMATTER = DateTimeFormatter.ofPattern("M/d/yyyy");
    private static final Pattern TOURNAMENT_YEAR_PATTERN = Pattern.compile("^(\\d{4})");

    @Override
    public Match mapToMatch(KaggleMatchCsvRow row) {
        if (row == null) {
            return null;
        }

        Integer homeGoals = parseSafeInteger(row.homeTeamScore());
        Integer awayGoals = parseSafeInteger(row.awayTeamScore());
        Integer homePenaltyScore = parseSafeInteger(row.homeTeamScorePenalties());
        Integer awayPenaltyScore = parseSafeInteger(row.awayTeamScorePenalties());

        // Note: Full construction of Tournament and Stage canonical objects is deferred to 
        // downstream deduplication/enrichment services (see story #20). 
        // This mapper extracts core attributes for initial canonical representation.
        return new Match(
                null, // matchId (internal, deferred)
                row.matchId(),
                extractYear(row.tournamentName()),
                formatKickoffDatetime(row.matchDate(), row.matchTime()),
                row.stageName(),
                new Venue(
                        null, // venueId (internal, deferred)
                        row.stadiumName(),
                        row.cityName(),
                        row.countryName()
                ),
                new Team(
                        null, // teamId (internal, deferred)
                        row.homeTeamName(),
                        row.homeTeamCode()
                ),
                new Team(
                        null, // teamId (internal, deferred)
                        row.awayTeamName(),
                        row.awayTeamCode()
                ),
                homeGoals,
                awayGoals,
                determineResult(homeGoals, awayGoals, homePenaltyScore, awayPenaltyScore),
                row.score(),
                "1".equals(row.draw()),
                "1".equals(row.extraTime()),
                "1".equals(row.penaltyShootout()),
                homePenaltyScore,
                awayPenaltyScore,
                row.scorePenalties(),
                parseSafeInteger(row.attendance())
        );
    }

    private Integer extractYear(String tournamentName) {
        if (tournamentName == null) {
            return null;
        }
        Matcher matcher = TOURNAMENT_YEAR_PATTERN.matcher(tournamentName);
        if (matcher.find()) {
            return Integer.parseInt(matcher.group(1));
        }
        return null;
    }

    private String formatKickoffDatetime(String matchDate, String matchTime) {
        if (matchDate == null || matchDate.isBlank()) {
            return null;
        }
        try {
            LocalDate date = LocalDate.parse(matchDate, CSV_DATE_FORMATTER);
            String time = (matchTime == null || matchTime.isBlank()) ? "00:00" : matchTime;
            // Assuming time is in HH:mm format in CSV.
            // Requirement says ISO-8601: YYYY-MM-DDTHH:mm:ss
            return date.format(DateTimeFormatter.ISO_LOCAL_DATE) + "T" + time + ":00";
        } catch (Exception e) {
            return null;
        }
    }

    private MatchResult determineResult(Integer homeGoals, Integer awayGoals, Integer homePenalties, Integer awayPenalties) {
        if (homeGoals == null || awayGoals == null) {
            return null;
        }
        if (homeGoals > awayGoals) {
            return MatchResult.HOME_WIN;
        }
        if (awayGoals > homeGoals) {
            return MatchResult.AWAY_WIN;
        }
        
        // If goals are equal, check penalties if they exist
        if (homePenalties != null && awayPenalties != null) {
            if (homePenalties > awayPenalties) {
                return MatchResult.HOME_WIN;
            }
            if (awayPenalties > homePenalties) {
                return MatchResult.AWAY_WIN;
            }
        }
        
        return MatchResult.DRAW;
    }

    private Integer parseSafeInteger(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }
        try {
            return Integer.parseInt(value.trim());
        } catch (NumberFormatException e) {
            return null;
        }
    }
}

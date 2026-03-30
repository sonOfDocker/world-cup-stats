package com.worldcupstats.api.ingestion.csv;

import com.worldcupstats.api.canonical.Match;
import com.worldcupstats.api.canonical.MatchResult;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class MatchMapperTest {

    private final MatchMapper mapper = new MatchMapperImpl();

    @Test
    void shouldMapBasicMatchData() {
        WorldCupMatchCsvRow row = createBaseRow()
                .withMatchId("M-1930-01")
                .withTournamentName("1930 FIFA World Cup")
                .withMatchDate("7/13/1930")
                .withMatchTime("15:00")
                .withStageName("group stage")
                .withStadiumName("Estadio Pocitos")
                .withCityName("Montevideo")
                .withCountryName("Uruguay")
                .withHomeTeamName("France")
                .withHomeTeamCode("FRA")
                .withAwayTeamName("Mexico")
                .withAwayTeamCode("MEX")
                .withHomeTeamScore("4")
                .withAwayTeamScore("1")
                .withScore("4–1")
                .withAttendance("4444")
                .build();

        Match match = mapper.mapToMatch(row);

        assertThat(match.sourceId()).isEqualTo("M-1930-01");
        assertThat(match.tournamentYear()).isEqualTo(1930);
        assertThat(match.kickoffDatetime()).isEqualTo("1930-07-13T15:00:00");
        assertThat(match.tournamentRound()).isEqualTo("group stage");
        
        assertThat(match.venue().stadiumName()).isEqualTo("Estadio Pocitos");
        assertThat(match.venue().city()).isEqualTo("Montevideo");
        assertThat(match.venue().country()).isEqualTo("Uruguay");

        assertThat(match.homeTeam().name()).isEqualTo("France");
        assertThat(match.homeTeam().fifaCode()).isEqualTo("FRA");
        assertThat(match.awayTeam().name()).isEqualTo("Mexico");
        assertThat(match.awayTeam().fifaCode()).isEqualTo("MEX");

        assertThat(match.homeGoals()).isEqualTo(4);
        assertThat(match.awayGoals()).isEqualTo(1);
        assertThat(match.result()).isEqualTo(MatchResult.HOME_WIN);
        assertThat(match.scoreDisplay()).isEqualTo("4–1");
        assertThat(match.attendance()).isEqualTo(4444);
    }

    @Test
    void shouldHandleDraw() {
        WorldCupMatchCsvRow row = createBaseRow()
                .withHomeTeamScore("1")
                .withAwayTeamScore("1")
                .withDraw("1")
                .build();

        Match match = mapper.mapToMatch(row);

        assertThat(match.result()).isEqualTo(MatchResult.DRAW);
        assertThat(match.draw()).isTrue();
    }

    @Test
    void shouldHandleAwayWin() {
        WorldCupMatchCsvRow row = createBaseRow()
                .withHomeTeamScore("1")
                .withAwayTeamScore("2")
                .build();

        Match match = mapper.mapToMatch(row);

        assertThat(match.result()).isEqualTo(MatchResult.AWAY_WIN);
    }

    @Test
    void shouldHandlePenalties() {
        WorldCupMatchCsvRow row = createBaseRow()
                .withHomeTeamScore("1")
                .withAwayTeamScore("1")
                .withPenaltyShootout("1")
                .withHomeTeamScorePenalties("4")
                .withAwayTeamScorePenalties("3")
                .withScorePenalties("4-3")
                .build();

        Match match = mapper.mapToMatch(row);

        assertThat(match.result()).isEqualTo(MatchResult.HOME_WIN);
        assertThat(match.decidedByPenalties()).isTrue();
        assertThat(match.homePenaltyScore()).isEqualTo(4);
        assertThat(match.awayPenaltyScore()).isEqualTo(3);
        assertThat(match.penaltyScoreDisplay()).isEqualTo("4-3");
    }

    @Test
    void shouldHandleExtraTime() {
        WorldCupMatchCsvRow row = createBaseRow()
                .withExtraTime("1")
                .build();

        Match match = mapper.mapToMatch(row);

        assertThat(match.extraTimePlayed()).isTrue();
    }

    @Test
    void shouldHandleMissingTime() {
        WorldCupMatchCsvRow row = createBaseRow()
                .withMatchDate("7/13/1930")
                .withMatchTime("")
                .build();

        Match match = mapper.mapToMatch(row);

        assertThat(match.kickoffDatetime()).isEqualTo("1930-07-13T00:00:00");
    }

    @Test
    void shouldHandleNullValues() {
        assertThat(mapper.mapToMatch(null)).isNull();
    }

    private RowBuilder createBaseRow() {
        return new RowBuilder();
    }

    private static class RowBuilder {
        private String keyId = "1";
        private String tournamentId = "WC-1930";
        private String tournamentName = "1930 FIFA World Cup";
        private String matchId = "M-1930-01";
        private String matchName = "France v Mexico";
        private String stageName = "group stage";
        private String groupName = "Group 1";
        private String groupStage = "1";
        private String knockoutStage = "0";
        private String replayed = "0";
        private String replay = "0";
        private String matchDate = "7/13/1930";
        private String matchTime = "15:00";
        private String stadiumId = "S-193";
        private String stadiumName = "Estadio Pocitos";
        private String cityName = "Montevideo";
        private String countryName = "Uruguay";
        private String homeTeamId = "T-28";
        private String homeTeamName = "France";
        private String homeTeamCode = "FRA";
        private String awayTeamId = "T-44";
        private String awayTeamName = "Mexico";
        private String awayTeamCode = "MEX";
        private String score = "4–1";
        private String homeTeamScore = "4";
        private String awayTeamScore = "1";
        private String homeTeamScoreMargin = "3";
        private String awayTeamScoreMargin = "-3";
        private String extraTime = "0";
        private String penaltyShootout = "0";
        private String scorePenalties = "0-0";
        private String homeTeamScorePenalties = "0";
        private String awayTeamScorePenalties = "0";
        private String result = "home team win";
        private String homeTeamWin = "1";
        private String awayTeamWin = "0";
        private String draw = "0";
        private String attendance = "4444";

        public RowBuilder withMatchId(String matchId) { this.matchId = matchId; return this; }
        public RowBuilder withTournamentName(String tournamentName) { this.tournamentName = tournamentName; return this; }
        public RowBuilder withMatchDate(String matchDate) { this.matchDate = matchDate; return this; }
        public RowBuilder withMatchTime(String matchTime) { this.matchTime = matchTime; return this; }
        public RowBuilder withStageName(String stageName) { this.stageName = stageName; return this; }
        public RowBuilder withStadiumName(String stadiumName) { this.stadiumName = stadiumName; return this; }
        public RowBuilder withCityName(String cityName) { this.cityName = cityName; return this; }
        public RowBuilder withCountryName(String countryName) { this.countryName = countryName; return this; }
        public RowBuilder withHomeTeamName(String homeTeamName) { this.homeTeamName = homeTeamName; return this; }
        public RowBuilder withHomeTeamCode(String homeTeamCode) { this.homeTeamCode = homeTeamCode; return this; }
        public RowBuilder withAwayTeamName(String awayTeamName) { this.awayTeamName = awayTeamName; return this; }
        public RowBuilder withAwayTeamCode(String awayTeamCode) { this.awayTeamCode = awayTeamCode; return this; }
        public RowBuilder withHomeTeamScore(String homeTeamScore) { this.homeTeamScore = homeTeamScore; return this; }
        public RowBuilder withAwayTeamScore(String awayTeamScore) { this.awayTeamScore = awayTeamScore; return this; }
        public RowBuilder withScore(String score) { this.score = score; return this; }
        public RowBuilder withDraw(String draw) { this.draw = draw; return this; }
        public RowBuilder withPenaltyShootout(String penaltyShootout) { this.penaltyShootout = penaltyShootout; return this; }
        public RowBuilder withHomeTeamScorePenalties(String homeTeamScorePenalties) { this.homeTeamScorePenalties = homeTeamScorePenalties; return this; }
        public RowBuilder withAwayTeamScorePenalties(String awayTeamScorePenalties) { this.awayTeamScorePenalties = awayTeamScorePenalties; return this; }
        public RowBuilder withScorePenalties(String scorePenalties) { this.scorePenalties = scorePenalties; return this; }
        public RowBuilder withExtraTime(String extraTime) { this.extraTime = extraTime; return this; }
        public RowBuilder withAttendance(String attendance) { this.attendance = attendance; return this; }

        public WorldCupMatchCsvRow build() {
            return new WorldCupMatchCsvRow(
                    keyId, tournamentId, tournamentName, matchId, matchName, stageName, groupName, groupStage, knockoutStage,
                    replayed, replay, matchDate, matchTime, stadiumId, stadiumName, cityName, countryName,
                    homeTeamId, homeTeamName, homeTeamCode, awayTeamId, awayTeamName, awayTeamCode,
                    score, homeTeamScore, awayTeamScore, homeTeamScoreMargin, awayTeamScoreMargin,
                    extraTime, penaltyShootout, scorePenalties, homeTeamScorePenalties, awayTeamScorePenalties,
                    result, homeTeamWin, awayTeamWin, draw, attendance
            );
        }
    }
}

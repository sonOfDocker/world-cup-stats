package com.worldcupstats.api.ingestion.kaggle.persistence;

import com.worldcupstats.api.canonical.MatchResult;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "matches")
public class MatchEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "source_id", nullable = false, unique = true)
    private String sourceId;

    @Column(name = "tournament_year")
    private Integer tournamentYear;

    @Column(name = "kickoff_datetime")
    private String kickoffDatetime;

    @Column(name = "tournament_round")
    private String tournamentRound;

    @Column(name = "stadium_name")
    private String stadiumName;

    @Column(name = "city")
    private String city;

    @Column(name = "country")
    private String country;

    @Column(name = "home_team_id")
    private String homeTeamId;

    @Column(name = "home_team_name")
    private String homeTeamName;

    @Column(name = "home_team_code")
    private String homeTeamCode;

    @Column(name = "away_team_id")
    private String awayTeamId;

    @Column(name = "away_team_name")
    private String awayTeamName;

    @Column(name = "away_team_code")
    private String awayTeamCode;

    @Column(name = "home_goals")
    private Integer homeGoals;

    @Column(name = "away_goals")
    private Integer awayGoals;

    @Enumerated(EnumType.STRING)
    @Column(name = "match_result")
    private MatchResult matchResult;

    @Column(name = "score_display")
    private String scoreDisplay;

    @Column(name = "is_draw")
    private boolean draw;

    @Column(name = "is_extra_time")
    private boolean extraTime;

    @Column(name = "is_penalty_shootout")
    private boolean penaltyShootout;

    @Column(name = "home_penalty_score")
    private Integer homePenaltyScore;

    @Column(name = "away_penalty_score")
    private Integer awayPenaltyScore;

    @Column(name = "penalty_score_display")
    private String penaltyScoreDisplay;

    @Column(name = "attendance")
    private Integer attendance;

    protected MatchEntity() {
    }

    public MatchEntity(String sourceId) {
        this.sourceId = sourceId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSourceId() {
        return sourceId;
    }

    public void setSourceId(String sourceId) {
        this.sourceId = sourceId;
    }

    public Integer getTournamentYear() {
        return tournamentYear;
    }

    public void setTournamentYear(Integer tournamentYear) {
        this.tournamentYear = tournamentYear;
    }

    public String getKickoffDatetime() {
        return kickoffDatetime;
    }

    public void setKickoffDatetime(String kickoffDatetime) {
        this.kickoffDatetime = kickoffDatetime;
    }

    public String getTournamentRound() {
        return tournamentRound;
    }

    public void setTournamentRound(String tournamentRound) {
        this.tournamentRound = tournamentRound;
    }

    public String getStadiumName() {
        return stadiumName;
    }

    public void setStadiumName(String stadiumName) {
        this.stadiumName = stadiumName;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getHomeTeamId() {
        return homeTeamId;
    }

    public void setHomeTeamId(String homeTeamId) {
        this.homeTeamId = homeTeamId;
    }

    public String getHomeTeamName() {
        return homeTeamName;
    }

    public void setHomeTeamName(String homeTeamName) {
        this.homeTeamName = homeTeamName;
    }

    public String getHomeTeamCode() {
        return homeTeamCode;
    }

    public void setHomeTeamCode(String homeTeamCode) {
        this.homeTeamCode = homeTeamCode;
    }

    public String getAwayTeamId() {
        return awayTeamId;
    }

    public void setAwayTeamId(String awayTeamId) {
        this.awayTeamId = awayTeamId;
    }

    public String getAwayTeamName() {
        return awayTeamName;
    }

    public void setAwayTeamName(String awayTeamName) {
        this.awayTeamName = awayTeamName;
    }

    public String getAwayTeamCode() {
        return awayTeamCode;
    }

    public void setAwayTeamCode(String awayTeamCode) {
        this.awayTeamCode = awayTeamCode;
    }

    public Integer getHomeGoals() {
        return homeGoals;
    }

    public void setHomeGoals(Integer homeGoals) {
        this.homeGoals = homeGoals;
    }

    public Integer getAwayGoals() {
        return awayGoals;
    }

    public void setAwayGoals(Integer awayGoals) {
        this.awayGoals = awayGoals;
    }

    public MatchResult getMatchResult() {
        return matchResult;
    }

    public void setMatchResult(MatchResult matchResult) {
        this.matchResult = matchResult;
    }

    public String getScoreDisplay() {
        return scoreDisplay;
    }

    public void setScoreDisplay(String scoreDisplay) {
        this.scoreDisplay = scoreDisplay;
    }

    public boolean isDraw() {
        return draw;
    }

    public void setDraw(boolean draw) {
        this.draw = draw;
    }

    public boolean isExtraTime() {
        return extraTime;
    }

    public void setExtraTime(boolean extraTime) {
        this.extraTime = extraTime;
    }

    public boolean isPenaltyShootout() {
        return penaltyShootout;
    }

    public void setPenaltyShootout(boolean penaltyShootout) {
        this.penaltyShootout = penaltyShootout;
    }

    public Integer getHomePenaltyScore() {
        return homePenaltyScore;
    }

    public void setHomePenaltyScore(Integer homePenaltyScore) {
        this.homePenaltyScore = homePenaltyScore;
    }

    public Integer getAwayPenaltyScore() {
        return awayPenaltyScore;
    }

    public void setAwayPenaltyScore(Integer awayPenaltyScore) {
        this.awayPenaltyScore = awayPenaltyScore;
    }

    public String getPenaltyScoreDisplay() {
        return penaltyScoreDisplay;
    }

    public void setPenaltyScoreDisplay(String penaltyScoreDisplay) {
        this.penaltyScoreDisplay = penaltyScoreDisplay;
    }

    public Integer getAttendance() {
        return attendance;
    }

    public void setAttendance(Integer attendance) {
        this.attendance = attendance;
    }
}

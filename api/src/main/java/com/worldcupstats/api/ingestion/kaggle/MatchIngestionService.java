package com.worldcupstats.api.ingestion.kaggle;

import com.worldcupstats.api.canonical.Match;
import com.worldcupstats.api.ingestion.kaggle.csv.KaggleWorldCupCsvParser;
import com.worldcupstats.api.ingestion.kaggle.csv.MatchMapper;
import com.worldcupstats.api.ingestion.kaggle.csv.KaggleMatchCsvRow;
import com.worldcupstats.api.ingestion.kaggle.persistence.JpaMatchRepository;
import com.worldcupstats.api.ingestion.kaggle.persistence.MatchEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Service
public class MatchIngestionService {

    private static final Logger log = LoggerFactory.getLogger(MatchIngestionService.class);

    private final KaggleWorldCupCsvParser kaggleWorldCupCsvParser;
    private final MatchMapper matchMapper;
    private final JpaMatchRepository matchRepository;

    public MatchIngestionService(KaggleWorldCupCsvParser kaggleWorldCupCsvParser,
                                 MatchMapper matchMapper,
                                 JpaMatchRepository matchRepository) {
        this.kaggleWorldCupCsvParser = kaggleWorldCupCsvParser;
        this.matchMapper = matchMapper;
        this.matchRepository = matchRepository;
    }

    @Transactional
    public IngestionResult ingestFromStream(InputStream inputStream) throws IOException {
        log.info("Starting match ingestion from stream...");
        
        try (Reader reader = new InputStreamReader(inputStream, StandardCharsets.UTF_8)) {
            List<KaggleMatchCsvRow> rows = kaggleWorldCupCsvParser.parseMatches(reader);
            log.info("Parsed {} rows from CSV", rows.size());
            
            int createdCount = 0;
            int skippedCount = 0;
            
            for (KaggleMatchCsvRow row : rows) {
                if (matchRepository.existsBySourceId(row.matchId())) {
                    log.debug("Skipping match with sourceId {} (already exists)", row.matchId());
                    skippedCount++;
                    continue;
                }
                
                Match match = matchMapper.mapToMatch(row);
                MatchEntity entity = mapToEntity(match);
                matchRepository.save(entity);
                createdCount++;
            }
            
            log.info("Ingestion completed. Created: {}, Skipped: {}", createdCount, skippedCount);
            return new IngestionResult(createdCount, skippedCount);
        }
    }

    private MatchEntity mapToEntity(Match match) {
        MatchEntity entity = new MatchEntity(match.sourceId());
        entity.setTournamentYear(match.tournamentYear());
        entity.setKickoffDatetime(match.kickoffDatetime());
        entity.setTournamentRound(match.tournamentRound());
        
        if (match.venue() != null) {
            entity.setStadiumName(match.venue().stadiumName());
            entity.setCity(match.venue().city());
            entity.setCountry(match.venue().country());
        }
        
        if (match.homeTeam() != null) {
            entity.setHomeTeamId(match.homeTeam().teamId());
            entity.setHomeTeamName(match.homeTeam().name());
            entity.setHomeTeamCode(match.homeTeam().fifaCode());
        }
        
        if (match.awayTeam() != null) {
            entity.setAwayTeamId(match.awayTeam().teamId());
            entity.setAwayTeamName(match.awayTeam().name());
            entity.setAwayTeamCode(match.awayTeam().fifaCode());
        }
        
        entity.setHomeGoals(match.homeGoals());
        entity.setAwayGoals(match.awayGoals());
        entity.setMatchResult(match.result());
        entity.setScoreDisplay(match.scoreDisplay());
        entity.setDraw(match.draw());
        entity.setExtraTime(match.extraTimePlayed());
        entity.setPenaltyShootout(match.decidedByPenalties());
        entity.setHomePenaltyScore(match.homePenaltyScore());
        entity.setAwayPenaltyScore(match.awayPenaltyScore());
        entity.setPenaltyScoreDisplay(match.penaltyScoreDisplay());
        entity.setAttendance(match.attendance());
        
        return entity;
    }

    public record IngestionResult(int createdCount, int skippedCount) { }
}

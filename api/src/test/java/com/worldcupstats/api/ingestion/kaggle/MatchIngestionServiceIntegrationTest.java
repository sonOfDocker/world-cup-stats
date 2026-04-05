package com.worldcupstats.api.ingestion.kaggle;

import com.worldcupstats.api.ingestion.kaggle.persistence.JpaMatchRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

import static org.assertj.core.api.Assertions.assertThat;

@Tag("integration")
@SpringBootTest
@ActiveProfiles("test")
@Transactional
class MatchIngestionServiceIntegrationTest {

    @Autowired
    private MatchIngestionService ingestionService;

    @Autowired
    private JpaMatchRepository matchRepository;

    private static final String CSV_CONTENT = """
            Key Id,Tournament Id,tournament Name,Match Id,Match Name,Stage Name,Group Name,Group Stage,Knockout Stage,Replayed,Replay,Match Date,Match Time,Stadium Id,Stadium Name,City Name,Country Name,Home Team Id,Home Team Name,Home Team Code,Away Team Id,Away Team Name,Away Team Code,Score,Home Team Score,Away Team Score,Home Team Score Margin,Away Team Score Margin,Extra Time,Penalty Shootout,Score Penalties,Home Team Score Penalties,Away Team Score Penalties,Result,Home Team Win,Away Team Win,Draw,Attendance
            1,1930,1930 FIFA World Cup,M-1930-01,France v Mexico,Group Stage,Group 1,1,0,0,0,7/13/1930,15:00,S-1,Estadio Pocitos,Montevideo,Uruguay,T-1,France,FRA,T-2,Mexico,MEX,4-1,4,1,3,-3,0,0,0-0,0,0,HOME_WIN,1,0,0,4444
            2,1930,1930 FIFA World Cup,M-1930-02,USA v Belgium,Group Stage,Group 4,1,0,0,0,7/13/1930,15:00,S-2,Estadio Parque Central,Montevideo,Uruguay,T-3,USA,USA,T-4,Belgium,BEL,3-0,3,0,3,-3,0,0,0-0,0,0,HOME_WIN,1,0,0,18346
            """;

    @BeforeEach
    void setUp() {
        matchRepository.deleteAll();
    }

    @Test
    void shouldIngestMatchesFromCsv() throws IOException {
        ByteArrayInputStream inputStream = new ByteArrayInputStream(CSV_CONTENT.getBytes(StandardCharsets.UTF_8));
        
        MatchIngestionService.IngestionResult result = ingestionService.ingestFromStream(inputStream);
        
        assertThat(result.createdCount()).isEqualTo(2);
        assertThat(result.skippedCount()).isEqualTo(0);
        assertThat(matchRepository.count()).isEqualTo(2);
        
        assertThat(matchRepository.existsBySourceId("M-1930-01")).isTrue();
        assertThat(matchRepository.existsBySourceId("M-1930-02")).isTrue();
    }

    @Test
    void shouldBeIdempotentWhenReIngestingSameData() throws IOException {
        // First ingestion
        ingestionService.ingestFromStream(new ByteArrayInputStream(CSV_CONTENT.getBytes(StandardCharsets.UTF_8)));
        assertThat(matchRepository.count()).isEqualTo(2);
        
        // Second ingestion with same data
        MatchIngestionService.IngestionResult result = ingestionService.ingestFromStream(new ByteArrayInputStream(CSV_CONTENT.getBytes(StandardCharsets.UTF_8)));
        
        assertThat(result.createdCount()).isEqualTo(0);
        assertThat(result.skippedCount()).isEqualTo(2);
        assertThat(matchRepository.count()).isEqualTo(2);
    }

    @Test
    void shouldIngestNewMatchesAndSkipExisting() throws IOException {
        // First ingestion with one match
        String oneMatchCsv = """
                Key Id,Tournament Id,tournament Name,Match Id,Match Name,Stage Name,Group Name,Group Stage,Knockout Stage,Replayed,Replay,Match Date,Match Time,Stadium Id,Stadium Name,City Name,Country Name,Home Team Id,Home Team Name,Home Team Code,Away Team Id,Away Team Name,Away Team Code,Score,Home Team Score,Away Team Score,Home Team Score Margin,Away Team Score Margin,Extra Time,Penalty Shootout,Score Penalties,Home Team Score Penalties,Away Team Score Penalties,Result,Home Team Win,Away Team Win,Draw,Attendance
                1,1930,1930 FIFA World Cup,M-1930-01,France v Mexico,Group Stage,Group 1,1,0,0,0,7/13/1930,15:00,S-1,Estadio Pocitos,Montevideo,Uruguay,T-1,France,FRA,T-2,Mexico,MEX,4-1,4,1,3,-3,0,0,0-0,0,0,HOME_WIN,1,0,0,4444
                """;
        ingestionService.ingestFromStream(new ByteArrayInputStream(oneMatchCsv.getBytes(StandardCharsets.UTF_8)));
        assertThat(matchRepository.count()).isEqualTo(1);
        
        // Second ingestion with two matches (one new, one existing)
        MatchIngestionService.IngestionResult result = ingestionService.ingestFromStream(new ByteArrayInputStream(CSV_CONTENT.getBytes(StandardCharsets.UTF_8)));
        
        assertThat(result.createdCount()).isEqualTo(1); // M-1930-02 is new
        assertThat(result.skippedCount()).isEqualTo(1); // M-1930-01 exists
        assertThat(matchRepository.count()).isEqualTo(2);
    }
}

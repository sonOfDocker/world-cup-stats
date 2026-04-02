package com.worldcupstats.api.ingestion;

import com.worldcupstats.api.canonical.MatchResult;
import com.worldcupstats.api.ingestion.persistence.JpaMatchRepository;
import com.worldcupstats.api.ingestion.persistence.MatchEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class KaggleDatasetIngestionIntegrationTest {

    @Autowired
    private MatchIngestionService ingestionService;

    @Autowired
    private JpaMatchRepository matchRepository;

    private static final String KAGGLE_CSV_PATH = "../data/raw/kaggle/fifa_world_cup_1930_2022_all_matches.csv";

    @BeforeEach
    void setUp() {
        matchRepository.deleteAll();
    }

    @Test
    void shouldIngestEntireKaggleDataset() throws IOException {
        Path path = Paths.get(KAGGLE_CSV_PATH);
        assertThat(path.toFile().exists()).as("Kaggle CSV file should exist at %s", path.toAbsolutePath()).isTrue();

        try (FileInputStream inputStream = new FileInputStream(path.toFile())) {
            MatchIngestionService.IngestionResult result = ingestionService.ingestFromStream(inputStream);

            // The dataset is known to have 964 matches (based on previous analysis or file check)
            // Let's verify it's a significant number.
            assertThat(result.createdCount()).isGreaterThan(900);
            assertThat(matchRepository.count()).isEqualTo(result.createdCount());
        }

        // Verify some specific data points
        
        // 1. First match: 1930 France v Mexico
        Optional<MatchEntity> firstMatch = matchRepository.findBySourceId("M-1930-01");
        assertThat(firstMatch).isPresent();
        assertThat(firstMatch.get().getHomeTeamName()).isEqualTo("France");
        assertThat(firstMatch.get().getAwayTeamName()).isEqualTo("Mexico");
        assertThat(firstMatch.get().getHomeGoals()).isEqualTo(4);
        assertThat(firstMatch.get().getAwayGoals()).isEqualTo(1);
        assertThat(firstMatch.get().getMatchResult()).isEqualTo(MatchResult.HOME_WIN);

        // 2. A match with penalties: 1982 West Germany v France (M-1982-50)
        // Note: In the CSV it's M-1982-50. 
        // Headers: Home Team Score Penalties, Away Team Score Penalties
        // 3-3 (5-4 penalties)
        Optional<MatchEntity> penaltyMatch = matchRepository.findBySourceId("M-1982-50");
        assertThat(penaltyMatch).isPresent();
        assertThat(penaltyMatch.get().getHomeTeamName()).isEqualTo("West Germany");
        assertThat(penaltyMatch.get().getAwayTeamName()).isEqualTo("France");
        assertThat(penaltyMatch.get().getHomeGoals()).isEqualTo(3);
        assertThat(penaltyMatch.get().getAwayGoals()).isEqualTo(3);
        assertThat(penaltyMatch.get().isPenaltyShootout()).isTrue();
        assertThat(penaltyMatch.get().getHomePenaltyScore()).isEqualTo(5);
        assertThat(penaltyMatch.get().getAwayPenaltyScore()).isEqualTo(4);
        assertThat(penaltyMatch.get().getMatchResult()).isEqualTo(MatchResult.HOME_WIN);

        // 3. Last match in dataset (2022 Final): Argentina v France (M-2022-64)
        Optional<MatchEntity> final2022 = matchRepository.findBySourceId("M-2022-64");
        assertThat(final2022).isPresent();
        assertThat(final2022.get().getTournamentYear()).isEqualTo(2022);
        assertThat(final2022.get().getHomeTeamName()).isEqualTo("Argentina");
        assertThat(final2022.get().getAwayTeamName()).isEqualTo("France");
        assertThat(final2022.get().getHomeGoals()).isEqualTo(3);
        assertThat(final2022.get().getAwayGoals()).isEqualTo(3);
        assertThat(final2022.get().getHomePenaltyScore()).isEqualTo(4);
        assertThat(final2022.get().getAwayPenaltyScore()).isEqualTo(2);
        assertThat(final2022.get().getMatchResult()).isEqualTo(MatchResult.HOME_WIN);
    }
}

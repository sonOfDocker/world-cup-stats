package com.worldcupstats.api.ingestion.kaggle;

import com.worldcupstats.api.ingestion.kaggle.persistence.JpaMatchRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.io.InputStream;

import static org.assertj.core.api.Assertions.assertThat;

@Tag("integration")
@SpringBootTest
@ActiveProfiles("test")
@Transactional
class KaggleFixtureIngestionIntegrationTest {

    @Autowired
    private MatchIngestionService ingestionService;

    @Autowired
    private JpaMatchRepository matchRepository;

    @BeforeEach
    void setUp() {
        matchRepository.deleteAll();
    }

    @Test
    void shouldIngestMatchesFromFixture() throws IOException {
        ClassPathResource resource = new ClassPathResource("fixtures/kaggle_sample.csv");
        assertThat(resource.exists()).isTrue();

        try (InputStream is = resource.getInputStream()) {
            MatchIngestionService.IngestionResult result = ingestionService.ingestFromStream(is);
            
            // Our sample has 10 matches
            assertThat(result.createdCount()).isEqualTo(10);
            assertThat(matchRepository.count()).isEqualTo(10);
        }

        assertThat(matchRepository.existsBySourceId("M-1930-01")).isTrue();
        assertThat(matchRepository.existsBySourceId("M-1930-10")).isTrue();
    }

    @Test
    void shouldBeIdempotentWithFixture() throws IOException {
        ClassPathResource resource = new ClassPathResource("fixtures/kaggle_sample.csv");

        // First ingestion
        try (InputStream is = resource.getInputStream()) {
            ingestionService.ingestFromStream(is);
        }
        assertThat(matchRepository.count()).isEqualTo(10);

        // Second ingestion
        try (InputStream is = resource.getInputStream()) {
            MatchIngestionService.IngestionResult result = ingestionService.ingestFromStream(is);
            assertThat(result.createdCount()).isEqualTo(0);
            assertThat(result.skippedCount()).isEqualTo(10);
        }

        assertThat(matchRepository.count()).isEqualTo(10);
    }
}

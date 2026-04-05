package com.worldcupstats.api.ingestion.kaggle;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.io.FileInputStream;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Runner to trigger match ingestion on startup if a specific profile is active or via configuration.
 */
@Component
@Profile("ingest")
public class MatchIngestionRunner implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(MatchIngestionRunner.class);
    private final MatchIngestionService ingestionService;

    public MatchIngestionRunner(MatchIngestionService ingestionService) {
        this.ingestionService = ingestionService;
    }

    @Override
    public void run(String... args) throws Exception {
        log.info("MatchIngestionRunner triggered.");
        
        // Default path relative to project root (assuming running from api or project root)
        Path csvPath = Paths.get("../data/raw/kaggle/fifa_world_cup_1930_2022_all_matches.csv");
        if (!Files.exists(csvPath)) {
            // Try alternative path if running from project root
            csvPath = Paths.get("data/raw/kaggle/fifa_world_cup_1930_2022_all_matches.csv");
        }

        if (Files.exists(csvPath)) {
            log.info("Ingesting matches from: {}", csvPath.toAbsolutePath());
            try (InputStream is = new FileInputStream(csvPath.toFile())) {
                MatchIngestionService.IngestionResult result = ingestionService.ingestFromStream(is);
                log.info("Ingestion result: Created={}, Skipped={}", result.createdCount(), result.skippedCount());
            }
        } else {
            log.warn("Kaggle dataset not found at expected paths. Skipping ingestion.");
        }
    }
}

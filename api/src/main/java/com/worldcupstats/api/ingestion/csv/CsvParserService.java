package com.worldcupstats.api.ingestion.csv;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.Reader;
import java.util.Arrays;
import java.util.List;
import java.util.stream.StreamSupport;

@Service
public class CsvParserService {

    private static final List<String> REQUIRED_HEADERS = Arrays.asList(
            "Key Id", "Tournament Id", "tournament Name", "Match Id", "Match Name",
            "Stage Name", "Group Name", "Group Stage", "Knockout Stage", "Replayed",
            "Replay", "Match Date", "Match Time", "Stadium Id", "Stadium Name",
            "City Name", "Country Name", "Home Team Id", "Home Team Name",
            "Home Team Code", "Away Team Id", "Away Team Name", "Away Team Code",
            "Score", "Home Team Score", "Away Team Score", "Home Team Score Margin",
            "Away Team Score Margin", "Extra Time", "Penalty Shootout", "Score Penalties",
            "Home Team Score Penalties", "Away Team Score Penalties", "Result",
            "Home Team Win", "Away Team Win", "Draw"
    );

    private static final CSVFormat FORMAT = CSVFormat.DEFAULT.builder()
            .setHeader()
            .setSkipHeaderRecord(true)
            .setIgnoreHeaderCase(true)
            .setTrim(true)
            .build();

    public List<WorldCupMatchCsvRow> parseMatches(Reader reader) throws IOException {
        try (CSVParser parser = FORMAT.parse(reader)) {
            validateHeaders(parser);
            return StreamSupport.stream(parser.spliterator(), false)
                    .map(this::mapToRow)
                    .toList();
        }
    }

    private void validateHeaders(CSVParser parser) {
        List<String> actualHeaders = parser.getHeaderNames();
        if (actualHeaders == null || actualHeaders.isEmpty()) {
            throw new IllegalArgumentException("CSV file is empty or missing headers");
        }
        List<String> missingHeaders = REQUIRED_HEADERS.stream()
                .filter(header -> !parser.getHeaderMap().containsKey(header))
                .toList();

        if (!missingHeaders.isEmpty()) {
            throw new IllegalArgumentException("Missing required CSV columns: " + missingHeaders);
        }
    }

    private WorldCupMatchCsvRow mapToRow(CSVRecord record) {
        return new WorldCupMatchCsvRow(
                record.get("Key Id"),
                record.get("Tournament Id"),
                record.get("tournament Name"),
                record.get("Match Id"),
                record.get("Match Name"),
                record.get("Stage Name"),
                record.get("Group Name"),
                record.get("Group Stage"),
                record.get("Knockout Stage"),
                record.get("Replayed"),
                record.get("Replay"),
                record.get("Match Date"),
                record.get("Match Time"),
                record.get("Stadium Id"),
                record.get("Stadium Name"),
                record.get("City Name"),
                record.get("Country Name"),
                record.get("Home Team Id"),
                record.get("Home Team Name"),
                record.get("Home Team Code"),
                record.get("Away Team Id"),
                record.get("Away Team Name"),
                record.get("Away Team Code"),
                record.get("Score"),
                record.get("Home Team Score"),
                record.get("Away Team Score"),
                record.get("Home Team Score Margin"),
                record.get("Away Team Score Margin"),
                record.get("Extra Time"),
                record.get("Penalty Shootout"),
                record.get("Score Penalties"),
                record.get("Home Team Score Penalties"),
                record.get("Away Team Score Penalties"),
                record.get("Result"),
                record.get("Home Team Win"),
                record.get("Away Team Win"),
                record.get("Draw")
        );
    }
}

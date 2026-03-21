package com.worldcupstats.api.ingestion.csv;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.Reader;
import java.util.List;
import java.util.stream.StreamSupport;

@Service
public class CsvParserService {

    private static final CSVFormat FORMAT = CSVFormat.DEFAULT.builder()
            .setHeader()
            .setSkipHeaderRecord(true)
            .setIgnoreHeaderCase(true)
            .setTrim(true)
            .build();

    public List<WorldCupMatchCsvRow> parseMatches(Reader reader) throws IOException {
        Iterable<CSVRecord> records = FORMAT.parse(reader);
        return StreamSupport.stream(records.spliterator(), false)
                .map(this::mapToRow)
                .toList();
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

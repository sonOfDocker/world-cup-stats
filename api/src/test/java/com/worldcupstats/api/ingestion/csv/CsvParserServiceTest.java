package com.worldcupstats.api.ingestion.csv;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.io.StringReader;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class CsvParserServiceTest {

    private final CsvParserService csvParserService = new CsvParserService();

    @Test
    void shouldParseValidCsv() throws IOException {
        String csv = """
                Key Id,Tournament Id,tournament Name,Match Id,Match Name,Stage Name,Group Name,Group Stage,Knockout Stage,Replayed,Replay,Match Date,Match Time,Stadium Id,Stadium Name,City Name,Country Name,Home Team Id,Home Team Name,Home Team Code,Away Team Id,Away Team Name,Away Team Code,Score,Home Team Score,Away Team Score,Home Team Score Margin,Away Team Score Margin,Extra Time,Penalty Shootout,Score Penalties,Home Team Score Penalties,Away Team Score Penalties,Result,Home Team Win,Away Team Win,Draw
                1,WC-1930,1930 FIFA World Cup,M-1930-01,France v Mexico,group stage,Group 1,1,0,0,0,7/13/1930,15:00,S-193,Estadio Pocitos,Montevideo,Uruguay,T-28,France,FRA,T-44,Mexico,MEX,4–1,4,1,3,-3,0,0,0-0,0,0,home team win,1,0,0
                """;

        List<WorldCupMatchCsvRow> rows = csvParserService.parseMatches(new StringReader(csv));

        assertThat(rows).hasSize(1);
        WorldCupMatchCsvRow row = rows.get(0);
        assertThat(row.keyId()).isEqualTo("1");
        assertThat(row.tournamentId()).isEqualTo("WC-1930");
        assertThat(row.tournamentName()).isEqualTo("1930 FIFA World Cup");
        assertThat(row.matchId()).isEqualTo("M-1930-01");
        assertThat(row.matchDate()).isEqualTo("7/13/1930");
        assertThat(row.score()).isEqualTo("4–1"); // Note the special dash
    }

    @Test
    void shouldHandleMixedCaseHeaders() throws IOException {
        String csv = """
                key id,TOURNAMENT ID,tournament name,MATCH ID,match name,stage name,group name,group stage,knockout stage,replayed,replay,match date,match time,stadium id,stadium name,city name,country name,home team id,home team name,home team code,away team id,away team name,away team code,score,home team score,away team score,home team score margin,away team score margin,extra time,penalty shootout,score penalties,home team score penalties,away team score penalties,result,home team win,away team win,draw
                1,WC-1930,1930 FIFA World Cup,M-1930-01,France v Mexico,group stage,Group 1,1,0,0,0,7/13/1930,15:00,S-193,Estadio Pocitos,Montevideo,Uruguay,T-28,France,FRA,T-44,Mexico,MEX,4–1,4,1,3,-3,0,0,0-0,0,0,home team win,1,0,0
                """;

        List<WorldCupMatchCsvRow> rows = csvParserService.parseMatches(new StringReader(csv));

        assertThat(rows).hasSize(1);
        assertThat(rows.get(0).tournamentId()).isEqualTo("WC-1930");
    }

    @Test
    void shouldThrowExceptionOnEmptyCsv() {
        String csv = "";
        assertThatThrownBy(() -> csvParserService.parseMatches(new StringReader(csv)))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("empty or missing headers");
    }

    @Test
    void shouldReturnEmptyListForOnlyHeaders() throws IOException {
        String csv = "Key Id,Tournament Id,tournament Name,Match Id,Match Name,Stage Name,Group Name,Group Stage,Knockout Stage,Replayed,Replay,Match Date,Match Time,Stadium Id,Stadium Name,City Name,Country Name,Home Team Id,Home Team Name,Home Team Code,Away Team Id,Away Team Name,Away Team Code,Score,Home Team Score,Away Team Score,Home Team Score Margin,Away Team Score Margin,Extra Time,Penalty Shootout,Score Penalties,Home Team Score Penalties,Away Team Score Penalties,Result,Home Team Win,Away Team Win,Draw";
        List<WorldCupMatchCsvRow> rows = csvParserService.parseMatches(new StringReader(csv));
        assertThat(rows).isEmpty();
    }

    @Test
    void shouldThrowExceptionOnMissingRequiredColumn() {
        String csv = "Key Id,Tournament Id,Match Id\n1,WC-1930,M-1";
        assertThatThrownBy(() -> csvParserService.parseMatches(new StringReader(csv)))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Missing required CSV columns");
    }

    @Test
    void shouldHandleMalformedCsvRows() throws IOException {
        // One row has too few columns, Apache Commons CSV by default handles this by returning null or empty if requested via header, 
        // but here we are using record.get(String) which will work if the header is there, but might have empty values.
        // If the row is truly malformed (e.g. unclosed quote), CSVParser will throw an exception during iteration.
        String csv = """
                Key Id,Tournament Id,tournament Name,Match Id,Match Name,Stage Name,Group Name,Group Stage,Knockout Stage,Replayed,Replay,Match Date,Match Time,Stadium Id,Stadium Name,City Name,Country Name,Home Team Id,Home Team Name,Home Team Code,Away Team Id,Away Team Name,Away Team Code,Score,Home Team Score,Away Team Score,Home Team Score Margin,Away Team Score Margin,Extra Time,Penalty Shootout,Score Penalties,Home Team Score Penalties,Away Team Score Penalties,Result,Home Team Win,Away Team Win,Draw
                "unclosed quote,WC-1930,1930 FIFA World Cup,M-1930-01,France v Mexico,group stage,Group 1,1,0,0,0,7/13/1930,15:00,S-193,Estadio Pocitos,Montevideo,Uruguay,T-28,France,FRA,T-44,Mexico,MEX,4–1,4,1,3,-3,0,0,0-0,0,0,home team win,1,0,0
                """;

        assertThatThrownBy(() -> {
            List<WorldCupMatchCsvRow> rows = csvParserService.parseMatches(new StringReader(csv));
            rows.size(); // Trigger iteration
        }).isInstanceOf(UncheckedIOException.class);
    }

    @Test
    void shouldParseActualFile(@TempDir Path tempDir) throws IOException {
        Path csvFile = tempDir.resolve("matches.csv");
        String content = """
                Key Id,Tournament Id,tournament Name,Match Id,Match Name,Stage Name,Group Name,Group Stage,Knockout Stage,Replayed,Replay,Match Date,Match Time,Stadium Id,Stadium Name,City Name,Country Name,Home Team Id,Home Team Name,Home Team Code,Away Team Id,Away Team Name,Away Team Code,Score,Home Team Score,Away Team Score,Home Team Score Margin,Away Team Score Margin,Extra Time,Penalty Shootout,Score Penalties,Home Team Score Penalties,Away Team Score Penalties,Result,Home Team Win,Away Team Win,Draw
                1,WC-1930,1930 FIFA World Cup,M-1930-01,France v Mexico,group stage,Group 1,1,0,0,0,7/13/1930,15:00,S-193,Estadio Pocitos,Montevideo,Uruguay,T-28,France,FRA,T-44,Mexico,MEX,4–1,4,1,3,-3,0,0,0-0,0,0,home team win,1,0,0
                """;
        Files.writeString(csvFile, content);

        try (var reader = Files.newBufferedReader(csvFile)) {
            List<WorldCupMatchCsvRow> rows = csvParserService.parseMatches(reader);
            assertThat(rows).hasSize(1);
            assertThat(rows.get(0).keyId()).isEqualTo("1");
        }
    }
}

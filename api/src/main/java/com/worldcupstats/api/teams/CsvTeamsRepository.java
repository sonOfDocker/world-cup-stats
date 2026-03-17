package com.worldcupstats.api.teams;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

@Repository
@Profile("!local")
class CsvTeamsRepository implements TeamsRepository {

    private final Path csvPath;

    public CsvTeamsRepository() {
        String projectRoot = System.getProperty("user.dir");
        Path rootPath = Paths.get(projectRoot);
        // When running from api module, go up one level to project root
        if (rootPath.endsWith("api")) {
            rootPath = rootPath.getParent();
        }
        this.csvPath = rootPath.resolve("data/raw/kaggle/fifa_world_cup_1930_2022_all_matches.csv");
    }

    // Constructor for testing
    CsvTeamsRepository(String csvPath) {
        this.csvPath = Paths.get(csvPath);
    }

    @Override
    public List<TeamDto> findAll() {
        Map<String, String> teamMap = new HashMap<>();

        try (Stream<String> lines = Files.lines(csvPath.toAbsolutePath(), StandardCharsets.ISO_8859_1)) {
            lines.skip(1) // Skip header
                    .forEach(line -> {
                        String[] fields = parseCsvLine(line);

                        if (fields.length >= 23) {
                            processTeamFields(teamMap, fields[19], fields[18]);
                            processTeamFields(teamMap, fields[22], fields[21]);
                        }
                    });
        } catch (IOException e) {
            throw new RuntimeException("Failed to read CSV file: " + csvPath, e);
        }

        return teamMap.entrySet().stream()
                .map(entry -> new TeamDto(
                        entry.getKey().toLowerCase(),
                        entry.getValue(),
                        entry.getKey()
                ))
                .sorted(Comparator.comparing(TeamDto::code))
                .toList();
    }

    private void processTeamFields(Map<String, String> teamMap, String codeField, String nameField) {
        String code = codeField.trim();
        String name = nameField.trim();
        if (!code.isEmpty() && !name.isEmpty()) {
            teamMap.putIfAbsent(code, name);
        }
    }
    
    private String[] parseCsvLine(String line) {
        // Simple CSV parser - splits by comma
        // Note: This doesn't handle quoted fields with commas inside
        return line.split(",");
    }
}

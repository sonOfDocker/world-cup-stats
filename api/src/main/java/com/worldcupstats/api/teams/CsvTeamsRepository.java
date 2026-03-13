package com.worldcupstats.api.teams;

import org.springframework.stereotype.Repository;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

@Repository
class CsvTeamsRepository implements TeamsRepository {

    private final String csvPath;

    public CsvTeamsRepository() {
        String projectRoot = System.getProperty("user.dir");
        // When running from api module, go up one level to project root
        if (projectRoot.endsWith("api")) {
            projectRoot = projectRoot.substring(0, projectRoot.length() - 4);
        }
        this.csvPath = projectRoot + "/data/raw/kaggle/fifa_world_cup_1930_2022_all_matches.csv";
    }

    // Constructor for testing
    CsvTeamsRepository(String csvPath) {
        this.csvPath = csvPath;
    }

    @Override
    public List<TeamDto> findAll() {
        Map<String, String> teamMap = new HashMap<>();
        
        Path absolutePath = Paths.get(csvPath).toAbsolutePath();
        
        try (BufferedReader reader = new BufferedReader(new FileReader(absolutePath.toFile()))) {
            String line = reader.readLine(); // Skip header
            
            while ((line = reader.readLine()) != null) {
                String[] fields = parseCsvLine(line);
                
                if (fields.length >= 23) {
                    // Home team: index 19 = Home Team Code, index 18 = Home Team Name
                    String homeCode = fields[19].trim();
                    String homeName = fields[18].trim();
                    if (!homeCode.isEmpty() && !homeName.isEmpty()) {
                        teamMap.putIfAbsent(homeCode, homeName);
                    }
                    
                    // Away team: index 22 = Away Team Code, index 21 = Away Team Name
                    String awayCode = fields[22].trim();
                    String awayName = fields[21].trim();
                    if (!awayCode.isEmpty() && !awayName.isEmpty()) {
                        teamMap.putIfAbsent(awayCode, awayName);
                    }
                }
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to read CSV file: " + absolutePath, e);
        }
        
        return teamMap.entrySet().stream()
                .map(entry -> new TeamDto(
                        entry.getKey().toLowerCase(),
                        entry.getValue(),
                        entry.getKey()
                ))
                .sorted(Comparator.comparing(TeamDto::code))
                .collect(Collectors.toList());
    }
    
    private String[] parseCsvLine(String line) {
        // Simple CSV parser - splits by comma
        // Note: This doesn't handle quoted fields with commas inside
        return line.split(",");
    }
}

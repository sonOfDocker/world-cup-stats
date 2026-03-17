package com.worldcupstats.api;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
class ApiApplicationTests {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Test
    void contextLoads() {
    }

    @Test
    void databaseInitializedByFlyway() {
        // Verify Flyway's own internal record of migrations
        Integer migrationCount = jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM \"flyway_schema_history\" WHERE \"success\" = TRUE", Integer.class);
        assertThat(migrationCount).isGreaterThanOrEqualTo(2);

        // Verify that tables from different migration files exist and have data
        Integer verificationCount = jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM schema_verification", Integer.class);
        assertThat(verificationCount).isGreaterThanOrEqualTo(1);

        Integer teamsCount = jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM teams", Integer.class);
        assertThat(teamsCount).isGreaterThanOrEqualTo(8);
    }
}

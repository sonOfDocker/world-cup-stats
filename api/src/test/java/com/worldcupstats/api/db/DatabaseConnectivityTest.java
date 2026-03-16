package com.worldcupstats.api.db;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Tag("integration")
class DatabaseConnectivityTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Test
    void shouldConnectToDatabaseAndVerifyFlywayMigration() {
        // Verify we can query the table created in V1 migration
        Integer count = jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM schema_verification", Integer.class);
        
        assertThat(count).isGreaterThanOrEqualTo(1);
    }
}

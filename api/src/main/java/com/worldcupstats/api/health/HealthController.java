package com.worldcupstats.api.health;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class HealthController {

    @GetMapping(value = "/api/v1/health", produces = MediaType.APPLICATION_JSON_VALUE)
    Map<String, String> health() {
        return Map.of("status", "UP");
    }

}

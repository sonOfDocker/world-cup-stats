package com.worldcupstats.api.canonical;

public record Tournament(
        String sourceId,
        String name,
        Integer year
) {
}
package com.worldcupstats.api.canonical;

public record Venue(
        String venueId,
        String stadiumName,
        String city,
        String country
) {
}
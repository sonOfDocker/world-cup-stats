package com.worldcupstats.api.canonical;

public record Stage(
        String name,
        String groupName,
        boolean groupStage,
        boolean knockoutStage
) {
}
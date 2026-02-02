package com.lets_play.api.products.dto;

import java.time.Instant;

public record ProductResponse(
        String id,
        String name,
        String description,
        Double price,
        String userId,
        Instant createdAt
) {}

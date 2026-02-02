package com.lets_play.api.products.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;

public record ProductUpdateRequest(
        @Size(min = 2, max = 120) String name,
        @Size(max = 500) String description,
        @Min(0) Double price
) {}

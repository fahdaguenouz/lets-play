package com.lets_play.api.products.dto;

import jakarta.validation.constraints.Size;

import jakarta.validation.constraints.PositiveOrZero;

public record ProductUpdateRequest(
  @Size(min = 2, max = 120) String name,
  @Size(max = 500) String description,
  @PositiveOrZero Double price
) {}

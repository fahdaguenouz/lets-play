package com.lets_play.api.products.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import jakarta.validation.constraints.PositiveOrZero;

public record ProductCreateRequest(
  @NotBlank @Size(min = 2, max = 120) String name,
  @Size(max = 500) String description,
  @NotNull @PositiveOrZero Double price
) {}

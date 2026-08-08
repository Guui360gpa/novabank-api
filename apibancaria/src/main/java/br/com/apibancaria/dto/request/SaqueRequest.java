package br.com.apibancaria.dto.request;

import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record SaqueRequest(
        @Positive
        BigDecimal valor
)
{}

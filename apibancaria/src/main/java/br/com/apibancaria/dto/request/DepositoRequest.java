package br.com.apibancaria.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record DepositoRequest(

        @NotNull
        @Positive
        BigDecimal valor
)
{}

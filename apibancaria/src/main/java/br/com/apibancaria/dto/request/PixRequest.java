package br.com.apibancaria.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record PixRequest(

        @NotNull
        Long contaOrigemId,

        @NotBlank
        String chavePixDestino,

        @NotNull
        @Positive
        BigDecimal valor
)
{}

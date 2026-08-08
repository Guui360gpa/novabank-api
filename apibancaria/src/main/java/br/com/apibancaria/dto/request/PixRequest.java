package br.com.apibancaria.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record PixRequest(

        @NotBlank
        String chaveDestino,

        @Positive
        BigDecimal valor
)
{}

package br.com.apibancaria.dto.response;

import br.com.apibancaria.enums.StatusConta;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record ContaResponse(
        Long id,
        String numeroConta,
        String agencia,
        BigDecimal saldo,
        StatusConta status,
        LocalDateTime dataCriacao
)
{}

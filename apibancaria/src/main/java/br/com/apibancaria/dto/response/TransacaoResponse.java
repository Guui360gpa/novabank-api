package br.com.apibancaria.dto.response;

import br.com.apibancaria.enums.TipoTransacao;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record TransacaoResponse(
        Long id,
        TipoTransacao tipo,
        BigDecimal valor,
        LocalDateTime dataHora,
        String descricao
)
{}

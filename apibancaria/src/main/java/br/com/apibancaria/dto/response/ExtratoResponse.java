package br.com.apibancaria.dto.response;

import java.util.List;

public record ExtratoResponse(
        ContaResponse conta,
        List<TransacaoResponse> transacoes
)
{}

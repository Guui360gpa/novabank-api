package br.com.apibancaria.dto.response;

import br.com.apibancaria.enums.TipoChavePix;

import java.time.LocalDateTime;

public record ChavePixResponse(
        Long id,
        TipoChavePix tipo,
        String chave,
        Boolean ativa,
        LocalDateTime dataCadastro
)
{}

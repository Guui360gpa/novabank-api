package br.com.apibancaria.dto.request;

import br.com.apibancaria.enums.TipoChavePix;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ChavePixRequest(
        @NotNull
        TipoChavePix tipo,

        @NotBlank
        String chave,

        @NotNull
        Long idConta

)
{}

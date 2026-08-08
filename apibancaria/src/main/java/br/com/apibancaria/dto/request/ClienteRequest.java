package br.com.apibancaria.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record ClienteRequest(

        @NotBlank
        String nome,

        @NotBlank
        String cpf,

        @Email
        String email,

        @NotBlank
        String telefone,

        @NotBlank
        String senha
)
{}

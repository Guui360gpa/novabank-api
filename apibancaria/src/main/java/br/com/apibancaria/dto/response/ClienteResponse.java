package br.com.apibancaria.dto.response;

public record ClienteResponse(

        Long id,
        String nome,
        String cpf,
        String email
)
{}

package br.com.apibancaria.dto.response;

public record LoginResponse(
        String token,
        String tipo
)
{}

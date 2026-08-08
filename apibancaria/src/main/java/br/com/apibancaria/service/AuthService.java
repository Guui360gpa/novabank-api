package br.com.apibancaria.service;

import br.com.apibancaria.dto.request.LoginRequest;
import br.com.apibancaria.dto.response.LoginResponse;
import jakarta.validation.Valid;
import org.springframework.stereotype.Service;

@Service
public class AuthService {


    public LoginResponse login(@Valid LoginRequest dto) {
    }
}

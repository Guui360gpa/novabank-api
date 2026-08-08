package br.com.apibancaria.controller;

import br.com.apibancaria.dto.request.LoginRequest;
import br.com.apibancaria.dto.response.LoginResponse;
import br.com.apibancaria.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService service;

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login (@Valid @RequestBody LoginRequest dto){
        LoginResponse loginResponse = service.login(dto);
        return ResponseEntity.ok(loginResponse);
    }
}

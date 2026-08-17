package br.com.apibancaria.service;

import br.com.apibancaria.dto.request.LoginRequest;
import br.com.apibancaria.dto.response.LoginResponse;
import br.com.apibancaria.exception.CredenciaisInvalidasException;
import br.com.apibancaria.model.Cliente;
import br.com.apibancaria.repository.ClienteRepository;
import br.com.apibancaria.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final ClienteRepository clienteRepository;
    private final PasswordEncoder encoder;
    private final JwtService jwtService;


    public LoginResponse login(LoginRequest dto) {
        Cliente cliente = clienteRepository.findByEmail(dto.email())
                .orElseThrow(() -> new CredenciaisInvalidasException("Email ou Senha inválido(s) !"));

        if(!encoder.matches(dto.senha(), cliente.getSenha())){
            throw new CredenciaisInvalidasException("Email ou senha inválidos(s) !");
        }

        String token = jwtService.gerarToken(cliente);

        return new LoginResponse(token);


    }
}

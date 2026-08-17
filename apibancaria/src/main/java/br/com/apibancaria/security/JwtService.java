package br.com.apibancaria.security;

import br.com.apibancaria.model.Cliente;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Service
public class JwtService {
    @Value("${jwt.secret}")
    private String secret;

    private SecretKey getSignKey(){
        return Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }

    public String gerarToken(Cliente cliente) {

        Date agora = new Date();

        Date expiracao = new Date(
                agora.getTime() + 1000 * 60 * 60
        );

        return Jwts.builder()
                .subject(cliente.getEmail())
                .issuedAt(agora)
                .expiration(expiracao)
                .signWith(getSignKey())
                .compact();
    }

    public String extrairEmail(String token){
        return Jwts.parser().verifyWith(getSignKey())
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();
    }

    public boolean validarToken(String token){
        try {
            Jwts.parser()
                    .verifyWith(getSignKey())
                    .build()
                    .parseSignedClaims(token);
            return true;
        }catch (Exception e){
            return false;
        }
    }

}

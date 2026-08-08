package br.com.apibancaria.exception;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class CpfJaCadastradoException extends RuntimeException {

    public CpfJaCadastradoException(String message) {
        super(message);
    }
}

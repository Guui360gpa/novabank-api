package br.com.apibancaria.exception;

public class ContaInativaException extends RuntimeException {
    public ContaInativaException(String message) {
        super(message);
    }
}

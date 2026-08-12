package br.com.apibancaria.exception;

public class TransferenciaParaSiMesmoException extends RuntimeException {
    public TransferenciaParaSiMesmoException(String message) {
        super(message);
    }
}

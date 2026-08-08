package br.com.apibancaria.service;

import br.com.apibancaria.dto.request.DepositoRequest;
import br.com.apibancaria.dto.request.SaqueRequest;
import br.com.apibancaria.dto.response.ExtratoResponse;
import br.com.apibancaria.dto.response.TransacaoResponse;
import jakarta.validation.Valid;
import org.springframework.stereotype.Service;

@Service
public class TransacaoService {

    public TransacaoResponse depositar(Long id,  DepositoRequest dto) {
    }

    public TransacaoResponse sacar(Long id,  SaqueRequest dto) {
    }

    public ExtratoResponse extrato(Long id) {
    }
}

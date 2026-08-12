package br.com.apibancaria.controller;


import br.com.apibancaria.dto.request.DepositoRequest;
import br.com.apibancaria.dto.request.SaqueRequest;
import br.com.apibancaria.dto.response.TransacaoResponse;
import br.com.apibancaria.service.TransacaoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/contas")
@RequiredArgsConstructor
public class TransacaoController {

    private final TransacaoService service;

    @PostMapping("/{id}/deposito")
    public ResponseEntity<TransacaoResponse> deposito(@PathVariable Long id, @Valid @RequestBody DepositoRequest dto){
        TransacaoResponse transacaoResponse = service.depositar(id, dto);

        return ResponseEntity.status(HttpStatus.CREATED).body(transacaoResponse);
    }

    @PostMapping("/{id}/saque")
    public ResponseEntity<TransacaoResponse> saque(@PathVariable Long id, @Valid @RequestBody SaqueRequest dto){
        TransacaoResponse transacaoResponse = service.sacar(id,dto);

        return ResponseEntity.status(HttpStatus.CREATED).body(transacaoResponse);
    }

    @GetMapping("/{id}/extrato")
    public ResponseEntity<List<TransacaoResponse>> extrato(@PathVariable Long id){
        List<TransacaoResponse> transacoesResponse = service.extrato(id);

        return ResponseEntity.ok(transacoesResponse);
    }
}

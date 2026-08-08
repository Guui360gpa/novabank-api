package br.com.apibancaria.controller;

import br.com.apibancaria.dto.response.ContaResponse;
import br.com.apibancaria.service.ContaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/contas")
@RequiredArgsConstructor
public class ContaController {

    private final ContaService service;

    @GetMapping
    public ResponseEntity<List<ContaResponse>> lista(){
        List<ContaResponse> contas = service.listar();
        return ResponseEntity.ok(contas);

    }

    @GetMapping("/{id}")
    public ResponseEntity<ContaResponse> buscar(@PathVariable Long id){
        ContaResponse contaResponse = service.buscarPorId(id);
        return ResponseEntity.ok(contaResponse);
    }
}

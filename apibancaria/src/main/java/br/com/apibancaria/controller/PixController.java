package br.com.apibancaria.controller;


import br.com.apibancaria.dto.request.ChavePixRequest;
import br.com.apibancaria.dto.request.PixRequest;
import br.com.apibancaria.dto.response.ChavePixResponse;
import br.com.apibancaria.dto.response.TransacaoResponse;
import br.com.apibancaria.service.PixService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/pix")
@RequiredArgsConstructor
public class PixController {

    private final PixService service;

    @PostMapping
    public ResponseEntity<TransacaoResponse> transferencia(@Valid @RequestBody PixRequest dto){
        TransacaoResponse transacaoResponse = service.transferir(dto);

        return ResponseEntity.status(HttpStatus.CREATED).body(transacaoResponse);
    }

    @PostMapping("/chaves")
    public ResponseEntity<ChavePixResponse> cadastrarChave(@Valid @RequestBody ChavePixRequest dto){
        ChavePixResponse chavePixResponse = service.cadastrar(dto);

        return ResponseEntity.status(HttpStatus.CREATED).body(chavePixResponse);
    }

    @DeleteMapping("/chaves/{id}")
    public ResponseEntity<Object> excluirChave(@PathVariable Long id){
        service.excluir(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/chaves")
    public ResponseEntity<List<ChavePixResponse>> listarChaves(@RequestParam(required = false) Long id){
        List<ChavePixResponse> chaves = service.listar(id);

        return ResponseEntity.ok(chaves);
    }

}

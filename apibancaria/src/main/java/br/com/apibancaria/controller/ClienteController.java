package br.com.apibancaria.controller;

import br.com.apibancaria.dto.request.ClienteRequest;
import br.com.apibancaria.dto.response.ClienteResponse;
import br.com.apibancaria.exception.ClienteNaoEncontradoException;
import br.com.apibancaria.service.ClienteService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/clientes")
@RequiredArgsConstructor
public class ClienteController {



    private final ClienteService service;

    @PostMapping
    public ResponseEntity<ClienteResponse> cadastro(@Valid @RequestBody ClienteRequest dto){
        ClienteResponse clienteResponse = service.cadastrar(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(clienteResponse);

    }

    @GetMapping
    public ResponseEntity<List<ClienteResponse>> lista(){
        List<ClienteResponse> clienteResponses = service.listar();
        return ResponseEntity.ok(clienteResponses);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ClienteResponse> buscar(@PathVariable Long id){
        try{
            ClienteResponse clienteResponse = service.buscarPorId(id);
            return ResponseEntity.ok(clienteResponse);
        }catch (ClienteNaoEncontradoException ex){
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<ClienteResponse> atualizar(@PathVariable Long id, @Valid @RequestBody ClienteRequest dto){
            ClienteResponse clienteResponse = service.atualizarPorId(id , dto);
            return ResponseEntity.ok(clienteResponse);

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> excluir(@PathVariable Long id){
        service.excluirPorId(id);
        return ResponseEntity.noContent().build();
    }
}

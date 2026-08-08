package br.com.apibancaria.service;

import br.com.apibancaria.dto.response.ContaResponse;
import br.com.apibancaria.enums.StatusConta;
import br.com.apibancaria.model.Cliente;
import br.com.apibancaria.model.Conta;
import br.com.apibancaria.repository.ContaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

@Service
@RequiredArgsConstructor
public class ContaService {

    private final ContaRepository contaRepository;

    public void criarConta(Cliente cliente){
        String numeroConta = String.valueOf(gerarNumeroConta());
        while (contaRepository.existsByNumeroConta(numeroConta)){
            numeroConta = String.valueOf(gerarNumeroConta());
        }

        Conta conta = new Conta();
        conta.setNumeroConta(numeroConta);
        conta.setCliente(cliente);
        conta.setAgencia("0001");
        conta.setSaldo(BigDecimal.ZERO);
        conta.setStatus(StatusConta.ATIVA);
        conta.setDataCriacao(LocalDateTime.now());

        contaRepository.save(conta);

    }

    private int gerarNumeroConta(){
        return ThreadLocalRandom.current().nextInt(100000, 1000000);
    }


    public List<ContaResponse> listar() {
    }

    public ContaResponse buscarPorId(Long id) {
    }
}

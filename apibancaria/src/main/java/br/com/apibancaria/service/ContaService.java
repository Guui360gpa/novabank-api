package br.com.apibancaria.service;

import br.com.apibancaria.dto.response.ContaResponse;
import br.com.apibancaria.enums.StatusConta;
import br.com.apibancaria.exception.ContaNaoEncontradaException;
import br.com.apibancaria.model.Cliente;
import br.com.apibancaria.model.Conta;
import br.com.apibancaria.repository.ContaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
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
        List<Conta> contas = contaRepository.findAll();
        return contas.stream()
                .map(c -> new ContaResponse(c.getId(),
                        c.getNumeroConta(),
                        c.getAgencia(),
                        c.getSaldo(),
                        c.getStatus(),
                        c.getDataCriacao()))
                .toList();
    }

    public ContaResponse buscarPorId(Long id) {
        Conta conta = contaRepository.findById(id)
                .orElseThrow(() ->
                        new ContaNaoEncontradaException("A conta não foi encontrada!"));

        return new ContaResponse(
                conta.getId(),
                conta.getNumeroConta(),
                conta.getAgencia(),
                conta.getSaldo(),
                conta.getStatus(),
                conta.getDataCriacao()
        );
    }
}

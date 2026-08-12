package br.com.apibancaria.service;

import br.com.apibancaria.dto.request.DepositoRequest;
import br.com.apibancaria.dto.request.SaqueRequest;
import br.com.apibancaria.dto.response.TransacaoResponse;
import br.com.apibancaria.enums.StatusConta;
import br.com.apibancaria.enums.TipoTransacao;
import br.com.apibancaria.exception.ContaInativaException;
import br.com.apibancaria.exception.ContaNaoEncontradaException;
import br.com.apibancaria.exception.SaldoInsuficienteException;
import br.com.apibancaria.model.Conta;
import br.com.apibancaria.model.Transacao;
import br.com.apibancaria.repository.ContaRepository;
import br.com.apibancaria.repository.TransacaoRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TransacaoService {

    private final ContaRepository contaRepository;

    private final TransacaoRepository transacaoRepository;

    @Transactional
    public TransacaoResponse depositar(Long id,  DepositoRequest dto) {
        Conta conta = contaRepository.findById(id)
                .orElseThrow(() ->
                        new ContaNaoEncontradaException("A conta não foi encontrada!"));

        if (conta.getStatus() != StatusConta.ATIVA){
            throw  new ContaInativaException("A conta está inativa!");
        }


        BigDecimal saldoAnterior = conta.getSaldo();

        BigDecimal saldoPosterior = saldoAnterior.add(dto.valor());

        conta.setSaldo(saldoPosterior);
        contaRepository.save(conta);

        Transacao transacao = new Transacao(TipoTransacao.DEPOSITO,
                dto.valor(),
                LocalDateTime.now(),
                "Deposito Realizado",
                null,
                conta,
                saldoAnterior,
                saldoPosterior);

        Transacao transacaoSalva = transacaoRepository.save(transacao);

        return new TransacaoResponse(
                transacaoSalva.getId(),
                transacaoSalva.getTipo(),
                transacaoSalva.getValor(),
                transacaoSalva.getDataHora(),
                transacaoSalva.getDescricao()
        );
    }

    @Transactional
    public TransacaoResponse sacar(Long id,  SaqueRequest dto) {
        Conta conta = contaRepository.findById(id)
                .orElseThrow(() ->
                        new ContaNaoEncontradaException("A conta não foi encontrada!"));

        if (conta.getStatus() != StatusConta.ATIVA){
            throw  new ContaInativaException("A conta está inativa!");
        }

        if (dto.valor().compareTo(conta.getSaldo()) > 0){
            throw new SaldoInsuficienteException("O saldo é insuficiente!");
        }

        BigDecimal saldoAnterior = conta.getSaldo();

        BigDecimal saldoPosterior = saldoAnterior.subtract(dto.valor());

        conta.setSaldo(saldoPosterior);
        contaRepository.save(conta);

        Transacao transacao = new Transacao(TipoTransacao.SAQUE,
                dto.valor(),
                LocalDateTime.now(),
                "Saque Realizado",
                conta,
                null,
                saldoAnterior,
                saldoPosterior);

        Transacao transacaoSalva = transacaoRepository.save(transacao);

        return new TransacaoResponse(
                transacaoSalva.getId(),
                transacaoSalva.getTipo(),
                transacaoSalva.getValor(),
                transacaoSalva.getDataHora(),
                transacaoSalva.getDescricao()
        );
    }

    public List<TransacaoResponse> extrato(Long id) {
        Conta conta = contaRepository.findById(id)
                .orElseThrow(() ->
                        new ContaNaoEncontradaException("A conta não foi encontrada!"));
        List<Transacao> transacoes = transacaoRepository.findByContaOrigemIdOrContaDestinoIdOrderByDataHoraDesc(id,id);

        return transacoes.stream()
                .map(t -> new TransacaoResponse(
                        t.getId(),
                        t.getTipo(),
                        t.getValor(),
                        t.getDataHora(),
                        t.getDescricao()
                )).toList();
    }
}

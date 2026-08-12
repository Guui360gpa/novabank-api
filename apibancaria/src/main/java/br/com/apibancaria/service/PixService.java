package br.com.apibancaria.service;

import br.com.apibancaria.dto.request.ChavePixRequest;
import br.com.apibancaria.dto.request.PixRequest;
import br.com.apibancaria.dto.response.ChavePixResponse;
import br.com.apibancaria.dto.response.TransacaoResponse;
import br.com.apibancaria.enums.StatusConta;
import br.com.apibancaria.enums.TipoTransacao;
import br.com.apibancaria.exception.*;
import br.com.apibancaria.model.ChavePix;
import br.com.apibancaria.model.Conta;
import br.com.apibancaria.model.Transacao;
import br.com.apibancaria.repository.ChavePixRepository;
import br.com.apibancaria.repository.ContaRepository;
import br.com.apibancaria.repository.TransacaoRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;


@Service
@RequiredArgsConstructor
public class PixService {

    private final ContaRepository contaRepository;
    private final TransacaoRepository transacaoRepository;
    private final ChavePixRepository chavePixRepository;


    @Transactional
    public TransacaoResponse transferir( PixRequest dto) {
        Conta contaOrigem = contaRepository.findById(dto.contaOrigemId())
                .orElseThrow(() ->
                        new ContaNaoEncontradaException("Conta origem não encontrada!"));

        if (contaOrigem.getStatus() != StatusConta.ATIVA){
            throw  new ContaInativaException("A conta está inativa!");
        }

        ChavePix chave = chavePixRepository
                .findByChaveAndAtivaTrue(dto.chavePixDestino())
                .orElseThrow(() ->
                        new ChavePixNaoEncontradaException("Chave pix não encontrada"));

        Conta contaDestino = chave.getConta();

        if (contaDestino.getStatus() != StatusConta.ATIVA){
            throw  new ContaInativaException("A conta está inativa!");
        }

        if (contaOrigem.getId().equals(contaDestino.getId())){
            throw new TransferenciaParaSiMesmoException("Tranferencia para conta destino inválida");
        }

        if (dto.valor().compareTo(contaOrigem.getSaldo()) > 0){
            throw new SaldoInsuficienteException("O saldo é insuficiente!");
        }

        BigDecimal saldoAnteriorOrigem = contaOrigem.getSaldo();
        BigDecimal saldoPosteriorOrigem = saldoAnteriorOrigem.subtract(dto.valor());
        contaOrigem.setSaldo(saldoPosteriorOrigem);

        contaRepository.save(contaOrigem);


        BigDecimal saldoAnteriorDestino = contaDestino.getSaldo();
        BigDecimal saldoPosteriorDestino = saldoAnteriorDestino.add(dto.valor());
        contaDestino.setSaldo(saldoPosteriorDestino);

        contaRepository.save(contaDestino);

        Transacao transacao = new Transacao(TipoTransacao.PIX,
                dto.valor(),
                LocalDateTime.now(),
                "Pix Realizado",
                contaOrigem,
                contaDestino,
                saldoAnteriorOrigem,
                saldoPosteriorOrigem);

        Transacao transacaoSalva = transacaoRepository.save(transacao);

        return new TransacaoResponse(
                transacaoSalva.getId(),
                transacaoSalva.getTipo(),
                transacaoSalva.getValor(),
                transacaoSalva.getDataHora(),
                transacaoSalva.getDescricao()
        );

    }

    public ChavePixResponse cadastrar(ChavePixRequest dto) {
        Conta conta = contaRepository.findById(dto.idConta())
                .orElseThrow(() ->
                        new ContaNaoEncontradaException("Conta origem não encontrada!"));

        if (conta.getStatus() != StatusConta.ATIVA){
            throw  new ContaInativaException("A conta está inativa!");
        }

        if (chavePixRepository.findByChave(dto.chave()).isPresent()){
            throw new ChavePixJaCadastradaException("Chave pix indisponível");
        }

        ChavePix chave = new ChavePix(dto.tipo(),dto.chave(),conta);

        ChavePix chaveSalva = chavePixRepository.save(chave);

        return new ChavePixResponse(
                chaveSalva.getId(),
                chaveSalva.getTipo(),
                chaveSalva.getChave(),
                chaveSalva.getAtiva(),
                chaveSalva.getDataCadastro()
        );

    }

    public List<ChavePixResponse> listar(Long id) {
        List<ChavePix> chaves;

        contaRepository.findById(id)
                .orElseThrow(() ->
                        new ContaNaoEncontradaException("A conta não foi encontrada!"));

        chaves = chavePixRepository.findByContaIdAndAtivaTrue(id);


        return chaves.stream()
                .map(c -> new ChavePixResponse(
                        c.getId(),
                        c.getTipo(),
                        c.getChave(),
                        c.getAtiva(),
                        c.getDataCadastro()
                ))
                .toList();
    }

    public void excluir(Long id) {
        ChavePix chavePix = chavePixRepository.findById(id)
                .orElseThrow(() ->
                        new ChavePixNaoEncontradaException("A chave pix não foi encontrada"));

        chavePix.setAtiva(false);

        chavePixRepository.save(chavePix);
    }
}

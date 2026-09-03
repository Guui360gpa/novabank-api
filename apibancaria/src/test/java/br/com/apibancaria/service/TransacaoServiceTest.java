package br.com.apibancaria.service;

import br.com.apibancaria.dto.request.DepositoRequest;
import br.com.apibancaria.dto.request.PixRequest;
import br.com.apibancaria.dto.request.SaqueRequest;
import br.com.apibancaria.dto.response.TransacaoResponse;
import br.com.apibancaria.enums.StatusCliente;
import br.com.apibancaria.enums.StatusConta;
import br.com.apibancaria.enums.TipoTransacao;
import br.com.apibancaria.exception.ContaInativaException;
import br.com.apibancaria.exception.SaldoInsuficienteException;
import br.com.apibancaria.model.Cliente;
import br.com.apibancaria.model.Conta;
import br.com.apibancaria.model.Transacao;
import br.com.apibancaria.repository.ContaRepository;
import br.com.apibancaria.repository.TransacaoRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.willReturn;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;


@ExtendWith(MockitoExtension.class)
class TransacaoServiceTest {

    @InjectMocks
    private TransacaoService service;

    @Mock
    private ContaRepository contaRepository;

    @Mock
    private TransacaoRepository transacaoRepository;


    private Cliente clienteSalvoValido() {
        Cliente cliente = new Cliente();
        cliente.setId(1L);
        cliente.setNome("João Silva");
        cliente.setCpf("12345678900");
        cliente.setEmail("joao@email.com");
        cliente.setTelefone("11999998888");
        cliente.setSenha("hashFicticio123");
        cliente.setStatus(StatusCliente.ATIVO);
        return cliente;
    }

    private Conta contaSalvaValida(){
        Conta conta = new Conta();

        conta.setId(1L);
        conta.setCliente(clienteSalvoValido());
        conta.setNumeroConta("12345678910");
        conta.setSaldo(BigDecimal.ZERO);
        conta.setStatus(StatusConta.ATIVA);
        conta.setDataCriacao(LocalDateTime.now());

        return conta;
    }

    private DepositoRequest depositoRequestValido(){
        return new DepositoRequest(new BigDecimal("100"));
    }

    private SaqueRequest saqueRequestValido(){
        return new SaqueRequest(new BigDecimal("100"));
    }

    @Test
    @DisplayName("deveriaLancarExceptionSeContaDesativadaNoDeposito")
    void LancarExceptionContaDesativadaDeposito(){
        //Arrange
        Conta conta = contaSalvaValida();
        conta.setStatus(StatusConta.ENCERRADA);

        DepositoRequest request = depositoRequestValido();

        BDDMockito.given(contaRepository.findById(1L))
                .willReturn(Optional.of(conta));

        //Act + Assert
        Assertions.assertThrows(ContaInativaException.class,() -> service.depositar(1L,request));
        verify(contaRepository, never()).save(any());
        verify(transacaoRepository, never()).save(any());

    }

    @Test
    @DisplayName("deveriaSalvarTransacaoDeDepositoNoBanco")
    void SalvarDepositoNoBanco(){
        //Arrange
        Conta conta = contaSalvaValida();
        conta.setSaldo(new BigDecimal("100"));

        DepositoRequest request = depositoRequestValido();

        BDDMockito.given(contaRepository.findById(1L))
                .willReturn(Optional.of(conta));

        BDDMockito.given(transacaoRepository.save(any()))
                .willAnswer(invocation -> invocation.getArgument(0));

        //Act
        TransacaoResponse response = service.depositar(1L, request);

        //Assert
        ArgumentCaptor<Conta> contaCaptor = ArgumentCaptor.forClass(Conta.class);
        verify(contaRepository).save(contaCaptor.capture());
        Conta contaCapturada = contaCaptor.getValue();

        assertEquals(new BigDecimal("200"), contaCapturada.getSaldo());

        ArgumentCaptor<Transacao> transacaoCaptor = ArgumentCaptor.forClass(Transacao.class);
        verify(transacaoRepository).save(transacaoCaptor.capture());
        Transacao transacaoCapturada = transacaoCaptor.getValue();

        assertEquals(TipoTransacao.DEPOSITO, transacaoCapturada.getTipo());
        assertEquals(new BigDecimal("100"), transacaoCapturada.getValor());
        assertNull(transacaoCapturada.getContaOrigem());
        assertEquals(conta, transacaoCapturada.getContaDestino());

        assertEquals(TipoTransacao.DEPOSITO, response.tipo());
        assertEquals(new BigDecimal("100"), response.valor());
    }


    @Test
    @DisplayName("deveriaLancarExceptionSeContaDesativadaNoSaque")
    void LancarExceptionContaDesativadaSaque(){
        //Arrange
        Conta conta = contaSalvaValida();
        conta.setStatus(StatusConta.ENCERRADA);

        SaqueRequest request = saqueRequestValido();

        BDDMockito.given(contaRepository.findById(1L))
                .willReturn(Optional.of(conta));

        //Act + Assert
        Assertions.assertThrows(ContaInativaException.class,() -> service.sacar(1L,request));
        verify(contaRepository, never()).save(any());
        verify(transacaoRepository, never()).save(any());

    }

    @Test
    @DisplayName("deveriaLancarExceptionSeValorMaiorQueSaldo")
    void LancarExceptionValorMaiorQueSaldo(){
        //Arrange
        Conta conta = contaSalvaValida();
        conta.setStatus(StatusConta.ATIVA);
        conta.setSaldo(new BigDecimal("50"));

        SaqueRequest saqueRequest = saqueRequestValido();

        BDDMockito.given(contaRepository.findById(1L))
                .willReturn(Optional.of(conta));

        //Act+ Assert
        Assertions.assertThrows(SaldoInsuficienteException.class,() -> service.sacar(1L,saqueRequest));
        verify(contaRepository, never()).save(any());
        verify(transacaoRepository, never()).save(any());
    }


    @Test
    @DisplayName("deveriaSalvarTransacaoDeSaqueNoBanco")
    void SalvarSaqueNoBanco(){
        //Arrange
        Conta conta = contaSalvaValida();
        conta.setSaldo(new BigDecimal("200"));

        SaqueRequest request = saqueRequestValido(); // valor = 100

        BDDMockito.given(contaRepository.findById(1L))
                .willReturn(Optional.of(conta));

        BDDMockito.given(transacaoRepository.save(any()))
                .willAnswer(invocation -> invocation.getArgument(0));

        //Act
        TransacaoResponse response = service.sacar(1L, request);

        //Assert
        ArgumentCaptor<Conta> contaCaptor = ArgumentCaptor.forClass(Conta.class);
        verify(contaRepository).save(contaCaptor.capture());
        Conta contaCapturada = contaCaptor.getValue();

        assertEquals(new BigDecimal("100"), contaCapturada.getSaldo());

        ArgumentCaptor<Transacao> transacaoCaptor = ArgumentCaptor.forClass(Transacao.class);
        verify(transacaoRepository).save(transacaoCaptor.capture());
        Transacao transacaoCapturada = transacaoCaptor.getValue();

        assertEquals(TipoTransacao.SAQUE, transacaoCapturada.getTipo());
        assertEquals(new BigDecimal("100"), transacaoCapturada.getValor());
        assertEquals(conta, transacaoCapturada.getContaOrigem());
        assertNull(transacaoCapturada.getContaDestino());

        assertEquals(TipoTransacao.SAQUE, response.tipo());
        assertEquals(new BigDecimal("100"), response.valor());
    }

}
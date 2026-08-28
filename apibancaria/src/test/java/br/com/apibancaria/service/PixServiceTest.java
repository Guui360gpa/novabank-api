package br.com.apibancaria.service;

import br.com.apibancaria.dto.request.PixRequest;
import br.com.apibancaria.enums.StatusCliente;
import br.com.apibancaria.enums.StatusConta;
import br.com.apibancaria.enums.TipoChavePix;
import br.com.apibancaria.exception.ChavePixNaoEncontradaException;
import br.com.apibancaria.exception.ContaInativaException;
import br.com.apibancaria.exception.SaldoInsuficienteException;
import br.com.apibancaria.exception.TransferenciaParaSiMesmoException;
import br.com.apibancaria.model.ChavePix;
import br.com.apibancaria.model.Cliente;
import br.com.apibancaria.model.Conta;
import br.com.apibancaria.repository.ChavePixRepository;
import br.com.apibancaria.repository.ContaRepository;
import br.com.apibancaria.repository.TransacaoRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class PixServiceTest {

    @Mock
    private ContaRepository contaRepository;

    @Mock
    private TransacaoRepository transacaoRepository;

    @Mock
    private ChavePixRepository chavePixRepository;

    @InjectMocks
    private PixService pixService;

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

    private PixRequest pixRequestValido(){
        return new PixRequest(
                1L,
                chavePixValida().getChave(),
                BigDecimal.valueOf(230)

        );
    }

    private ChavePix chavePixValida(){
        ChavePix chavePix = new ChavePix();

        chavePix.setAtiva(true);
        chavePix.setChave("12345678910");
        Conta conta = contaSalvaValida();
        conta.setId(2L);
        conta.setNumeroConta("10987654321");
        chavePix.setConta(conta);
        chavePix.setTipo(TipoChavePix.CPF);
        chavePix.setId(1L);
        chavePix.setDataCadastro(LocalDateTime.now());

        return chavePix;
    }


    @Test
    @DisplayName("deveriaLancarExceptionEmTranferirSeContaOrigemDesativa")
    void LancarExceptionSeContaOrigemDesativa(){

        //Arrange
        Conta contaOrigem = contaSalvaValida();
        contaOrigem.setStatus(StatusConta.ENCERRADA);

        PixRequest request = pixRequestValido();

        BDDMockito.given(contaRepository.findById(request.contaOrigemId()))
                .willReturn(Optional.of(contaOrigem));

        //Act + Assert
        Assertions.assertThrows(ContaInativaException.class,() -> pixService.transferir(request));
        verify(contaRepository, never()).save(any());
        verify(transacaoRepository, never()).save(any());

    }

    @Test
    @DisplayName("deveriaLancarExceptionSeChavePixNaoEncontrada")
    void LancarExceptionSeChavePixNaoEncontrada(){
        //Arrange
        Conta contaOrigem = contaSalvaValida();
        contaOrigem.setStatus(StatusConta.ATIVA);

        PixRequest dto = pixRequestValido();

        BDDMockito.given(contaRepository.findById(dto.contaOrigemId()))
                .willReturn(Optional.of(contaOrigem));
        BDDMockito.given(chavePixRepository.findByChaveAndAtivaTrue(dto.chavePixDestino()))
                .willReturn(Optional.empty());

        //Act + Assert
        Assertions.assertThrows(ChavePixNaoEncontradaException.class,()-> pixService.transferir(dto));

        verify(contaRepository, never()).save(any());
        verify(transacaoRepository, never()).save(any());

    }

    @Test
    @DisplayName("deveriaNaoTranferirSeContaOrigemDesativa")
    void NaoTranferirSeContaDestinoDesativa(){

        //Arrange
        ChavePix chavePix = chavePixValida();
        Conta contaDestino = chavePix.getConta();
        contaDestino.setStatus(StatusConta.ENCERRADA);

        Conta contaOrigem = contaSalvaValida();
        contaOrigem.setStatus(StatusConta.ATIVA);

        PixRequest pixRequest = pixRequestValido();

        BDDMockito.given(contaRepository.findById(pixRequest.contaOrigemId()))
                .willReturn(Optional.of(contaOrigem));
        BDDMockito.given(chavePixRepository.findByChaveAndAtivaTrue(pixRequest.chavePixDestino()))
                .willReturn(Optional.of(chavePix));


        //Act + Assert
        Assertions.assertThrows(ContaInativaException.class,() -> pixService.transferir(pixRequest));
        verify(contaRepository, never()).save(any());
        verify(transacaoRepository, never()).save(any());

    }

    @Test
    @DisplayName("deveriaNaoTranferirSeContaOrigemForIgualQueContaDestino")
    void NaoTranferirSeContaOrigemIgualDestino(){
        //Arrange
        Conta contaOrigem = contaSalvaValida();

        ChavePix chavePix = chavePixValida();
        chavePix.getConta().setId(1L);
        chavePix.getConta().setStatus(StatusConta.ATIVA);

        PixRequest pixRequest = pixRequestValido();

        BDDMockito.given(contaRepository.findById(pixRequest.contaOrigemId()))
                .willReturn(Optional.of(contaOrigem));
        BDDMockito.given(chavePixRepository.findByChaveAndAtivaTrue(pixRequest.chavePixDestino()))
                .willReturn(Optional.of(chavePix));

        //Act + Assert
        Assertions.assertThrows(TransferenciaParaSiMesmoException.class,() -> pixService.transferir(pixRequestValido()));

        verify(contaRepository, never()).save(any());
        verify(transacaoRepository, never()).save(any());

    }

    @Test
    @DisplayName("deveriaNaoTranferirSeContaOrigemTiverSaldoInsuficiente")
    void NaoTranferirSeContaOrigemSaldoInsuficiente(){
        //Arrange
        Conta contaOrigem = contaSalvaValida();
        contaOrigem.setStatus(StatusConta.ATIVA);
        contaOrigem.setSaldo(BigDecimal.ZERO);

        ChavePix chavePix = chavePixValida();
        chavePix.getConta().setId(2L);
        chavePix.getConta().setStatus(StatusConta.ATIVA);

        PixRequest dto = pixRequestValido();

        BDDMockito.given(contaRepository.findById(dto.contaOrigemId()))
                .willReturn(Optional.of(contaOrigem));
        BDDMockito.given(chavePixRepository.findByChaveAndAtivaTrue(dto.chavePixDestino()))
                .willReturn(Optional.of(chavePix));

        //Act + Assert
        Assertions.assertThrows(SaldoInsuficienteException.class, () -> pixService.transferir(dto));

        verify(contaRepository, never()).save(any());
        verify(transacaoRepository, never()).save(any());
    }



}
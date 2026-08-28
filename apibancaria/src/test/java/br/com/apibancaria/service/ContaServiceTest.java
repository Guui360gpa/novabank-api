package br.com.apibancaria.service;

import br.com.apibancaria.dto.response.ContaResponse;
import br.com.apibancaria.enums.StatusCliente;
import br.com.apibancaria.enums.StatusConta;
import br.com.apibancaria.model.Cliente;
import br.com.apibancaria.model.Conta;
import br.com.apibancaria.repository.ContaRepository;
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
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class ContaServiceTest {

    @Mock
    private ContaRepository contaRepository;

    @InjectMocks
    private ContaService contaService;

    @Mock
    private Optional<Conta> optionalConta;

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

    @Test
    @DisplayName("deveriaCriarConta")
    void CriarConta(){
        //Arrange
        Cliente cliente = clienteSalvoValido();
        BDDMockito.given(contaRepository.existsByNumeroConta(any())).willReturn(false);


        //Act
        contaService.criarConta(cliente);


        //Assert
        ArgumentCaptor<Conta> captor = ArgumentCaptor.forClass(Conta.class);
        verify(contaRepository).save(captor.capture());
        Conta contaCapturada = captor.getValue();

        assertEquals(cliente,contaCapturada.getCliente());
        assertEquals("0001",contaCapturada.getAgencia());
        assertEquals(BigDecimal.ZERO,contaCapturada.getSaldo());
        assertEquals(StatusConta.ATIVA,contaCapturada.getStatus());
        assertNotNull(contaCapturada.getNumeroConta());
        assertNotNull(contaCapturada.getDataCriacao());
    }

    @Test
    @DisplayName("deveriaListarContasCriadas")
    void ListarContas(){
        //Arrange
        Conta conta1 = contaSalvaValida();
        Conta conta2 = contaSalvaValida();
        conta2.setId(2L);
        conta2.setNumeroConta("10987654321");
        Cliente cliente = clienteSalvoValido();
        cliente.setId(2L);
        cliente.setCpf("12345678910");
        cliente.setEmail("outro@gmail.com");
        conta2.setCliente(cliente);

        List<Conta> contas = List.of(conta1,conta2);

        BDDMockito.given(contaRepository.findAll()).willReturn(contas);

        List<ContaResponse> contasResponse = contas.stream()
                .map(c -> new ContaResponse(c.getId(),
                        c.getNumeroConta(),
                        c.getAgencia(),
                        c.getSaldo(),
                        c.getStatus(),
                        c.getDataCriacao()))
                .toList();


        //Act
        List<ContaResponse> responses = contaService.listar();

        //Assert
        assertEquals(contasResponse,responses);
    }

    @Test
    @DisplayName("deveriaBuscarContaPorId")
    void BuscarContaPorId(){
        //Arrange
        Conta conta = contaSalvaValida();
        BDDMockito.given(contaRepository.findById(1L)).willReturn(Optional.of(conta));

        ContaResponse respostaEsperada = new  ContaResponse(
                conta.getId(),
                conta.getNumeroConta(),
                conta.getAgencia(),
                conta.getSaldo(),
                conta.getStatus(),
                conta.getDataCriacao()
        );

        //Act
        ContaResponse resultado = contaService.buscarPorId(1L);


        //Assert
        assertEquals(respostaEsperada,resultado);


    }

}
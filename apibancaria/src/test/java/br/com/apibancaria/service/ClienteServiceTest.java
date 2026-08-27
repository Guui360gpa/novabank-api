package br.com.apibancaria.service;

import br.com.apibancaria.dto.request.ClienteRequest;
import br.com.apibancaria.dto.response.ClienteResponse;
import br.com.apibancaria.enums.StatusCliente;
import br.com.apibancaria.exception.ClienteNaoEncontradoException;
import br.com.apibancaria.exception.CpfJaCadastradoException;
import br.com.apibancaria.exception.EmailJaCadastradoException;
import br.com.apibancaria.model.Cliente;
import br.com.apibancaria.repository.ClienteRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class ClienteServiceTest {

    @Mock
    private ClienteRepository clienteRepository;

    @InjectMocks
    private ClienteService clienteService;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private ContaService contaService;

    @Mock
    private Optional<Cliente> optionalCliente;

    @Mock
    private ClienteRequest dto;

    private ClienteRequest clienteRequestValido() {
        return new ClienteRequest(
                "João Silva",
                "12345678900",
                "joao@email.com",
                "11999998888",
                "senha123"
        );
    }

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

    @Test
    @DisplayName("deveriaLancarExceptionSeCpfJaCadastrado")
    void ExceptionSeCpfJaCadastrado(){

        //Arrange
        ClienteRequest request = clienteRequestValido();
        BDDMockito.given(clienteRepository.existsByCpf(request.cpf())).willReturn(true);

        //Act + Assert
        Assertions.assertThrows(CpfJaCadastradoException.class,() -> clienteService.cadastrar(request));

    }

    @Test
    @DisplayName("deveriaLancarExceptionSeEmailJaCadastrado")
    void ExceptionSeEmailJaCadastrado(){
        //Arrange
        ClienteRequest request = clienteRequestValido();
        BDDMockito.given(clienteRepository.existsByEmail(request.email())).willReturn(true);

        //Act + Assert
        Assertions.assertThrows(EmailJaCadastradoException.class,() -> clienteService.cadastrar(request));

    }

    @Test
    @DisplayName("deveriaCriptografarSenhaAntesDoTerminoDoCadastro")
    void SenhaCriptografadaCorretamente(){
        //Arrange
        ClienteRequest request = clienteRequestValido();
        Cliente cliente = clienteSalvoValido();

        BDDMockito.given(clienteRepository.existsByCpf(request.cpf())).willReturn(false);
        BDDMockito.given(clienteRepository.existsByEmail(request.email())).willReturn(false);
        BDDMockito.given(passwordEncoder.encode(request.senha())).willReturn("hashFicticio123");
        BDDMockito.given(clienteRepository.save(any())).willReturn(cliente);

        //Act
        clienteService.cadastrar(request);

        //Assert

        ArgumentCaptor<Cliente> captor = ArgumentCaptor.forClass(Cliente.class);
        verify(clienteRepository).save(captor.capture());
        Cliente clienteCapturado = captor.getValue();

        assertEquals("hashFicticio123", clienteCapturado.getSenha());
        assertNotEquals(request.senha(), clienteCapturado.getSenha());

    }

    @Test
    @DisplayName("deveriaListarTodosOsClientes")
    void ListarClientes(){
        //Arrange
        Cliente cliente1 = clienteSalvoValido();
        Cliente cliente2 = clienteSalvoValido();
        cliente2.setId(2L);
        cliente2.setCpf("12345678910");
        cliente2.setEmail("outro@gmail.com");

        List<Cliente> listClientes = List.of(cliente1,cliente2);
        BDDMockito.given(clienteRepository.findAll()).willReturn(listClientes);

        List<ClienteResponse> respostasEsperadas = List.of(
                new ClienteResponse(cliente1.getId(), cliente1.getNome(), cliente1.getCpf(), cliente1.getEmail()),
                new ClienteResponse(cliente2.getId(), cliente2.getNome(), cliente2.getCpf(), cliente2.getEmail())
        );

        //Act + Assert
        Assertions.assertEquals(respostasEsperadas,clienteService.listar());


    }

    @Test
    @DisplayName("deveriaBuscarClientePorId")
    void BuscarClientePorId(){
        //Arrange
        BDDMockito.given(clienteRepository.findById(1L)).willReturn(optionalCliente);
        BDDMockito.given(optionalCliente.isPresent()).willReturn(true);
        Cliente cliente = clienteSalvoValido();
        BDDMockito.given(optionalCliente.get()).willReturn(cliente);

        ClienteResponse response = new ClienteResponse(cliente.getId(),cliente.getNome(),cliente.getCpf(),cliente.getEmail());

        //Act + Assert
        Assertions.assertEquals(response,clienteService.buscarPorId(1L));
    }


    @Test
    @DisplayName("deveriaNaoBuscarClientePorId")
    void NaoBuscarClientePorId(){
        //Arrange
        BDDMockito.given(clienteRepository.findById(1L)).willReturn(Optional.empty());

        //Act + Assert
        Assertions.assertThrows(ClienteNaoEncontradoException.class,() -> clienteService.buscarPorId(1L));
    }

    @Test
    @DisplayName("deveriaAtualizarClientePorId")
    void AtualizarClientePorId(){

        //Arrange

        Cliente cliente = clienteSalvoValido();
        ClienteRequest dto = new ClienteRequest(
                "Novo nome",
                    "12345678910",
                "novo@email.com",
                "11999999999",
                "novaSenha"
        );
        BDDMockito.given(clienteRepository.findById(1L)).willReturn(Optional.of(cliente));
        BDDMockito.given(clienteRepository.existsByEmailAndIdNot(dto.email(),1L)).willReturn(false);
        BDDMockito.given(passwordEncoder.encode(dto.senha())).willReturn("senhaCriptografada");
        BDDMockito.given(clienteRepository.save(cliente)).willReturn(cliente);

        //Act

        ClienteResponse response = clienteService.atualizarPorId(1L, dto);

        //Assert

        Assertions.assertNotNull(response);
        Assertions.assertEquals(cliente.getId(), response.id());
        Assertions.assertEquals(dto.nome(),response.nome());
        Assertions.assertEquals(dto.email(),response.email());


        Assertions.assertEquals(dto.nome(), cliente.getNome());
        Assertions.assertEquals(dto.email(), cliente.getEmail());
        Assertions.assertEquals(dto.telefone(), cliente.getTelefone());
        Assertions.assertEquals("senhaCriptografada", cliente.getSenha());

        BDDMockito.then(clienteRepository).should().findById(1L);
        BDDMockito.then(clienteRepository).should()
                .existsByEmailAndIdNot(dto.email(), 1L);
        BDDMockito.then(passwordEncoder).should().encode(dto.senha());
        BDDMockito.then(clienteRepository).should().save(cliente);
    }

    @Test
    @DisplayName("deveriaExcluirClientePorId")
    void ExcluirClientePorId(){
        //Arrange
        Cliente cliente = clienteSalvoValido();
        BDDMockito.given(clienteRepository.findById(1L)).willReturn(Optional.of(cliente));
        StatusCliente statusCliente = cliente.getStatus();

        //Act
        clienteService.excluirPorId(1L);

        //Assert
        Assertions.assertNotEquals(cliente.getStatus(),statusCliente);
    }



}
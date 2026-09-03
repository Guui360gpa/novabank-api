package br.com.apibancaria.controller;

import br.com.apibancaria.service.ClienteService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;

import static org.junit.jupiter.api.Assertions.*;

@WebMvcTest(ClienteController.class)
class ClienteControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ClienteService clienteService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("deveriaCadastrarClienteComSucesso")
    void CadastrarClienteComSucesso(){

    }

    @Test
    @DisplayName("deveriaRetornarBadRequestQuandoRequestDeCadastroForInvalido")
    void RetornarBadRequestCadastroInvalido()throws Exception {

    }

    @Test
    @DisplayName("deveriaRetornarConflictQuandoCpfJaCadastrado")
    void RetornarConflictCpfDuplicado() throws Exception {

    }

    @Test
    @DisplayName("deveriaListarTodosOsClientes")
    void ListarTodosOsClientes() throws Exception {

    }

    @Test
    @DisplayName("deveriaListarClientesVazioQuandoNaoHaCadastros")
    void ListarClientesVazio() throws Exception {

    }

    @Test
    @DisplayName("deveriaBuscarClientePorIdComSucesso")
    void BuscarClientePorIdComSucesso() throws Exception {

    }

    @Test
    @DisplayName("deveriaRetornarNotFoundQuandoClienteNaoExiste")
    void RetornarNotFoundClienteInexistente() throws Exception {

    }

    @Test
    @DisplayName("deveriaAtualizarClienteComSucesso")
    void AtualizarClienteComSucessi() throws Exception {

    }

    @Test
    @DisplayName("deveriaRetornarBadRequestDeAtualizacaoForInvalido")
    void RetornarBadRequestAtualizacaoInvalida() throws Exception {

    }

    @Test
    @DisplayName("deveriaExcluirClienteComSucesso")
    void ExcluirClienteComSucesso() throws Exception {

    }

}
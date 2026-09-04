package br.com.apibancaria.controller;

import br.com.apibancaria.dto.request.ClienteRequest;
import br.com.apibancaria.dto.response.ClienteResponse;
import br.com.apibancaria.exception.ClienteNaoEncontradoException;
import br.com.apibancaria.exception.CpfJaCadastradoException;
import br.com.apibancaria.service.ClienteService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;


@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
class ClienteControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ClienteService service;

    @Autowired
    private ObjectMapper objectMapper;

    private ClienteRequest clienteRequestValido() {
        return new ClienteRequest(
                "Vanderson Lima Freitas",
                "12345678910",
                "vanlimafre13@gmail.com",
                "11999998888",
                "senha123"
        );
    }

    @Test
    @DisplayName("deveriaCadastrarClienteComSucesso")
    void CadastrarClienteComSucesso() throws Exception {
        //Arrange
        String json = """
                    {
                        "nome": "Vanderson Lima Freitas",
                        "cpf": "12345678910",
                        "email": "vanlimafre13@gmail.com",
                        "telefone": "11999998888",
                        "senha": "senha123"
                    }
                """;

        ClienteResponse responseEsperado = new ClienteResponse(
                1L, "Vanderson Lima Freitas", "12345678910", "vanlimafre13@gmail.com"
        );

        given(service.cadastrar(any())).willReturn(responseEsperado);

        //Act
        MockHttpServletResponse response = mockMvc.perform(
                        post("/clientes")
                                .content(json)
                                .contentType(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        //Assert
        Assertions.assertEquals(201,response.getStatus());
    }

    @Test
    @DisplayName("deveriaRetornarBadRequestQuandoRequestDeCadastroForInvalido")
    void RetornarBadRequestCadastroInvalido()throws Exception {
        //Arrange
        String json = """
                    {
                        "nome": "",
                        "cpf": "12345678910",
                        "email": "vanlimafre13@gmail.com",
                        "telefone": "11999998888",
                        "senha": "senha123"
                    }
                """;

        //Act
        MockHttpServletResponse response = mockMvc.perform(
                        post("/clientes")
                                .content(json)
                                .contentType(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        //Assert
        Assertions.assertEquals(400,response.getStatus());
        verify(service, never()).cadastrar(any());

    }

    @Test
    @DisplayName("deveriaRetornarConflictQuandoCpfJaCadastrado")
    void RetornarConflictCpfDuplicado() throws Exception {
        //Arrange
        String json = """
                    {
                        "nome": "Vanderson Lima Freitas",
                        "cpf": "12345678910",
                        "email": "vanlimafre13@gmail.com",
                        "telefone": "11999998888",
                        "senha": "senha123"
                    }
                """;
        given(service.cadastrar(any())).willThrow(new CpfJaCadastradoException("CPF já cadastrado"));

        //Act
        MockHttpServletResponse response = mockMvc.perform(
                        post("/clientes")
                                .content(json)
                                .contentType(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        //Assert
        Assertions.assertEquals(409,response.getStatus());
    }

    @Test
    @DisplayName("deveriaListarTodosOsClientes")
    void ListarTodosOsClientes() throws Exception {
        //Act
        MockHttpServletResponse response = mockMvc.perform(
                get("/clientes"))
                .andReturn().getResponse();

        //Assert
        Assertions.assertEquals(200,response.getStatus());
    }

    @Test
    @DisplayName("deveriaListarClientesVazioQuandoNaoHaCadastros")
    void ListarClientesVazio() throws Exception {
        //Arrange
        given(service.listar()).willReturn(null);

        //Act
        MockHttpServletResponse response = mockMvc.perform(
                        get("/clientes"))
                .andReturn().getResponse();

        //Assert
        Assertions.assertEquals(200,response.getStatus());
    }

    @Test
    @DisplayName("deveriaBuscarClientePorIdComSucesso")
    void BuscarClientePorIdComSucesso() throws Exception {
        //Arrange
        ClienteResponse responseEsperado = new ClienteResponse(
                1L, "Vanderson Lima Freitas", "12345678910", "vanlimafre13@gmail.com"
        );
        given(service.buscarPorId(1L)).willReturn(responseEsperado);

        //Act
        MockHttpServletResponse response = mockMvc.perform(
                        get("/clientes/{id}",1L))
                .andReturn().getResponse();

        //Assert
        Assertions.assertEquals(200,response.getStatus());

    }

    @Test
    @DisplayName("deveriaRetornarNotFoundQuandoClienteNaoExiste")
    void RetornarNotFoundClienteInexistente() throws Exception {
        //Arrange
        given(service.buscarPorId(1L)).willThrow(new ClienteNaoEncontradoException("Cliente nao encontrado"));

        //Act
        MockHttpServletResponse response = mockMvc.perform(
                        get("/clientes/{id}",1L))
                .andReturn().getResponse();

        //Assert
        Assertions.assertEquals(404,response.getStatus());

    }

    @Test
    @DisplayName("deveriaAtualizarClienteComSucesso")
    void AtualizarClienteComSucesso() throws Exception {
        //Arrange
        ClienteRequest clienteRequest = clienteRequestValido();
        ClienteResponse responseEsperado = new ClienteResponse(
                1L, "Vanderson Lima Freitas", "12345678910", "vanlimafre13@gmail.com"
        );

        given(service.atualizarPorId(1L, clienteRequest)).willReturn(responseEsperado);

        //Act
        MockHttpServletResponse response = mockMvc.perform(
                        put("/clientes/{id}", 1L)
                                .content(objectMapper.writeValueAsString(clienteRequest))
                                .contentType(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        //Assert
        Assertions.assertEquals(200, response.getStatus());
    }

    @Test
    @DisplayName("deveriaRetornarBadRequestDeAtualizacaoForInvalido")
    void RetornarBadRequestAtualizacaoInvalida() throws Exception {
        //Arrange
        String json = """
                {
                    "nome": "",
                    "cpf": "12345678910",
                    "email": "vanlimafre13@gmail.com",
                    "telefone": "11999998888",
                    "senha": "senha123"
                }
            """;

        //Act
        MockHttpServletResponse response = mockMvc.perform(
                        put("/clientes/{id}", 1L)
                                .content(json)
                                .contentType(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        //Assert
        Assertions.assertEquals(400, response.getStatus());
        verify(service, never()).atualizarPorId(any(), any());
    }

    @Test
    @DisplayName("deveriaExcluirClienteComSucesso")
    void ExcluirClienteComSucesso() throws Exception {
        //Act
        MockHttpServletResponse response = mockMvc.perform(
                        delete("/clientes/{id}", 1L))
                .andReturn().getResponse();

        //Assert
        Assertions.assertEquals(204, response.getStatus());
        Assertions.assertEquals("", response.getContentAsString());
        verify(service).excluirPorId(1L);
    }

}
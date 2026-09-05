package br.com.apibancaria.controller;

import br.com.apibancaria.dto.response.ClienteResponse;
import br.com.apibancaria.dto.response.ContaResponse;
import br.com.apibancaria.enums.StatusConta;
import br.com.apibancaria.exception.ClienteNaoEncontradoException;
import br.com.apibancaria.exception.ContaNaoEncontradaException;
import br.com.apibancaria.service.ContaService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
class ContaControllerTest {

    @MockitoBean
    private ContaService contaService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("deveriaListarTodasAsContas")
    void ListarContas() throws Exception{
        //Act
        MockHttpServletResponse response = mockMvc.perform(
                        get("/contas"))
                .andReturn().getResponse();

        //Assert
        Assertions.assertEquals(200,response.getStatus());
    }

    @Test
    @DisplayName("deveriaBuscarContaPorIdComSucesso")
    void BuscarContaPorIdComSucesso() throws  Exception{
        //Arrange
        ContaResponse responseEsperado = new ContaResponse(
                1L, "12345678910", "0001", new BigDecimal("230"), StatusConta.ATIVA, LocalDateTime.now()
        );
        given(contaService.buscarPorId(1L)).willReturn(responseEsperado);

        //Act
        MockHttpServletResponse response = mockMvc.perform(
                        get("/contas/{id}",1L))
                .andReturn().getResponse();

        //Assert
        Assertions.assertEquals(200,response.getStatus());

    }

    @Test
    @DisplayName("deveriaRetornarNotFoundQuandoContaNaoExiste")
    void RetornarNotFoundContaInexistente() throws Exception{
        //Arrange
        given(contaService.buscarPorId(1L)).willThrow(new ContaNaoEncontradaException("Conta nao encontrada"));

        //Act
        MockHttpServletResponse response = mockMvc.perform(
                        get("/clientes/{id}",1L))
                .andReturn().getResponse();

        //Assert
        Assertions.assertEquals(404,response.getStatus());

    }



}
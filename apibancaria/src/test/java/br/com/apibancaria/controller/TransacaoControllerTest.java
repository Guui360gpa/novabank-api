package br.com.apibancaria.controller;

import br.com.apibancaria.dto.request.DepositoRequest;
import br.com.apibancaria.dto.request.SaqueRequest;
import br.com.apibancaria.dto.response.TransacaoResponse;
import br.com.apibancaria.enums.TipoTransacao;
import br.com.apibancaria.exception.ContaInativaException;
import br.com.apibancaria.exception.ContaNaoEncontradaException;
import br.com.apibancaria.exception.SaldoInsuficienteException;
import br.com.apibancaria.service.TransacaoService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
@WithMockUser
@RequiredArgsConstructor
class TransacaoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private TransacaoService service;

    private final ObjectMapper objectMapper = new ObjectMapper();

    private DepositoRequest depositoRequestValido() {
        return new DepositoRequest(new BigDecimal("100"));
    }

    private SaqueRequest saqueRequestValido() {
        return new SaqueRequest(new BigDecimal("100"));
    }

    private TransacaoResponse transacaoResponseValida(TipoTransacao tipo) {
        return new TransacaoResponse(1L, tipo, new BigDecimal("100"), LocalDateTime.now(), "Operação Realizada");
    }



    @Test
    @DisplayName("deveriaDepositarComSucesso")
    void DepositarComSucesso() throws Exception {
        given(service.depositar(eq(1L), any())).willReturn(transacaoResponseValida(TipoTransacao.DEPOSITO));

        MockHttpServletResponse response = mockMvc.perform(post("/contas/{id}/deposito", 1L)
                        .content(objectMapper.writeValueAsString(depositoRequestValido()))
                        .contentType(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        assertEquals(201, response.getStatus());
    }

    @Test
    @DisplayName("deveriaRetornarBadRequestQuandoDepositoRequestInvalido")
    void RetornarBadRequestDepositoInvalido() throws Exception {
        String json = """
                {
                    "valor": -10
                }
                """;

        MockHttpServletResponse response = mockMvc.perform(post("/contas/{id}/deposito", 1L)
                        .content(json)
                        .contentType(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        assertEquals(400, response.getStatus());
        verify(service, never()).depositar(any(), any());
    }

    @Test
    @DisplayName("deveriaRetornarNotFoundQuandoContaNaoEncontradaNoDeposito")
    void RetornarNotFoundContaNaoEncontradaDeposito() throws Exception {
        given(service.depositar(eq(1L), any()))
                .willThrow(new ContaNaoEncontradaException("A conta não foi encontrada!"));

        MockHttpServletResponse response = mockMvc.perform(post("/contas/{id}/deposito", 1L)
                        .content(objectMapper.writeValueAsString(depositoRequestValido()))
                        .contentType(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        assertEquals(404, response.getStatus());
    }

    @Test
    @DisplayName("deveriaRetornarConflictQuandoContaInativaNoDeposito")
    void RetornarConflictContaInativaDeposito() throws Exception {
        given(service.depositar(eq(1L), any()))
                .willThrow(new ContaInativaException("A conta está inativa!"));

        MockHttpServletResponse response = mockMvc.perform(post("/contas/{id}/deposito", 1L)
                        .content(objectMapper.writeValueAsString(depositoRequestValido()))
                        .contentType(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        assertEquals(409, response.getStatus());
    }


    @Test
    @DisplayName("deveriaSacarComSucesso")
    void SacarComSucesso() throws Exception {
        given(service.sacar(eq(1L), any())).willReturn(transacaoResponseValida(TipoTransacao.SAQUE));

        MockHttpServletResponse response = mockMvc.perform(post("/contas/{id}/saque", 1L)
                        .content(objectMapper.writeValueAsString(saqueRequestValido()))
                        .contentType(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        assertEquals(201, response.getStatus());
    }

    @Test
    @DisplayName("deveriaRetornarBadRequestQuandoSaqueRequestInvalido")
    void RetornarBadRequestSaqueInvalido() throws Exception {
        String json = """
                {
                    "valor": -10
                }
                """;

        MockHttpServletResponse response = mockMvc.perform(post("/contas/{id}/saque", 1L)
                        .content(json)
                        .contentType(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        assertEquals(400, response.getStatus());
        verify(service, never()).sacar(any(), any());
    }

    @Test
    @DisplayName("deveriaRetornarNotFoundQuandoContaNaoEncontradaNoSaque")
    void RetornarNotFoundContaNaoEncontradaSaque() throws Exception {
        given(service.sacar(eq(1L), any()))
                .willThrow(new ContaNaoEncontradaException("A conta não foi encontrada!"));

        MockHttpServletResponse response = mockMvc.perform(post("/contas/{id}/saque", 1L)
                        .content(objectMapper.writeValueAsString(saqueRequestValido()))
                        .contentType(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        assertEquals(404, response.getStatus());
    }

    @Test
    @DisplayName("deveriaRetornarConflictQuandoContaInativaNoSaque")
    void RetornarConflictContaInativaSaque() throws Exception {
        given(service.sacar(eq(1L), any()))
                .willThrow(new ContaInativaException("A conta está inativa!"));

        MockHttpServletResponse response = mockMvc.perform(post("/contas/{id}/saque", 1L)
                        .content(objectMapper.writeValueAsString(saqueRequestValido()))
                        .contentType(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        assertEquals(409, response.getStatus());
    }

    @Test
    @DisplayName("deveriaRetornarBadRequestQuandoSaldoInsuficienteNoSaque")
    void RetornarBadRequestSaldoInsuficienteSaque() throws Exception {
        given(service.sacar(eq(1L), any()))
                .willThrow(new SaldoInsuficienteException("O saldo é insuficiente!"));

        MockHttpServletResponse response = mockMvc.perform(post("/contas/{id}/saque", 1L)
                        .content(objectMapper.writeValueAsString(saqueRequestValido()))
                        .contentType(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        assertEquals(400, response.getStatus());
    }


    @Test
    @DisplayName("deveriaRetornarExtratoComSucesso")
    void RetornarExtratoComSucesso() throws Exception {
        given(service.extrato(1L)).willReturn(List.of(transacaoResponseValida(TipoTransacao.DEPOSITO)));

        MockHttpServletResponse response = mockMvc.perform(get("/contas/{id}/extrato", 1L))
                .andReturn().getResponse();

        assertEquals(200, response.getStatus());
    }

    @Test
    @DisplayName("deveriaRetornarNotFoundQuandoContaNaoEncontradaNoExtrato")
    void RetornarNotFoundContaNaoEncontradaExtrato() throws Exception {
        given(service.extrato(1L))
                .willThrow(new ContaNaoEncontradaException("A conta não foi encontrada!"));

        MockHttpServletResponse response = mockMvc.perform(get("/contas/{id}/extrato", 1L))
                .andReturn().getResponse();

        assertEquals(404, response.getStatus());
    }
}
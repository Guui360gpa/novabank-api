package br.com.apibancaria.controller;

import br.com.apibancaria.dto.request.ChavePixRequest;
import br.com.apibancaria.dto.request.PixRequest;
import br.com.apibancaria.dto.response.ChavePixResponse;
import br.com.apibancaria.dto.response.TransacaoResponse;
import br.com.apibancaria.enums.TipoChavePix;
import br.com.apibancaria.enums.TipoTransacao;
import br.com.apibancaria.exception.*;
import br.com.apibancaria.service.PixService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
@RequiredArgsConstructor
@WithMockUser
class PixControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private PixService service;

    private final ObjectMapper objectMapper = new ObjectMapper();

    private PixRequest pixRequestValido() {
        return new PixRequest(1L, "chave@email.com", new BigDecimal("100"));
    }

    private ChavePixRequest chavePixRequestValido() {
        return new ChavePixRequest(TipoChavePix.EMAIL, "chave@email.com", 1L);
    }

    private TransacaoResponse transacaoResponseValida() {
        return new TransacaoResponse(1L, TipoTransacao.PIX, new BigDecimal("100"), LocalDateTime.now(), "Pix Realizado");
    }

    private ChavePixResponse chavePixResponseValida() {
        return new ChavePixResponse(1L, TipoChavePix.EMAIL, "chave@email.com", true, LocalDateTime.now());
    }

    @Test
    @DisplayName("deveriaTransferirComSucesso")
    void TransferirComSucesso() throws Exception {
        //Arrange
        PixRequest request = pixRequestValido();
        given(service.transferir(request)).willReturn(transacaoResponseValida());

        //Act
        MockHttpServletResponse response = mockMvc.perform(
                        post("/pix")
                                .content(objectMapper.writeValueAsString(request))
                                .contentType(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        //Assert
        Assertions.assertEquals(201, response.getStatus());
    }

    @Test
    @DisplayName("deveriaRetornarBadRequestQuandoRequestDeTransferenciaForInvalido")
    void RetornarBadRequestTransferenciaInvalida() throws Exception {
        //Arrange
        String json = """
                {
                    "contaOrigemId": null,
                    "chavePixDestino": "chave@email.com",
                    "valor": 100
                }
                """;

        //Act
        MockHttpServletResponse response = mockMvc.perform(
                        post("/pix")
                                .content(json)
                                .contentType(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        //Assert
        Assertions.assertEquals(400, response.getStatus());
        verify(service, never()).transferir(any());
    }

    @Test
    @DisplayName("deveriaRetornarNotFoundQuandoContaOrigemNaoExiste")
    void RetornarNotFoundContaOrigemInexistente() throws Exception {
        //Arrange
        PixRequest request = pixRequestValido();
        given(service.transferir(request))
                .willThrow(new ContaNaoEncontradaException("Conta origem não encontrada"));

        //Act
        MockHttpServletResponse response = mockMvc.perform(
                        post("/pix")
                                .content(objectMapper.writeValueAsString(request))
                                .contentType(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        //Assert
        Assertions.assertEquals(404, response.getStatus());
    }

    @Test
    @DisplayName("deveriaRetornarNotFoundQuandoChavePixNaoExiste")
    void RetornarNotFoundChavePixInexistente() throws Exception {
        //Arrange
        PixRequest request = pixRequestValido();
        given(service.transferir(request))
                .willThrow(new ChavePixNaoEncontradaException("Chave pix não encontrada"));

        //Act
        MockHttpServletResponse response = mockMvc.perform(
                        post("/pix")
                                .with(csrf())
                                .content(objectMapper.writeValueAsString(request))
                                .contentType(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        //Assert
        Assertions.assertEquals(404, response.getStatus());
    }

    @Test
    @DisplayName("deveriaRetornarConflictQuandoContaInativaNaTransferencia")
    void RetornarConflictContaInativaTransferencia() throws Exception {
        //Arrange
        PixRequest request = pixRequestValido();
        given(service.transferir(request))
                .willThrow(new ContaInativaException("Conta inativa"));

        //Act
        MockHttpServletResponse response = mockMvc.perform(
                        post("/pix")
                                .with(csrf())
                                .content(objectMapper.writeValueAsString(request))
                                .contentType(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        //Assert
        Assertions.assertEquals(409, response.getStatus());
    }

    @Test
    @DisplayName("deveriaRetornarConflictQuandoTransferenciaParaSiMesmo")
    void RetornarConflictTransferenciaParaSiMesmo() throws Exception {
        //Arrange
        PixRequest request = pixRequestValido();
        given(service.transferir(request))
                .willThrow(new TransferenciaParaSiMesmoException("Transferência inválida"));

        //Act
        MockHttpServletResponse response = mockMvc.perform(
                        post("/pix")
                                .with(csrf())
                                .content(objectMapper.writeValueAsString(request))
                                .contentType(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        //Assert
        Assertions.assertEquals(409, response.getStatus());
    }

    @Test
    @DisplayName("deveriaRetornarConflictQuandoSaldoInsuficienteNaTransferencia")
    void RetornarConflictSaldoInsuficienteTransferencia() throws Exception {
        //Arrange
        PixRequest request = pixRequestValido();
        given(service.transferir(request))
                .willThrow(new SaldoInsuficienteException("Saldo insuficiente"));

        //Act
        MockHttpServletResponse response = mockMvc.perform(
                        post("/pix")
                                .with(csrf())
                                .content(objectMapper.writeValueAsString(request))
                                .contentType(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        //Assert
        Assertions.assertEquals(409, response.getStatus());
    }

    // ---------- POST /pix/chaves ----------

    @Test
    @DisplayName("deveriaCadastrarChaveComSucesso")
    void CadastrarChaveComSucesso() throws Exception {
        //Arrange
        ChavePixRequest request = chavePixRequestValido();
        given(service.cadastrar(request)).willReturn(chavePixResponseValida());

        //Act
        MockHttpServletResponse response = mockMvc.perform(
                        post("/pix/chaves")
                                .with(csrf())
                                .content(objectMapper.writeValueAsString(request))
                                .contentType(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        //Assert
        Assertions.assertEquals(201, response.getStatus());
    }

    @Test
    @DisplayName("deveriaRetornarBadRequestQuandoRequestDeChaveForInvalido")
    void RetornarBadRequestChaveInvalida() throws Exception {
        //Arrange
        String json = """
                {
                    "tipo": null,
                    "chave": "chave@email.com",
                    "idConta": 1
                }
                """;

        //Act
        MockHttpServletResponse response = mockMvc.perform(
                        post("/pix/chaves")
                                .with(csrf())
                                .content(json)
                                .contentType(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        //Assert
        Assertions.assertEquals(400, response.getStatus());
        verify(service, never()).cadastrar(any());
    }

    @Test
    @DisplayName("deveriaRetornarConflictQuandoChaveJaCadastrada")
    void RetornarConflictChaveJaCadastrada() throws Exception {
        //Arrange
        ChavePixRequest request = chavePixRequestValido();
        given(service.cadastrar(request))
                .willThrow(new ChavePixJaCadastradaException("Chave pix indisponível"));

        //Act
        MockHttpServletResponse response = mockMvc.perform(
                        post("/pix/chaves")
                                .with(csrf())
                                .content(objectMapper.writeValueAsString(request))
                                .contentType(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        //Assert
        Assertions.assertEquals(409, response.getStatus());
    }

    @Test
    @DisplayName("deveriaRetornarNotFoundQuandoContaNaoExisteNoCadastroDeChave")
    void RetornarNotFoundContaInexistenteCadastroChave() throws Exception {
        //Arrange
        ChavePixRequest request = chavePixRequestValido();
        given(service.cadastrar(request))
                .willThrow(new ContaNaoEncontradaException("Conta não encontrada"));

        //Act
        MockHttpServletResponse response = mockMvc.perform(
                        post("/pix/chaves")
                                .with(csrf())
                                .content(objectMapper.writeValueAsString(request))
                                .contentType(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        //Assert
        Assertions.assertEquals(404, response.getStatus());
    }

    // ---------- DELETE /pix/chaves/{id} ----------

    @Test
    @DisplayName("deveriaExcluirChaveComSucesso")
    void ExcluirChaveComSucesso() throws Exception {
        //Act
        MockHttpServletResponse response = mockMvc.perform(
                        delete("/pix/chaves/{id}", 1L)
                                .with(csrf()))
                .andReturn().getResponse();

        //Assert
        Assertions.assertEquals(204, response.getStatus());
        verify(service).excluir(1L);
    }

    @Test
    @DisplayName("deveriaRetornarNotFoundQuandoChaveNaoExisteParaExcluir")
    void RetornarNotFoundChaveInexistenteExcluir() throws Exception {
        //Arrange
        org.mockito.BDDMockito.willThrow(new ChavePixNaoEncontradaException("Chave não encontrada"))
                .given(service).excluir(1L);

        //Act
        MockHttpServletResponse response = mockMvc.perform(
                        delete("/pix/chaves/{id}", 1L)
                                .with(csrf()))
                .andReturn().getResponse();

        //Assert
        Assertions.assertEquals(404, response.getStatus());
    }

    // ---------- GET /pix/chaves ----------

    @Test
    @DisplayName("deveriaListarChavesComSucesso")
    void ListarChavesComSucesso() throws Exception {
        //Arrange
        List<ChavePixResponse> chaves = List.of(chavePixResponseValida());
        given(service.listar(1L)).willReturn(chaves);

        //Act
        MockHttpServletResponse response = mockMvc.perform(get("/pix/chaves").param("id", "1"))
                .andReturn().getResponse();

        //Assert
        Assertions.assertEquals(200, response.getStatus());
    }

    @Test
    @DisplayName("deveriaRetornarNotFoundQuandoContaNaoExisteNaListagemDeChaves")
    void RetornarNotFoundContaInexistenteListagemChaves() throws Exception {
        //Arrange
        given(service.listar(1L)).willThrow(new ContaNaoEncontradaException("Conta não encontrada"));

        //Act
        MockHttpServletResponse response = mockMvc.perform(get("/pix/chaves").param("id", "1"))
                .andReturn().getResponse();

        //Assert
        Assertions.assertEquals(404, response.getStatus());
    }
}
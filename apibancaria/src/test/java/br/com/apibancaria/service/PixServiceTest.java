package br.com.apibancaria.service;

import br.com.apibancaria.dto.request.PixRequest;
import br.com.apibancaria.repository.ChavePixRepository;
import br.com.apibancaria.repository.ContaRepository;
import br.com.apibancaria.repository.TransacaoRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

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

    private PixRequest pixRequest;


    @Test
    @DisplayName("deveriaNaoTranferirSeContaOrigemDesativa")
    void NaoTranferirSeContaOrigemDesativa(){

    }

    @Test
    @DisplayName("deveriaEncontrarChavePix")
    void EncontrarChavePix(){

    }

    @Test
    @DisplayName("deveriaNaoTranferirSeContaOrigemDesativa")
    void NaoTranferirSeContaDestinoDesativa(){

    }

    @Test
    @DisplayName("deveriaNaoTranferirSeContaOrigemForIgualQueContaDestino")
    void NaoTranferirSeContaOrigemIgualDestino(){

    }

    @Test
    @DisplayName("deveriaNaoTranferirSeContaOrigemTiverSaldoInsuficiente")
    void NaoTranferirSeContaOrigemSaldoInsuficiente(){

    }



}
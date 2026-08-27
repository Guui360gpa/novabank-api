package br.com.apibancaria.service;

import br.com.apibancaria.model.Cliente;
import br.com.apibancaria.repository.ContaRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class ContaServiceTest {

    @Mock
    private ContaRepository contaRepository;

    @InjectMocks
    private ContaService contaService;

    @Mock
    private Cliente cliente;

    @Test
    @DisplayName("deveriaCriarConta")
    void CriarConta(){
        //Arrange
        //Act
        //Assert
    }

    @Test
    @DisplayName("deveriaListarContasCriadas")
    void ListarContas(){
        //Arrange
        //Act
        //Assert
    }

    @Test
    @DisplayName("deveriaBuscarContaPorId")
    void BuscarContaPorId(){
        //Arrange
        //Act
        //Assert
    }

}
package br.com.apibancaria.service;

import br.com.apibancaria.dto.request.ChavePixRequest;
import br.com.apibancaria.dto.request.PixRequest;
import br.com.apibancaria.dto.response.ChavePixResponse;
import br.com.apibancaria.dto.response.TransacaoResponse;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class PixService {
    public TransacaoResponse transferir( PixRequest dto) {
    }

    public ChavePixResponse cadastrar(ChavePixRequest dto) {
    }

    public List<ChavePixResponse> listar(Long id) {
        return null;
    }

    public void excluir(Long id) {
    }
}

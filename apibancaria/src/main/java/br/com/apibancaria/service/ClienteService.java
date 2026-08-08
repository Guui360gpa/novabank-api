package br.com.apibancaria.service;

import br.com.apibancaria.dto.request.ClienteRequest;
import br.com.apibancaria.dto.response.ClienteResponse;
import br.com.apibancaria.enums.StatusCliente;
import br.com.apibancaria.exception.ClienteNaoEncontradoException;
import br.com.apibancaria.exception.CpfJaCadastradoException;
import br.com.apibancaria.exception.EmailJaCadastradoException;
import br.com.apibancaria.model.Cliente;
import br.com.apibancaria.repository.ClienteRepository;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ClienteService {

    private final ClienteRepository clienteRepository;
    private final ContaService contaService;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public ClienteResponse cadastrar(ClienteRequest dto){

        if(clienteRepository.existsByCpf(dto.cpf())) {
            throw new CpfJaCadastradoException("CPF já cadastrado");
        }
        if (clienteRepository.existsByEmail(dto.email())) {
            throw new EmailJaCadastradoException("Email já cadastrado");
        }

        String senhaCriptografada = passwordEncoder.encode(dto.senha());

        Cliente cliente =new Cliente();
        cliente.setNome(dto.nome());
        cliente.setCpf(dto.cpf());
        cliente.setEmail(dto.email());
        cliente.setTelefone(dto.telefone());
        cliente.setSenha(senhaCriptografada);
        cliente.setStatus(StatusCliente.ATIVO);

        Cliente clienteSalvo = clienteRepository.save(cliente);

        contaService.criarConta(clienteSalvo);

        return new ClienteResponse(
                clienteSalvo.getId(),
                clienteSalvo.getNome(),
                clienteSalvo.getCpf(),
                clienteSalvo.getEmail()
        );

    }


    public List<ClienteResponse> listar(){

        List<Cliente> clientes = clienteRepository.findAll();

        return clientes.stream()
                .map(c -> new ClienteResponse(
                        c.getId(),
                        c.getNome(),
                        c.getCpf(),
                        c.getEmail()
                )).toList();

    }

    @Transactional
    public ClienteResponse buscarPorId(Long id) {

            Optional<Cliente> clienteBuscado = clienteRepository.findById(id);
            if (clienteBuscado.isPresent()){
                Cliente cliente = clienteBuscado.get();

                return new ClienteResponse(
                        cliente.getId(),
                        cliente.getNome(),
                        cliente.getCpf(),
                        cliente.getEmail()
                );

            }else {
                throw new ClienteNaoEncontradoException("Cliente inexistente");

            }
    }

    @Transactional
    public ClienteResponse atualizarPorId(Long id,ClienteRequest dto) {
        Cliente cliente = clienteRepository.findById(id)
                .orElseThrow(() ->
                        new ClienteNaoEncontradoException("Cliente não encontrado"));


        if (clienteRepository.existsByEmailAndIdNot(dto.email(),id)){
            throw new EmailJaCadastradoException("Email já cadastrado!");

        }

        cliente.setNome(dto.nome());
        cliente.setEmail(dto.email());
        cliente.setTelefone(dto.telefone());

        String senhaCriptografada = passwordEncoder.encode(dto.senha());
        cliente.setSenha(senhaCriptografada);

        Cliente clienteAtualizado = clienteRepository.save(cliente);

        return new ClienteResponse(
                clienteAtualizado.getId(),
                clienteAtualizado.getNome(),
                clienteAtualizado.getCpf(),
                clienteAtualizado.getEmail()
        );
    }

    @Transactional
    public void excluirPorId(Long id) {
        Cliente cliente = clienteRepository.findById(id)
                .orElseThrow(() ->
                        new ClienteNaoEncontradoException("Cliente não encontrado"));
        cliente.setStatus(StatusCliente.INATIVO);
        clienteRepository.save(cliente);
    }
}

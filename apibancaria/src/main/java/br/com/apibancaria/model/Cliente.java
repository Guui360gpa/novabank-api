package br.com.apibancaria.model;

import br.com.apibancaria.enums.StatusCliente;
import jakarta.persistence.*;
import lombok.Builder;

import java.time.LocalDate;

@Entity()
@Table(name = "clientes")
public class Cliente {

    @Id()
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nome", nullable = false, length = 100)
    private String nome;

    @Column(name = "cpf", nullable = false, length = 14, unique = true)
    private String cpf;

    @Column(name = "email",nullable = false,length = 50, unique = true)
    private String email;

    @Column(name = "telefone",nullable = false,length = 50,unique = true)
    private String telefone;

    @Column(name = "data_nascimento", nullable = false, length = 10)
    private LocalDate dataNascimento;

    @Column(name = "senha", nullable = false, length = 100)
    private String senha;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "conta_id")
    private Conta conta;

    @Enumerated(EnumType.STRING)
    private StatusCliente status;


    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getNome() {
        return nome;
    }
    public void setNome(String nome) {
        this.nome = nome;
    }
    public String getCpf() {
        return cpf;
    }
    public void setCpf(String cpf) {
        this.cpf = cpf;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public LocalDate getDataNascimento() {
        return dataNascimento;
    }
    public void setDataNascimento(LocalDate dataNascimento) {
        this.dataNascimento = dataNascimento;
    }
    public String getSenha() {
        return senha;
    }
    public void setSenha(String senha) {
        this.senha = senha;
    }
    public Conta getConta() {
        return conta;
    }
    public void setConta(Conta conta) {
        this.conta = conta;
    }
    public String getTelefone() {
        return telefone;
    }
    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }
    public StatusCliente getStatus() {
        return status;
    }
    public void setStatus(StatusCliente status) {
        this.status = status;
    }
}

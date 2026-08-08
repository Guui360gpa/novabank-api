package br.com.apibancaria.model;

import br.com.apibancaria.enums.StatusConta;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity()
@Table(name = "contas")
public class Conta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "numero_conta",nullable = false,length = 7,unique = true)
    private String numeroConta;

    @Column(name = "agencia",nullable = false,length = 4)
    private String agencia;

    @Column(name = "saldo", nullable = false)
    private BigDecimal saldo;

    @OneToOne(mappedBy = "conta")
    private Cliente cliente;

    @Enumerated(EnumType.STRING)
    private StatusConta status;

    @Column(name = "dataCriacao",nullable = false)
    private LocalDateTime dataCriacao;


    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getNumeroConta() {
        return numeroConta;
    }
    public void setNumeroConta(String numeroConta) {
        this.numeroConta = numeroConta;
    }
    public String getAgencia() {
        return agencia;
    }
    public void setAgencia(String agencia) {
        this.agencia = agencia;
    }
    public BigDecimal getSaldo() {
        return saldo;
    }
    public void setSaldo(BigDecimal saldo) {
        this.saldo = saldo;
    }
    public Cliente getCliente() {
        return cliente;
    }
    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }
    public StatusConta getStatus() {
        return status;
    }
    public void setStatus(StatusConta status) {
        this.status = status;
    }
    public LocalDateTime getDataCriacao() {
        return dataCriacao;
    }
    public void setDataCriacao(LocalDateTime dataCriacao) {
        this.dataCriacao = dataCriacao;
    }
}

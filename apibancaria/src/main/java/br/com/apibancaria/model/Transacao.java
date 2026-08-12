package br.com.apibancaria.model;

import br.com.apibancaria.enums.TipoTransacao;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "transacoes")
public class Transacao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private TipoTransacao tipo;

    @Column(name = "valor", nullable = false,length = 5)
    private BigDecimal valor;

    @Column(name = "data_hora",nullable = false)
    private LocalDateTime dataHora;

    @Column(name = "descricao")
    private String descricao;

    @ManyToOne
    @JoinColumn(name = "conta_origem_id")
    private Conta contaOrigem;

    @ManyToOne
    @JoinColumn(name = "conta_destino_id")
    private Conta contaDestino;

    @Column(name = "saldo_anterior",nullable = false)
    private BigDecimal saldoAnterior;

    @Column(name = "saldo_posterior",nullable = false)
    private BigDecimal saldoPosterior;

    public Transacao(TipoTransacao tipo, BigDecimal valor, LocalDateTime dataHora, String descricao, Conta contaOrigem, Conta contaDestino, BigDecimal saldoAnterior, BigDecimal saldoPosterior) {
        this.tipo = tipo;
        this.valor = valor;
        this.dataHora = dataHora;
        this.descricao = descricao;
        this.contaOrigem = contaOrigem;
        this.contaDestino = contaDestino;
        this.saldoAnterior = saldoAnterior;
        this.saldoPosterior = saldoPosterior;
    }

    public Transacao() {}

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public TipoTransacao getTipo() {
        return tipo;
    }
    public void setTipo(TipoTransacao tipo) {
        this.tipo = tipo;
    }
    public BigDecimal getValor() {
        return valor;
    }
    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }
    public LocalDateTime getDataHora() {
        return dataHora;
    }
    public void setDataHora(LocalDateTime dataHora) {
        this.dataHora = dataHora;
    }
    public String getDescricao() {
        return descricao;
    }
    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }
    public Conta getContaOrigem() {
        return contaOrigem;
    }
    public void setContaOrigem(Conta contaOrigem) {
        this.contaOrigem = contaOrigem;
    }
    public Conta getContaDestino() {
        return contaDestino;
    }
    public void setContaDestino(Conta contaDestino) {
        this.contaDestino = contaDestino;
    }
    public BigDecimal getSaldoAnterior() {
        return saldoAnterior;
    }
    public void setSaldoAnterior(BigDecimal saldoAnterior) {
        this.saldoAnterior = saldoAnterior;
    }
    public BigDecimal getSaldoPosterior() {
        return saldoPosterior;
    }
    public void setSaldoPosterior(BigDecimal saldoPosterior) {
        this.saldoPosterior = saldoPosterior;
    }
}

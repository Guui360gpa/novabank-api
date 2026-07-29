package br.com.apibancaria.model;

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

}

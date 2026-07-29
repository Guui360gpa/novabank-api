package br.com.apibancaria.model;

import jakarta.persistence.*;

import java.math.BigDecimal;

@Entity()
@Table(name = "contas")
public class Conta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "numero_conta",nullable = false,length = 7,unique = true)
    private int numeroConta;

    @Column(name = "agencia",nullable = false,length = 4)
    private int agencia;

    @Column(name = "saldo", nullable = false)
    private BigDecimal saldo;

    @OneToOne(mappedBy = "conta")
    private Cliente cliente;

    @Enumerated(EnumType.STRING)
    private StatusConta status;




}

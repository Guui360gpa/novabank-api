package br.com.apibancaria.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "chavesPix")
public class ChavePix {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private TipoChavePix tipo;

    @Column(name = "chave",nullable = false,length = 20, unique = true)
    private String chave;

    @Column(name = "ativa",nullable = false)
    private Boolean ativa;

    @Column(name = "data_cadastro", nullable = false)
    private LocalDateTime dataCadastro;

    @ManyToOne
    @JoinColumn(name = "conta_id")
    private Conta conta;

}

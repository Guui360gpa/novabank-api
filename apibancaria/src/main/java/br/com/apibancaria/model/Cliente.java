package br.com.apibancaria.model;

import jakarta.persistence.*;

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

    @Column(name = "data_nascimento", nullable = false, length = 10)
    private LocalDate dataNascimento;

    @Column(name = "senha", nullable = false, length = 8, unique = true)
    private int senha;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "conta_id")
    private Conta conta;

}

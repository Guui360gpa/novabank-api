package br.com.apibancaria.model;

import br.com.apibancaria.enums.TipoChavePix;
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


    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public TipoChavePix getTipo() {
        return tipo;
    }
    public void setTipo(TipoChavePix tipo) {
        this.tipo = tipo;
    }
    public String getChave() {
        return chave;
    }
    public void setChave(String chave) {
        this.chave = chave;
    }
    public Boolean getAtiva() {
        return ativa;
    }
    public void setAtiva(Boolean ativa) {
        this.ativa = ativa;
    }
    public LocalDateTime getDataCadastro() {
        return dataCadastro;
    }
    public void setDataCadastro(LocalDateTime dataCadastro) {
        this.dataCadastro = dataCadastro;
    }
    public Conta getConta() {
        return conta;
    }
    public void setConta(Conta conta) {
        this.conta = conta;
    }
}

package com.example.projeto.model;

import java.io.Serializable;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

@Entity
public class Produto implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Preencha o nome do produto")
    @NotEmpty(message = "Prencha o nome corretamente")
    private String nome;

    @NotNull(message = "Preencha a descrição do produto")
    @NotEmpty(message = "Prencha a descrição corretamente")
    private String descricao;

    @NotNull(message = "Preencha o preço do produto")
    @Min(value = 0, message = "Preço não pode ser menor que 0")
    private Double preco;

    @NotNull(message = "Preencha a quantidade do produto")
    @Min(0)
    private Integer quantidade;

    @NotNull(message = "Preencha o código de barras do produto")
    @NotEmpty(message = "Prencha o código de barras corretamente")
    @Column(unique = true, nullable = false)
    private String codBarras;

    public Produto(String nome, Double preco, String descricao, Integer quantidade, String codBarras) {
        this.nome = nome;
        this.preco = preco;
        this.descricao = descricao;
        this.quantidade = quantidade;
        this.codBarras = codBarras;
    }

    public Produto() {
    }

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

    public Double getPreco() {
        return preco;
    }

    public void setPreco(Double preco) {
        this.preco = preco;
    }

    public String getCodBarras() {
        return codBarras;
    }

    public void setCodBarras(String codBarras) {
        this.codBarras = codBarras;
    }

    public Integer getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(Integer quantidade) {
        this.quantidade = quantidade;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + (int) (id ^ (id >>> 32));
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Produto other = (Produto) obj;
        if (id != other.id)
            return false;
        return true;
    }

}

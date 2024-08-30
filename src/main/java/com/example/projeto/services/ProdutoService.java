package com.example.projeto.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.projeto.model.Produto;
import com.example.projeto.reposiories.ProdutoRepository;

import jakarta.persistence.EntityNotFoundException;

@Service
public class ProdutoService {

    @Autowired
    private ProdutoRepository produtoRepository;

    public Produto save(Produto produto) {

        return produtoRepository.save(produto);
    }

    public Produto updateproduto(Produto newproduto) {

        if (!produtoRepository.findById(newproduto.getId()).isPresent()) {
            throw new EntityNotFoundException("Produto não encontrado");
        }

        return produtoRepository.save(newproduto);
    }

    public void deleteById(long id) {
        if (!produtoRepository.findById(id).isPresent()) {
            throw new EntityNotFoundException("Produto com id" + id + " não encontrado");
        }
        produtoRepository.deleteById(id);
    }

    public List<Produto> findAll() {
        return produtoRepository.findAll();
    }

    public Produto findByCodBarras(String codBarras) {
        Produto produto = produtoRepository.findByCodBarras(codBarras);
        if (produto == null) {
            throw new EntityNotFoundException("Produto não encontrado");
        }
        return produto;

    }

    public Optional<Produto> findById(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("Id passado é nulo");
        }
        return produtoRepository.findById(id);
    }

    public List<Produto> findByName(String nome) {
        return produtoRepository.findByName(nome);
    }

}

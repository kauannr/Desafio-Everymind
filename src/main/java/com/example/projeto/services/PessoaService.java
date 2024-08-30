package com.example.projeto.services;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.projeto.model.Pessoa;
import com.example.projeto.reposiories.PessoaRepository;

import java.util.*;

@Service
public class PessoaService {

    @Autowired
    private PessoaRepository pessoaRepository;

    public List<Pessoa> findByName(String nome ) {
        List<Pessoa> listPorNomes = new ArrayList<>();

        listPorNomes = pessoaRepository.consultarPornNome(nome.trim().toLowerCase());

        return listPorNomes;
    }

    public Optional<Pessoa> findById(Long id){
        if (id == null) {
            throw new IllegalArgumentException("Id fornecido é nulo");
        }
        return pessoaRepository.findById(id);
    }

    public List<Pessoa> findAll(){
        return pessoaRepository.findAll();
    }

    public Pessoa save(Pessoa pessoa){
        return pessoaRepository.save(pessoa);
    }

    public void delete(Pessoa pessoa){
        pessoaRepository.delete(pessoa);
    }

}

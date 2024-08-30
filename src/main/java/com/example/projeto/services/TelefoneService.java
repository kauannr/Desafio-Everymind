package com.example.projeto.services;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.projeto.model.Telefone;
import com.example.projeto.reposiories.TelefoneRepository;

import java.util.*;

@Service
public class TelefoneService {
    @Autowired
    private TelefoneRepository telefoneRepository;

    public List<Telefone> findAll(){
        return telefoneRepository.findAll();
    }

    public Telefone save(Telefone telefone){
       return telefoneRepository.save(telefone);
    }

    public Optional<Telefone> findById(Long id){

        if (id == null) {
        throw new IllegalArgumentException("Id fornecido é nulo");
        }

        return telefoneRepository.findById(id);
    }

    public void delete(Telefone telefone){
        telefoneRepository.delete(telefone);
    }


}

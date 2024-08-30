package com.example.projeto.reposiories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.example.projeto.model.Pessoa;

@Repository
@Transactional
public interface PessoaRepository extends JpaRepository<Pessoa, Long>{
    
    @Query("select p from Pessoa p where lower(p.nome) like %?1%")
    public List<Pessoa> consultarPornNome(String nome);
}




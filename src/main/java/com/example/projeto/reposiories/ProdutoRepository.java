package com.example.projeto.reposiories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.projeto.model.Produto;



@Repository
public interface ProdutoRepository extends JpaRepository<Produto, Long> {

    @Query(value = "SELECT p.* FROM Produto p WHERE lower(p.nome) = lower(:nome)", nativeQuery = true)
    public List<Produto> findByName(@Param("nome") String nome);

    @Query(value = "SELECT p.* FROM Produto p WHERE p.codBarras = :codBarras", nativeQuery = true)
    public Produto findByCodBarras(@Param("codBarras") String codBarras);
}

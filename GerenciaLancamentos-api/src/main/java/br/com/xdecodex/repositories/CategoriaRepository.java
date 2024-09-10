package br.com.xdecodex.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.xdecodex.model.Categoria;

public interface CategoriaRepository extends JpaRepository<Categoria, Long> {

}
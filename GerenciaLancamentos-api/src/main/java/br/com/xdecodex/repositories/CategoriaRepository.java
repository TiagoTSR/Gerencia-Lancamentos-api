package br.com.xdecodex.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import br.com.xdecodex.model.Categoria;
import br.com.xdecodex.model.Lancamento;

public interface CategoriaRepository extends JpaRepository<Categoria, Long> {
	
	@Query("SELECT c FROM Categoria c WHERE c.nome LIKE LOWER(CONCAT ('%',:nome,'%'))")
	Page<Lancamento> findCategoriasByNome(@Param("nome") String nome, Pageable pageable);
}
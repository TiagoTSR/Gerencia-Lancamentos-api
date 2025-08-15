package br.com.xdecodex.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import br.com.xdecodex.model.Estado;

public interface EstadoRepository extends JpaRepository<Estado, Long> {
	
	@Query("SELECT c FROM Categoria c WHERE c.nome LIKE LOWER(CONCAT ('%',:nome,'%'))")
	Page<Estado> findCategoriasByNome(@Param("nome") String nome, Pageable pageable);

}
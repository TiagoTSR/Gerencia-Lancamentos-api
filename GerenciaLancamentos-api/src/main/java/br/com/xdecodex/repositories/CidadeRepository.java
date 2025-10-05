package br.com.xdecodex.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import br.com.xdecodex.model.Cidade;

public interface CidadeRepository extends JpaRepository<Cidade, Long> {

	@Query("SELECT c FROM Cidade c WHERE c.nome LIKE LOWER(CONCAT ('%',:nome,'%'))")
	Page<Cidade> findCidadesByNome(@Param("nome") String nome, Pageable pageable);

}
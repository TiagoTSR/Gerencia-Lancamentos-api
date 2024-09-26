package br.com.xdecodex.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import br.com.xdecodex.model.Lancamento;

public interface LancamentoRepository extends JpaRepository<Lancamento, Long> {
	
	@Query("SELECT p FROM Person p WHERE p.descricao LIKE LOWER(CONCAT ('%',:descricao,'%'))")
	Page<Lancamento> findPersonsByName(@Param("descricao") String firstName, Pageable pageable);

}

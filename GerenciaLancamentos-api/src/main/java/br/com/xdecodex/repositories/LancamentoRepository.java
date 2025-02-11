package br.com.xdecodex.repositories;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import br.com.xdecodex.dto.LancamentoEstatisticaCategoria;
import br.com.xdecodex.model.Lancamento;

public interface LancamentoRepository extends JpaRepository<Lancamento, Long> {
	
	@Query("SELECT l FROM Lancamento l WHERE l.descricao LIKE LOWER(CONCAT ('%',:descricao,'%'))")
	Page<Lancamento> findLancamentosByDescricao(@Param("descricao") String descricao, Pageable pageable);

}

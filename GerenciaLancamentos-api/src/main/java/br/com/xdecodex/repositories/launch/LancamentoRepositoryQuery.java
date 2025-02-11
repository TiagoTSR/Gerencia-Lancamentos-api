package br.com.xdecodex.repositories.launch;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import br.com.xdecodex.data.vo.v1.LancamentoVO;
import br.com.xdecodex.dto.LancamentoEstatisticaCategoria;
import br.com.xdecodex.repositories.filter.LancamentoFilter;
import br.com.xdecodex.repositories.projection.ResumoLancamento;

public interface LancamentoRepositoryQuery {

	public List<LancamentoEstatisticaCategoria> porCategoria(LocalDate mesReferencia);

	List<LancamentoVO> filtrar(LancamentoFilter lancamentoFilter,Pageable pageable);

	public Page<ResumoLancamento> resumir(LancamentoFilter lancamentoFilter, Pageable pageable);
}
package br.com.xdecodex.repositories.lancamento;

import java.util.List;

import br.com.xdecodex.data.vo.v1.LancamentoVO;
import br.com.xdecodex.repositories.filter.LancamentoFilter;

public interface LancamentoRepositoryQuery {
    List<LancamentoVO> filtrar(LancamentoFilter lancamentoFilter);
}
package br.com.xdecodex.unittests.mapper.mocks;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import br.com.xdecodex.data.vo.v1.LancamentoVO;
import br.com.xdecodex.model.Lancamento;
import br.com.xdecodex.model.Pessoa;
import br.com.xdecodex.model.Categoria;
import br.com.xdecodex.model.TipoLancamento;

public class MockLancamento {

    public Lancamento mockEntity() {
        return mockEntity(0);
    }
    
    public LancamentoVO mockVO() {
        return mockVO(0);
    }

    public List<Lancamento> mockEntityList() {
        List<Lancamento> lancamentos = new ArrayList<>();
        for (int i = 0; i < 14; i++) {
            lancamentos.add(mockEntity(i));
        }
        return lancamentos;
    }

    public List<LancamentoVO> mockVOList() {
        List<LancamentoVO> lancamentos = new ArrayList<>();
        for (int i = 0; i < 14; i++) {
            lancamentos.add(mockVO(i));
        }
        return lancamentos;
    }
    
    public Lancamento mockEntity(Integer number) {
        Lancamento lancamento = new Lancamento();
        lancamento.setCodigo(number.longValue());
        lancamento.setDescricao("Descricao Teste " + number);
        lancamento.setDataVencimento(LocalDate.now().plusDays(number));
        lancamento.setDataPagamento(LocalDate.now().plusDays(number));
        lancamento.setValor(BigDecimal.valueOf(number));
        lancamento.setObservacao("Observacao Teste " + number);
        lancamento.setTipo(TipoLancamento.values()[number % TipoLancamento.values().length]);
        lancamento.setCategoria(mockCategoria(number));
        lancamento.setPessoa(mockPessoa(number));
        return lancamento;
    }

    public LancamentoVO mockVO(Integer number) {
        LancamentoVO lancamentoVO = new LancamentoVO();
        lancamentoVO.setCodigo(number.longValue());
        lancamentoVO.setDescricao("Descricao Teste " + number);
        lancamentoVO.setDataVencimento(LocalDate.now().plusDays(number));
        lancamentoVO.setDataPagamento(LocalDate.now().plusDays(number));
        lancamentoVO.setValor(BigDecimal.valueOf(number));
        lancamentoVO.setObservacao("Observacao Teste " + number);
        lancamentoVO.setTipo(TipoLancamento.values()[number % TipoLancamento.values().length]);
        lancamentoVO.setCategoria(mockCategoria(number));
        lancamentoVO.setPessoa(mockPessoa(number));
        return lancamentoVO;
    }

    public Pessoa mockPessoa(Integer number) {
        Pessoa pessoa = new Pessoa();
        pessoa.setCodigo(number.longValue());
        pessoa.setNome("Nome Teste " + number);
        pessoa.setAtivo(true);
        return pessoa;
    }

    private Categoria mockCategoria(Integer number) {
        Categoria categoria = new Categoria();
        categoria.setCodigo(number.longValue());
        categoria.setNome("Categoria Teste " + number);
        return categoria;
    }
}

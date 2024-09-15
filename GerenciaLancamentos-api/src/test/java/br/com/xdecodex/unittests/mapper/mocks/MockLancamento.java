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
        lancamento.setDescricao("Descricao Teste" + number);
        lancamento.setDataVencimento(LocalDate.now().plusDays(number)); // Exemplo de data
        lancamento.setDataPagamento(LocalDate.now().plusDays(number)); // Exemplo de data
        lancamento.setValor(BigDecimal.valueOf(number)); // Exemplo de valor
        lancamento.setObservacao("Observacao Teste" + number);
        lancamento.setTipo(TipoLancamento.values()[number % TipoLancamento.values().length]); // Exemplo de tipo
        lancamento.setCategoria(mockCategoria(number)); // Método para criar Categoria
        lancamento.setPessoa(mockPessoa(number)); // Assumindo que a pessoa é necessária
        return lancamento;
    }

    public LancamentoVO mockVO(Integer number) {
        LancamentoVO lancamentoVO = new LancamentoVO();
        lancamentoVO.setCodigo(number.longValue());
        lancamentoVO.setDescricao("Descricao Teste" + number);
        lancamentoVO.setDataVencimento(LocalDate.now().plusDays(number)); // Exemplo de data
        lancamentoVO.setDataPagamento(LocalDate.now().plusDays(number)); // Exemplo de data
        lancamentoVO.setValor(BigDecimal.valueOf(number)); // Exemplo de valor
        lancamentoVO.setObservacao("Observacao Teste" + number);
        lancamentoVO.setTipo(TipoLancamento.values()[number % TipoLancamento.values().length]); // Exemplo de tipo
        lancamentoVO.setCategoria(mockCategoria(number)); // Método para criar Categoria
        lancamentoVO.setPessoa(mockPessoa(number)); // Passa um Pessoa, não um PessoaVO
        return lancamentoVO;
    }

    public Pessoa mockPessoa(Integer number) {
        Pessoa pessoa = new Pessoa();
        pessoa.setCodigo(number.longValue());
        pessoa.setNome("Nome Teste" + number);
        pessoa.setAtivo(true); // Definido como ativo por padrão
        return pessoa;
    }

    private Categoria mockCategoria(Integer number) {
        Categoria categoria = new Categoria();
        // Configure a categoria conforme necessário
        return categoria;
    }
}

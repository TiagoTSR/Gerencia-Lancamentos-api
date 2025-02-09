package br.com.xdecodex.unittests.mapper;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import br.com.xdecodex.data.vo.v1.LancamentoVO;
import br.com.xdecodex.mapper.DozerMapper;
import br.com.xdecodex.model.Lancamento;
import br.com.xdecodex.unittests.mapper.mocks.MockLancamento;

public class DozerLancamentoConverterTest {

    MockLancamento inputObject;

    @BeforeEach
    public void setUp() {
        inputObject = new MockLancamento();
    }

    @Test
    public void parseEntityToVOTest() {
        LancamentoVO output = DozerMapper.parseObject(inputObject.mockEntity(), LancamentoVO.class);
        assertEquals(Long.valueOf(0L), output.getId());
        assertEquals("Descricao Teste 0", output.getDescricao());
        assertEquals(LocalDate.now().plusDays(0), output.getDataVencimento());
        assertEquals(LocalDate.now().plusDays(0), output.getDataPagamento());
        assertEquals(BigDecimal.valueOf(0), output.getValor());
        assertEquals("Categoria Teste 0", output.getCategoria().getNome());
        assertEquals("Nome Teste 0", output.getPessoa().getNome());
    }

    @Test
    public void parseEntityListToVOListTest() {
        List<LancamentoVO> outputList = DozerMapper.parseListObjects(inputObject.mockEntityList(), LancamentoVO.class);
        LancamentoVO outputZero = outputList.get(0);
        
        assertEquals(Long.valueOf(0L), outputZero.getId());
        assertEquals("Descricao Teste 0", outputZero.getDescricao());
        assertEquals(LocalDate.now().plusDays(0), outputZero.getDataVencimento());
        assertEquals(LocalDate.now().plusDays(0), outputZero.getDataPagamento());
        assertEquals(BigDecimal.valueOf(0), outputZero.getValor());
        assertEquals("Categoria Teste 0", outputZero.getCategoria().getNome());
        assertEquals("Nome Teste 0", outputZero.getPessoa().getNome());
        
        LancamentoVO outputSeven = outputList.get(7);
        
        assertEquals(Long.valueOf(7L), outputSeven.getId());
        assertEquals("Descricao Teste 7", outputSeven.getDescricao());
        assertEquals(LocalDate.now().plusDays(7), outputSeven.getDataVencimento());
        assertEquals(LocalDate.now().plusDays(7), outputSeven.getDataPagamento());
        assertEquals(BigDecimal.valueOf(7), outputSeven.getValor());
        assertEquals("Categoria Teste 7", outputSeven.getCategoria().getNome());
        assertEquals("Nome Teste 7", outputSeven.getPessoa().getNome());
        
        LancamentoVO outputTwelve = outputList.get(12);
        
        assertEquals(Long.valueOf(12L), outputTwelve.getId());
        assertEquals("Descricao Teste 12", outputTwelve.getDescricao());
        assertEquals(LocalDate.now().plusDays(12), outputTwelve.getDataVencimento());
        assertEquals(LocalDate.now().plusDays(12), outputTwelve.getDataPagamento());
        assertEquals(BigDecimal.valueOf(12), outputTwelve.getValor());
        assertEquals("Categoria Teste 12", outputTwelve.getCategoria().getNome());
        assertEquals("Nome Teste 12", outputTwelve.getPessoa().getNome());
    }

    @Test
    public void parseVOToEntityTest() {
        Lancamento output = DozerMapper.parseObject(inputObject.mockVO(), Lancamento.class);
        assertEquals(Long.valueOf(0L), output.getId());
        assertEquals("Descricao Teste 0", output.getDescricao());
        assertEquals(LocalDate.now().plusDays(0), output.getDataVencimento());
        assertEquals(LocalDate.now().plusDays(0), output.getDataPagamento());
        assertEquals(BigDecimal.valueOf(0), output.getValor());
        assertEquals("Categoria Teste 0", output.getCategoria().getNome());
        assertEquals("Nome Teste 0", output.getPessoa().getNome());
    }

    @Test
    public void parserVOListToEntityListTest() {
        List<Lancamento> outputList = DozerMapper.parseListObjects(inputObject.mockVOList(), Lancamento.class);
        Lancamento outputZero = outputList.get(0);
        
        assertEquals(Long.valueOf(0L), outputZero.getId());
        assertEquals("Descricao Teste 0", outputZero.getDescricao());
        assertEquals(LocalDate.now().plusDays(0), outputZero.getDataVencimento());
        assertEquals(LocalDate.now().plusDays(0), outputZero.getDataPagamento());
        assertEquals(BigDecimal.valueOf(0), outputZero.getValor());
        assertEquals("Categoria Teste 0", outputZero.getCategoria().getNome());
        assertEquals("Nome Teste 0", outputZero.getPessoa().getNome());
        
        Lancamento outputSeven = outputList.get(7);
        
        assertEquals(Long.valueOf(7L), outputSeven.getId());
        assertEquals("Descricao Teste 7", outputSeven.getDescricao());
        assertEquals(LocalDate.now().plusDays(7), outputSeven.getDataVencimento());
        assertEquals(LocalDate.now().plusDays(7), outputSeven.getDataPagamento());
        assertEquals(BigDecimal.valueOf(7), outputSeven.getValor());
        assertEquals("Categoria Teste 7", outputSeven.getCategoria().getNome());
        assertEquals("Nome Teste 7", outputSeven.getPessoa().getNome());
        
        Lancamento outputTwelve = outputList.get(12);
        
        assertEquals(Long.valueOf(12L), outputTwelve.getId());
        assertEquals("Descricao Teste 12", outputTwelve.getDescricao());
        assertEquals(LocalDate.now().plusDays(12), outputTwelve.getDataVencimento());
        assertEquals(LocalDate.now().plusDays(12), outputTwelve.getDataPagamento());
        assertEquals(BigDecimal.valueOf(12), outputTwelve.getValor());
        assertEquals("Categoria Teste 12", outputTwelve.getCategoria().getNome());
        assertEquals("Nome Teste 12", outputTwelve.getPessoa().getNome());
    }
}

package br.com.xdecodex.unittests.mapper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import br.com.xdecodex.data.vo.v1.LancamentoVO;
import br.com.xdecodex.exceptions.PessoaInexistenteOuInativaException;
import br.com.xdecodex.exceptions.ResourceNotFoundException;
import br.com.xdecodex.model.Lancamento;
import br.com.xdecodex.model.Pessoa;
import br.com.xdecodex.repositories.LancamentoRepository;
import br.com.xdecodex.repositories.PessoaRepository;
import br.com.xdecodex.services.LancamentoService;
import br.com.xdecodex.unittests.mapper.mock.MockLancamento;

public class DozerLancamentoConverterTest {

    @InjectMocks
    private LancamentoService lancamentoService;

    @Mock
    private LancamentoRepository lancamentoRepository;

    @Mock
    private PessoaRepository pessoaRepository;

    private MockLancamento mockLancamento;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockLancamento = new MockLancamento();
    }

    @Test
    void testFindAll() {
        List<Lancamento> lancamentos = mockLancamento.mockEntityList();
        when(lancamentoRepository.findAll()).thenReturn(lancamentos);

        List<LancamentoVO> result = lancamentoService.findAll();

        assertNotNull(result);
        assertEquals(14, result.size());
        assertEquals("Descricao Teste0", result.get(0).getDescricao());
        assertEquals("Descricao Teste7", result.get(7).getDescricao());
    }

    @Test
    void testFindByIdSuccess() {
        Lancamento lancamento = mockLancamento.mockEntity(1);
        when(lancamentoRepository.findById(1L)).thenReturn(Optional.of(lancamento));

        LancamentoVO result = lancamentoService.findById(1L);

        assertNotNull(result);
        assertEquals("Descricao Teste1", result.getDescricao());
    }

    @Test
    void testFindByIdNotFound() {
        when(lancamentoRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            lancamentoService.findById(1L);
        });
    }

    @Test
    void testSaveSuccess() {
        LancamentoVO lancamentoVO = mockLancamento.mockVO(1);
        Pessoa pessoa = mockLancamento.mockPessoa(1);
        when(pessoaRepository.findById(1L)).thenReturn(Optional.of(pessoa));
        when(lancamentoRepository.save(any(Lancamento.class))).thenReturn(mockLancamento.mockEntity(1));

        LancamentoVO result = lancamentoService.create(lancamentoVO);

        assertNotNull(result);
        assertEquals("Descricao Teste1", result.getDescricao());
    }

    @Test
    void testSavePessoaInativa() {
        LancamentoVO lancamentoVO = mockLancamento.mockVO(1);
        Pessoa pessoa = mockLancamento.mockPessoa(1);
        pessoa.setAtivo(false); // Pessoa está inativa
        when(pessoaRepository.findById(1L)).thenReturn(Optional.of(pessoa));

        assertThrows(PessoaInexistenteOuInativaException.class, () -> {
            lancamentoService.create(lancamentoVO);
        });
    }

    @Test
    void testUpdate() {
        Lancamento lancamento = mockLancamento.mockEntity(1);
        LancamentoVO lancamentoVO = mockLancamento.mockVO(1);

        when(lancamentoRepository.findById(1L)).thenReturn(Optional.of(lancamento));
        when(lancamentoRepository.save(any(Lancamento.class))).thenReturn(lancamento);

        LancamentoVO result = lancamentoService.update(lancamentoVO);

        assertNotNull(result);
        assertEquals("Descricao Teste1", result.getDescricao());
    }

    @Test
    void testUpdateNotFound() {
        LancamentoVO lancamentoVO = mockLancamento.mockVO(1);

        when(lancamentoRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            lancamentoService.update(lancamentoVO);
        });
    }

    @Test
    void testDelete() {
        Lancamento lancamento = mockLancamento.mockEntity(1);

        when(lancamentoRepository.findById(1L)).thenReturn(Optional.of(lancamento));

        boolean result = lancamentoService.delete(1L);

        assertTrue(result);
        verify(lancamentoRepository, times(1)).delete(lancamento);
    }

    @Test
    void testDeleteNotFound() {
        when(lancamentoRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            lancamentoService.delete(1L);
        });
    }
}

package br.com.xdecodex.unittests.mockito.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import br.com.xdecodex.data.vo.v1.LancamentoVO;
import br.com.xdecodex.exceptions.PessoaInexistenteOuInativaException;
import br.com.xdecodex.exceptions.ResourceNotFoundException;
import br.com.xdecodex.model.Lancamento;
import br.com.xdecodex.model.Pessoa;
import br.com.xdecodex.repositories.LancamentoRepository;
import br.com.xdecodex.repositories.PessoaRepository;
import br.com.xdecodex.services.LancamentoService;
import br.com.xdecodex.unittests.mapper.mocks.MockLancamento;

@ExtendWith(MockitoExtension.class)
class LancamentoServiceTest {

    @InjectMocks
    private LancamentoService service;

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
    void testFindById() {
        Lancamento lancamento = mockLancamento.mockEntity(1);
        lancamento.setCodigo(1L);

        when(lancamentoRepository.findById(1L)).thenReturn(Optional.of(lancamento));

        LancamentoVO result = service.findById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getCodigo());
        assertEquals("Descricao Teste 1", result.getDescricao());
    }

    @Test
    void testFindByIdNotFound() {
        when(lancamentoRepository.findById(1L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(ResourceNotFoundException.class, () -> {
            service.findById(1L);
        });

        assertEquals("Lancamento not encontrado pelo ID: 1", exception.getMessage());
    }

    @Test
    void testCreate() {
        LancamentoVO vo = mockLancamento.mockVO(1);
        Pessoa pessoa = mock(Pessoa.class);
        Lancamento lancamento = mockLancamento.mockEntity(1);

        when(pessoaRepository.findById(vo.getPessoa().getCodigo())).thenReturn(Optional.of(pessoa));
        when(lancamentoRepository.save(any(Lancamento.class))).thenReturn(lancamento);
        when(pessoa.isInativo()).thenReturn(false);

        LancamentoVO result = service.create(vo);

        assertNotNull(result);
        assertEquals(vo.getCodigo(), result.getCodigo());
        assertEquals(vo.getDescricao(), result.getDescricao());
    }

    @Test
    void testCreateWithInactivePessoa() {
        LancamentoVO vo = mockLancamento.mockVO(1);
        Pessoa pessoa = mock(Pessoa.class);

        when(pessoaRepository.findById(vo.getPessoa().getCodigo())).thenReturn(Optional.of(pessoa));
        when(pessoa.isInativo()).thenReturn(true);

        Exception exception = assertThrows(PessoaInexistenteOuInativaException.class, () -> {
            service.create(vo);
        });

        assertNotNull(exception);
    }

    @Test
    void testUpdate() {
        Lancamento lancamento = mockLancamento.mockEntity(1);
        LancamentoVO vo = mockLancamento.mockVO(1);

        when(lancamentoRepository.findById(vo.getCodigo())).thenReturn(Optional.of(lancamento));
        when(lancamentoRepository.save(any(Lancamento.class))).thenReturn(lancamento);

        LancamentoVO result = service.update(vo);

        assertNotNull(result);
        assertEquals(vo.getCodigo(), result.getCodigo());
        assertEquals(vo.getDescricao(), result.getDescricao());
    }

    @Test
    void testUpdateNotFound() {
        LancamentoVO vo = mockLancamento.mockVO(1);

        when(lancamentoRepository.findById(vo.getCodigo())).thenReturn(Optional.empty());

        Exception exception = assertThrows(ResourceNotFoundException.class, () -> {
            service.update(vo);
        });

        assertEquals("Lancamento não encontrado para atualização", exception.getMessage());
    }

    @Test
    void testDelete() {
        Lancamento lancamento = mockLancamento.mockEntity(1);

        when(lancamentoRepository.findById(1L)).thenReturn(Optional.of(lancamento));

        boolean result = service.delete(1L);

        assertTrue(result);
        verify(lancamentoRepository, times(1)).delete(lancamento);
    }

    @Test
    void testDeleteNotFound() {
        when(lancamentoRepository.findById(1L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(ResourceNotFoundException.class, () -> {
            service.delete(1L);
        });

        assertEquals("Lancamento não encontrado para deletar", exception.getMessage());
    }

}
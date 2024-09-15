package br.com.xdecodex.unittests.mapper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
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

import br.com.xdecodex.data.vo.v1.PessoaVO;
import br.com.xdecodex.exceptions.ResourceNotFoundException;
import br.com.xdecodex.model.Pessoa;
import br.com.xdecodex.repositories.PessoaRepository;
import br.com.xdecodex.services.PessoaService;
import br.com.xdecodex.unittests.mapper.mocks.MockPessoa;


public class DozerPessoaConverterTest {

    @InjectMocks
    private PessoaService pessoaService;

    @Mock
    private PessoaRepository pessoaRepository;

    private MockPessoa mockPessoa;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockPessoa = new MockPessoa();
    }

    @Test
    void testFindAll() {
        List<Pessoa> pessoas = mockPessoa.mockEntityList();
        when(pessoaRepository.findAll()).thenReturn(pessoas);

        List<PessoaVO> result = pessoaService.findAll();

        assertNotNull(result);
        assertEquals(14, result.size());
        assertEquals("Nome Teste0", result.get(0).getNome());
        assertEquals("Nome Teste7", result.get(7).getNome());
    }

    @Test
    void testFindByIdSuccess() {
        Pessoa pessoa = mockPessoa.mockEntity(1);
        when(pessoaRepository.findById(1L)).thenReturn(Optional.of(pessoa));

        PessoaVO result = pessoaService.findById(1L);

        assertNotNull(result);
        assertEquals("Nome Teste1", result.getNome());
    }

    @Test
    void testFindByIdNotFound() {
        when(pessoaRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            pessoaService.findById(1L);
        });
    }

    @Test
    void testCreate() {
        Pessoa pessoa = mockPessoa.mockEntity(1);
        PessoaVO pessoaVO = mockPessoa.mockVO(1);

        when(pessoaRepository.save(any(Pessoa.class))).thenReturn(pessoa);

        PessoaVO result = pessoaService.create(pessoaVO);

        assertNotNull(result);
        assertEquals("Nome Teste1", result.getNome());
    }

    @Test
    void testUpdate() {
        Pessoa pessoa = mockPessoa.mockEntity(1);
        PessoaVO pessoaVO = mockPessoa.mockVO(1);

        when(pessoaRepository.findById(1L)).thenReturn(Optional.of(pessoa));
        when(pessoaRepository.save(any(Pessoa.class))).thenReturn(pessoa);

        PessoaVO result = pessoaService.update(pessoaVO);

        assertNotNull(result);
        assertEquals("Nome Teste1", result.getNome());
    }

    @Test
    void testUpdateNotFound() {
        PessoaVO pessoaVO = mockPessoa.mockVO(1);

        when(pessoaRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            pessoaService.update(pessoaVO);
        });
    }

    @Test
    void testDelete() {
        Pessoa pessoa = mockPessoa.mockEntity(1);

        when(pessoaRepository.findById(1L)).thenReturn(Optional.of(pessoa));

        boolean result = pessoaService.delete(1L);

        assertTrue(result);
        verify(pessoaRepository, times(1)).delete(pessoa);
    }

    @Test
    void testDeleteNotFound() {
        when(pessoaRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            pessoaService.delete(1L);
        });
    }
}

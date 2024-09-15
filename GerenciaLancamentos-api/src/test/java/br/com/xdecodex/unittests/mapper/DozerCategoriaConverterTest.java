package br.com.xdecodex.unittests.mapper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertThrows;
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

import br.com.xdecodex.data.vo.v1.CategoriaVO;
import br.com.xdecodex.exceptions.ResourceNotFoundException;
import br.com.xdecodex.model.Categoria;
import br.com.xdecodex.repositories.CategoriaRepository;
import br.com.xdecodex.services.CategoriaService;
import br.com.xdecodex.unittests.mapper.mocks.MockCategoria;

public class DozerCategoriaConverterTest {

    @InjectMocks
    private CategoriaService categoriaService;

    @Mock
    private CategoriaRepository categoriaRepository;

    private MockCategoria mockCategoria;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockCategoria = new MockCategoria();
    }

    @Test
    void testFindAll() {
        List<Categoria> categorias = mockCategoria.mockEntityList();
        when(categoriaRepository.findAll()).thenReturn(categorias);

        List<CategoriaVO> result = categoriaService.findAll();

        assertNotNull(result);
        assertEquals(14, result.size());
        assertEquals("Categoria Teste0", result.get(0).getNome());
        assertEquals("Categoria Teste7", result.get(7).getNome());
    }

    @Test
    void testFindByIdSuccess() {
        Categoria categoria = mockCategoria.mockEntity(1);
        when(categoriaRepository.findById(1L)).thenReturn(Optional.of(categoria));

        CategoriaVO result = categoriaService.findById(1L);

        assertNotNull(result);
        assertEquals("Categoria Teste1", result.getNome());
    }

    @Test
    void testFindByIdNotFound() {
        when(categoriaRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            categoriaService.findById(1L);
        });
    }

    @Test
    void testSaveSuccess() {
        CategoriaVO categoriaVO = mockCategoria.mockVO(1);
        Categoria categoria = mockCategoria.mockEntity(1);

        when(categoriaRepository.save(categoria)).thenReturn(categoria);

        CategoriaVO result = categoriaService.create(categoriaVO);

        assertNotNull(result);
        assertEquals("Categoria Teste1", result.getNome());
    }

    @Test
    void testUpdateSuccess() {
        Categoria categoria = mockCategoria.mockEntity(1);
        CategoriaVO categoriaVO = mockCategoria.mockVO(1);

        when(categoriaRepository.findById(1L)).thenReturn(Optional.of(categoria));
        when(categoriaRepository.save(categoria)).thenReturn(categoria);

        CategoriaVO result = categoriaService.update(categoriaVO);

        assertNotNull(result);
        assertEquals("Categoria Teste1", result.getNome());
    }

    @Test
    void testUpdateNotFound() {
        CategoriaVO categoriaVO = mockCategoria.mockVO(1);

        when(categoriaRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            categoriaService.update(categoriaVO);
        });
    }

    @Test
    void testDeleteSuccess() {

        when(categoriaRepository.existsById(1L)).thenReturn(true);

        boolean result = categoriaService.delete(1L);

        assertTrue(result);
        verify(categoriaRepository, times(1)).deleteById(1L); 
    }

    @Test
    void testDeleteNotFound() {
        when(categoriaRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            categoriaService.delete(1L);
        });
    }
}

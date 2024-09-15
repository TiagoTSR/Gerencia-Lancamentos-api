package br.com.xdecodex.unittests.mockito.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
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

import br.com.xdecodex.data.vo.v1.CategoriaVO;
import br.com.xdecodex.exceptions.ResourceNotFoundException;
import br.com.xdecodex.model.Categoria;
import br.com.xdecodex.repositories.CategoriaRepository;
import br.com.xdecodex.services.CategoriaService;
import br.com.xdecodex.unittests.mapper.mocks.MockCategoria;

@ExtendWith(MockitoExtension.class)
class CategoriaServiceTest {

    @InjectMocks
    private CategoriaService service;

    @Mock
    private CategoriaRepository categoriaRepository;

    private MockCategoria mockCategoria;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockCategoria = new MockCategoria();
    }

    @Test
    void testFindById() {
        Categoria categoria = mockCategoria.mockEntity(1);
        categoria.setCodigo(1L);
        categoria.setNome("Categoria Teste1"); // Ajuste o valor esperado aqui

        when(categoriaRepository.findById(1L)).thenReturn(Optional.of(categoria));

        CategoriaVO result = service.findById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getCodigo());
        assertEquals("Categoria Teste1", result.getNome()); // Ajuste o valor esperado
    }


    @Test
    void testFindByIdNotFound() {
        when(categoriaRepository.findById(1L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(ResourceNotFoundException.class, () -> {
            service.findById(1L);
        });

        assertEquals("Categoria not found for ID: 1", exception.getMessage());
    }

    @Test
    void testCreate() {
        CategoriaVO vo = mockCategoria.mockVO(1);
        Categoria categoria = mockCategoria.mockEntity(1);

        when(categoriaRepository.save(any(Categoria.class))).thenReturn(categoria);

        CategoriaVO result = service.create(vo);

        assertNotNull(result);
        assertEquals(vo.getCodigo(), result.getCodigo());
        assertEquals(vo.getNome(), result.getNome());
    }

    @Test
    void testUpdate() {
        Categoria categoria = mockCategoria.mockEntity(1);
        CategoriaVO vo = mockCategoria.mockVO(1);

        when(categoriaRepository.findById(vo.getCodigo())).thenReturn(Optional.of(categoria));
        when(categoriaRepository.save(any(Categoria.class))).thenReturn(categoria);

        CategoriaVO result = service.update(vo);

        assertNotNull(result);
        assertEquals(vo.getCodigo(), result.getCodigo());
        assertEquals(vo.getNome(), result.getNome());
    }

    @Test
    void testUpdateNotFound() {
        CategoriaVO vo = mockCategoria.mockVO(1);

        when(categoriaRepository.findById(vo.getCodigo())).thenReturn(Optional.empty());

        Exception exception = assertThrows(ResourceNotFoundException.class, () -> {
            service.update(vo);
        });

        assertEquals("Categoria not found for update", exception.getMessage());
    }

    @Test
    void testDelete() {
        when(categoriaRepository.existsById(1L)).thenReturn(true);

        boolean result = service.delete(1L);

        assertTrue(result);
        verify(categoriaRepository, times(1)).deleteById(1L);
    }

    @Test
    void testDeleteNotFound() {
        when(categoriaRepository.existsById(1L)).thenReturn(false);

        Exception exception = assertThrows(ResourceNotFoundException.class, () -> {
            service.delete(1L);
        });

        assertEquals("Categoria not found for delete", exception.getMessage());
    }
}

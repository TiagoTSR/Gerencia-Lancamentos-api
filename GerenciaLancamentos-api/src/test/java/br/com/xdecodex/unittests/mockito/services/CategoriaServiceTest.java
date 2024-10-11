package br.com.xdecodex.unittests.mockito.services;

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
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import br.com.xdecodex.data.vo.v1.CategoryVO;
import br.com.xdecodex.exceptions.ResourceNotFoundException;
import br.com.xdecodex.model.Category;
import br.com.xdecodex.repositories.CategoryRepository;
import br.com.xdecodex.services.CategoryService;
import br.com.xdecodex.unittests.mapper.mocks.MockCategory;

@ExtendWith(MockitoExtension.class)
class CategoryServiceTest {

    @InjectMocks
    private CategoryService service;

    @Mock
    private CategoryRepository categoryRepository;

    private MockCategory mockCategory;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockCategory = new MockCategory();
    }

    @Test
    void testFindById() {
        Category category = mockCategory.mockEntity(1);
        category.setId(1L);
        category.setName("Category Teste1"); // Ajuste o valor esperado aqui

        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));

        CategoryVO result = service.findById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Category Teste1", result.getName()); // Ajuste o valor esperado
    }


    @Test
    void testFindByIdNotFound() {
        when(categoryRepository.findById(1L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(ResourceNotFoundException.class, () -> {
            service.findById(1L);
        });

        assertEquals("Category not found for ID: 1", exception.getMessage());
    }

    @Test
    void testCreate() {
        CategoryVO vo = mockCategory.mockVO(1);
        Category category = mockCategory.mockEntity(1);

        when(categoryRepository.save(any(Category.class))).thenReturn(category);

        CategoryVO result = service.create(vo);

        assertNotNull(result);
        assertEquals(vo.getId(), result.getId());
        assertEquals(vo.getName(), result.getName());
    }

    @Test
    void testUpdate() {
        Category category = mockCategory.mockEntity(1);
        CategoryVO vo = mockCategory.mockVO(1);

        when(categoryRepository.findById(vo.getId())).thenReturn(Optional.of(category));
        when(categoryRepository.save(any(Category.class))).thenReturn(category);

        CategoryVO result = service.update(vo);

        assertNotNull(result);
        assertEquals(vo.getId(), result.getId());
        assertEquals(vo.getName(), result.getName());
    }

    @Test
    void testUpdateNotFound() {
        CategoryVO vo = mockCategory.mockVO(1);

        when(categoryRepository.findById(vo.getId())).thenReturn(Optional.empty());

        Exception exception = assertThrows(ResourceNotFoundException.class, () -> {
            service.update(vo);
        });

        assertEquals("Category not found for update", exception.getMessage());
    }

    @Test
    void testDelete() {
        when(categoryRepository.existsById(1L)).thenReturn(true);

        boolean result = service.delete(1L);

        assertTrue(result);
        verify(categoryRepository, times(1)).deleteById(1L);
    }

    @Test
    void testDeleteNotFound() {
        when(categoryRepository.existsById(1L)).thenReturn(false);

        Exception exception = assertThrows(ResourceNotFoundException.class, () -> {
            service.delete(1L);
        });

        assertEquals("Category not found for delete", exception.getMessage());
    }
    
    @Test
    void testFindAll() {
        List<Category> list = mockCategory.mockEntityList();
        System.out.println("Mock list size: " + list.size());
       
        when(categoryRepository.findAll()).thenReturn(list);

        var categories = service.findAll();
        System.out.println("Size of the list returned by the service: " + categories.size());

        assertNotNull(categories);
        assertEquals(14, categories.size()); 

        var categoryOne = categories.get(0);
        assertNotNull(categoryOne);
        assertNotNull(categoryOne.getId());
        assertNotNull(categoryOne.getName());
        System.out.println("CategoryOne: " + categoryOne);

        assertEquals("Category Teste0", categoryOne.getName()); 

        var categoryFour = categories.get(3);
        assertNotNull(categoryFour);
        assertNotNull(categoryFour.getId());
        assertNotNull(categoryFour.getName());
        System.out.println("CategoryFour: " + categoryFour);

        assertEquals("Category Teste3", categoryFour.getName()); 

        var categorySeven = categories.get(6);
        assertNotNull(categorySeven);
        assertNotNull(categorySeven.getId());
        assertNotNull(categorySeven.getName());
        System.out.println("CategorySeven: " + categorySeven);

        assertEquals("Category Teste6", categorySeven.getName()); 
    }

}

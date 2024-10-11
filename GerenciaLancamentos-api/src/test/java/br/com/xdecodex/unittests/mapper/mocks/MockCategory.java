package br.com.xdecodex.unittests.mapper.mocks;

import java.util.ArrayList;
import java.util.List;

import br.com.xdecodex.data.vo.v1.CategoryVO;
import br.com.xdecodex.model.Category;

public class MockCategory {

    public Category mockEntity() {
        return mockEntity(0);
    }
    
    public CategoryVO mockVO() {
        return mockVO(0);
    }

    public List<Category> mockEntityList() {
        List<Category> categorias = new ArrayList<>();
        for (int i = 0; i < 14; i++) {
            categorias.add(mockEntity(i));
        }
        return categorias;
    }

    public List<CategoryVO> mockVOList() {
        List<CategoryVO> categorias = new ArrayList<>();
        for (int i = 0; i < 14; i++) {
            categorias.add(mockVO(i));
        }
        return categorias;
    }
    
    public Category mockEntity(Integer number) {
        Category categoria = new Category();
        categoria.setId(number.longValue());
        categoria.setName("Category Teste" + number);
        return categoria;
    }

    public CategoryVO mockVO(Integer number) {
        CategoryVO categoriaVO = new CategoryVO();
        categoriaVO.setId(number.longValue());
        categoriaVO.setName("Category Teste" + number);
        return categoriaVO;
    }
}

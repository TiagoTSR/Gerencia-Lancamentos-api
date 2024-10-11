package br.com.xdecodex.unittests.mapper.mocks;

import java.util.ArrayList;
import java.util.List;

import br.com.xdecodex.data.vo.v1.CategoriaVO;
import br.com.xdecodex.model.Categoria;

public class MockCategoria {

    public Categoria mockEntity() {
        return mockEntity(0);
    }
    
    public CategoriaVO mockVO() {
        return mockVO(0);
    }

    public List<Categoria> mockEntityList() {
        List<Categoria> categorias = new ArrayList<>();
        for (int i = 0; i < 14; i++) {
            categorias.add(mockEntity(i));
        }
        return categorias;
    }

    public List<CategoriaVO> mockVOList() {
        List<CategoriaVO> categorias = new ArrayList<>();
        for (int i = 0; i < 14; i++) {
            categorias.add(mockVO(i));
        }
        return categorias;
    }
    
    public Categoria mockEntity(Integer number) {
        Categoria categoria = new Categoria();
        categoria.setCodigo(number.longValue());
        categoria.setNome("Categoria Teste" + number);
        return categoria;
    }

    public CategoriaVO mockVO(Integer number) {
        CategoriaVO categoriaVO = new CategoriaVO();
        categoriaVO.setCodigo(number.longValue());
        categoriaVO.setNome("Categoria Teste" + number);
        return categoriaVO;
    }
}

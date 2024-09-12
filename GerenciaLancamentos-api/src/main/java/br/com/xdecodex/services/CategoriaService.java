package br.com.xdecodex.services;

import java.util.List;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.xdecodex.data.vo.v1.CategoriaVO;
import br.com.xdecodex.exceptions.ResourceNotFoundException;
import br.com.xdecodex.mapper.DozerMapper;
import br.com.xdecodex.model.Categoria;
import br.com.xdecodex.repositories.CategoriaRepository;

@Service
public class CategoriaService {

    private Logger logger = Logger.getLogger(CategoriaService.class.getName());

    @Autowired
    private CategoriaRepository categoriaRepository;

    public List<CategoriaVO> findAll() {
        logger.info("Finding all Categorias");
        List<Categoria> categorias = categoriaRepository.findAll();
        return DozerMapper.parseListObjects(categorias, CategoriaVO.class);
    }

    public CategoriaVO findById(Long id) {
        logger.info("Finding Categoria by ID");
        Categoria categoria = categoriaRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Categoria not found for ID: " + id));
        return DozerMapper.parseObject(categoria, CategoriaVO.class);
    }

    public CategoriaVO create(CategoriaVO categoriaVO) {
        logger.info("Creating a new Categoria");
        var categoria = DozerMapper.parseObject(categoriaVO, Categoria.class);
        var savedCategoria = categoriaRepository.save(categoria);
        return DozerMapper.parseObject(savedCategoria, CategoriaVO.class);
    }

    public CategoriaVO update(CategoriaVO categoriaVO) {
        logger.info("Updating Categoria");
        var categoria = categoriaRepository.findById(categoriaVO.getCodigo())
            .orElseThrow(() -> new ResourceNotFoundException("Categoria not found for update"));

        categoria.setNome(categoriaVO.getNome());
        // Atualize outros campos conforme necessário

        var updatedCategoria = categoriaRepository.save(categoria);
        return DozerMapper.parseObject(updatedCategoria, CategoriaVO.class);
    }

    public boolean delete(Long id) {
        logger.info("Deleting Categoria");
        if (!categoriaRepository.existsById(id)) {
            throw new ResourceNotFoundException("Categoria not found for delete");
        }
        categoriaRepository.deleteById(id);
        return true;
    }
}

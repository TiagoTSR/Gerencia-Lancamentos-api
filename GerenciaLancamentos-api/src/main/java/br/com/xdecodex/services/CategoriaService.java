package br.com.xdecodex.services;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.util.List;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.xdecodex.controllers.CategoriaController;
import br.com.xdecodex.data.vo.v1.CategoriaVO;
import br.com.xdecodex.exceptions.ResourceNotFoundException;
import br.com.xdecodex.mapper.DozerMapper;
import br.com.xdecodex.model.Categoria;
import br.com.xdecodex.repositories.CategoriaRepository;

@Service
public class CategoriaService {

    private Logger logger = Logger.getLogger(CategoriaService.class.getName());

    @Autowired
    private CategoriaRepository categoraiaRepository;
    
    public List<CategoriaVO> findAll() {

		logger.info("Encontrando todos os Lancamentos!");

		List<CategoriaVO> categorias = DozerMapper.parseListObjects(categoraiaRepository.findAll(),CategoriaVO.class);
		categorias
			.stream()
			.forEach(c -> c.add(linkTo(methodOn(CategoriaController.class).findById(c.getCodigo())).withSelfRel()));
		return categorias;
	}
    

    public CategoriaVO findById(Long id) {
        logger.info("Finding Categoria by ID");
        
        Categoria categoria = categoraiaRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Categoria not found for ID: " + id));
        CategoriaVO vo = DozerMapper.parseObject(categoria, CategoriaVO.class);
        return vo;
    }

    public CategoriaVO create(CategoriaVO categoriaVO) {
        logger.info("Creating a new Categoria");
        Categoria categoria = DozerMapper.parseObject(categoriaVO, Categoria.class);
        Categoria savedCategoria = categoraiaRepository.save(categoria);
        return DozerMapper.parseObject(savedCategoria, CategoriaVO.class);
    }

    public CategoriaVO update(CategoriaVO categoriaVO) {
        logger.info("Updating Categoria");
        Categoria categoria = categoraiaRepository.findById(categoriaVO.getCodigo())
            .orElseThrow(() -> new ResourceNotFoundException("Categoria not found for update"));

        categoria.setNome(categoriaVO.getNome());
        // Atualize outros campos conforme necessário

        Categoria updatedCategoria = categoraiaRepository.save(categoria);
        return DozerMapper.parseObject(updatedCategoria, CategoriaVO.class);
    }

    public boolean delete(Long id) {
        logger.info("Deleting Categoria");
        if (!categoraiaRepository.existsById(id)) {
            throw new ResourceNotFoundException("Categoria not found for delete");
        }
        categoraiaRepository.deleteById(id);
        return true;
    }
}

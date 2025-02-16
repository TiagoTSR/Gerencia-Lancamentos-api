package br.com.xdecodex.services;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.util.List;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.PagedModel;
import org.springframework.stereotype.Service;

import br.com.xdecodex.controllers.CategoriaController;
import br.com.xdecodex.controllers.LancamentoController;
import br.com.xdecodex.controllers.PessoaController;
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
    
    @Autowired
	PagedResourcesAssembler<CategoriaVO> assembler;
    
    public List<CategoriaVO> findAll() {

		logger.info("Encontrando todos os Launchs!");

		List<CategoriaVO> categorias = DozerMapper.parseListObjects(categoriaRepository.findAll(),CategoriaVO.class);
		categorias
			.stream()
			.forEach(c -> c.add(linkTo(methodOn(CategoriaController.class).findById(c.getId())).withSelfRel()));
		return categorias;
	}
    
    public PagedModel<EntityModel<CategoriaVO>> findAll(Pageable pageable) {

        logger.info("Encontrando todos as categorias!");

        Page<Categoria> categoriaPage = categoriaRepository.findAll(pageable);

        Page<CategoriaVO> categoriaVosPage = categoriaPage.map(categoria -> DozerMapper.parseObject(categoria, CategoriaVO.class));

        categoriaVosPage.forEach(categoriaVO -> categoriaVO.add(
                linkTo(methodOn(LancamentoController.class).findById(categoriaVO.getId())).withSelfRel()));

        Link link = linkTo(methodOn(PessoaController.class)
                .findAll(pageable.getPageNumber(), pageable.getPageSize(), "asc")).withSelfRel();

        return assembler.toModel(categoriaVosPage, link);
    }
    

    public CategoriaVO findById(Long id) {
        logger.info("Finding Categoria by ID");
        
        Categoria categoria = categoriaRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Categoria not found for ID: " + id));
        CategoriaVO vo = DozerMapper.parseObject(categoria, CategoriaVO.class);
        return vo;
    }

    public CategoriaVO create(CategoriaVO categoriaVO) {
        logger.info("Creating a new Category");
        Categoria categoria = DozerMapper.parseObject(categoriaVO, Categoria.class);
        Categoria savedCategoria = categoriaRepository.save(categoria);
        return DozerMapper.parseObject(savedCategoria, CategoriaVO.class);
    }

    public CategoriaVO update(CategoriaVO categoriaVO) {
        logger.info("Updating Categoria");
        Categoria categoria = categoriaRepository.findById(categoriaVO.getId())
            .orElseThrow(() -> new ResourceNotFoundException("Categoria not found for update"));

        categoria.setNome(categoriaVO.getNome());
        // Atualize outros campos conforme necessário

        Categoria updatedCategoria = categoriaRepository.save(categoria);
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

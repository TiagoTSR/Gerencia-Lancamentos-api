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

import br.com.xdecodex.controllers.CategoryController;
import br.com.xdecodex.controllers.LaunchController;
import br.com.xdecodex.controllers.PersonController;
import br.com.xdecodex.data.vo.v1.CategoryVO;
import br.com.xdecodex.exceptions.ResourceNotFoundException;
import br.com.xdecodex.mapper.DozerMapper;
import br.com.xdecodex.model.Category;
import br.com.xdecodex.repositories.CategoryRepository;

@Service
public class CategoryService {

    private Logger logger = Logger.getLogger(CategoryService.class.getName());

    @Autowired
    private CategoryRepository categoryRepository;
    
    @Autowired
	PagedResourcesAssembler<CategoryVO> assembler;
    
    public List<CategoryVO> findAll() {

		logger.info("Encontrando todos os Launchs!");

		List<CategoryVO> categorys = DozerMapper.parseListObjects(categoryRepository.findAll(),CategoryVO.class);
		categorys
			.stream()
			.forEach(c -> c.add(linkTo(methodOn(CategoryController.class).findById(c.getId())).withSelfRel()));
		return categorys;
	}
    
    public PagedModel<EntityModel<CategoryVO>> findAll(Pageable pageable) {

        logger.info("Encontrando todos as categorys!");

        Page<Category> categoryPage = categoryRepository.findAll(pageable);

        Page<CategoryVO> categoryVosPage = categoryPage.map(category -> DozerMapper.parseObject(category, CategoryVO.class));

        categoryVosPage.forEach(categoryVO -> categoryVO.add(
                linkTo(methodOn(LaunchController.class).findById(categoryVO.getId())).withSelfRel()));

        Link link = linkTo(methodOn(PersonController.class)
                .findAll(pageable.getPageNumber(), pageable.getPageSize(), "asc")).withSelfRel();

        return assembler.toModel(categoryVosPage, link);
    }
    

    public CategoryVO findById(Long id) {
        logger.info("Finding Category by ID");
        
        Category category = categoryRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Category not found for ID: " + id));
        CategoryVO vo = DozerMapper.parseObject(category, CategoryVO.class);
        return vo;
    }

    public CategoryVO create(CategoryVO categoryVO) {
        logger.info("Creating a new Category");
        Category category = DozerMapper.parseObject(categoryVO, Category.class);
        Category savedCategory = categoryRepository.save(category);
        return DozerMapper.parseObject(savedCategory, CategoryVO.class);
    }

    public CategoryVO update(CategoryVO categoryVO) {
        logger.info("Updating Category");
        Category category = categoryRepository.findById(categoryVO.getId())
            .orElseThrow(() -> new ResourceNotFoundException("Category not found for update"));

        category.setName(categoryVO.getName());
        // Atualize outros campos conforme necessário

        Category updatedCategory = categoryRepository.save(category);
        return DozerMapper.parseObject(updatedCategory, CategoryVO.class);
    }

    public boolean delete(Long id) {
        logger.info("Deleting Category");
        if (!categoryRepository.existsById(id)) {
            throw new ResourceNotFoundException("Category not found for delete");
        }
        categoryRepository.deleteById(id);
        return true;
    }
}

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

import br.com.xdecodex.controllers.CidadeController;
import br.com.xdecodex.controllers.LancamentoController;
import br.com.xdecodex.controllers.PessoaController;
import br.com.xdecodex.data.vo.v1.CategoriaVO;
import br.com.xdecodex.data.vo.v1.CidadeVO;
import br.com.xdecodex.exceptions.RequiredObjectIsNullException;
import br.com.xdecodex.exceptions.ResourceNotFoundException;
import br.com.xdecodex.mapper.DozerMapper;
import br.com.xdecodex.model.Categoria;
import br.com.xdecodex.model.Cidade;
import br.com.xdecodex.repositories.CidadeRepository;

@Service
public class CidadeService {
	
	private Logger logger = Logger.getLogger(CidadeService.class.getName());

    @Autowired
    private CidadeRepository cidadeRepository;
    
    @Autowired
	PagedResourcesAssembler<CidadeVO> assembler;
   

    public List<CidadeVO> findAll() {

		logger.info("Encontrando todas as Cidades!");

		List<CidadeVO> cidades = DozerMapper.parseListObjects(cidadeRepository.findAll(), CidadeVO.class);
		cidades
			.stream()
			.forEach(p -> p.add(linkTo(methodOn(CidadeController.class).findById(p.getId())).withSelfRel()));
		return cidades;
	}
    
    
    public PagedModel<EntityModel<CidadeVO>> findAll(Pageable pageable) {

        logger.info("Encontrando todos as categorias!");

        Page<Cidade> cidadePage = cidadeRepository.findAll(pageable);

        Page<CidadeVO> cidadeVosPage = cidadePage.map(cidade -> DozerMapper.parseObject(cidade, CidadeVO.class));

        cidadeVosPage.forEach(cidadeVO -> cidadeVO.add(
                linkTo(methodOn(LancamentoController.class).findById(cidadeVO.getId())).withSelfRel()));

        Link link = linkTo(methodOn(PessoaController.class)
                .findAll(pageable.getPageNumber(), pageable.getPageSize(), "asc")).withSelfRel();

        return assembler.toModel(cidadeVosPage, link);
    }


    public CidadeVO findById(Long id) {
		
		logger.info("Encontrando uma cidade!");
		
		Cidade cidade = cidadeRepository.findById(id)
			.orElseThrow(() -> new ResourceNotFoundException("Sem registro para esse ID!"));
		CidadeVO vo = DozerMapper.parseObject(cidade, CidadeVO.class);
		vo.add(linkTo(methodOn(CidadeController.class).findById(id)).withSelfRel());
		return vo;
}

    public CidadeVO create(CidadeVO cidade) {
    	if (cidade == null) {
            throw new RequiredObjectIsNullException("It is not allowed to persist a null object!");
        }
        logger.info("Criando uma cidade!");
        Cidade entity = DozerMapper.parseObject(cidade, Cidade.class);
        CidadeVO vo = DozerMapper.parseObject(cidadeRepository.save(entity), CidadeVO.class);
        vo.add(linkTo(methodOn(CidadeController.class).findById(vo.getId())).withSelfRel());
        return vo;
    }
    
    public CidadeVO update(CidadeVO cidadeVO) {
        logger.info("Updating Cidade");
        Cidade cidade = cidadeRepository.findById(cidadeVO.getId())
            .orElseThrow(() -> new ResourceNotFoundException("Cidade not found for update"));

        cidade.setNome(cidadeVO.getNome());
        // Atualize outros campos conforme necessário

        Cidade updatedCidade = cidadeRepository.save(cidade);
        return DozerMapper.parseObject(updatedCidade, CidadeVO.class);
    }
    
    public boolean delete(Long id) {
        logger.info("Deleting Cidade");
        if (!cidadeRepository.existsById(id)) {
            throw new ResourceNotFoundException("Cidade not found for delete");
        }
        cidadeRepository.deleteById(id);
        return true;
    }
}


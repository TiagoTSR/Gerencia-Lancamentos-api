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

import br.com.xdecodex.controllers.LancamentoController;
import br.com.xdecodex.controllers.PessoaController;
import br.com.xdecodex.data.vo.v1.CidadeVO;
import br.com.xdecodex.data.vo.v1.PessoaVO;
import br.com.xdecodex.data.vo.v2.PessoaVOV2;
import br.com.xdecodex.exceptions.RequiredObjectIsNullException;
import br.com.xdecodex.exceptions.ResourceNotFoundException;
import br.com.xdecodex.mapper.DozerMapper;
import br.com.xdecodex.mapper.custom.PessoaMapper;
import br.com.xdecodex.model.Endereco;
import br.com.xdecodex.model.Pessoa;
import br.com.xdecodex.repositories.PessoaRepository;

@Service
public class PessoaService {

    private Logger logger = Logger.getLogger(PessoaService.class.getName());

    @Autowired
    private PessoaRepository pessoaRepository;
    
    @Autowired
    private PessoaMapper mapper;
    
    @Autowired
	PagedResourcesAssembler<PessoaVO> assembler;

    public List<PessoaVO> findAll() {

		logger.info("Encontrando todas as Pessoas!");

		List<PessoaVO> pessoas = DozerMapper.parseListObjects(pessoaRepository.findAll(), PessoaVO.class);
		pessoas
			.stream()
			.forEach(p -> p.add(linkTo(methodOn(PessoaController.class).findById(p.getId())).withSelfRel()));
		return pessoas;
	}
    
    public PagedModel<EntityModel<PessoaVO>> findAll(Pageable pageable) {

        logger.info("Encontrando todos as pessoas!");

        Page<Pessoa> pessoaPage = pessoaRepository.findAll(pageable);

        Page<PessoaVO> pessoaVosPage = pessoaPage.map(pessoa -> DozerMapper.parseObject(pessoa, PessoaVO.class));

        pessoaVosPage.forEach(pessoaVO -> pessoaVO.add(
                linkTo(methodOn(LancamentoController.class).findById(pessoaVO.getId())).withSelfRel()));

        Link link = linkTo(methodOn(PessoaController.class)
                .findAll(pageable.getPageNumber(), pageable.getPageSize(), "asc")).withSelfRel();

        return assembler.toModel(pessoaVosPage, link);
    }

    public PessoaVO findById(Long id) {
		
		logger.info("Encontrando uma pessoa!");
		
		Pessoa pessoa = pessoaRepository.findById(id)
			.orElseThrow(() -> new ResourceNotFoundException("Sem registro para esse ID!"));
		PessoaVO vo = DozerMapper.parseObject(pessoa, PessoaVO.class);
		
		 if (pessoa.getCidade() != null) {
		        CidadeVO cidadeVO = DozerMapper.parseObject(pessoa.getCidade(), CidadeVO.class);
		        vo.setCidade(cidadeVO);
		    }
		vo.add(linkTo(methodOn(PessoaController.class).findById(id)).withSelfRel());
		return vo;
}

    public PessoaVO create(PessoaVO pessoa) {
    	if (pessoa == null) {
            throw new RequiredObjectIsNullException("It is not allowed to persist a null object!");
        }
        logger.info("Criando uma pessoa!");
        Pessoa entity = DozerMapper.parseObject(pessoa, Pessoa.class);
        PessoaVO vo = DozerMapper.parseObject(pessoaRepository.save(entity), PessoaVO.class);
        vo.add(linkTo(methodOn(PessoaController.class).findById(vo.getId())).withSelfRel());
        return vo;
    }
    
    public PessoaVOV2 createV2(PessoaVOV2 pessoa) {
        logger.info("Creating uma pessoa com V2!");
        Pessoa entity = mapper.convertyVoToEntity(pessoa);
        PessoaVOV2 vo = mapper.convertyEntityToVo(pessoaRepository.save(entity));
        return vo;
    }

    public PessoaVO update(PessoaVO pessoa) {
        // Verifique se o objeto é nulo e lance a exceção apropriada
        if (pessoa == null) {
            throw new RequiredObjectIsNullException("It is not allowed to persist a null object!");
        }

        logger.info("Atualizando uma pessoa!");


        Pessoa entity = pessoaRepository.findById(pessoa.getId())
            .orElseThrow(() -> new ResourceNotFoundException("Nenhum registro para esse ID!"));

        entity.setNome(pessoa.getNome());

        Endereco endereco = pessoa.getEndereco();
        if (endereco != null) {
            entity.setEndereco(endereco);
        }
        
        PessoaVO vo = DozerMapper.parseObject(pessoaRepository.save(entity), PessoaVO.class);
        
        vo.add(linkTo(methodOn(PessoaController.class).findById(vo.getId())).withSelfRel());
        return vo;
    }


    public boolean delete(Long id) {
        logger.info("Deletando uma pessoa!");
        var entity = pessoaRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Pessoa não  encontrada para deletar!"));
        pessoaRepository.delete(entity);
        return true;
    }
}

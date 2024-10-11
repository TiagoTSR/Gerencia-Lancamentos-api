package br.com.xdecodex.services;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.util.ArrayList;
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
import org.springframework.util.ObjectUtils;

import br.com.xdecodex.controllers.LancamentoController;
import br.com.xdecodex.data.vo.v1.LancamentoVO;
import br.com.xdecodex.exceptions.PessoaInexistenteOuInativaException;
import br.com.xdecodex.exceptions.ResourceNotFoundException;
import br.com.xdecodex.mapper.DozerMapper;
import br.com.xdecodex.model.Lancamento;
import br.com.xdecodex.model.Lancamento_;
import br.com.xdecodex.model.Pessoa;
import br.com.xdecodex.repositories.LancamentoRepository;
import br.com.xdecodex.repositories.PessoaRepository;
import br.com.xdecodex.repositories.filter.LancamentoFilter;
import br.com.xdecodex.repositories.lancamento.LancamentoRepositoryQuery;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

@Service
public class LancamentoService implements LancamentoRepositoryQuery {

    private Logger logger = Logger.getLogger(LancamentoService.class.getName());

    @Autowired
    private LancamentoRepository lancamentoRepository;
    
    @Autowired
    private PessoaRepository pessoaRepository;

    @PersistenceContext
    private EntityManager manager;
    
    @Autowired
	PagedResourcesAssembler<LancamentoVO> assembler;
    

    public PagedModel<EntityModel<LancamentoVO>> findAll(Pageable pageable) {

        logger.info("Encontrando todos os lancamentos!");

        // Buscar a página de lançamentos
        Page<Lancamento> lancamentoPage = lancamentoRepository.findAll(pageable);

        // Mapear os lançamentos para LancamentoVO
        Page<LancamentoVO> lancamentoVosPage = lancamentoPage.map(lancamento -> DozerMapper.parseObject(lancamento, LancamentoVO.class));

        // Adicionar o link de self-rel para cada LancamentoVO
        lancamentoVosPage.forEach(lancamentoVO -> lancamentoVO.add(
                linkTo(methodOn(LancamentoController.class).findById(lancamentoVO.getCodigo())).withSelfRel()));

        // Criar o link para a paginação
        Link link = linkTo(methodOn(LancamentoController.class)
                .findAll(pageable.getPageNumber(), pageable.getPageSize(), "asc")).withSelfRel();

        return assembler.toModel(lancamentoVosPage, link);
    }

    public LancamentoVO findById(Long id) {
        logger.info("Encontrando Lancamento pelo ID");
        Lancamento lancamento = lancamentoRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Lancamento not encontrado pelo ID: " + id));
        LancamentoVO vo = DozerMapper.parseObject(lancamento, LancamentoVO.class);
        vo.add(linkTo(methodOn(LancamentoController.class).findById(id)).withSelfRel());
        return vo;
    }

    public LancamentoVO create(LancamentoVO lancamentoVO) {
        logger.info("Salvando um novo Lancamento");

        // Busca a pessoa pelo código no VO e verifica se está inativa
        Pessoa pessoa = pessoaRepository.findById(lancamentoVO.getPessoa().getCodigo())
            .orElseThrow(() -> new PessoaInexistenteOuInativaException());

        if (pessoa.isInativo()) {
            throw new PessoaInexistenteOuInativaException();
        }

        // Conversão do VO para a entidade Lancamento
        Lancamento entity = DozerMapper.parseObject(lancamentoVO, Lancamento.class);
        Lancamento savedEntity = lancamentoRepository.save(entity);
        LancamentoVO vo = DozerMapper.parseObject(savedEntity, LancamentoVO.class);
        vo.add(linkTo(methodOn(LancamentoController.class).findById(vo.getCodigo())).withSelfRel());

        return vo;
    }

    public LancamentoVO update(LancamentoVO lancamentoVO) {
        logger.info("Atualizando Lancamento");

        Lancamento lancamento = lancamentoRepository.findById(lancamentoVO.getCodigo())
            .orElseThrow(() -> new ResourceNotFoundException("Lancamento não encontrado para atualização"));

        // Atualiza os campos do Lancamento
        lancamento.setDescricao(lancamentoVO.getDescricao());
        // Outros campos a serem atualizados...

        Lancamento updatedLancamento = lancamentoRepository.save(lancamento);
        LancamentoVO vo = DozerMapper.parseObject(updatedLancamento, LancamentoVO.class);
        vo.add(linkTo(methodOn(LancamentoController.class).findById(vo.getCodigo())).withSelfRel());

        return vo;
    }

    public boolean delete(Long id) {
        logger.info("Deletando Lancamento");
        Lancamento entity = lancamentoRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Lancamento não encontrado para deletar"));

        lancamentoRepository.delete(entity);
        return true;
    }

    @Override
    public List<LancamentoVO> filtrar(LancamentoFilter lancamentoFilter) {
        CriteriaBuilder builder = manager.getCriteriaBuilder();
        CriteriaQuery<Lancamento> criteria = builder.createQuery(Lancamento.class);
        Root<Lancamento> root = criteria.from(Lancamento.class);

        Predicate[] predicates = criarRestricoes(lancamentoFilter, builder, root);
        criteria.where(predicates);

        TypedQuery<Lancamento> query = manager.createQuery(criteria);
        List<Lancamento> lancamentos = query.getResultList();

        return DozerMapper.parseListObjects(lancamentos, LancamentoVO.class);
    }

    private Predicate[] criarRestricoes(LancamentoFilter lancamentoFilter, CriteriaBuilder builder, Root<Lancamento> root) {
        List<Predicate> predicates = new ArrayList<>();

        if (!ObjectUtils.isEmpty(lancamentoFilter.getDescricao())) {
            predicates.add(builder.like(
                builder.lower(root.get(Lancamento_.descricao)), "%" + lancamentoFilter.getDescricao().toLowerCase() + "%"));
        }

        if (lancamentoFilter.getDataVencimentoDe() != null) {
            predicates.add(
                builder.greaterThanOrEqualTo(root.get(Lancamento_.dataVencimento), lancamentoFilter.getDataVencimentoDe()));
        }

        if (lancamentoFilter.getDataVencimentoAte() != null) {
            predicates.add(
                builder.lessThanOrEqualTo(root.get(Lancamento_.dataVencimento), lancamentoFilter.getDataVencimentoAte()));
        }

        return predicates.toArray(new Predicate[0]);
    }
}
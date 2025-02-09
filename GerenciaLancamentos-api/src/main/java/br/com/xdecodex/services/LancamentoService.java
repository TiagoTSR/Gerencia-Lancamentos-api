package br.com.xdecodex.services;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import br.com.xdecodex.controllers.LancamentoController;
import br.com.xdecodex.data.vo.v1.LancamentoVO;
import br.com.xdecodex.dto.LancamentoStatisticaCategoria;
import br.com.xdecodex.exceptions.PessoaInexistenteOuInativaException;
import br.com.xdecodex.exceptions.ResourceNotFoundException;
import br.com.xdecodex.mapper.DozerMapper;
import br.com.xdecodex.model.Categoria_;
import br.com.xdecodex.model.Lancamento;
import br.com.xdecodex.model.Lancamento_;
import br.com.xdecodex.model.Pessoa;
import br.com.xdecodex.model.Pessoa_;
import br.com.xdecodex.repositories.LancamentoRepository;
import br.com.xdecodex.repositories.PessoaRepository;
import br.com.xdecodex.repositories.filter.LancamentoFilter;
import br.com.xdecodex.repositories.launch.LancamentoRepositoryQuery;
import br.com.xdecodex.repositories.projection.ResumoLancamento;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

@Service
public class LancamentoService implements LancamentoRepositoryQuery {

    private static final Logger logger = Logger.getLogger(LancamentoService.class.getName());

    @Autowired
    private LancamentoRepository lancamentoRepository;

    @Autowired
    private PessoaRepository pessoaRepository;

    @PersistenceContext
    private EntityManager manager;

    @Autowired
    private PagedResourcesAssembler<LancamentoVO> assembler;

  
    public PagedModel<EntityModel<LancamentoVO>> findAll(Pageable pageable) {
        logger.info("Finding all lancamentos!");

        Page<Lancamento> lancamentoPage = lancamentoRepository.findAll(pageable);
        Page<LancamentoVO> lancamentoVosPage = lancamentoPage.map(lancamento -> DozerMapper.parseObject(lancamento, LancamentoVO.class));

        return assembler.toModel(lancamentoVosPage, 
            linkTo(methodOn(LancamentoController.class).findAll(pageable.getPageNumber(), pageable.getPageSize(), "asc")).withSelfRel());
    }

    
    public LancamentoVO findById(Long id) {
        logger.info("Finding Lancamento by ID");
        Lancamento lancamento = lancamentoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Lancamento not found by ID: " + id));
        return DozerMapper.parseObject(lancamento, LancamentoVO.class);
    }

    
    public LancamentoVO create(LancamentoVO lancamentoVO) {
        logger.info("Saving a new Lancamento");

        // Verifica a existência e status da pessoa associada
        Pessoa pessoa = pessoaRepository.findById(lancamentoVO.getPessoa().getId())
                .orElseThrow(PessoaInexistenteOuInativaException::new);

        if (pessoa.isInactive()) {
            throw new PessoaInexistenteOuInativaException();
        }

        Lancamento entity = DozerMapper.parseObject(lancamentoVO, Lancamento.class);
        Lancamento savedEntity = lancamentoRepository.save(entity);

        LancamentoVO vo = DozerMapper.parseObject(savedEntity, LancamentoVO.class);
        vo.add(linkTo(methodOn(LancamentoController.class).findById(vo.getId())).withSelfRel());
        return vo;
    }

    
    public LancamentoVO update(LancamentoVO lancamentoVO) {
        logger.info("Updating Lancamento");

        Lancamento lancamento = lancamentoRepository.findById(lancamentoVO.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Lancamento not found for update"));

        // Atualiza os campos necessários
        lancamento.setDescricao(lancamentoVO.getDescricao());
        // Outros campos...

        Lancamento updatedLancamento = lancamentoRepository.save(lancamento);

        LancamentoVO vo = DozerMapper.parseObject(updatedLancamento, LancamentoVO.class);
        vo.add(linkTo(methodOn(LancamentoController.class).findById(vo.getId())).withSelfRel());
        return vo;
    }

    
    public boolean delete(Long id) {
        logger.info("Deleting Lancamento");
        Lancamento entity = lancamentoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Lancamento not found to delete"));

        lancamentoRepository.delete(entity);
        return true;
    }

    @Override
    public List<LancamentoStatisticaCategoria> porCategoria(LocalDate mesReferencia) {
        CriteriaBuilder builder = manager.getCriteriaBuilder();
        CriteriaQuery<LancamentoStatisticaCategoria> criteria = builder.createQuery(LancamentoStatisticaCategoria.class);
        Root<Lancamento> root = criteria.from(Lancamento.class);

        criteria.select(builder.construct(
                LancamentoStatisticaCategoria.class,
                root.get(Lancamento_.categoria),
                builder.sum(root.get(Lancamento_.valor))));

        LocalDate primeiroDia = mesReferencia.withDayOfMonth(1);
        LocalDate ultimoDia = mesReferencia.withDayOfMonth(mesReferencia.lengthOfMonth());

        criteria.where(builder.between(root.get(Lancamento_.dataVencimento), primeiroDia, ultimoDia));
        criteria.groupBy(root.get(Lancamento_.categoria));

        return manager.createQuery(criteria).getResultList();
    }

    @Override
    public List<LancamentoVO> filtrar(LancamentoFilter lancamentoFilter, Pageable pageable) {
        CriteriaBuilder builder = manager.getCriteriaBuilder();
        CriteriaQuery<Lancamento> criteria = builder.createQuery(Lancamento.class);
        Root<Lancamento> root = criteria.from(Lancamento.class);

        Predicate[] predicates = criarRestricoes(lancamentoFilter, builder, root);
        criteria.where(predicates);

        TypedQuery<Lancamento> query = manager.createQuery(criteria);
        adicionarRestricoesDePaginacao(query, pageable);

        List<Lancamento> lancamentos = query.getResultList();
        return DozerMapper.parseListObjects(lancamentos, LancamentoVO.class);
    }

    @Override
    public Page<ResumoLancamento> resumir(LancamentoFilter lancamentoFilter, Pageable pageable) {
        CriteriaBuilder builder = manager.getCriteriaBuilder();
        CriteriaQuery<ResumoLancamento> criteria = builder.createQuery(ResumoLancamento.class);
        Root<Lancamento> root = criteria.from(Lancamento.class);

        criteria.select(builder.construct(
                ResumoLancamento.class,
                root.get(Lancamento_.id),
                root.get(Lancamento_.descricao),
                root.get(Lancamento_.dataVencimento),
                root.get(Lancamento_.dataPagamento),
                root.get(Lancamento_.valor),
                root.get(Lancamento_.tipo),
                root.get(Lancamento_.categoria).get(Categoria_.nome),
                root.get(Lancamento_.pessoa).get(Pessoa_.nome)));

        Predicate[] predicates = criarRestricoes(lancamentoFilter, builder, root);
        criteria.where(predicates);

        TypedQuery<ResumoLancamento> query = manager.createQuery(criteria);
        adicionarRestricoesDePaginacao(query, pageable);

        return new PageImpl<>(query.getResultList(), pageable, total(lancamentoFilter));
    }

    private Predicate[] criarRestricoes(LancamentoFilter lancamentoFilter, CriteriaBuilder builder, Root<Lancamento> root) {
        List<Predicate> predicates = new ArrayList<>();

        if (!ObjectUtils.isEmpty(lancamentoFilter.getDescricao())) {
            predicates.add(builder.like(builder.lower(root.get(Lancamento_.descricao)), "%" + lancamentoFilter.getDescricao().toLowerCase() + "%"));
        }

        if (lancamentoFilter.getDataVencimentoDe() != null) {
            predicates.add(builder.greaterThanOrEqualTo(root.get(Lancamento_.dataVencimento), lancamentoFilter.getDataVencimentoDe()));
        }

        if (lancamentoFilter.getDataVencimentoAte() != null) {
            predicates.add(builder.lessThanOrEqualTo(root.get(Lancamento_.dataVencimento), lancamentoFilter.getDataVencimentoAte()));
        }

        return predicates.toArray(new Predicate[0]);
    }

    private void adicionarRestricoesDePaginacao(TypedQuery<?> query, Pageable pageable) {
        query.setFirstResult(pageable.getPageNumber() * pageable.getPageSize());
        query.setMaxResults(pageable.getPageSize());
    }

    private Long total(LancamentoFilter lancamentoFilter) {
        CriteriaBuilder builder = manager.getCriteriaBuilder();
        CriteriaQuery<Long> criteria = builder.createQuery(Long.class);
        Root<Lancamento> root = criteria.from(Lancamento.class);

        Predicate[] predicates = criarRestricoes(lancamentoFilter, builder, root);
        criteria.where(predicates);

        criteria.select(builder.count(root));
        return manager.createQuery(criteria).getSingleResult();
    }
}
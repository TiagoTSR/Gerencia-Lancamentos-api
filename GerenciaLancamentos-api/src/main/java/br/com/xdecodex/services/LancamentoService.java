package br.com.xdecodex.services;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import br.com.xdecodex.controllers.LancamentoController;
import br.com.xdecodex.data.vo.v1.LancamentoVO;
import br.com.xdecodex.dto.LancamentoEstatisticaCategoria;
import br.com.xdecodex.dto.LancamentoEstatisticaDia;
import br.com.xdecodex.dto.LancamentoEstatisticaPessoa;
import br.com.xdecodex.exceptions.PessoaInexistenteOuInativaException;
import br.com.xdecodex.exceptions.ResourceNotFoundException;
import br.com.xdecodex.mail.Mailer;
import br.com.xdecodex.mapper.DozerMapper;
import br.com.xdecodex.model.Categoria_;
import br.com.xdecodex.model.Lancamento;
import br.com.xdecodex.model.Lancamento_;
import br.com.xdecodex.model.Pessoa;
import br.com.xdecodex.model.Pessoa_;
import br.com.xdecodex.model.Usuario;
import br.com.xdecodex.repositories.LancamentoRepository;
import br.com.xdecodex.repositories.PessoaRepository;
import br.com.xdecodex.repositories.UsuarioRepository;
import br.com.xdecodex.repositories.filter.LancamentoFilter;
import br.com.xdecodex.repositories.launch.LancamentoRepositoryQuery;
import br.com.xdecodex.repositories.projection.ResumoLancamento;
import br.com.xdecodex.storage.S3;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

@Service
public class LancamentoService implements LancamentoRepositoryQuery {

    private static final Logger logger = Logger.getLogger(LancamentoService.class.getName());
    
    private static final String DESTINATARIOS = "ROLE_PESQUISAR_LANCAMENTO";

    @Autowired
    private LancamentoRepository lancamentoRepository;
    
    @Lazy
    @Autowired
    private LancamentoRepositoryQuery lancamentoRepositoryQuery;
    
    @Autowired
	private UsuarioRepository usuarioRepository;

    @Autowired
    private PessoaRepository pessoaRepository;

    @PersistenceContext
    private EntityManager manager;
    
    @Autowired
    private S3 s3;
    
    @Autowired  
	private Mailer mailer;

    @Autowired
    private PagedResourcesAssembler<LancamentoVO> assembler;
    
    @Scheduled(cron = "0 0 6 * * *")
    public void avisarSobreLancamentosVencidos() {
        if (logger.isLoggable(Level.FINE)) {  // Nível equivalente ao debug
            logger.fine("Preparando envio de e-mails de aviso de lançamentos vencidos.");
        }

        List<Lancamento> vencidos = lancamentoRepository
                .findByDataVencimentoLessThanEqualAndDataPagamentoIsNull(LocalDate.now());

        if (vencidos.isEmpty()) {
            logger.info("Sem lançamentos vencidos para aviso.");
            return;
        }

        logger.info("Existem " + vencidos.size() + " lançamentos vencidos.");

        List<Usuario> destinatarios = usuarioRepository
                .findByPermissoesDescricao(DESTINATARIOS);

        if (destinatarios.isEmpty()) {
            logger.warning("Existem lançamentos vencidos, mas o sistema não encontrou destinatários.");
            return;
        }

        mailer.avisarSobreLancamentosVencidos(vencidos, destinatarios);

        logger.info("Envio de e-mail de aviso concluído.");
    }
    
    @SuppressWarnings("deprecation")
    public byte[] relatorioPorPessoa(LocalDate inicio, LocalDate fim) throws Exception {
        List<LancamentoEstatisticaPessoa> dados = lancamentoRepositoryQuery.porPessoa(inicio, fim);
        
        Map<String, Object> parametros = new HashMap<>();
        parametros.put("DT_INICIO", Date.valueOf(inicio));
        parametros.put("DT_FIM", Date.valueOf(fim));
        parametros.put("REPORT_LOCALE", new Locale("pt", "BR"));
        
        // Load the report file
        InputStream inputStream = this.getClass().getResourceAsStream("/relatorios/lancamentos-por-pessoa.jasper");

        
        if (inputStream == null) {
            throw new FileNotFoundException("Jasper report not found at /relatorios/lancamentos-por-pessoa.jasper");
        }

        JasperPrint jasperPrint = JasperFillManager.fillReport(inputStream, parametros,
                new JRBeanCollectionDataSource(dados));
        
        return JasperExportManager.exportReportToPdf(jasperPrint);
    }


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
        
        if (StringUtils.hasText(lancamentoVO.getAnexo())) {
			s3.create(lancamentoVO.getAnexo());
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
	public List<LancamentoEstatisticaPessoa> porPessoa(LocalDate inicio, LocalDate fim) {
		CriteriaBuilder criteriaBuilder = manager.getCriteriaBuilder();
		
		CriteriaQuery<LancamentoEstatisticaPessoa> criteriaQuery = criteriaBuilder.
				createQuery(LancamentoEstatisticaPessoa.class);
		
		Root<Lancamento> root = criteriaQuery.from(Lancamento.class);
		
		criteriaQuery.select(criteriaBuilder.construct(LancamentoEstatisticaPessoa.class, 
				root.get(Lancamento_.tipo),
				root.get(Lancamento_.pessoa),
				criteriaBuilder.sum(root.get(Lancamento_.valor))));
		
		criteriaQuery.where(
				criteriaBuilder.greaterThanOrEqualTo(root.get(Lancamento_.dataVencimento), 
						inicio),
				criteriaBuilder.lessThanOrEqualTo(root.get(Lancamento_.dataVencimento), 
						fim));
		
		criteriaQuery.groupBy(root.get(Lancamento_.tipo), 
				root.get(Lancamento_.pessoa));
		
		TypedQuery<LancamentoEstatisticaPessoa> typedQuery = manager
				.createQuery(criteriaQuery);
		
		return typedQuery.getResultList();
	}
	
	@Override
	public List<LancamentoEstatisticaDia> porDia(LocalDate mesReferencia) {
		CriteriaBuilder criteriaBuilder = manager.getCriteriaBuilder();
		
		CriteriaQuery<LancamentoEstatisticaDia> criteriaQuery = criteriaBuilder.
				createQuery(LancamentoEstatisticaDia.class);
		
		Root<Lancamento> root = criteriaQuery.from(Lancamento.class);
		
		criteriaQuery.select(criteriaBuilder.construct(LancamentoEstatisticaDia.class, 
				root.get(Lancamento_.tipo),
				root.get(Lancamento_.dataVencimento),
				criteriaBuilder.sum(root.get(Lancamento_.valor))));
		
		LocalDate primeiroDia = mesReferencia.withDayOfMonth(1);
		LocalDate ultimoDia = mesReferencia.withDayOfMonth(mesReferencia.lengthOfMonth());
		
		criteriaQuery.where(
				criteriaBuilder.greaterThanOrEqualTo(root.get(Lancamento_.dataVencimento), 
						primeiroDia),
				criteriaBuilder.lessThanOrEqualTo(root.get(Lancamento_.dataVencimento), 
						ultimoDia));
		
		criteriaQuery.groupBy(root.get(Lancamento_.tipo), 
				root.get(Lancamento_.dataVencimento));
		
		TypedQuery<LancamentoEstatisticaDia> typedQuery = manager
				.createQuery(criteriaQuery);
		
		return typedQuery.getResultList();
	}

    @Override
	public List<LancamentoEstatisticaCategoria> porCategoria(LocalDate mesReferencia) {
		CriteriaBuilder criteriaBuilder = manager.getCriteriaBuilder();
		
		CriteriaQuery<LancamentoEstatisticaCategoria> criteriaQuery = criteriaBuilder.
				createQuery(LancamentoEstatisticaCategoria.class);
		
		Root<Lancamento> root = criteriaQuery.from(Lancamento.class);
		
		criteriaQuery.select(criteriaBuilder.construct(LancamentoEstatisticaCategoria.class, 
				root.get(Lancamento_.categoria),
				criteriaBuilder.sum(root.get(Lancamento_.valor))));
		
		LocalDate primeiroDia = mesReferencia.withDayOfMonth(1);
		LocalDate ultimoDia = mesReferencia.withDayOfMonth(mesReferencia.lengthOfMonth());
		
		criteriaQuery.where(
				criteriaBuilder.greaterThanOrEqualTo(root.get(Lancamento_.dataVencimento), 
						primeiroDia),
				criteriaBuilder.lessThanOrEqualTo(root.get(Lancamento_.dataVencimento), 
						ultimoDia));
		
		criteriaQuery.groupBy(root.get(Lancamento_.categoria));
		
		TypedQuery<LancamentoEstatisticaCategoria> typedQuery = manager
				.createQuery(criteriaQuery);
		
		return typedQuery.getResultList();
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
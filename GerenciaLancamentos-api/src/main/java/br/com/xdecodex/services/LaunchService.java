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

import br.com.xdecodex.controllers.LaunchController;
import br.com.xdecodex.data.vo.v1.LaunchVO;
import br.com.xdecodex.dto.LaunchStatisticCategory;
import br.com.xdecodex.exceptions.PersonInexistenteOuInativaException;
import br.com.xdecodex.exceptions.ResourceNotFoundException;
import br.com.xdecodex.mapper.DozerMapper;
import br.com.xdecodex.model.Launch;
import br.com.xdecodex.model.Launch_;
import br.com.xdecodex.model.Person_;
import br.com.xdecodex.model.Category_;
import br.com.xdecodex.model.Person;
import br.com.xdecodex.repositories.LaunchRepository;
import br.com.xdecodex.repositories.PersonRepository;
import br.com.xdecodex.repositories.filter.LaunchFilter;
import br.com.xdecodex.repositories.launch.LaunchRepositoryQuery;
import br.com.xdecodex.repositories.projection.ResumeLaunch;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

@Service
public class LaunchService implements LaunchRepositoryQuery {

    private static final Logger logger = Logger.getLogger(LaunchService.class.getName());

    @Autowired
    private LaunchRepository launchRepository;

    @Autowired
    private PersonRepository personRepository;

    @PersistenceContext
    private EntityManager manager;

    @Autowired
    private PagedResourcesAssembler<LaunchVO> assembler;

  
    public PagedModel<EntityModel<LaunchVO>> findAll(Pageable pageable) {
        logger.info("Finding all launches!");

        Page<Launch> launchPage = launchRepository.findAll(pageable);
        Page<LaunchVO> launchVosPage = launchPage.map(launch -> DozerMapper.parseObject(launch, LaunchVO.class));

        return assembler.toModel(launchVosPage, 
            linkTo(methodOn(LaunchController.class).findAll(pageable.getPageNumber(), pageable.getPageSize(), "asc")).withSelfRel());
    }

    
    public LaunchVO findById(Long id) {
        logger.info("Finding Launch by ID");
        Launch launch = launchRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Launch not found by ID: " + id));
        return DozerMapper.parseObject(launch, LaunchVO.class);
    }

    
    public LaunchVO create(LaunchVO launchVO) {
        logger.info("Saving a new Launch");

        // Verifica a existência e status da pessoa associada
        Person person = personRepository.findById(launchVO.getPerson().getId())
                .orElseThrow(PersonInexistenteOuInativaException::new);

        if (person.isInactive()) {
            throw new PersonInexistenteOuInativaException();
        }

        Launch entity = DozerMapper.parseObject(launchVO, Launch.class);
        Launch savedEntity = launchRepository.save(entity);

        LaunchVO vo = DozerMapper.parseObject(savedEntity, LaunchVO.class);
        vo.add(linkTo(methodOn(LaunchController.class).findById(vo.getId())).withSelfRel());
        return vo;
    }

    
    public LaunchVO update(LaunchVO launchVO) {
        logger.info("Updating Launch");

        Launch launch = launchRepository.findById(launchVO.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Launch not found for update"));

        // Atualiza os campos necessários
        launch.setDescription(launchVO.getDescription());
        // Outros campos...

        Launch updatedLaunch = launchRepository.save(launch);

        LaunchVO vo = DozerMapper.parseObject(updatedLaunch, LaunchVO.class);
        vo.add(linkTo(methodOn(LaunchController.class).findById(vo.getId())).withSelfRel());
        return vo;
    }

    
    public boolean delete(Long id) {
        logger.info("Deleting Launch");
        Launch entity = launchRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Launch not found to delete"));

        launchRepository.delete(entity);
        return true;
    }

    @Override
    public List<LaunchStatisticCategory> porCategoria(LocalDate mesReferencia) {
        CriteriaBuilder builder = manager.getCriteriaBuilder();
        CriteriaQuery<LaunchStatisticCategory> criteria = builder.createQuery(LaunchStatisticCategory.class);
        Root<Launch> root = criteria.from(Launch.class);

        criteria.select(builder.construct(
                LaunchStatisticCategory.class,
                root.get(Launch_.category),
                builder.sum(root.get(Launch_.value))));

        LocalDate primeiroDia = mesReferencia.withDayOfMonth(1);
        LocalDate ultimoDia = mesReferencia.withDayOfMonth(mesReferencia.lengthOfMonth());

        criteria.where(builder.between(root.get(Launch_.expirationDate), primeiroDia, ultimoDia));
        criteria.groupBy(root.get(Launch_.category));

        return manager.createQuery(criteria).getResultList();
    }

    @Override
    public List<LaunchVO> filtrar(LaunchFilter launchFilter, Pageable pageable) {
        CriteriaBuilder builder = manager.getCriteriaBuilder();
        CriteriaQuery<Launch> criteria = builder.createQuery(Launch.class);
        Root<Launch> root = criteria.from(Launch.class);

        Predicate[] predicates = criarRestricoes(launchFilter, builder, root);
        criteria.where(predicates);

        TypedQuery<Launch> query = manager.createQuery(criteria);
        adicionarRestricoesDePaginacao(query, pageable);

        List<Launch> launches = query.getResultList();
        return DozerMapper.parseListObjects(launches, LaunchVO.class);
    }

    @Override
    public Page<ResumeLaunch> resumir(LaunchFilter launchFilter, Pageable pageable) {
        CriteriaBuilder builder = manager.getCriteriaBuilder();
        CriteriaQuery<ResumeLaunch> criteria = builder.createQuery(ResumeLaunch.class);
        Root<Launch> root = criteria.from(Launch.class);

        criteria.select(builder.construct(
                ResumeLaunch.class,
                root.get(Launch_.id),
                root.get(Launch_.description),
                root.get(Launch_.expirationDate),
                root.get(Launch_.paymentDate),
                root.get(Launch_.value),
                root.get(Launch_.type),
                root.get(Launch_.category).get(Category_.name),
                root.get(Launch_.person).get(Person_.name)));

        Predicate[] predicates = criarRestricoes(launchFilter, builder, root);
        criteria.where(predicates);

        TypedQuery<ResumeLaunch> query = manager.createQuery(criteria);
        adicionarRestricoesDePaginacao(query, pageable);

        return new PageImpl<>(query.getResultList(), pageable, total(launchFilter));
    }

    private Predicate[] criarRestricoes(LaunchFilter launchFilter, CriteriaBuilder builder, Root<Launch> root) {
        List<Predicate> predicates = new ArrayList<>();

        if (!ObjectUtils.isEmpty(launchFilter.getDescription())) {
            predicates.add(builder.like(builder.lower(root.get(Launch_.description)), "%" + launchFilter.getDescription().toLowerCase() + "%"));
        }

        if (launchFilter.getExpirationDateFrom() != null) {
            predicates.add(builder.greaterThanOrEqualTo(root.get(Launch_.expirationDate), launchFilter.getExpirationDateFrom()));
        }

        if (launchFilter.getExpiryDateBy() != null) {
            predicates.add(builder.lessThanOrEqualTo(root.get(Launch_.expirationDate), launchFilter.getExpiryDateBy()));
        }

        return predicates.toArray(new Predicate[0]);
    }

    private void adicionarRestricoesDePaginacao(TypedQuery<?> query, Pageable pageable) {
        query.setFirstResult(pageable.getPageNumber() * pageable.getPageSize());
        query.setMaxResults(pageable.getPageSize());
    }

    private Long total(LaunchFilter launchFilter) {
        CriteriaBuilder builder = manager.getCriteriaBuilder();
        CriteriaQuery<Long> criteria = builder.createQuery(Long.class);
        Root<Launch> root = criteria.from(Launch.class);

        Predicate[] predicates = criarRestricoes(launchFilter, builder, root);
        criteria.where(predicates);

        criteria.select(builder.count(root));
        return manager.createQuery(criteria).getSingleResult();
    }
}
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

import br.com.xdecodex.controllers.LaunchController;
import br.com.xdecodex.data.vo.v1.LaunchVO;
import br.com.xdecodex.exceptions.PersonInexistenteOuInativaException;
import br.com.xdecodex.exceptions.ResourceNotFoundException;
import br.com.xdecodex.mapper.DozerMapper;
import br.com.xdecodex.model.Launch;
import br.com.xdecodex.model.Launch_;
import br.com.xdecodex.model.Person;
import br.com.xdecodex.repositories.LaunchRepository;
import br.com.xdecodex.repositories.PersonRepository;
import br.com.xdecodex.repositories.filter.LaunchFilter;
import br.com.xdecodex.repositories.launch.LaunchRepositoryQuery;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

@Service
public class LaunchService implements LaunchRepositoryQuery {

    private Logger logger = Logger.getLogger(LaunchService.class.getName());

    @Autowired
    private LaunchRepository launchRepository;
    
    @Autowired
    private PersonRepository personRepository;

    @PersistenceContext
    private EntityManager manager;
    
    @Autowired
	PagedResourcesAssembler<LaunchVO> assembler;
    

    public PagedModel<EntityModel<LaunchVO>> findAll(Pageable pageable) {

        logger.info("Finding all launches!");

        // Buscar a página de lançamentos
        Page<Launch> launchPage = launchRepository.findAll(pageable);

        // Mapear os lançamentos para LaunchVO
        Page<LaunchVO> launchVosPage = launchPage.map(launch -> DozerMapper.parseObject(launch, LaunchVO.class));

        // Adicionar o link de self-rel para cada LaunchVO
        launchVosPage.forEach(launchVO -> launchVO.add(
                linkTo(methodOn(LaunchController.class).findById(launchVO.getId())).withSelfRel()));

        // Criar o link para a paginação
        Link link = linkTo(methodOn(LaunchController.class)
                .findAll(pageable.getPageNumber(), pageable.getPageSize(), "asc")).withSelfRel();

        return assembler.toModel(launchVosPage, link);
    }

    public LaunchVO findById(Long id) {
        logger.info("Finding Launch by ID");
        Launch launch = launchRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Launch not found by ID: " + id));
        LaunchVO vo = DozerMapper.parseObject(launch, LaunchVO.class);
        vo.add(linkTo(methodOn(LaunchController.class).findById(id)).withSelfRel());
        return vo;
    }

    public LaunchVO create(LaunchVO launchVO) {
        logger.info("Saving a new Launch");

        // Busca a person pelo código no VO e verifica se está inativa
        Person person = personRepository.findById(launchVO.getPerson().getId())
            .orElseThrow(() -> new PersonInexistenteOuInativaException());

        if (person.isInactive()) {
            throw new PersonInexistenteOuInativaException();
        }

        // Conversão do VO para a entidade Launch
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

        // Atualiza os campos do Launch
        launch.setDescription(launchVO.getDescription());
        // Outros campos a serem atualizados...

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
    public List<LaunchVO> filtrar(LaunchFilter launchFilter) {
        CriteriaBuilder builder = manager.getCriteriaBuilder();
        CriteriaQuery<Launch> criteria = builder.createQuery(Launch.class);
        Root<Launch> root = criteria.from(Launch.class);

        Predicate[] predicates = criarRestricoes(launchFilter, builder, root);
        criteria.where(predicates);

        TypedQuery<Launch> query = manager.createQuery(criteria);
        List<Launch> launchs = query.getResultList();

        return DozerMapper.parseListObjects(launchs, LaunchVO.class);
    }

    private Predicate[] criarRestricoes(LaunchFilter launchFilter, CriteriaBuilder builder, Root<Launch> root) {
        List<Predicate> predicates = new ArrayList<>();

        if (!ObjectUtils.isEmpty(launchFilter.getDescription())) {
            predicates.add(builder.like(
                builder.lower(root.get(Launch_.description)), "%" + launchFilter.getDescription().toLowerCase() + "%"));
        }

        if (launchFilter.getExpirationDateFrom() != null) {
            predicates.add(
                builder.greaterThanOrEqualTo(root.get(Launch_.expirationDate), launchFilter.getExpirationDateFrom()));
        }

        if (launchFilter.getExpiryDateBy() != null) {
            predicates.add(
                builder.lessThanOrEqualTo(root.get(Launch_.expirationDate), launchFilter.getExpiryDateBy()));
        }

        return predicates.toArray(new Predicate[0]);
    }
}
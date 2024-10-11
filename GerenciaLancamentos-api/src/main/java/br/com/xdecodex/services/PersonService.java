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

import br.com.xdecodex.controllers.LaunchController;
import br.com.xdecodex.controllers.PersonController;
import br.com.xdecodex.data.vo.v1.PersonVO;
import br.com.xdecodex.data.vo.v2.PersonVOV2;
import br.com.xdecodex.exceptions.RequiredObjectIsNullException;
import br.com.xdecodex.exceptions.ResourceNotFoundException;
import br.com.xdecodex.mapper.DozerMapper;
import br.com.xdecodex.mapper.custom.PersonMapper;
import br.com.xdecodex.model.Address;
import br.com.xdecodex.model.Person;
import br.com.xdecodex.repositories.PersonRepository;

@Service
public class PersonService {

    private Logger logger = Logger.getLogger(PersonService.class.getName());

    @Autowired
    private PersonRepository personRepository;
    
    @Autowired
    private PersonMapper mapper;
    
    @Autowired
	PagedResourcesAssembler<PersonVO> assembler;

    public List<PersonVO> findAll() {

		logger.info("Encontrando todas as Persons!");

		List<PersonVO> persons = DozerMapper.parseListObjects(personRepository.findAll(), PersonVO.class);
		persons
			.stream()
			.forEach(p -> p.add(linkTo(methodOn(PersonController.class).findById(p.getId())).withSelfRel()));
		return persons;
	}
    
    public PagedModel<EntityModel<PersonVO>> findAll(Pageable pageable) {

        logger.info("Encontrando todos as persons!");

        Page<Person> personPage = personRepository.findAll(pageable);

        Page<PersonVO> personVosPage = personPage.map(person -> DozerMapper.parseObject(person, PersonVO.class));

        personVosPage.forEach(personVO -> personVO.add(
                linkTo(methodOn(LaunchController.class).findById(personVO.getId())).withSelfRel()));

        Link link = linkTo(methodOn(PersonController.class)
                .findAll(pageable.getPageNumber(), pageable.getPageSize(), "asc")).withSelfRel();

        return assembler.toModel(personVosPage, link);
    }

    public PersonVO findById(Long id) {
		
		logger.info("Encontrando uma person!");
		
		Person person = personRepository.findById(id)
			.orElseThrow(() -> new ResourceNotFoundException("Sem registro para esse ID!"));
		PersonVO vo = DozerMapper.parseObject(person, PersonVO.class);
		vo.add(linkTo(methodOn(PersonController.class).findById(id)).withSelfRel());
		return vo;
}

    public PersonVO create(PersonVO person) {
    	if (person == null) {
            throw new RequiredObjectIsNullException("It is not allowed to persist a null object!");
        }
        logger.info("Criando uma person!");
        Person entity = DozerMapper.parseObject(person, Person.class);
        PersonVO vo = DozerMapper.parseObject(personRepository.save(entity), PersonVO.class);
        vo.add(linkTo(methodOn(PersonController.class).findById(vo.getId())).withSelfRel());
        return vo;
    }
    
    public PersonVOV2 createV2(PersonVOV2 person) {
        logger.info("Creating uma person com V2!");
        Person entity = mapper.convertyVoToEntity(person);
        PersonVOV2 vo = mapper.convertyEntityToVo(personRepository.save(entity));
        return vo;
    }

    public PersonVO update(PersonVO person) {
        // Verifique se o objeto é nulo e lance a exceção apropriada
        if (person == null) {
            throw new RequiredObjectIsNullException("It is not allowed to persist a null object!");
        }

        logger.info("Atualizando uma person!");


        Person entity = personRepository.findById(person.getId())
            .orElseThrow(() -> new ResourceNotFoundException("Nenhum registro para esse ID!"));

        entity.setName(person.getName());

        Address endereco = person.getAddress();
        if (endereco != null) {
            entity.setAddress(endereco);
        }
        
        PersonVO vo = DozerMapper.parseObject(personRepository.save(entity), PersonVO.class);
        
        vo.add(linkTo(methodOn(PersonController.class).findById(vo.getId())).withSelfRel());
        return vo;
    }


    public boolean delete(Long id) {
        logger.info("Deletando uma person!");
        var entity = personRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Person não  encontrada para deletar!"));
        personRepository.delete(entity);
        return true;
    }
}

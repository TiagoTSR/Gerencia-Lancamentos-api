package br.com.xdecodex.unittests.mockito.services;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import br.com.xdecodex.data.vo.v1.PersonVO;
import br.com.xdecodex.exceptions.RequiredObjectIsNullException;
import br.com.xdecodex.model.Person;
import br.com.xdecodex.repositories.PersonRepository;
import br.com.xdecodex.services.PersonService;
import br.com.xdecodex.unittests.mapper.mocks.MockPerson;

@TestInstance(Lifecycle.PER_CLASS)
@ExtendWith(MockitoExtension.class)
class PersonServicesTest {

	MockPerson input;

	@InjectMocks
	private PersonService service;

	@Mock
	PersonRepository repository;

	@BeforeEach
	void setUpMocks() throws Exception {
		input = new MockPerson();
		MockitoAnnotations.openMocks(this);
	}

	@Test
	void testFindById() {
		Person entity = input.mockEntity(1);
		entity.setId(1L);

		when(repository.findById(1L)).thenReturn(Optional.of(entity));

		var result = service.findById(1L);
		assertNotNull(result);
		assertNotNull(result.getId());
		assertNotNull(result.getLinks());

		assertTrue(result.toString().contains("links: [</api/pessoas/v1/1>;rel=\"self\"]"));
		assertEquals("Endereço Teste 1", result.getAddress().getLogradouro());
		assertEquals("Name Teste 1", result.getName());
		assertEquals(true, result.getEnabled());
	}

	@Test
	void testCreate() {
		Person entity = input.mockEntity(1);
		entity.setId(1L);

		Person persisted = entity;

		PersonVO vo = input.mockVO(1);
		vo.setId(1L);

		when(repository.save(entity)).thenReturn(persisted);

		var result = service.create(vo);

		assertNotNull(result);
		assertNotNull(result.getId());
		assertNotNull(result.getLinks());

		assertTrue(result.toString().contains("links: [</api/pessoas/v1/1>;rel=\"self\"]"));
		assertEquals("Endereço Teste 1", result.getAddress().getLogradouro());
		assertEquals("Name Teste 1", result.getName());
		assertEquals(true, result.getEnabled());
	}

	@Test
	void testCreateWithNullPerson() {
		Exception exception = assertThrows(RequiredObjectIsNullException.class, () -> {
			service.create(null);
		});

		String expectedMessage = "It is not allowed to persist a null object!";
		String actualMessage = exception.getMessage();

		assertTrue(actualMessage.contains(expectedMessage));
	}

	@Test
	void testUpdate() {
		Person entity = input.mockEntity(1);
		entity.setId(1L);

		Person persisted = entity;

		PersonVO vo = input.mockVO(1);
		vo.setId(1L);

		when(repository.findById(1L)).thenReturn(Optional.of(entity));
		when(repository.save(entity)).thenReturn(persisted);

		var result = service.update(vo);

		assertNotNull(result);
		assertNotNull(result.getId());
		assertNotNull(result.getLinks());

		assertTrue(result.toString().contains("links: [</api/pessoas/v1/1>;rel=\"self\"]"));
		assertEquals("Endereço Teste 1", result.getAddress().getLogradouro());
		assertEquals("Name Teste 1", result.getName());
		assertEquals(true, result.getEnabled());
	}

	@Test
	void testUpdateWithNullPerson() {
		Exception exception = assertThrows(RequiredObjectIsNullException.class, () -> {
			service.update(null);
		});

		String expectedMessage = "It is not allowed to persist a null object!";
		String actualMessage = exception.getMessage();

		assertTrue(actualMessage.contains(expectedMessage));
	}

	@Test
	void testDelete() {
		Person entity = input.mockEntity(1);
		entity.setId(1L);

		when(repository.findById(1L)).thenReturn(Optional.of(entity));

		service.delete(1L);
	}

	@Test
	void testFindAll() {
	    // Obter a lista de mock e verificar o tamanho
	    List<Person> list = input.mockEntityList();
	    System.out.println("Tamanho da lista de mock: " + list.size());
	    
	    // Retornar essa lista no mock do repositório
	    when(repository.findAll()).thenReturn(list);

	    // Chamar o serviço
	    var people = service.findAll();
	    System.out.println("Tamanho da lista retornada pelo serviço: " + people.size());

	    // Verificações gerais
	    assertNotNull(people);
	    assertEquals(14, people.size()); //

	    // Verificar a primeira pessoa (índice 1)
	    var PersonOne = people.get(1);
	    assertNotNull(PersonOne);
	    assertNotNull(PersonOne.getId());
	    assertNotNull(PersonOne.getLinks());
	    System.out.println("PersonOne links: " + PersonOne.getLinks());

	    assertTrue(PersonOne.toString().contains("links: [</api/pessoas/v1/1>;rel=\"self\"]"));
	    assertEquals("Endereço Teste 1", PersonOne.getAddress().getLogradouro());
	    assertEquals("Name Teste 1", PersonOne.getName());
	    assertEquals(true, PersonOne.getEnabled());

	    // Verificar a quarta pessoa (índice 4)
	    var PersonFour = people.get(4);
	    assertNotNull(PersonFour);
	    assertNotNull(PersonFour.getId());
	    assertNotNull(PersonFour.getLinks());
	    System.out.println("PersonFour links: " + PersonFour.getLinks());

	    assertTrue(PersonFour.toString().contains("links: [</api/pessoas/v1/4>;rel=\"self\"]"));
	    assertEquals("Endereço Teste 4", PersonFour.getAddress().getLogradouro());
	    assertEquals("Name Teste 4", PersonFour.getName());
	    assertEquals(true, PersonFour.getEnabled());

	    // Verificar a sétima pessoa (índice 7)
	    var PersonSeven = people.get(7);
	    assertNotNull(PersonSeven);
	    assertNotNull(PersonSeven.getId());
	    assertNotNull(PersonSeven.getLinks());
	    System.out.println("PersonSeven links: " + PersonSeven.getLinks());

	    assertTrue(PersonSeven.toString().contains("links: [</api/pessoas/v1/7>;rel=\"self\"]"));
	    assertEquals("Endereço Teste 7", PersonSeven.getAddress().getLogradouro());
	    assertEquals("Name Teste 7", PersonSeven.getName());
	    assertEquals(true, PersonSeven.getEnabled());
	}


}

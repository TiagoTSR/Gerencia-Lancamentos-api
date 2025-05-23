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

import br.com.xdecodex.data.vo.v1.PessoaVO;
import br.com.xdecodex.exceptions.RequiredObjectIsNullException;
import br.com.xdecodex.model.Pessoa;
import br.com.xdecodex.repositories.PessoaRepository;
import br.com.xdecodex.services.PessoaService;
import br.com.xdecodex.unittests.mapper.mocks.MockPessoa;

@TestInstance(Lifecycle.PER_CLASS)
@ExtendWith(MockitoExtension.class)
class PessoaServicesTest {

	MockPessoa input;

	@InjectMocks
	private PessoaService service;

	@Mock
	PessoaRepository repository;

	@BeforeEach
	void setUpMocks() throws Exception {
		input = new MockPessoa();
		MockitoAnnotations.openMocks(this);
	}

	@Test
	void testFindById() {
		Pessoa entity = input.mockEntity(1);
		entity.setId(1L);

		when(repository.findById(1L)).thenReturn(Optional.of(entity));

		var result = service.findById(1L);
		assertNotNull(result);
		assertNotNull(result.getId());
		assertNotNull(result.getLinks());

		assertTrue(result.toString().contains("links: [</api/pessoas/v1/1>;rel=\"self\"]"));
		assertEquals("Endereço Teste 1", result.getEndereco().getLogradouro());
		assertEquals("Nome Teste 1", result.getNome());
		assertEquals(true, result.getEnabled());
	}

	@Test
	void testCreate() {
		Pessoa entity = input.mockEntity(1);
		entity.setId(1L);

		Pessoa persisted = entity;

		PessoaVO vo = input.mockVO(1);
		vo.setId(1L);

		when(repository.save(entity)).thenReturn(persisted);

		var result = service.create(vo);

		assertNotNull(result);
		assertNotNull(result.getId());
		assertNotNull(result.getLinks());

		assertTrue(result.toString().contains("links: [</api/pessoas/v1/1>;rel=\"self\"]"));
		assertEquals("Endereço Teste 1", result.getEndereco().getLogradouro());
		assertEquals("Nome Teste 1", result.getNome());
		assertEquals(true, result.getEnabled());
	}

	@Test
	void testCreateWithNullPessoa() {
		Exception exception = assertThrows(RequiredObjectIsNullException.class, () -> {
			service.create(null);
		});

		String expectedMessage = "It is not allowed to persist a null object!";
		String actualMessage = exception.getMessage();

		assertTrue(actualMessage.contains(expectedMessage));
	}

	@Test
	void testUpdate() {
		Pessoa entity = input.mockEntity(1);
		entity.setId(1L);

		Pessoa persisted = entity;

		PessoaVO vo = input.mockVO(1);
		vo.setId(1L);

		when(repository.findById(1L)).thenReturn(Optional.of(entity));
		when(repository.save(entity)).thenReturn(persisted);

		var result = service.update(vo);

		assertNotNull(result);
		assertNotNull(result.getId());
		assertNotNull(result.getLinks());

		assertTrue(result.toString().contains("links: [</api/pessoas/v1/1>;rel=\"self\"]"));
		assertEquals("Endereço Teste 1", result.getEndereco().getLogradouro());
		assertEquals("Nome Teste 1", result.getNome());
		assertEquals(true, result.getEnabled());
	}

	@Test
	void testUpdateWithNullPessoa() {
		Exception exception = assertThrows(RequiredObjectIsNullException.class, () -> {
			service.update(null);
		});

		String expectedMessage = "It is not allowed to persist a null object!";
		String actualMessage = exception.getMessage();

		assertTrue(actualMessage.contains(expectedMessage));
	}

	@Test
	void testDelete() {
		Pessoa entity = input.mockEntity(1);
		entity.setId(1L);

		when(repository.findById(1L)).thenReturn(Optional.of(entity));

		service.delete(1L);
	}

	@Test
	void testFindAll() {
	    // Obter a lista de mock e verificar o tamanho
	    List<Pessoa> list = input.mockEntityList();
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
	    var pessoaOne = people.get(1);
	    assertNotNull(pessoaOne);
	    assertNotNull(pessoaOne.getId());
	    assertNotNull(pessoaOne.getLinks());
	    System.out.println("pessoaOne links: " + pessoaOne.getLinks());

	    assertTrue(pessoaOne.toString().contains("links: [</api/pessoas/v1/1>;rel=\"self\"]"));
	    assertEquals("Endereço Teste 1", pessoaOne.getEndereco().getLogradouro());
	    assertEquals("Nome Teste 1", pessoaOne.getNome());
	    assertEquals(true, pessoaOne.getEnabled());

	    // Verificar a quarta pessoa (índice 4)
	    var pessoaFour = people.get(4);
	    assertNotNull(pessoaFour);
	    assertNotNull(pessoaFour.getId());
	    assertNotNull(pessoaFour.getLinks());
	    System.out.println("pessoaFour links: " + pessoaFour.getLinks());

	    assertTrue(pessoaFour.toString().contains("links: [</api/pessoas/v1/4>;rel=\"self\"]"));
	    assertEquals("Endereço Teste 4", pessoaFour.getEndereco().getLogradouro());
	    assertEquals("Nome Teste 4", pessoaFour.getNome());
	    assertEquals(true, pessoaFour.getEnabled());

	    // Verificar a sétima pessoa (índice 7)
	    var pessoaSeven = people.get(7);
	    assertNotNull(pessoaSeven);
	    assertNotNull(pessoaSeven.getId());
	    assertNotNull(pessoaSeven.getLinks());
	    System.out.println("pessoaSeven links: " + pessoaSeven.getLinks());

	    assertTrue(pessoaSeven.toString().contains("links: [</api/pessoas/v1/7>;rel=\"self\"]"));
	    assertEquals("Endereço Teste 7", pessoaSeven.getEndereco().getLogradouro());
	    assertEquals("Nome Teste 7", pessoaSeven.getNome());
	    assertEquals(true, pessoaSeven.getEnabled());
	}


}

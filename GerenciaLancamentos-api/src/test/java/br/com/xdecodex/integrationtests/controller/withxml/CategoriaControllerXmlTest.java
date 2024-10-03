package br.com.xdecodex.integrationtests.controller.withxml;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.boot.test.context.SpringBootTest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;

import br.com.xdecodex.configs.TestConfigs;
import br.com.xdecodex.data.vo.v1.CategoriaVO;
import br.com.xdecodex.integrationtests.testcontainers.AbstractIntegrationTest;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.specification.RequestSpecification;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@TestMethodOrder(OrderAnnotation.class)
public class CategoriaControllerXmlTest extends AbstractIntegrationTest {
	
	private static RequestSpecification specification;
	private static XmlMapper objectMapper;

	private static CategoriaVO categoria;
	
	@BeforeAll
	public static void setup() {
		objectMapper = new XmlMapper();
		objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
		
		categoria = new CategoriaVO();
	}
	
	@Test
	@Order(1)
	public void testCreate() throws JsonMappingException, JsonProcessingException {
		mockCategoria();
		
		specification = new RequestSpecBuilder()
			.addHeader(TestConfigs.HEADER_PARAM_ORIGIN, TestConfigs.ORIGIN_XDECODEX)
			.setBasePath("/api/categorias/v1")
			.setPort(TestConfigs.SERVER_PORT)
			.addFilter(new RequestLoggingFilter(LogDetail.ALL))
			.addFilter(new ResponseLoggingFilter(LogDetail.ALL))
			.build();
		
		var content = given().spec(specification)
				.contentType(TestConfigs.CONTENT_TYPE_XML)
				.accept(TestConfigs.CONTENT_TYPE_XML)
				.body(categoria)
				.when()
				.post()
				.then()
				.statusCode(201)
				.extract()
				.body()
				.asString();
		
		CategoriaVO persistedCategoria = objectMapper.readValue(content, CategoriaVO.class);
		categoria = persistedCategoria;
		
		assertNotNull(persistedCategoria, "Categoria persistida não deve ser nula");
		assertNotNull(persistedCategoria.getCodigo(), "O código da categoria não pode ser nulo");
		assertNotNull(persistedCategoria.getNome(), "O nome da categoria não pode ser nulo");
		assertTrue(persistedCategoria.getCodigo() > 0, "O código da categoria deve ser maior que zero");
		assertEquals("Alimentação", persistedCategoria.getNome(), "O nome da categoria não corresponde ao esperado");
	}

	@Test
	@Order(2)
	public void testCreateWithWrongOrigin() throws JsonMappingException, JsonProcessingException {
		mockCategoria();
		
		specification = new RequestSpecBuilder()
			.addHeader(TestConfigs.HEADER_PARAM_ORIGIN, TestConfigs.ORIGIN_EXAMPLE)
			.setBasePath("/api/categorias/v1")
			.setPort(TestConfigs.SERVER_PORT)
			.addFilter(new RequestLoggingFilter(LogDetail.ALL))
			.addFilter(new ResponseLoggingFilter(LogDetail.ALL))
			.build();
		
		var content = given().spec(specification)
				.contentType(TestConfigs.CONTENT_TYPE_XML)
				.accept(TestConfigs.CONTENT_TYPE_XML)
				.body(categoria)
				.when()
				.post()
				.then()
				.statusCode(403)
				.extract()
				.body()
				.asString();
		
		assertNotNull(content, "O conteúdo da resposta não pode ser nulo");
		assertEquals("Invalid CORS request", content, "A mensagem de erro não corresponde ao esperado");
	}

	@Test
	@Order(3)
	public void testFindById() throws JsonMappingException, JsonProcessingException {
		mockCategoria();
		
		specification = new RequestSpecBuilder()
			.addHeader(TestConfigs.HEADER_PARAM_ORIGIN, TestConfigs.ORIGIN_XDECODEX)
			.setBasePath("/api/categorias/v1")
			.setPort(TestConfigs.SERVER_PORT)
			.addFilter(new RequestLoggingFilter(LogDetail.ALL))
			.addFilter(new ResponseLoggingFilter(LogDetail.ALL))
			.build();
		
		var content = given().spec(specification)
				.contentType(TestConfigs.CONTENT_TYPE_XML)
				.accept(TestConfigs.CONTENT_TYPE_XML)
				.pathParam("codigo", categoria.getCodigo())
				.when()
				.get("/{codigo}")
				.then()
				.statusCode(200)
				.extract()
				.body()
				.asString();
		
		CategoriaVO persistedCategoria = objectMapper.readValue(content, CategoriaVO.class);
		categoria = persistedCategoria;
		
		assertNotNull(persistedCategoria, "Categoria retornada não deve ser nula");
		assertNotNull(persistedCategoria.getCodigo(), "O código da categoria não pode ser nulo");
		assertNotNull(persistedCategoria.getNome(), "O nome da categoria não pode ser nulo");
		assertTrue(persistedCategoria.getCodigo() > 0, "O código da categoria deve ser maior que zero");
		assertEquals("Alimentação", persistedCategoria.getNome(), "O nome da categoria não corresponde ao esperado");
	}
	
	@Test
	@Order(4)
	public void testFindByIdWithWrongOrigin() throws JsonMappingException, JsonProcessingException {
		mockCategoria();
		
		specification = new RequestSpecBuilder()
			.addHeader(TestConfigs.HEADER_PARAM_ORIGIN, TestConfigs.ORIGIN_EXAMPLE)
			.setBasePath("/api/categorias/v1")
			.setPort(TestConfigs.SERVER_PORT)
			.addFilter(new RequestLoggingFilter(LogDetail.ALL))
			.addFilter(new ResponseLoggingFilter(LogDetail.ALL))
			.build();
		
		var content = given().spec(specification)
				.contentType(TestConfigs.CONTENT_TYPE_XML)
				.accept(TestConfigs.CONTENT_TYPE_XML)
				.pathParam("codigo", categoria.getCodigo())
				.when()
				.get("/{codigo}")
				.then()
				.statusCode(403)
				.extract()
				.body()
				.asString();

		assertNotNull(content, "O conteúdo da resposta não pode ser nulo");
		assertEquals("Invalid CORS request", content, "A mensagem de erro não corresponde ao esperado");
	}
	
	private RequestSpecification given() {
		// Implementa o método dado que deve retornar uma RequestSpecification
		return io.restassured.RestAssured.given();
	}

	private void mockCategoria() {
		categoria.setNome("Alimentação");
	}
}

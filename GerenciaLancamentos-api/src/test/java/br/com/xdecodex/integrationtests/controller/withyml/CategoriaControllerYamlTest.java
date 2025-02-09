package br.com.xdecodex.integrationtests.controller.withyml;

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
import com.fasterxml.jackson.databind.JsonMappingException;

import br.com.xdecodex.configs.TestConfigs;
import br.com.xdecodex.data.vo.v1.CategoriaVO;
import br.com.xdecodex.data.vo.v1.security.TokenVO;
import br.com.xdecodex.integrationtests.controller.withyml.mapper.YMLMapper;
import br.com.xdecodex.integrationtests.testcontainers.AbstractIntegrationTest;
import br.com.xdecodex.integrationtests.vo.AccountCredentialsVO;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.config.EncoderConfig;
import io.restassured.config.RestAssuredConfig;
import io.restassured.filter.log.LogDetail;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@TestMethodOrder(OrderAnnotation.class)
public class CategoriaControllerYamlTest extends AbstractIntegrationTest {
	
	private static RequestSpecification specification;
	private static RequestSpecification authSpecification;
	private static YMLMapper objectMapper;

	private static CategoriaVO categoria;
	
	@BeforeAll
	public static void setup() {
		objectMapper = new YMLMapper();
		categoria = new CategoriaVO();
	}
	
	@Test
	@Order(0)
	public void authorization() throws JsonMappingException, JsonProcessingException {
		AccountCredentialsVO user = new AccountCredentialsVO("Tiago", "none345");
		
		var accessToken = given()
				.basePath("/auth/signin")
					.port(TestConfigs.SERVER_PORT)
					.contentType(TestConfigs.CONTENT_TYPE_JSON)
				.body(user)
					.when()
				.post()
					.then()
						.statusCode(200)
							.extract()
							.body()
								.as(TokenVO.class)
							.getAccessToken();
		
		authSpecification = new RequestSpecBuilder()
				.addHeader(TestConfigs.HEADER_PARAM_AUTHORIZATION, "Bearer " + accessToken)
				.setBasePath("/api/categories/v1")
				.setPort(TestConfigs.SERVER_PORT)
					.addFilter(new RequestLoggingFilter(LogDetail.ALL))
					.addFilter(new ResponseLoggingFilter(LogDetail.ALL))
				.build();
	}
	
	@Test
	@Order(1)
	public void testCreate() throws JsonMappingException, JsonProcessingException {
		mockCategoria();
		
		specification = new RequestSpecBuilder()
			.addHeader(TestConfigs.HEADER_PARAM_ORIGIN, TestConfigs.ORIGIN_XDECODEX)
			.setBasePath("/api/categories/v1")
			.setPort(TestConfigs.SERVER_PORT)
			.addFilter(new RequestLoggingFilter(LogDetail.ALL))
			.addFilter(new ResponseLoggingFilter(LogDetail.ALL))
			.build();
		
		var persistedCategoria = given().spec(authSpecification)
				.config(
						RestAssuredConfig
							.config()
							.encoderConfig(EncoderConfig.encoderConfig()
								.encodeContentTypeAs(
									TestConfigs.CONTENT_TYPE_YML,
									ContentType.TEXT)))
				.contentType(TestConfigs.CONTENT_TYPE_YML)
				.accept(TestConfigs.CONTENT_TYPE_YML)
					.body(categoria, objectMapper)
					.when()
					.post()
				.then()
					.statusCode(201)
						.extract()
						.body()
							.as(CategoriaVO.class, objectMapper);
		
		categoria = persistedCategoria;
		
		assertNotNull(persistedCategoria, "Categoria persisted must not be null");
		assertNotNull(persistedCategoria.getId(), "Categoria code cannot be null");
		assertNotNull(persistedCategoria.getNome(), "Categoria name cannot be null");
		assertTrue(persistedCategoria.getId() > 0, "Categoria code must be greater than zero");
		assertEquals("Alimentação", persistedCategoria.getNome(), "The categoria name does not match what was expected");
	}

	@Test
	@Order(2)
	public void testCreateWithWrongOrigin() throws JsonMappingException, JsonProcessingException {
		mockCategoria();
		
		specification = new RequestSpecBuilder()
			.addHeader(TestConfigs.HEADER_PARAM_ORIGIN, TestConfigs.ORIGIN_EXAMPLE)
			.setBasePath("/api/categories/v1")
			.setPort(TestConfigs.SERVER_PORT)
			.addFilter(new RequestLoggingFilter(LogDetail.ALL))
    	    .addFilter(new ResponseLoggingFilter(LogDetail.ALL))
    	    .setConfig(RestAssured.config().encoderConfig(EncoderConfig.encoderConfig()
    	        .encodeContentTypeAs(TestConfigs.CONTENT_TYPE_YML, ContentType.TEXT))) // Configura a serialização para YAML
    	    .build();
		
		String content = given().spec(specification)
                .contentType(TestConfigs.CONTENT_TYPE_YML) // Define o tipo de conteúdo como YAML
                .accept(TestConfigs.CONTENT_TYPE_YML) // Aceita respostas em YAML
                .body(categoria, objectMapper)
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
			.setBasePath("/api/categories/v1")
			.setPort(TestConfigs.SERVER_PORT)
			.addFilter(new RequestLoggingFilter(LogDetail.ALL))
			.addFilter(new ResponseLoggingFilter(LogDetail.ALL))
			.build();
		
		var persistedCategoria = given().spec(authSpecification)
				.config(
						RestAssuredConfig
							.config()
							.encoderConfig(EncoderConfig.encoderConfig()
								.encodeContentTypeAs(
									TestConfigs.CONTENT_TYPE_YML,
									ContentType.TEXT)))
				.contentType(TestConfigs.CONTENT_TYPE_YML)
				.accept(TestConfigs.CONTENT_TYPE_YML)
					.pathParam("id", categoria.getId())
					.when()
					.get("{id}")
				.then()
					.statusCode(200)
						.extract()
						.body()
						.as(CategoriaVO.class, objectMapper);
		
		categoria = persistedCategoria;
		
		assertNotNull(persistedCategoria, "Categoria retornada não deve ser nula");
		assertNotNull(persistedCategoria.getId(), "O código da categoria não pode ser nulo");
		assertNotNull(persistedCategoria.getNome(), "O nome da categoria não pode ser nulo");
		assertTrue(persistedCategoria.getId() > 0, "O código da categoria deve ser maior que zero");
		assertEquals("Alimentação", persistedCategoria.getNome(), "O nome da categoria não corresponde ao esperado");
	}
	
	@Test
	@Order(4)
	public void testFindByIdWithWrongOrigin() throws JsonMappingException, JsonProcessingException {
		mockCategoria();
		
		specification = new RequestSpecBuilder()
			.addHeader(TestConfigs.HEADER_PARAM_ORIGIN, TestConfigs.ORIGIN_EXAMPLE)
			.setBasePath("/api/categories/v1")
			.setPort(TestConfigs.SERVER_PORT)
			.addFilter(new RequestLoggingFilter(LogDetail.ALL))
			.addFilter(new ResponseLoggingFilter(LogDetail.ALL))
			.build();
		
		var content = given()
                .spec(specification)
                .contentType(TestConfigs.CONTENT_TYPE_YML)
                .accept(TestConfigs.CONTENT_TYPE_YML)
                .pathParam("id", categoria.getId())
                .when()
                .get("{id}")
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

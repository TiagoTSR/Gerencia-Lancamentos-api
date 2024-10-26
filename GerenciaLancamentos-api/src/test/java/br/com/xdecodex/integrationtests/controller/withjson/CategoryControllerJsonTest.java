package br.com.xdecodex.integrationtests.controller.withjson;

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
import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.xdecodex.configs.TestConfigs;
import br.com.xdecodex.data.vo.v1.CategoryVO;
import br.com.xdecodex.data.vo.v1.security.TokenVO;
import br.com.xdecodex.integrationtests.testcontainers.AbstractIntegrationTest;
import br.com.xdecodex.integrationtests.vo.AccountCredentialsVO;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.specification.RequestSpecification;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@TestMethodOrder(OrderAnnotation.class)
public class CategoryControllerJsonTest extends AbstractIntegrationTest {
	
	private static RequestSpecification specification;
	private static RequestSpecification authSpecification;
	private static ObjectMapper objectMapper;

	private static CategoryVO category;
	
	@BeforeAll
	public static void setup() {
		objectMapper = new ObjectMapper();
		objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
		
		category = new CategoryVO();
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
		mockCategory();
		
		specification = new RequestSpecBuilder()
			.addHeader(TestConfigs.HEADER_PARAM_ORIGIN, TestConfigs.ORIGIN_XDECODEX)
			.setBasePath("/api/categories/v1")
			.setPort(TestConfigs.SERVER_PORT)
			.addFilter(new RequestLoggingFilter(LogDetail.ALL))
			.addFilter(new ResponseLoggingFilter(LogDetail.ALL))
			.build();
		
		var content = given().spec(authSpecification)
				.contentType(TestConfigs.CONTENT_TYPE_JSON)
				.body(category)
				.when()
				.post()
				.then()
				.statusCode(201)
				.extract()
				.body()
				.asString();
		
		CategoryVO persistedCategory = objectMapper.readValue(content, CategoryVO.class);
		category = persistedCategory;
		
		assertNotNull(persistedCategory, "Category persisted must not be null");
		assertNotNull(persistedCategory.getId(), "Category code cannot be null");
		assertNotNull(persistedCategory.getName(), "Category name cannot be null");
		assertTrue(persistedCategory.getId() > 0, "Category code must be greater than zero");
		assertEquals("Alimentação", persistedCategory.getName(), "The category name does not match what was expected");
	}

	@Test
	@Order(2)
	public void testCreateWithWrongOrigin() throws JsonMappingException, JsonProcessingException {
		mockCategory();
		
		specification = new RequestSpecBuilder()
			.addHeader(TestConfigs.HEADER_PARAM_ORIGIN, TestConfigs.ORIGIN_EXAMPLE)
			.setBasePath("/api/categories/v1")
			.setPort(TestConfigs.SERVER_PORT)
			.addFilter(new RequestLoggingFilter(LogDetail.ALL))
			.addFilter(new ResponseLoggingFilter(LogDetail.ALL))
			.build();
		
		var content = given().spec(specification)
				.contentType(TestConfigs.CONTENT_TYPE_JSON)
				.body(category)
				.when()
				.post()
				.then()
				.statusCode(403)
				.extract()
				.body()
				.asString();
		
		assertNotNull(content, "Response content cannot be null");
		assertEquals("Invalid CORS request", content, "The error message does not match what was expected");
	}

	@Test
	@Order(3)
	public void testFindById() throws JsonMappingException, JsonProcessingException {
		mockCategory();
		
		specification = new RequestSpecBuilder()
			.addHeader(TestConfigs.HEADER_PARAM_ORIGIN, TestConfigs.ORIGIN_XDECODEX)
			.setBasePath("/api/categories/v1")
			.setPort(TestConfigs.SERVER_PORT)
			.addFilter(new RequestLoggingFilter(LogDetail.ALL))
			.addFilter(new ResponseLoggingFilter(LogDetail.ALL))
			.build();
		
		var content = given().spec(authSpecification)
				.contentType(TestConfigs.CONTENT_TYPE_JSON)
				.pathParam("id", category.getId())
				.when()
				.get("/{id}")
				.then()
				.statusCode(200)
				.extract()
				.body()
				.asString();
		
		CategoryVO persistedCategory = objectMapper.readValue(content, CategoryVO.class);
		category = persistedCategory;
		
		assertNotNull(persistedCategory, "Category returned must not be null");
		assertNotNull(persistedCategory.getId(), "The category code cannot be null");
		assertNotNull(persistedCategory.getName(), "The category name cannot be null");
		assertTrue(persistedCategory.getId() > 0, "The category code must be greater than zero");
		assertEquals("Alimentação", persistedCategory.getName(), "The category name does not match what was expected");
	}
	
	@Test
	@Order(4)
	public void testFindByIdWithWrongOrigin() throws JsonMappingException, JsonProcessingException {
		mockCategory();
		
		specification = new RequestSpecBuilder()
			.addHeader(TestConfigs.HEADER_PARAM_ORIGIN, TestConfigs.ORIGIN_EXAMPLE)
			.setBasePath("/api/categories/v1")
			.setPort(TestConfigs.SERVER_PORT)
			.addFilter(new RequestLoggingFilter(LogDetail.ALL))
			.addFilter(new ResponseLoggingFilter(LogDetail.ALL))
			.build();
		
		var content = given().spec(specification)
				.contentType(TestConfigs.CONTENT_TYPE_JSON)
				.pathParam("id", category.getId())
				.when()
				.get("/{id}")
				.then()
				.statusCode(403)
				.extract()
				.body()
				.asString();

		assertNotNull(content, "Response content cannot be null");
		assertEquals("Invalid CORS request", content, "The error message does not match what was expected");
	}
	
	private RequestSpecification given() {
		// Implementa o método dado que deve retornar uma RequestSpecification
		return io.restassured.RestAssured.given();
	}

	private void mockCategory() {
		category.setName("Alimentação");
	}
}

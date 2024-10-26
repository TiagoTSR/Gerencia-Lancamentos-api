package br.com.xdecodex.integrationtests.controller.withxml;

import static io.restassured.RestAssured.given;
import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;
import java.time.LocalDate;

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
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import br.com.xdecodex.configs.TestConfigs;
import br.com.xdecodex.data.vo.v1.LaunchVO;
import br.com.xdecodex.data.vo.v1.security.TokenVO;
import br.com.xdecodex.integrationtests.testcontainers.AbstractIntegrationTest;
import br.com.xdecodex.integrationtests.vo.AccountCredentialsVO;
import br.com.xdecodex.model.Category;
import br.com.xdecodex.model.Person;
import br.com.xdecodex.model.TypeLaunch;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.specification.RequestSpecification; 


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@TestMethodOrder(OrderAnnotation.class)
public class LaunchControllerXmlTest extends AbstractIntegrationTest {
    
    private static RequestSpecification specification;
    private static RequestSpecification authSpecification;
    private static XmlMapper objectMapper;

    private static LaunchVO launch;
    
    @BeforeAll
    public static void setup() {
        objectMapper = new XmlMapper();
        objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        
        // Registra o módulo para suporte às datas do Java 8
        objectMapper.registerModule(new JavaTimeModule());
        
        launch = new LaunchVO();
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
				.setBasePath("/api/launchs/v1")
				.setPort(TestConfigs.SERVER_PORT)
					.addFilter(new RequestLoggingFilter(LogDetail.ALL))
					.addFilter(new ResponseLoggingFilter(LogDetail.ALL))
				.build();
	}
    
    @Test
    @Order(1)
    public void testCreate() throws Exception {
        mockLaunch();

        specification = new RequestSpecBuilder()
            .addHeader(TestConfigs.HEADER_PARAM_ORIGIN, TestConfigs.ORIGIN_XDECODEX)
            .setBasePath("/api/launchs/v1")
            .setPort(TestConfigs.SERVER_PORT)
            .addFilter(new RequestLoggingFilter(LogDetail.ALL))
            .addFilter(new ResponseLoggingFilter(LogDetail.ALL))
            .build();

        // Serializa o objeto para XML
        var xmlContent = objectMapper.writeValueAsString(launch);

        var content = given()
                .spec(authSpecification)
                .contentType(TestConfigs.CONTENT_TYPE_XML) // Altere para application/xml
                .accept(TestConfigs.CONTENT_TYPE_XML)      // Altere para application/xml
                .body(xmlContent)                          // Envia o corpo como XML
                .when()
                .post()
                .then()
                .statusCode(201)
                .extract()
                .body()
                .asString();

        // Deserializa o XML recebido de volta para um objeto LaunchVO
        LaunchVO persistedLaunch = objectMapper.readValue(content, LaunchVO.class);
        launch = persistedLaunch;

        // Verificações
        assertNotNull(persistedLaunch);
        assertNotNull(persistedLaunch.getId());
        assertTrue(persistedLaunch.getId() > 0);
        assertEquals("Bahamas", persistedLaunch.getDescription());
        assertEquals(LocalDate.of(2017, 2, 10), persistedLaunch.getExpirationDate());
        assertEquals(LocalDate.of(2017, 2, 10), persistedLaunch.getPaymentDate());
        assertEquals(new BigDecimal("100.32"), persistedLaunch.getValue());
        assertEquals("", persistedLaunch.getObservation());
        assertEquals(TypeLaunch.EXPENSE, persistedLaunch.getType());
        assertEquals(2L, persistedLaunch.getCategory().getId());
        assertEquals(2L, persistedLaunch.getPerson().getId());
    }


    @Test
    @Order(2)
    public void testCreateWithWrongOrigin() throws JsonMappingException, JsonProcessingException {
        mockLaunch();
        
        specification = new RequestSpecBuilder()
            .addHeader(TestConfigs.HEADER_PARAM_ORIGIN, TestConfigs.ORIGIN_EXAMPLE)
            .setBasePath("/api/launchs/v1")
            .setPort(TestConfigs.SERVER_PORT)
            .addFilter(new RequestLoggingFilter(LogDetail.ALL))
            .addFilter(new ResponseLoggingFilter(LogDetail.ALL))
            .build();
        
        var content = given()
                .spec(specification)
                .contentType(TestConfigs.CONTENT_TYPE_XML)
                .accept(TestConfigs.CONTENT_TYPE_XML)
                .body(launch)
                .when()
                .post()
                .then()
                .statusCode(403)
                .extract()
                .body()
                .asString();
        
        assertNotNull(content);
        assertEquals("Invalid CORS request", content);
    }

    @Test
    @Order(3)
    public void testFindById() throws JsonMappingException, JsonProcessingException {
        mockLaunch();
        
        specification = new RequestSpecBuilder()
            .addHeader(TestConfigs.HEADER_PARAM_ORIGIN, TestConfigs.ORIGIN_XDECODEX)
            .setBasePath("/api/launchs/v1")
            .setPort(TestConfigs.SERVER_PORT)
            .addFilter(new RequestLoggingFilter(LogDetail.ALL))
            .addFilter(new ResponseLoggingFilter(LogDetail.ALL))
            .build();
        
        var content = given()
                .spec(authSpecification)
                .contentType(TestConfigs.CONTENT_TYPE_XML)
                .accept(TestConfigs.CONTENT_TYPE_XML)
                .pathParam("codigo", launch.getId())
                .when()
                .get("{codigo}")
                .then()
                .statusCode(200)
                .extract()
                .body()
                .asString();
        
        LaunchVO persistedLaunch = objectMapper.readValue(content, LaunchVO.class);
        launch = persistedLaunch;
        
        assertNotNull(persistedLaunch);
        assertNotNull(persistedLaunch.getId());
        assertNotNull(persistedLaunch.getDescription());
        assertNotNull(persistedLaunch.getExpirationDate());
        assertNotNull(persistedLaunch.getPaymentDate());
        assertNotNull(persistedLaunch.getValue());
        assertNotNull(persistedLaunch.getObservation());
        assertNotNull(persistedLaunch.getType());
        assertNotNull(persistedLaunch.getCategory());
        assertNotNull(persistedLaunch.getPerson());
        
        assertTrue(persistedLaunch.getId() > 0);
        assertEquals("Bahamas", persistedLaunch.getDescription());
        assertEquals(LocalDate.of(2017, 2, 10), persistedLaunch.getExpirationDate());
        assertEquals(LocalDate.of(2017, 2, 10), persistedLaunch.getPaymentDate());
        assertEquals(new BigDecimal("100.32"), persistedLaunch.getValue());
        assertEquals("", persistedLaunch.getObservation());
        assertEquals(TypeLaunch.EXPENSE, persistedLaunch.getType());
        assertEquals(2L, persistedLaunch.getCategory().getId());
        assertEquals(2L, persistedLaunch.getPerson().getId());
    }
    
    @Test
    @Order(4)
    public void testFindByIdWithWrongOrigin() throws JsonMappingException, JsonProcessingException {
        mockLaunch();
        
        specification = new RequestSpecBuilder()
            .addHeader(TestConfigs.HEADER_PARAM_ORIGIN, TestConfigs.ORIGIN_EXAMPLE)
            .setBasePath("/api/launchs/v1")
            .setPort(TestConfigs.SERVER_PORT)
            .addFilter(new RequestLoggingFilter(LogDetail.ALL))
            .addFilter(new ResponseLoggingFilter(LogDetail.ALL))
            .build();
        
        var content = given()
                .spec(specification)
                .contentType(TestConfigs.CONTENT_TYPE_XML)
                .accept(TestConfigs.CONTENT_TYPE_XML)
                .pathParam("codigo", launch.getId())
                .when()
                .get("{codigo}")
                .then()
                .statusCode(403)
                .extract()
                .body()
                .asString();
        
        assertNotNull(content);
        assertEquals("Invalid CORS request", content);
    }
    
    private void mockLaunch() {
        Category categoria = new Category();
        categoria.setId(2L);
        categoria.setName("Alimentação");
        
        Person pessoa = new Person();
        pessoa.setId(2L);
        pessoa.setName("Maria Rita");
        
        launch.setDescription("Bahamas");
        launch.setExpirationDate(LocalDate.of(2017, 2, 10));
        launch.setPaymentDate(LocalDate.of(2017, 2, 10));
        launch.setValue(new BigDecimal("100.32"));
        launch.setObservation("");
        launch.setType(TypeLaunch.EXPENSE);
        launch.setCategory(categoria);
        launch.setPerson(pessoa);
    }
}
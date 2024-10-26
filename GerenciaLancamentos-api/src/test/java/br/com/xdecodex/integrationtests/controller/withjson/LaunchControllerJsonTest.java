package br.com.xdecodex.integrationtests.controller.withjson;

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
import com.fasterxml.jackson.databind.ObjectMapper;
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
public class LaunchControllerJsonTest extends AbstractIntegrationTest {
    
    private static RequestSpecification specification;
    private static RequestSpecification authSpecification;
    private static ObjectMapper objectMapper;

    private static LaunchVO launch;
    
    @BeforeAll
    public static void setup() {
        objectMapper = new ObjectMapper();
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
    public void testCreate() throws JsonMappingException, JsonProcessingException {
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
                .contentType(TestConfigs.CONTENT_TYPE_JSON)
                .body(launch)
                .when()
                .post()
                .then()
                .statusCode(201)
                .extract()
                .body()
                .asString();
        
        LaunchVO persistedLaunch = objectMapper.readValue(content, LaunchVO.class);
        launch = persistedLaunch;
        
        // Verifique o conteúdo recebido para depuração
        System.out.println("Persisted Launch: " + persistedLaunch);
        
        // Verifique se o objeto é nulo
        assertNotNull(persistedLaunch);
        
        // Verifique se o código é nulo
        Long id = persistedLaunch.getId();
        assertNotNull(id);
        
        assertNotNull(persistedLaunch.getDescription());
        assertNotNull(persistedLaunch.getExpirationDate());
        assertNotNull(persistedLaunch.getPaymentDate());
        assertNotNull(persistedLaunch.getValue());
        assertNotNull(persistedLaunch.getObservation());
        assertNotNull(persistedLaunch.getType());
        assertNotNull(persistedLaunch.getCategory());
        assertNotNull(persistedLaunch.getPerson());
        
        assertTrue(id > 0);
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
                .contentType(TestConfigs.CONTENT_TYPE_JSON)
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
                .contentType(TestConfigs.CONTENT_TYPE_JSON)
                .pathParam("id", launch.getId())
                .when()
                .get("{id}")
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
                .contentType(TestConfigs.CONTENT_TYPE_JSON)
                .pathParam("id", launch.getId())
                .when()
                .get("{id}")
                .then()
                .statusCode(403)
                .extract()
                .body()
                .asString();
        
        assertNotNull(content);
        assertEquals("Invalid CORS request", content);
    }
    
    private void mockLaunch() {
        Category category = new Category();
        category.setId(2L);
        category.setName("Alimentação");
        
        Person person = new Person();
        person.setId(2L);
        person.setName("Maria Rita");
        
        launch.setDescription("Bahamas");
        launch.setExpirationDate(LocalDate.of(2017, 2, 10));
        launch.setPaymentDate(LocalDate.of(2017, 2, 10));
        launch.setValue(new BigDecimal("100.32"));
        launch.setObservation("");
        launch.setType(TypeLaunch.EXPENSE);
        launch.setCategory(category);
        launch.setPerson(person);
    }
}
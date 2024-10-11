package br.com.xdecodex.integrationtests.controller.withjson;

import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
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
import br.com.xdecodex.data.vo.v1.PersonVO;
import br.com.xdecodex.integrationtests.testcontainers.AbstractIntegrationTest;
import br.com.xdecodex.model.Address;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.specification.RequestSpecification;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@TestMethodOrder(OrderAnnotation.class)
public class PersonControllerJsonTest extends AbstractIntegrationTest {

    private static RequestSpecification specification;
    private static ObjectMapper objectMapper;

    private static PersonVO pessoa;

    @BeforeAll
    public static void setup() {
        objectMapper = new ObjectMapper();
        objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);

        pessoa = new PersonVO();
    }

    @Test
    @Order(1)
    public void testCreate() throws JsonMappingException, JsonProcessingException {
        mockPerson();

        specification = new RequestSpecBuilder()
                .addHeader(TestConfigs.HEADER_PARAM_ORIGIN, TestConfigs.ORIGIN_XDECODEX)
                .setBasePath("/api/persons/v1")
                .setPort(TestConfigs.SERVER_PORT)
                .addFilter(new RequestLoggingFilter(LogDetail.ALL))
                .addFilter(new ResponseLoggingFilter(LogDetail.ALL))
                .build();

        var content = given().spec(specification)
                .contentType(TestConfigs.CONTENT_TYPE_JSON)
                .body(pessoa)
                .when()
                .post()
                .then()
                .statusCode(201)
                .extract()
                .body()
                .asString();

        PersonVO persistedPerson = objectMapper.readValue(content, PersonVO.class);
        pessoa = persistedPerson;

        assertNotNull(persistedPerson);

        assertNotNull(persistedPerson.getId());
        assertNotNull(persistedPerson.getName());
        assertNotNull(persistedPerson.getAddress());
        assertNotNull(persistedPerson.getEnabled());

        assertTrue(persistedPerson.getId() > 0);

        assertEquals("Henrique Medeiros", persistedPerson.getName());
		assertEquals("Rua do Sapo", persistedPerson.getAddress().getPublicPlace());
	    assertEquals("1120", persistedPerson.getAddress().getNumber());
	    assertEquals("Apto 201", persistedPerson.getAddress().getComplement());
	    assertEquals("Centro", persistedPerson.getAddress().getNeighborhood());
	    assertEquals("12.400-12", persistedPerson.getAddress().getCep());
	    assertEquals("Rio de Janeiro", persistedPerson.getAddress().getCity());
	    assertEquals("RJ", persistedPerson.getAddress().getState());
	    assertNotNull(persistedPerson.getEnabled());
    }

    @Test
    @Order(2)
    public void testCreateWithWrongOrigin() throws JsonMappingException, JsonProcessingException {
        mockPerson();

        specification = new RequestSpecBuilder()
                .addHeader(TestConfigs.HEADER_PARAM_ORIGIN, TestConfigs.ORIGIN_EXAMPLE)
                .setBasePath("/api/persons/v1")
                .setPort(TestConfigs.SERVER_PORT)
                .addFilter(new RequestLoggingFilter(LogDetail.ALL))
                .addFilter(new ResponseLoggingFilter(LogDetail.ALL))
                .build();

        var content = given().spec(specification)
                .contentType(TestConfigs.CONTENT_TYPE_JSON)
                .body(pessoa)
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
        mockPerson();

        specification = new RequestSpecBuilder()
                .addHeader(TestConfigs.HEADER_PARAM_ORIGIN, TestConfigs.ORIGIN_XDECODEX)
                .setBasePath("/api/persons/v1")
                .setPort(TestConfigs.SERVER_PORT)
                .addFilter(new RequestLoggingFilter(LogDetail.ALL))
                .addFilter(new ResponseLoggingFilter(LogDetail.ALL))
                .build();

        var content = given().spec(specification)
                .contentType(TestConfigs.CONTENT_TYPE_JSON)
                .pathParam("id", pessoa.getId())
                .when()
                .get("{id}")
                .then()
                .statusCode(200)
                .extract()
                .body()
                .asString();

        PersonVO persistedPerson = objectMapper.readValue(content, PersonVO.class);
        pessoa = persistedPerson;

        assertNotNull(persistedPerson);

        assertNotNull(persistedPerson.getId());
        assertNotNull(persistedPerson.getName());
        assertNotNull(persistedPerson.getAddress().getPublicPlace());
        assertNotNull(persistedPerson.getEnabled());

        assertTrue(persistedPerson.getId() > 0);

        assertEquals("Henrique Medeiros", persistedPerson.getName());
		assertEquals("Rua do Sapo", persistedPerson.getAddress().getPublicPlace());
	    assertEquals("1120", persistedPerson.getAddress().getNumber());
	    assertEquals("Apto 201", persistedPerson.getAddress().getComplement());
	    assertEquals("Centro", persistedPerson.getAddress().getNeighborhood());
	    assertEquals("12.400-12", persistedPerson.getAddress().getCep());
	    assertEquals("Rio de Janeiro", persistedPerson.getAddress().getCity());
	    assertEquals("RJ", persistedPerson.getAddress().getState());
	    assertNotNull(persistedPerson.getEnabled());
    }

    @Test
    @Order(4)
    public void testFindByIdWithWrongOrigin() throws JsonMappingException, JsonProcessingException {
        mockPerson();

        specification = new RequestSpecBuilder()
                .addHeader(TestConfigs.HEADER_PARAM_ORIGIN, TestConfigs.ORIGIN_EXAMPLE)
                .setBasePath("/api/persons/v1")
                .setPort(TestConfigs.SERVER_PORT)
                .addFilter(new RequestLoggingFilter(LogDetail.ALL))
                .addFilter(new ResponseLoggingFilter(LogDetail.ALL))
                .build();

        var content = given().spec(specification)
                .contentType(TestConfigs.CONTENT_TYPE_JSON)
                .pathParam("id", pessoa.getId())
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

    private RequestSpecification given() {
        return io.restassured.RestAssured.given();
    }

    private void mockPerson() {
    	pessoa.setName("Henrique Medeiros");
	    pessoa.setEnabled(true);

	    Address address = new Address();
	    address.setPublicPlace("Rua do Sapo");
	    address.setNumber("1120");
	    address.setComplement("Apto 201");
	    address.setNeighborhood("Centro");
	    address.setCep("12.400-12");
	    address.setCity("Rio de Janeiro");
	    address.setState("RJ");

	    pessoa.setAddress(address);
    }
}

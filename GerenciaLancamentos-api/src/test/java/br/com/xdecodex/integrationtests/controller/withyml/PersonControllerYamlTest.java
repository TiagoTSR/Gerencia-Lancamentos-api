package br.com.xdecodex.integrationtests.controller.withyml;

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
import com.fasterxml.jackson.databind.JsonMappingException;

import br.com.xdecodex.configs.TestConfigs;
import br.com.xdecodex.data.vo.v1.PersonVO;
import br.com.xdecodex.integrationtests.controller.withyml.mapper.YMLMapper;
import br.com.xdecodex.integrationtests.testcontainers.AbstractIntegrationTest;
import br.com.xdecodex.model.Address;
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
public class PersonControllerYamlTest extends AbstractIntegrationTest {

    private static RequestSpecification specification;
    private static YMLMapper objectMapper;

    private static PersonVO pessoa;

    @BeforeAll
    public static void setup() {
    	objectMapper = new YMLMapper();
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
        
        var persistedPerson = given().spec(specification)
				.config(
						RestAssuredConfig
							.config()
							.encoderConfig(EncoderConfig.encoderConfig()
								.encodeContentTypeAs(
									TestConfigs.CONTENT_TYPE_YML,
									ContentType.TEXT)))
				.contentType(TestConfigs.CONTENT_TYPE_YML)
				.accept(TestConfigs.CONTENT_TYPE_YML)
					.body(pessoa, objectMapper)
					.when()
					.post()
				.then()
					.statusCode(201)
						.extract()
						.body()
							.as(PersonVO.class, objectMapper);

      
        pessoa = persistedPerson;

        // Asserções para verificar o objeto persistido
        assertNotNull(persistedPerson);
        assertNotNull(persistedPerson.getId());
        assertNotNull(persistedPerson.getName());
        assertNotNull(persistedPerson.getAddress());
        assertNotNull(persistedPerson.getEnabled());

        assertTrue(persistedPerson.getId() > 0);

        // Validações específicas dos campos de PersonVO
        assertEquals("Henrique Medeiros", persistedPerson.getName());
        assertEquals("Rua do Sapo", persistedPerson.getAddress().getLogradouro());
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
                .setConfig(RestAssured.config()
                        .encoderConfig(EncoderConfig.encoderConfig()
                        .encodeContentTypeAs(TestConfigs.CONTENT_TYPE_YML, ContentType.TEXT)))
                .build();

        // Envia a requisição e espera receber um status 403 (CORS inválido)
        String content = given().spec(specification)
                .contentType(TestConfigs.CONTENT_TYPE_YML) // Define o tipo de conteúdo como YAML
                .accept(TestConfigs.CONTENT_TYPE_YML) // Aceita respostas em YAML
                .body(pessoa, objectMapper)
                .when()
                .post()
                .then()
                .statusCode(403)
                .extract()
                .body()
                .asString();

        // Valida que o retorno é a mensagem esperada
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

        // Envia a requisição e extrai a resposta como string (YAML)
        var persistedPerson = given().spec(specification)
				.config(
						RestAssuredConfig
							.config()
							.encoderConfig(EncoderConfig.encoderConfig()
								.encodeContentTypeAs(
									TestConfigs.CONTENT_TYPE_YML,
									ContentType.TEXT)))
				.contentType(TestConfigs.CONTENT_TYPE_YML)
				.accept(TestConfigs.CONTENT_TYPE_YML)
					.pathParam("id", pessoa.getId())
					.when()
					.get("{id}")
				.then()
					.statusCode(200)
						.extract()
						.body()
						.as(PersonVO.class, objectMapper);

        pessoa = persistedPerson;

        // Validações do objeto
        assertNotNull(persistedPerson);
        assertNotNull(persistedPerson.getId());
        assertNotNull(persistedPerson.getName());
        assertNotNull(persistedPerson.getAddress().getLogradouro());
        assertNotNull(persistedPerson.getEnabled());

        assertTrue(persistedPerson.getId() > 0);

        // Valida os campos específicos
        assertEquals("Henrique Medeiros", persistedPerson.getName());
        assertEquals("Rua do Sapo", persistedPerson.getAddress().getLogradouro());
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

        // Envia a requisição com origem incorreta e espera receber um status 403
        String content = given().spec(specification)
                .contentType(TestConfigs.CONTENT_TYPE_YML) // Define o tipo de conteúdo como YAML
                .accept(TestConfigs.CONTENT_TYPE_YML)
                .pathParam("id", pessoa.getId())
                .when()
                .get("{id}")
                .then()
                .statusCode(403)
                .extract()
                .body()
                .asString();

        // Valida que o retorno é a mensagem esperada
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
	    address.setLogradouro("Rua do Sapo");
	    address.setNumber("1120");
	    address.setComplement("Apto 201");
	    address.setNeighborhood("Centro");
	    address.setCep("12.400-12");
	    address.setCity("Rio de Janeiro");
	    address.setState("RJ");

	    pessoa.setAddress(address);
    }
}

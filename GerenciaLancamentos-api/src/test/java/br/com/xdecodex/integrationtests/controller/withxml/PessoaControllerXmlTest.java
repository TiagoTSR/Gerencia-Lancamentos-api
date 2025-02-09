package br.com.xdecodex.integrationtests.controller.withxml;

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
import com.fasterxml.jackson.dataformat.xml.XmlMapper;

import br.com.xdecodex.configs.TestConfigs;
import br.com.xdecodex.data.vo.v1.PessoaVO;
import br.com.xdecodex.data.vo.v1.security.TokenVO;
import br.com.xdecodex.integrationtests.testcontainers.AbstractIntegrationTest;
import br.com.xdecodex.integrationtests.vo.AccountCredentialsVO;
import br.com.xdecodex.model.Endereco;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.specification.RequestSpecification;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@TestMethodOrder(OrderAnnotation.class)
public class PessoaControllerXmlTest extends AbstractIntegrationTest {

    private static RequestSpecification specification;
    private static RequestSpecification authSpecification;
    private static XmlMapper objectMapper;

    private static PessoaVO pessoa;

    @BeforeAll
    public static void setup() {
        objectMapper = new XmlMapper();
        objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);

        pessoa = new PessoaVO();
    }
    
    @Test
	@Order(0)
	public void authorization() throws JsonMappingException, JsonProcessingException {
		AccountCredentialsVO user = new AccountCredentialsVO("Tiago", "none345");
		
		var accessToken = given()
				.basePath("/auth/signin")
					.port(TestConfigs.SERVER_PORT)
					.contentType(TestConfigs.CONTENT_TYPE_XML)
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
				.setBasePath("/api/pessoas/v1")
				.setPort(TestConfigs.SERVER_PORT)
					.addFilter(new RequestLoggingFilter(LogDetail.ALL))
					.addFilter(new ResponseLoggingFilter(LogDetail.ALL))
				.build();
	}

    @Test
    @Order(1)
    public void testCreate() throws JsonMappingException, JsonProcessingException {
        mockPessoa();

        specification = new RequestSpecBuilder()
                .addHeader(TestConfigs.HEADER_PARAM_ORIGIN, TestConfigs.ORIGIN_XDECODEX)
                .setBasePath("/api/pessoas/v1")
                .setPort(TestConfigs.SERVER_PORT)
                .addFilter(new RequestLoggingFilter(LogDetail.ALL))
                .addFilter(new ResponseLoggingFilter(LogDetail.ALL))
                .build();

        var content = given().spec(authSpecification)
        		.contentType(TestConfigs.CONTENT_TYPE_XML) // Altere para application/xml
                .accept(TestConfigs.CONTENT_TYPE_XML)
                .body(pessoa)
                .when()
                .post()
                .then()
                .statusCode(201)
                .extract()
                .body()
                .asString();

        PessoaVO persistedPessoa = objectMapper.readValue(content, PessoaVO.class);
        pessoa = persistedPessoa;

        assertNotNull(persistedPessoa);

        assertNotNull(persistedPessoa.getId());
        assertNotNull(persistedPessoa.getNome());
        assertNotNull(persistedPessoa.getEndereco());
        assertNotNull(persistedPessoa.getEnabled());

        assertTrue(persistedPessoa.getId() > 0);

        assertEquals("Henrique Medeiros", persistedPessoa.getNome());
		assertEquals("Rua do Sapo", persistedPessoa.getEndereco().getLogradouro());
	    assertEquals("1120", persistedPessoa.getEndereco().getNumero());
	    assertEquals("Apto 201", persistedPessoa.getEndereco().getComplemento());
	    assertEquals("Centro", persistedPessoa.getEndereco().getBairro());
	    assertEquals("12.400-12", persistedPessoa.getEndereco().getCep());
	    assertEquals("Rio de Janeiro", persistedPessoa.getEndereco().getCidade());
	    assertEquals("RJ", persistedPessoa.getEndereco().getEstado());
	    assertNotNull(persistedPessoa.getEnabled());
    }

    @Test
    @Order(2)
    public void testCreateWithWrongOrigin() throws JsonMappingException, JsonProcessingException {
        mockPessoa();

        specification = new RequestSpecBuilder()
                .addHeader(TestConfigs.HEADER_PARAM_ORIGIN, TestConfigs.ORIGIN_EXAMPLE)
                .setBasePath("/api/pessoas/v1")
                .setPort(TestConfigs.SERVER_PORT)
                .addFilter(new RequestLoggingFilter(LogDetail.ALL))
                .addFilter(new ResponseLoggingFilter(LogDetail.ALL))
                .build();

        var content = given().spec(specification)
        		.contentType(TestConfigs.CONTENT_TYPE_XML) // Altere para application/xml
                .accept(TestConfigs.CONTENT_TYPE_XML)
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
        mockPessoa();

        specification = new RequestSpecBuilder()
                .addHeader(TestConfigs.HEADER_PARAM_ORIGIN, TestConfigs.ORIGIN_XDECODEX)
                .setBasePath("/api/pessoas/v1")
                .setPort(TestConfigs.SERVER_PORT)
                .addFilter(new RequestLoggingFilter(LogDetail.ALL))
                .addFilter(new ResponseLoggingFilter(LogDetail.ALL))
                .build();

        var content = given().spec(authSpecification)
        		.contentType(TestConfigs.CONTENT_TYPE_XML) // Altere para application/xml
                .accept(TestConfigs.CONTENT_TYPE_XML)
                .pathParam("id", pessoa.getId())
                .when()
                .get("{id}")
                .then()
                .statusCode(200)
                .extract()
                .body()
                .asString();

        PessoaVO persistedPessoa = objectMapper.readValue(content, PessoaVO.class);
        pessoa = persistedPessoa;

        assertNotNull(persistedPessoa);

        assertNotNull(persistedPessoa.getId());
        assertNotNull(persistedPessoa.getNome());
        assertNotNull(persistedPessoa.getEndereco().getLogradouro());
        assertNotNull(persistedPessoa.getEnabled());

        assertTrue(persistedPessoa.getId() > 0);

        assertEquals("Henrique Medeiros", persistedPessoa.getNome());
		assertEquals("Rua do Sapo", persistedPessoa.getEndereco().getLogradouro());
	    assertEquals("1120", persistedPessoa.getEndereco().getNumero());
	    assertEquals("Apto 201", persistedPessoa.getEndereco().getComplemento());
	    assertEquals("Centro", persistedPessoa.getEndereco().getBairro());
	    assertEquals("12.400-12", persistedPessoa.getEndereco().getCep());
	    assertEquals("Rio de Janeiro", persistedPessoa.getEndereco().getCidade());
	    assertEquals("RJ", persistedPessoa.getEndereco().getEstado());
	    assertNotNull(persistedPessoa.getEnabled());
    }

    @Test
    @Order(4)
    public void testFindByIdWithWrongOrigin() throws JsonMappingException, JsonProcessingException {
        mockPessoa();

        specification = new RequestSpecBuilder()
                .addHeader(TestConfigs.HEADER_PARAM_ORIGIN, TestConfigs.ORIGIN_EXAMPLE)
                .setBasePath("/api/pessoas/v1")
                .setPort(TestConfigs.SERVER_PORT)
                .addFilter(new RequestLoggingFilter(LogDetail.ALL))
                .addFilter(new ResponseLoggingFilter(LogDetail.ALL))
                .build();

        String content = given().spec(specification)
        		.contentType(TestConfigs.CONTENT_TYPE_XML) // Altere para application/xml
                .accept(TestConfigs.CONTENT_TYPE_XML)
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

    private void mockPessoa() {
    	pessoa.setNome("Henrique Medeiros");
	    pessoa.setEnabled(true);

	    Endereco address = new Endereco();
	    address.setLogradouro("Rua do Sapo");
	    address.setNumero("1120");
	    address.setComplemento("Apto 201");
	    address.setBairro("Centro");
	    address.setCep("12.400-12");
	    address.setCidade("Rio de Janeiro");
	    address.setEstado("RJ");

	    pessoa.setEndereco(address);
    }
}

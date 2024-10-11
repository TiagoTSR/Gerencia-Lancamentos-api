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
import br.com.xdecodex.data.vo.v1.PessoaVO;
import br.com.xdecodex.integrationtests.testcontainers.AbstractIntegrationTest;
import br.com.xdecodex.model.Endereco;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.specification.RequestSpecification;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@TestMethodOrder(OrderAnnotation.class)
public class PessoaControllerJsonTest extends AbstractIntegrationTest {

    private static RequestSpecification specification;
    private static ObjectMapper objectMapper;

    private static PessoaVO pessoa;

    @BeforeAll
    public static void setup() {
        objectMapper = new ObjectMapper();
        objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);

        pessoa = new PessoaVO();
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

        PessoaVO persistedPessoa = objectMapper.readValue(content, PessoaVO.class);
        pessoa = persistedPessoa;

        assertNotNull(persistedPessoa);

        assertNotNull(persistedPessoa.getCodigo());
        assertNotNull(persistedPessoa.getNome());
        assertNotNull(persistedPessoa.getEndereco());
        assertNotNull(persistedPessoa.getAtivo());

        assertTrue(persistedPessoa.getCodigo() > 0);

        assertEquals("Henrique Medeiros", persistedPessoa.getNome());
		assertEquals("Rua do Sapo", persistedPessoa.getEndereco().getLogradouro());
	    assertEquals("1120", persistedPessoa.getEndereco().getNumero());
	    assertEquals("Apto 201", persistedPessoa.getEndereco().getComplemento());
	    assertEquals("Centro", persistedPessoa.getEndereco().getBairro());
	    assertEquals("12.400-12", persistedPessoa.getEndereco().getCep());
	    assertEquals("Rio de Janeiro", persistedPessoa.getEndereco().getCidade());
	    assertEquals("RJ", persistedPessoa.getEndereco().getEstado());
	    assertNotNull(persistedPessoa.getAtivo());
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
        mockPessoa();

        specification = new RequestSpecBuilder()
                .addHeader(TestConfigs.HEADER_PARAM_ORIGIN, TestConfigs.ORIGIN_XDECODEX)
                .setBasePath("/api/pessoas/v1")
                .setPort(TestConfigs.SERVER_PORT)
                .addFilter(new RequestLoggingFilter(LogDetail.ALL))
                .addFilter(new ResponseLoggingFilter(LogDetail.ALL))
                .build();

        var content = given().spec(specification)
                .contentType(TestConfigs.CONTENT_TYPE_JSON)
                .pathParam("codigo", pessoa.getCodigo())
                .when()
                .get("{codigo}")
                .then()
                .statusCode(200)
                .extract()
                .body()
                .asString();

        PessoaVO persistedPessoa = objectMapper.readValue(content, PessoaVO.class);
        pessoa = persistedPessoa;

        assertNotNull(persistedPessoa);

        assertNotNull(persistedPessoa.getCodigo());
        assertNotNull(persistedPessoa.getNome());
        assertNotNull(persistedPessoa.getEndereco().getLogradouro());
        assertNotNull(persistedPessoa.getAtivo());

        assertTrue(persistedPessoa.getCodigo() > 0);

        assertEquals("Henrique Medeiros", persistedPessoa.getNome());
		assertEquals("Rua do Sapo", persistedPessoa.getEndereco().getLogradouro());
	    assertEquals("1120", persistedPessoa.getEndereco().getNumero());
	    assertEquals("Apto 201", persistedPessoa.getEndereco().getComplemento());
	    assertEquals("Centro", persistedPessoa.getEndereco().getBairro());
	    assertEquals("12.400-12", persistedPessoa.getEndereco().getCep());
	    assertEquals("Rio de Janeiro", persistedPessoa.getEndereco().getCidade());
	    assertEquals("RJ", persistedPessoa.getEndereco().getEstado());
	    assertNotNull(persistedPessoa.getAtivo());
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

        var content = given().spec(specification)
                .contentType(TestConfigs.CONTENT_TYPE_JSON)
                .pathParam("codigo", pessoa.getCodigo())
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

    private RequestSpecification given() {
        return io.restassured.RestAssured.given();
    }

    private void mockPessoa() {
    	pessoa.setNome("Henrique Medeiros");
	    pessoa.setAtivo(true);

	    Endereco endereco = new Endereco();
	    endereco.setLogradouro("Rua do Sapo");
	    endereco.setNumero("1120");
	    endereco.setComplemento("Apto 201");
	    endereco.setBairro("Centro");
	    endereco.setCep("12.400-12");
	    endereco.setCidade("Rio de Janeiro");
	    endereco.setEstado("RJ");

	    pessoa.setEndereco(endereco);
    }
}

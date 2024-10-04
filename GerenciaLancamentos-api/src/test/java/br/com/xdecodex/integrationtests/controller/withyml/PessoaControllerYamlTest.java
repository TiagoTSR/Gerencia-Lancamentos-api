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
import br.com.xdecodex.data.vo.v1.PessoaVO;
import br.com.xdecodex.integrationtests.controller.withyml.mapper.YMLMapper;
import br.com.xdecodex.integrationtests.testcontainers.AbstractIntegrationTest;
import br.com.xdecodex.model.Endereco;
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
public class PessoaControllerYamlTest extends AbstractIntegrationTest {

    private static RequestSpecification specification;
    private static YMLMapper objectMapper;

    private static PessoaVO pessoa;

    @BeforeAll
    public static void setup() {
    	objectMapper = new YMLMapper();
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
        
        var persistedPessoa = given().spec(specification)
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
							.as(PessoaVO.class, objectMapper);

      
        pessoa = persistedPessoa;

        // Asserções para verificar o objeto persistido
        assertNotNull(persistedPessoa);
        assertNotNull(persistedPessoa.getCodigo());
        assertNotNull(persistedPessoa.getNome());
        assertNotNull(persistedPessoa.getEndereco());
        assertNotNull(persistedPessoa.getAtivo());

        assertTrue(persistedPessoa.getCodigo() > 0);

        // Validações específicas dos campos de PessoaVO
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
        mockPessoa();

        specification = new RequestSpecBuilder()
                .addHeader(TestConfigs.HEADER_PARAM_ORIGIN, TestConfigs.ORIGIN_XDECODEX)
                .setBasePath("/api/pessoas/v1")
                .setPort(TestConfigs.SERVER_PORT)
                .addFilter(new RequestLoggingFilter(LogDetail.ALL))
                .addFilter(new ResponseLoggingFilter(LogDetail.ALL))
                .build();

        // Envia a requisição e extrai a resposta como string (YAML)
        var persistedPessoa = given().spec(specification)
				.config(
						RestAssuredConfig
							.config()
							.encoderConfig(EncoderConfig.encoderConfig()
								.encodeContentTypeAs(
									TestConfigs.CONTENT_TYPE_YML,
									ContentType.TEXT)))
				.contentType(TestConfigs.CONTENT_TYPE_YML)
				.accept(TestConfigs.CONTENT_TYPE_YML)
					.pathParam("codigo", pessoa.getCodigo())
					.when()
					.get("{codigo}")
				.then()
					.statusCode(200)
						.extract()
						.body()
						.as(PessoaVO.class, objectMapper);

        pessoa = persistedPessoa;

        // Validações do objeto
        assertNotNull(persistedPessoa);
        assertNotNull(persistedPessoa.getCodigo());
        assertNotNull(persistedPessoa.getNome());
        assertNotNull(persistedPessoa.getEndereco().getLogradouro());
        assertNotNull(persistedPessoa.getAtivo());

        assertTrue(persistedPessoa.getCodigo() > 0);

        // Valida os campos específicos
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

        // Envia a requisição com origem incorreta e espera receber um status 403
        String content = given().spec(specification)
                .contentType(TestConfigs.CONTENT_TYPE_YML) // Define o tipo de conteúdo como YAML
                .accept(TestConfigs.CONTENT_TYPE_YML)
                .pathParam("codigo", pessoa.getCodigo())
                .when()
                .get("{codigo}")
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

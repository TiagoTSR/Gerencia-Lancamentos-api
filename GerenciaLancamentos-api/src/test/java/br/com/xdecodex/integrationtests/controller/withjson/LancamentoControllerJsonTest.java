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
import br.com.xdecodex.data.vo.v1.LancamentoVO;
import br.com.xdecodex.data.vo.v1.security.TokenVO;
import br.com.xdecodex.integrationtests.testcontainers.AbstractIntegrationTest;
import br.com.xdecodex.integrationtests.vo.AccountCredentialsVO;
import br.com.xdecodex.model.Categoria;
import br.com.xdecodex.model.Pessoa;
import br.com.xdecodex.model.TipoLancamento;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.specification.RequestSpecification; 


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@TestMethodOrder(OrderAnnotation.class)
public class LancamentoControllerJsonTest extends AbstractIntegrationTest {
    
    private static RequestSpecification specification;
    private static RequestSpecification authSpecification;
    private static ObjectMapper objectMapper;

    private static LancamentoVO lancamento;
    
    @BeforeAll
    public static void setup() {
        objectMapper = new ObjectMapper();
        objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        
        // Registra o módulo para suporte às datas do Java 8
        objectMapper.registerModule(new JavaTimeModule());
        
        lancamento = new LancamentoVO();
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
   				.setBasePath("/api/lancamentos/v1")
   				.setPort(TestConfigs.SERVER_PORT)
   					.addFilter(new RequestLoggingFilter(LogDetail.ALL))
   					.addFilter(new ResponseLoggingFilter(LogDetail.ALL))
   				.build();
   	}
    
    @Test
    @Order(1)
    public void testCreate() throws JsonMappingException, JsonProcessingException {
        mockLancamento();
        
        specification = new RequestSpecBuilder()
            .addHeader(TestConfigs.HEADER_PARAM_ORIGIN, TestConfigs.ORIGIN_XDECODEX)
            .setBasePath("/api/lancamentos/v1")
            .setPort(TestConfigs.SERVER_PORT)
            .addFilter(new RequestLoggingFilter(LogDetail.ALL))
            .addFilter(new ResponseLoggingFilter(LogDetail.ALL))
            .build();
        
        var content = given()
                .spec(authSpecification)
                .contentType(TestConfigs.CONTENT_TYPE_JSON)
                .body(lancamento)
                .when()
                .post()
                .then()
                .statusCode(201)
                .extract()
                .body()
                .asString();
        
        LancamentoVO persistedLancamento = objectMapper.readValue(content, LancamentoVO.class);
        lancamento = persistedLancamento;
        
        // Verifique o conteúdo recebido para depuração
        System.out.println("Persisted Lancamento: " + persistedLancamento);
        
        // Verifique se o objeto é nulo
        assertNotNull(persistedLancamento);
        
        // Verifique se o código é nulo
        Long id = persistedLancamento.getId();
        assertNotNull(id);
        
        assertNotNull(persistedLancamento.getDescricao());
        assertNotNull(persistedLancamento.getDataVencimento());
        assertNotNull(persistedLancamento.getDataPagamento());
        assertNotNull(persistedLancamento.getValor());
        assertNotNull(persistedLancamento.getObservacao());
        assertNotNull(persistedLancamento.getTipo());
        assertNotNull(persistedLancamento.getCategoria());
        assertNotNull(persistedLancamento.getPessoa());
        
        assertTrue(id > 0);
        assertEquals("Bahamas", persistedLancamento.getDescricao());
        assertEquals(LocalDate.of(2017, 2, 10), persistedLancamento.getDataVencimento());
        assertEquals(LocalDate.of(2017, 2, 10), persistedLancamento.getDataPagamento());
        assertEquals(new BigDecimal("100.32"), persistedLancamento.getValor());
        assertEquals("", persistedLancamento.getObservacao());
        assertEquals(TipoLancamento.DESPESA, persistedLancamento.getTipo());
        assertEquals(2L, persistedLancamento.getCategoria().getId());
        assertEquals(2L, persistedLancamento.getPessoa().getId());
    }


    @Test
    @Order(2)
    public void testCreateWithWrongOrigin() throws JsonMappingException, JsonProcessingException {
        mockLancamento();
        
        specification = new RequestSpecBuilder()
            .addHeader(TestConfigs.HEADER_PARAM_ORIGIN, TestConfigs.ORIGIN_EXAMPLE)
            .setBasePath("/api/lancamentos/v1")
            .setPort(TestConfigs.SERVER_PORT)
            .addFilter(new RequestLoggingFilter(LogDetail.ALL))
            .addFilter(new ResponseLoggingFilter(LogDetail.ALL))
            .build();
        
        var content = given()
                .spec(specification)
                .contentType(TestConfigs.CONTENT_TYPE_JSON)
                .body(lancamento)
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
        mockLancamento();
        
        specification = new RequestSpecBuilder()
            .addHeader(TestConfigs.HEADER_PARAM_ORIGIN, TestConfigs.ORIGIN_XDECODEX)
            .setBasePath("/api/lancamentos/v1")
            .setPort(TestConfigs.SERVER_PORT)
            .addFilter(new RequestLoggingFilter(LogDetail.ALL))
            .addFilter(new ResponseLoggingFilter(LogDetail.ALL))
            .build();
        
        var content = given()
                .spec(authSpecification)
                .contentType(TestConfigs.CONTENT_TYPE_JSON)
                .pathParam("id", lancamento.getId())
                .when()
                .get("{id}")
                .then()
                .statusCode(200)
                .extract()
                .body()
                .asString();
        
        LancamentoVO persistedLancamento = objectMapper.readValue(content, LancamentoVO.class);
        lancamento = persistedLancamento;
        
        assertNotNull(persistedLancamento);
        assertNotNull(persistedLancamento.getId());
        assertNotNull(persistedLancamento.getDescricao());
        assertNotNull(persistedLancamento.getDataVencimento());
        assertNotNull(persistedLancamento.getDataPagamento());
        assertNotNull(persistedLancamento.getValor());
        assertNotNull(persistedLancamento.getObservacao());
        assertNotNull(persistedLancamento.getTipo());
        assertNotNull(persistedLancamento.getCategoria());
        assertNotNull(persistedLancamento.getPessoa());
        
        assertTrue(persistedLancamento.getId() > 0);
        assertEquals("Bahamas", persistedLancamento.getDescricao());
        assertEquals(LocalDate.of(2017, 2, 10), persistedLancamento.getDataVencimento());
        assertEquals(LocalDate.of(2017, 2, 10), persistedLancamento.getDataPagamento());
        assertEquals(new BigDecimal("100.32"), persistedLancamento.getValor());
        assertEquals("", persistedLancamento.getObservacao());
        assertEquals(TipoLancamento.DESPESA, persistedLancamento.getTipo());
        assertEquals(2L, persistedLancamento.getCategoria().getId());
        assertEquals(2L, persistedLancamento.getPessoa().getId());
    }
    
    @Test
    @Order(4)
    public void testFindByIdWithWrongOrigin() throws JsonMappingException, JsonProcessingException {
        mockLancamento();
        
        specification = new RequestSpecBuilder()
            .addHeader(TestConfigs.HEADER_PARAM_ORIGIN, TestConfigs.ORIGIN_EXAMPLE)
            .setBasePath("/api/lancamentos/v1")
            .setPort(TestConfigs.SERVER_PORT)
            .addFilter(new RequestLoggingFilter(LogDetail.ALL))
            .addFilter(new ResponseLoggingFilter(LogDetail.ALL))
            .build();
        
        var content = given()
                .spec(specification)
                .contentType(TestConfigs.CONTENT_TYPE_JSON)
                .pathParam("id", lancamento.getId())
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
    
    private void mockLancamento() {
        Categoria categoria = new Categoria();
        categoria.setId(2L);
        categoria.setNome("Alimentação");
        
        Pessoa pessoa = new Pessoa();
        pessoa.setId(2L);
        pessoa.setNome("Maria Rita");
        
        lancamento.setDescricao("Bahamas");
        lancamento.setDataVencimento(LocalDate.of(2017, 2, 10));
        lancamento.setDataPagamento(LocalDate.of(2017, 2, 10));
        lancamento.setValor(new BigDecimal("100.32"));
        lancamento.setObservacao("");
        lancamento.setTipo(TipoLancamento.DESPESA);
        lancamento.setCategoria(categoria);
        lancamento.setPessoa(pessoa);
    }
}
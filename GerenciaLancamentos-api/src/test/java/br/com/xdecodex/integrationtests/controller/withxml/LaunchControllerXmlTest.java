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
import br.com.xdecodex.data.vo.v1.LancamentoVO;
import br.com.xdecodex.integrationtests.testcontainers.AbstractIntegrationTest;
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
public class LancamentoControllerXmlTest extends AbstractIntegrationTest {
    
    private static RequestSpecification specification;
    private static XmlMapper objectMapper;

    private static LancamentoVO lancamento;
    
    @BeforeAll
    public static void setup() {
        objectMapper = new XmlMapper();
        objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        
        // Registra o módulo para suporte às datas do Java 8
        objectMapper.registerModule(new JavaTimeModule());
        
        lancamento = new LancamentoVO();
    }
    
    @Test
    @Order(1)
    public void testCreate() throws Exception {
        mockLancamento();

        specification = new RequestSpecBuilder()
            .addHeader(TestConfigs.HEADER_PARAM_ORIGIN, TestConfigs.ORIGIN_XDECODEX)
            .setBasePath("/api/lancamentos/v1")
            .setPort(TestConfigs.SERVER_PORT)
            .addFilter(new RequestLoggingFilter(LogDetail.ALL))
            .addFilter(new ResponseLoggingFilter(LogDetail.ALL))
            .build();

        // Serializa o objeto para XML
        var xmlContent = objectMapper.writeValueAsString(lancamento);

        var content = given()
                .spec(specification)
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

        // Deserializa o XML recebido de volta para um objeto LancamentoVO
        LancamentoVO persistedLancamento = objectMapper.readValue(content, LancamentoVO.class);
        lancamento = persistedLancamento;

        // Verificações
        assertNotNull(persistedLancamento);
        assertNotNull(persistedLancamento.getCodigo());
        assertTrue(persistedLancamento.getCodigo() > 0);
        assertEquals("Bahamas", persistedLancamento.getDescricao());
        assertEquals(LocalDate.of(2017, 2, 10), persistedLancamento.getDataVencimento());
        assertEquals(LocalDate.of(2017, 2, 10), persistedLancamento.getDataPagamento());
        assertEquals(new BigDecimal("100.32"), persistedLancamento.getValor());
        assertEquals("", persistedLancamento.getObservacao());
        assertEquals(TipoLancamento.DESPESA, persistedLancamento.getTipo());
        assertEquals(2L, persistedLancamento.getCategoria().getCodigo());
        assertEquals(2L, persistedLancamento.getPessoa().getCodigo());
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
                .contentType(TestConfigs.CONTENT_TYPE_XML)
                .accept(TestConfigs.CONTENT_TYPE_XML)
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
                .spec(specification)
                .contentType(TestConfigs.CONTENT_TYPE_XML)
                .accept(TestConfigs.CONTENT_TYPE_XML)
                .pathParam("codigo", lancamento.getCodigo())
                .when()
                .get("{codigo}")
                .then()
                .statusCode(200)
                .extract()
                .body()
                .asString();
        
        LancamentoVO persistedLancamento = objectMapper.readValue(content, LancamentoVO.class);
        lancamento = persistedLancamento;
        
        assertNotNull(persistedLancamento);
        assertNotNull(persistedLancamento.getCodigo());
        assertNotNull(persistedLancamento.getDescricao());
        assertNotNull(persistedLancamento.getDataVencimento());
        assertNotNull(persistedLancamento.getDataPagamento());
        assertNotNull(persistedLancamento.getValor());
        assertNotNull(persistedLancamento.getObservacao());
        assertNotNull(persistedLancamento.getTipo());
        assertNotNull(persistedLancamento.getCategoria());
        assertNotNull(persistedLancamento.getPessoa());
        
        assertTrue(persistedLancamento.getCodigo() > 0);
        assertEquals("Bahamas", persistedLancamento.getDescricao());
        assertEquals(LocalDate.of(2017, 2, 10), persistedLancamento.getDataVencimento());
        assertEquals(LocalDate.of(2017, 2, 10), persistedLancamento.getDataPagamento());
        assertEquals(new BigDecimal("100.32"), persistedLancamento.getValor());
        assertEquals("", persistedLancamento.getObservacao());
        assertEquals(TipoLancamento.DESPESA, persistedLancamento.getTipo());
        assertEquals(2L, persistedLancamento.getCategoria().getCodigo());
        assertEquals(2L, persistedLancamento.getPessoa().getCodigo());
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
                .contentType(TestConfigs.CONTENT_TYPE_XML)
                .accept(TestConfigs.CONTENT_TYPE_XML)
                .pathParam("codigo", lancamento.getCodigo())
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
    
    private void mockLancamento() {
        Categoria categoria = new Categoria();
        categoria.setCodigo(2L);
        categoria.setNome("Alimentação");
        
        Pessoa pessoa = new Pessoa();
        pessoa.setCodigo(2L);
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
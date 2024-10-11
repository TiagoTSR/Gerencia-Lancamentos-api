package br.com.xdecodex.unittests.mapper;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import br.com.xdecodex.data.vo.v1.LaunchVO;
import br.com.xdecodex.mapper.DozerMapper;
import br.com.xdecodex.model.Launch;
import br.com.xdecodex.unittests.mapper.mocks.MockLaunch;

public class DozerLaunchConverterTest {

    MockLaunch inputObject;

    @BeforeEach
    public void setUp() {
        inputObject = new MockLaunch();
    }

    @Test
    public void parseEntityToVOTest() {
        LaunchVO output = DozerMapper.parseObject(inputObject.mockEntity(), LaunchVO.class);
        assertEquals(Long.valueOf(0L), output.getId());
        assertEquals("Description Teste 0", output.getDescription());
        assertEquals(LocalDate.now().plusDays(0), output.getExpirationDate());
        assertEquals(LocalDate.now().plusDays(0), output.getPaymentDate());
        assertEquals(BigDecimal.valueOf(0), output.getValue());
        assertEquals("Category Teste 0", output.getCategory().getName());
        assertEquals("Name Teste 0", output.getPerson().getName());
    }

    @Test
    public void parseEntityListToVOListTest() {
        List<LaunchVO> outputList = DozerMapper.parseListObjects(inputObject.mockEntityList(), LaunchVO.class);
        LaunchVO outputZero = outputList.get(0);
        
        assertEquals(Long.valueOf(0L), outputZero.getId());
        assertEquals("Description Teste 0", outputZero.getDescription());
        assertEquals(LocalDate.now().plusDays(0), outputZero.getExpirationDate());
        assertEquals(LocalDate.now().plusDays(0), outputZero.getPaymentDate());
        assertEquals(BigDecimal.valueOf(0), outputZero.getValue());
        assertEquals("Category Teste 0", outputZero.getCategory().getName());
        assertEquals("Name Teste 0", outputZero.getPerson().getName());
        
        LaunchVO outputSeven = outputList.get(7);
        
        assertEquals(Long.valueOf(7L), outputSeven.getId());
        assertEquals("Description Teste 7", outputSeven.getDescription());
        assertEquals(LocalDate.now().plusDays(7), outputSeven.getExpirationDate());
        assertEquals(LocalDate.now().plusDays(7), outputSeven.getPaymentDate());
        assertEquals(BigDecimal.valueOf(7), outputSeven.getValue());
        assertEquals("Category Teste 7", outputSeven.getCategory().getName());
        assertEquals("Name Teste 7", outputSeven.getPerson().getName());
        
        LaunchVO outputTwelve = outputList.get(12);
        
        assertEquals(Long.valueOf(12L), outputTwelve.getId());
        assertEquals("Description Teste 12", outputTwelve.getDescription());
        assertEquals(LocalDate.now().plusDays(12), outputTwelve.getExpirationDate());
        assertEquals(LocalDate.now().plusDays(12), outputTwelve.getPaymentDate());
        assertEquals(BigDecimal.valueOf(12), outputTwelve.getValue());
        assertEquals("Category Teste 12", outputTwelve.getCategory().getName());
        assertEquals("Name Teste 12", outputTwelve.getPerson().getName());
    }

    @Test
    public void parseVOToEntityTest() {
        Launch output = DozerMapper.parseObject(inputObject.mockVO(), Launch.class);
        assertEquals(Long.valueOf(0L), output.getId());
        assertEquals("Description Teste 0", output.getDescription());
        assertEquals(LocalDate.now().plusDays(0), output.getExpirationDate());
        assertEquals(LocalDate.now().plusDays(0), output.getPaymentDate());
        assertEquals(BigDecimal.valueOf(0), output.getValue());
        assertEquals("Category Teste 0", output.getCategory().getName());
        assertEquals("Name Teste 0", output.getPerson().getName());
    }

    @Test
    public void parserVOListToEntityListTest() {
        List<Launch> outputList = DozerMapper.parseListObjects(inputObject.mockVOList(), Launch.class);
        Launch outputZero = outputList.get(0);
        
        assertEquals(Long.valueOf(0L), outputZero.getId());
        assertEquals("Description Teste 0", outputZero.getDescription());
        assertEquals(LocalDate.now().plusDays(0), outputZero.getExpirationDate());
        assertEquals(LocalDate.now().plusDays(0), outputZero.getPaymentDate());
        assertEquals(BigDecimal.valueOf(0), outputZero.getValue());
        assertEquals("Category Teste 0", outputZero.getCategory().getName());
        assertEquals("Name Teste 0", outputZero.getPerson().getName());
        
        Launch outputSeven = outputList.get(7);
        
        assertEquals(Long.valueOf(7L), outputSeven.getId());
        assertEquals("Description Teste 7", outputSeven.getDescription());
        assertEquals(LocalDate.now().plusDays(7), outputSeven.getExpirationDate());
        assertEquals(LocalDate.now().plusDays(7), outputSeven.getPaymentDate());
        assertEquals(BigDecimal.valueOf(7), outputSeven.getValue());
        assertEquals("Category Teste 7", outputSeven.getCategory().getName());
        assertEquals("Name Teste 7", outputSeven.getPerson().getName());
        
        Launch outputTwelve = outputList.get(12);
        
        assertEquals(Long.valueOf(12L), outputTwelve.getId());
        assertEquals("Description Teste 12", outputTwelve.getDescription());
        assertEquals(LocalDate.now().plusDays(12), outputTwelve.getExpirationDate());
        assertEquals(LocalDate.now().plusDays(12), outputTwelve.getPaymentDate());
        assertEquals(BigDecimal.valueOf(12), outputTwelve.getValue());
        assertEquals("Category Teste 12", outputTwelve.getCategory().getName());
        assertEquals("Name Teste 12", outputTwelve.getPerson().getName());
    }
}

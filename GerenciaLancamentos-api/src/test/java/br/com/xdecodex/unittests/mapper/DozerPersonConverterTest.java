package br.com.xdecodex.unittests.mapper;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import br.com.xdecodex.data.vo.v1.PersonVO;
import br.com.xdecodex.mapper.DozerMapper;
import br.com.xdecodex.model.Person;
import br.com.xdecodex.unittests.mapper.mocks.MockPerson;

public class DozerPersonConverterTest {

    MockPerson inputObject;

    @BeforeEach
    public void setUp() {
        inputObject = new MockPerson();
    }

    @Test
    public void parseEntityToVOTest() {
        PersonVO output = DozerMapper.parseObject(inputObject.mockEntity(), PersonVO.class);
        assertEquals(Long.valueOf(0L), output.getId());
        assertEquals("Name Teste 0", output.getName());
        assertEquals("Endereço Teste 0", output.getAddress().getPublicPlace());
        assertEquals("Número Teste 0", output.getAddress().getNumber());
        assertEquals("Complement Teste 0", output.getAddress().getComplement());
        assertEquals("Neighborhood Teste 0", output.getAddress().getNeighborhood());
        assertEquals("CEP Teste 0", output.getAddress().getCep());
        assertEquals("City Teste 0", output.getAddress().getCity());
        assertEquals("State Teste 0", output.getAddress().getState());
        assertEquals(true, output.getEnabled());
    }

    @Test
    public void parseEntityListToVOListTest() {
        List<PersonVO> outputList = DozerMapper.parseListObjects(inputObject.mockEntityList(), PersonVO.class);
        PersonVO outputZero = outputList.get(0);

        assertEquals(Long.valueOf(0L), outputZero.getId());
        assertEquals("Name Teste 0", outputZero.getName());
        assertEquals("Endereço Teste 0", outputZero.getAddress().getPublicPlace());
        assertEquals("Número Teste 0", outputZero.getAddress().getNumber());
        assertEquals("Complement Teste 0", outputZero.getAddress().getComplement());
        assertEquals("Neighborhood Teste 0", outputZero.getAddress().getNeighborhood());
        assertEquals("CEP Teste 0", outputZero.getAddress().getCep());
        assertEquals("City Teste 0", outputZero.getAddress().getCity());
        assertEquals("State Teste 0", outputZero.getAddress().getState());
        assertEquals(true, outputZero.getEnabled());

        PersonVO outputSeven = outputList.get(7);
        assertEquals(Long.valueOf(7L), outputSeven.getId());
        assertEquals("Name Teste 7", outputSeven.getName());
        assertEquals("Endereço Teste 7", outputSeven.getAddress().getPublicPlace());
        assertEquals("Número Teste 7", outputSeven.getAddress().getNumber());
        assertEquals("Complement Teste 7", outputSeven.getAddress().getComplement());
        assertEquals("Neighborhood Teste 7", outputSeven.getAddress().getNeighborhood());
        assertEquals("CEP Teste 7", outputSeven.getAddress().getCep());
        assertEquals("City Teste 7", outputSeven.getAddress().getCity());
        assertEquals("State Teste 7", outputSeven.getAddress().getState());
        assertEquals(true, outputSeven.getEnabled());

        PersonVO outputTwelve = outputList.get(12);
        assertEquals(Long.valueOf(12L), outputTwelve.getId());
        assertEquals("Name Teste 12", outputTwelve.getName());
        assertEquals("Endereço Teste 12", outputTwelve.getAddress().getPublicPlace());
        assertEquals("Número Teste 12", outputTwelve.getAddress().getNumber());
        assertEquals("Complement Teste 12", outputTwelve.getAddress().getComplement());
        assertEquals("Neighborhood Teste 12", outputTwelve.getAddress().getNeighborhood());
        assertEquals("CEP Teste 12", outputTwelve.getAddress().getCep());
        assertEquals("City Teste 12", outputTwelve.getAddress().getCity());
        assertEquals("State Teste 12", outputTwelve.getAddress().getState());
        assertEquals(true, outputTwelve.getEnabled());
    }

    @Test
    public void parseVOToEntityTest() {
        Person output = DozerMapper.parseObject(inputObject.mockVO(), Person.class);
        assertEquals(Long.valueOf(0L), output.getId());
        assertEquals("Name Teste 0", output.getName());
        assertEquals("Endereço Teste 0", output.getAddress().getPublicPlace());
        assertEquals("Número Teste 0", output.getAddress().getNumber());
        assertEquals("Complement Teste 0", output.getAddress().getComplement());
        assertEquals("Neighborhood Teste 0", output.getAddress().getNeighborhood());
        assertEquals("CEP Teste 0", output.getAddress().getCep());
        assertEquals("City Teste 0", output.getAddress().getCity());
        assertEquals("State Teste 0", output.getAddress().getState());
        assertEquals(true, output.getEnabled());
    }

    @Test
    public void parserVOListToEntityListTest() {
        List<Person> outputList = DozerMapper.parseListObjects(inputObject.mockVOList(), Person.class);
        Person outputZero = outputList.get(0);

        assertEquals(Long.valueOf(0L), outputZero.getId());
        assertEquals("Name Teste 0", outputZero.getName());
        assertEquals("Endereço Teste 0", outputZero.getAddress().getPublicPlace());
        assertEquals("Número Teste 0", outputZero.getAddress().getNumber());
        assertEquals("Complement Teste 0", outputZero.getAddress().getComplement());
        assertEquals("Neighborhood Teste 0", outputZero.getAddress().getNeighborhood());
        assertEquals("CEP Teste 0", outputZero.getAddress().getCep());
        assertEquals("City Teste 0", outputZero.getAddress().getCity());
        assertEquals("State Teste 0", outputZero.getAddress().getState());
        assertEquals(true, outputZero.getEnabled());

        Person outputSeven = outputList.get(7);
        assertEquals(Long.valueOf(7L), outputSeven.getId());
        assertEquals("Name Teste 7", outputSeven.getName());
        assertEquals("Endereço Teste 7", outputSeven.getAddress().getPublicPlace());
        assertEquals("Número Teste 7", outputSeven.getAddress().getNumber());
        assertEquals("Complement Teste 7", outputSeven.getAddress().getComplement());
        assertEquals("Neighborhood Teste 7", outputSeven.getAddress().getNeighborhood());
        assertEquals("CEP Teste 7", outputSeven.getAddress().getCep());
        assertEquals("City Teste 7", outputSeven.getAddress().getCity());
        assertEquals("State Teste 7", outputSeven.getAddress().getState());
        assertEquals(true, outputSeven.getEnabled());

        Person outputTwelve = outputList.get(12);
        assertEquals(Long.valueOf(12L), outputTwelve.getId());
        assertEquals("Name Teste 12", outputTwelve.getName());
        assertEquals("Endereço Teste 12", outputTwelve.getAddress().getPublicPlace());
        assertEquals("Número Teste 12", outputTwelve.getAddress().getNumber());
        assertEquals("Complement Teste 12", outputTwelve.getAddress().getComplement());
        assertEquals("Neighborhood Teste 12", outputTwelve.getAddress().getNeighborhood());
        assertEquals("CEP Teste 12", outputTwelve.getAddress().getCep());
        assertEquals("City Teste 12", outputTwelve.getAddress().getCity());
        assertEquals("State Teste 12", outputTwelve.getAddress().getState());
        assertEquals(true, outputTwelve.getEnabled());
    }
}

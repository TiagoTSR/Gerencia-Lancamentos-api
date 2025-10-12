package br.com.xdecodex.unittests.mapper;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import br.com.xdecodex.data.vo.v1.PessoaVO;
import br.com.xdecodex.mapper.DozerMapper;
import br.com.xdecodex.model.Pessoa;
import br.com.xdecodex.unittests.mapper.mocks.MockPessoa;

public class DozerPessoaConverterTest {

    MockPessoa inputObject;

    @BeforeEach
    public void setUp() {
        inputObject = new MockPessoa();
    }

    @Test
    public void parseEntityToVOTest() {
        PessoaVO output = DozerMapper.parseObject(inputObject.mockEntity(), PessoaVO.class);
        assertEquals(Long.valueOf(0L), output.getId());
        assertEquals("Nome Teste 0", output.getNome());
        assertEquals("Endereço Teste 0", output.getEndereco().getLogradouro());
        assertEquals("Número Teste 0", output.getEndereco().getNumero());
        assertEquals("Complemento Teste 0", output.getEndereco().getComplemento());
        assertEquals("Bairro Teste 0", output.getEndereco().getBairro());
        assertEquals("CEP Teste 0", output.getEndereco().getCep());
        assertEquals(Long.valueOf(0L), output.getEndereco().getCidade());
        assertEquals(true, output.getEnabled());
    }

    @Test
    public void parseEntityListToVOListTest() {
        List<PessoaVO> outputList = DozerMapper.parseListObjects(inputObject.mockEntityList(), PessoaVO.class);
        PessoaVO outputZero = outputList.get(0);

        assertEquals(Long.valueOf(0L), outputZero.getId());
        assertEquals("Nome Teste 0", outputZero.getNome());
        assertEquals("Endereço Teste 0", outputZero.getEndereco().getLogradouro());
        assertEquals("Número Teste 0", outputZero.getEndereco().getNumero());
        assertEquals("Complemento Teste 0", outputZero.getEndereco().getComplemento());
        assertEquals("Bairro Teste 0", outputZero.getEndereco().getBairro());
        assertEquals("CEP Teste 0", outputZero.getEndereco().getCep());
        assertEquals(Long.valueOf(0L), outputZero.getEndereco().getCidade());
        assertEquals(true, outputZero.getEnabled());

        PessoaVO outputSeven = outputList.get(7);
        assertEquals(Long.valueOf(7L), outputSeven.getId());
        assertEquals("Nome Teste 7", outputSeven.getNome());
        assertEquals("Endereço Teste 7", outputSeven.getEndereco().getLogradouro());
        assertEquals("Número Teste 7", outputSeven.getEndereco().getNumero());
        assertEquals("Complemento Teste 7", outputSeven.getEndereco().getComplemento());
        assertEquals("Bairro Teste 7", outputSeven.getEndereco().getBairro());
        assertEquals("CEP Teste 7", outputSeven.getEndereco().getCep());
        assertEquals(Long.valueOf(0L), outputSeven.getEndereco().getCidade());
        assertEquals(true, outputSeven.getEnabled());

        PessoaVO outputTwelve = outputList.get(12);
        assertEquals(Long.valueOf(12L), outputTwelve.getId());
        assertEquals("Nome Teste 12", outputTwelve.getNome());
        assertEquals("Endereço Teste 12", outputTwelve.getEndereco().getLogradouro());
        assertEquals("Número Teste 12", outputTwelve.getEndereco().getNumero());
        assertEquals("Complemento Teste 12", outputTwelve.getEndereco().getComplemento());
        assertEquals("Bairro Teste 12", outputTwelve.getEndereco().getBairro());
        assertEquals("CEP Teste 12", outputTwelve.getEndereco().getCep());
        assertEquals(Long.valueOf(0L), outputTwelve.getEndereco().getCidade());
        assertEquals(true, outputTwelve.getEnabled());
    }

    @Test
    public void parseVOToEntityTest() {
        Pessoa output = DozerMapper.parseObject(inputObject.mockVO(), Pessoa.class);
        assertEquals(Long.valueOf(0L), output.getId());
        assertEquals("Nome Teste 0", output.getNome());
        assertEquals("Endereço Teste 0", output.getEndereco().getLogradouro());
        assertEquals("Número Teste 0", output.getEndereco().getNumero());
        assertEquals("Complemento Teste 0", output.getEndereco().getComplemento());
        assertEquals("Bairro Teste 0", output.getEndereco().getBairro());
        assertEquals("CEP Teste 0", output.getEndereco().getCep());
        assertEquals(Long.valueOf(0L), output.getEndereco().getCidade());
        assertEquals(true, output.getEnabled());
    }

    @Test
    public void parserVOListToEntityListTest() {
        List<Pessoa> outputList = DozerMapper.parseListObjects(inputObject.mockVOList(), Pessoa.class);
        Pessoa outputZero = outputList.get(0);

        assertEquals(Long.valueOf(0L), outputZero.getId());
        assertEquals("Nome Teste 0", outputZero.getNome());
        assertEquals("Endereço Teste 0", outputZero.getEndereco().getLogradouro());
        assertEquals("Número Teste 0", outputZero.getEndereco().getNumero());
        assertEquals("Complemento Teste 0", outputZero.getEndereco().getComplemento());
        assertEquals("Bairro Teste 0", outputZero.getEndereco().getBairro());
        assertEquals("CEP Teste 0", outputZero.getEndereco().getCep());
        assertEquals(Long.valueOf(0L), outputZero.getEndereco().getCidade());
        assertEquals(true, outputZero.getEnabled());

        Pessoa outputSeven = outputList.get(7);
        assertEquals(Long.valueOf(7L), outputSeven.getId());
        assertEquals("Nome Teste 7", outputSeven.getNome());
        assertEquals("Endereço Teste 7", outputSeven.getEndereco().getLogradouro());
        assertEquals("Número Teste 7", outputSeven.getEndereco().getNumero());
        assertEquals("Complemento Teste 7", outputSeven.getEndereco().getComplemento());
        assertEquals("Bairro Teste 7", outputSeven.getEndereco().getBairro());
        assertEquals("CEP Teste 7", outputSeven.getEndereco().getCep());
        assertEquals(Long.valueOf(0L), outputSeven.getEndereco().getCidade());
        assertEquals(true, outputSeven.getEnabled());

        Pessoa outputTwelve = outputList.get(12);
        assertEquals(Long.valueOf(12L), outputTwelve.getId());
        assertEquals("Nome Teste 12", outputTwelve.getNome());
        assertEquals("Endereço Teste 12", outputTwelve.getEndereco().getLogradouro());
        assertEquals("Número Teste 12", outputTwelve.getEndereco().getNumero());
        assertEquals("Complemento Teste 12", outputTwelve.getEndereco().getComplemento());
        assertEquals("Bairro Teste 12", outputTwelve.getEndereco().getBairro());
        assertEquals("CEP Teste 12", outputTwelve.getEndereco().getCep());
        assertEquals(Long.valueOf(0L), outputTwelve.getEndereco().getCidade());
        assertEquals(true, outputTwelve.getEnabled());
    }
}

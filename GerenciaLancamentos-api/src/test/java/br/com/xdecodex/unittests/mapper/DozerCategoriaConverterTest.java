package br.com.xdecodex.unittests.mapper;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import br.com.xdecodex.data.vo.v1.CategoriaVO;
import br.com.xdecodex.mapper.DozerMapper;
import br.com.xdecodex.model.Categoria;
import br.com.xdecodex.unittests.mapper.mocks.MockCategoria;

public class DozerCategoriaConverterTest {

	 MockCategoria inputObject;

	 @BeforeEach
	 public void setUp() {
	    inputObject = new MockCategoria();
	 }

	 @Test
	 public void parseEntityToVOTest() {
	    CategoriaVO output = DozerMapper.parseObject(inputObject.mockEntity(), CategoriaVO.class);
	    assertEquals(Long.valueOf(0L), output.getCodigo());
	    assertEquals("Categoria Teste0", output.getNome());
	 }

	 @Test
	 public void parseEntityListToVOListTest() {
	    List<CategoriaVO> outputList = DozerMapper.parseListObjects(inputObject.mockEntityList(), CategoriaVO.class);
	    CategoriaVO outputZero = outputList.get(0);
	        
	    assertEquals(Long.valueOf(0L), outputZero.getCodigo());
	    assertEquals("Categoria Teste0", outputZero.getNome());
	        
	    CategoriaVO outputSeven = outputList.get(7);
	        
	    assertEquals(Long.valueOf(7L), outputSeven.getCodigo());
	    assertEquals("Categoria Teste7", outputSeven.getNome());
	        
	    CategoriaVO outputTwelve = outputList.get(12);
	        
	    assertEquals(Long.valueOf(12L), outputTwelve.getCodigo());
	    assertEquals("Categoria Teste12", outputTwelve.getNome());
	 }

	 @Test
	 public void parseVOToEntityTest() {
	    Categoria output = DozerMapper.parseObject(inputObject.mockVO(), Categoria.class);
	    assertEquals(Long.valueOf(0L), output.getCodigo());
	    assertEquals("Categoria Teste0", output.getNome());
	 }
	 
	 @Test
	 public void parserVOListToEntityListTest() {
	    List<Categoria> outputList = DozerMapper.parseListObjects(inputObject.mockVOList(), Categoria.class);
	    Categoria outputZero = outputList.get(0);
	        
	    assertEquals(Long.valueOf(0L), outputZero.getCodigo());
	    assertEquals("Categoria Teste0", outputZero.getNome());
	        
	    Categoria outputSeven = outputList.get(7);
	        
	    assertEquals(Long.valueOf(7L), outputSeven.getCodigo());
	    assertEquals("Categoria Teste7", outputSeven.getNome());
	        
	    Categoria outputTwelve = outputList.get(12);
	        
	    assertEquals(Long.valueOf(12L), outputTwelve.getCodigo());
	    assertEquals("Categoria Teste12", outputTwelve.getNome());
	}
}

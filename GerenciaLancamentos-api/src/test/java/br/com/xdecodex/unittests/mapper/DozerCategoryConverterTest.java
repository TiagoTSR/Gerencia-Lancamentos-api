package br.com.xdecodex.unittests.mapper;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import br.com.xdecodex.data.vo.v1.CategoryVO;
import br.com.xdecodex.mapper.DozerMapper;
import br.com.xdecodex.model.Category;
import br.com.xdecodex.unittests.mapper.mocks.MockCategory;

public class DozerCategoryConverterTest {

	 MockCategory inputObject;

	 @BeforeEach
	 public void setUp() {
	    inputObject = new MockCategory();
	 }

	 @Test
	 public void parseEntityToVOTest() {
	    CategoryVO output = DozerMapper.parseObject(inputObject.mockEntity(), CategoryVO.class);
	    assertEquals(Long.valueOf(0L), output.getId());
	    assertEquals("Category Teste0", output.getName());
	 }

	 @Test
	 public void parseEntityListToVOListTest() {
	    List<CategoryVO> outputList = DozerMapper.parseListObjects(inputObject.mockEntityList(), CategoryVO.class);
	    CategoryVO outputZero = outputList.get(0);
	        
	    assertEquals(Long.valueOf(0L), outputZero.getId());
	    assertEquals("Category Teste0", outputZero.getName());
	        
	    CategoryVO outputSeven = outputList.get(7);
	        
	    assertEquals(Long.valueOf(7L), outputSeven.getId());
	    assertEquals("Category Teste7", outputSeven.getName());
	        
	    CategoryVO outputTwelve = outputList.get(12);
	        
	    assertEquals(Long.valueOf(12L), outputTwelve.getId());
	    assertEquals("Category Teste12", outputTwelve.getName());
	 }

	 @Test
	 public void parseVOToEntityTest() {
	    Category output = DozerMapper.parseObject(inputObject.mockVO(), Category.class);
	    assertEquals(Long.valueOf(0L), output.getId());
	    assertEquals("Category Teste0", output.getName());
	 }
	 
	 @Test
	 public void parserVOListToEntityListTest() {
	    List<Category> outputList = DozerMapper.parseListObjects(inputObject.mockVOList(), Category.class);
	    Category outputZero = outputList.get(0);
	        
	    assertEquals(Long.valueOf(0L), outputZero.getId());
	    assertEquals("Category Teste0", outputZero.getName());
	        
	    Category outputSeven = outputList.get(7);
	        
	    assertEquals(Long.valueOf(7L), outputSeven.getId());
	    assertEquals("Category Teste7", outputSeven.getName());
	        
	    Category outputTwelve = outputList.get(12);
	        
	    assertEquals(Long.valueOf(12L), outputTwelve.getId());
	    assertEquals("Category Teste12", outputTwelve.getName());
	}
}

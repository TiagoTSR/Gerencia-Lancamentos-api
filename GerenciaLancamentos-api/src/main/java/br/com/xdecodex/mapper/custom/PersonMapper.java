package br.com.xdecodex.mapper.custom;

import java.util.Date;

import org.springframework.stereotype.Service;

import br.com.xdecodex.data.vo.v2.PersonVOV2;
import br.com.xdecodex.model.Person;

@Service
public class PersonMapper {
	
	public PersonVOV2 convertyEntityToVo(Person person) {
		PersonVOV2 vo = new PersonVOV2();
		vo.setId(person.getId());
		vo.setName(person.getName());
		vo.setBirthDay(new Date());
		vo.setAddress(person.getAddress());
		vo.setEnabled(person.getEnabled());
		return vo;
	}
	
	public Person convertyVoToEntity(PersonVOV2 person) {
		Person entity = new Person();
		entity.setId(person.getId());
		entity.setName(person.getName());
		/*entity.setBirthDay(new Date());*/
		entity.setAddress(person.getAddress());
		entity.setEnabled(person.getEnabled());
		return entity;
	}
	
}
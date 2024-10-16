package br.com.xdecodex.unittests.mapper.mocks;

import java.util.ArrayList;
import java.util.List;

import br.com.xdecodex.data.vo.v1.PersonVO;
import br.com.xdecodex.model.Person;
import br.com.xdecodex.model.Address;

public class MockPerson {

    public Person mockEntity() {
        return mockEntity(0);
    }
    
    public PersonVO mockVO() {
        return mockVO(0);
    }

    public List<Person> mockEntityList() {
        List<Person> persons = new ArrayList<>();
        for (int i = 0; i < 14; i++) {
            persons.add(mockEntity(i));
        }
        return persons;
    }

    public List<PersonVO> mockVOList() {
        List<PersonVO> persons = new ArrayList<>();
        for (int i = 0; i < 14; i++) {
            persons.add(mockVO(i));
        }
        return persons;
    }

    public Person mockEntity(Integer number) {
        Person person = new Person();
        person.setId(number.longValue());
        person.setName("Name Teste " + number);

        Address address = new Address();
        address.setLogradouro("Endereço Teste " + number);
        address.setNumber("Número Teste " + number);
        address.setComplement("Complement Teste " + number);
        address.setNeighborhood("Neighborhood Teste " + number);
        address.setCep("CEP Teste " + number);
        address.setCity("City Teste " + number);
        address.setState("State Teste " + number);

        person.setAddress(address);
        person.setEnabled(true);
        return person;
    }

    public PersonVO mockVO(Integer number) {
        PersonVO personVO = new PersonVO();
        personVO.setId(number.longValue());
        personVO.setName("Name Teste " + number);

        Address address = new Address();
        address.setLogradouro("Endereço Teste " + number);
        address.setNumber("Número Teste " + number);
        address.setComplement("Complement Teste " + number);
        address.setNeighborhood("Neighborhood Teste " + number);
        address.setCep("CEP Teste " + number);
        address.setCity("City Teste " + number);
        address.setState("State Teste " + number);

        personVO.setAddress(address);
        personVO.setEnabled(true);
        return personVO;
    }
}

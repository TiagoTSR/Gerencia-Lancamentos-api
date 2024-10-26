package br.com.xdecodex.unittests.mapper.mocks;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import br.com.xdecodex.data.vo.v1.LaunchVO;
import br.com.xdecodex.model.Launch;
import br.com.xdecodex.model.Person;
import br.com.xdecodex.model.Category;
import br.com.xdecodex.model.TypeLaunch;

public class MockLaunch {

    public Launch mockEntity() {
        return mockEntity(0);
    }
    
    public LaunchVO mockVO() {
        return mockVO(0);
    }

    public List<Launch> mockEntityList() {
        List<Launch> launchs = new ArrayList<>();
        for (int i = 0; i < 14; i++) {
            launchs.add(mockEntity(i));
        }
        return launchs;
    }

    public List<LaunchVO> mockVOList() {
        List<LaunchVO> launchs = new ArrayList<>();
        for (int i = 0; i < 14; i++) {
            launchs.add(mockVO(i));
        }
        return launchs;
    }
    
    public Launch mockEntity(Integer number) {
        Launch launch = new Launch();
        launch.setId(number.longValue());
        launch.setDescription("Description Teste " + number);
        launch.setExpirationDate(LocalDate.now().plusDays(number));
        launch.setPaymentDate(LocalDate.now().plusDays(number));// Definindo ExpirationDate corretamente
        launch.setValue(BigDecimal.valueOf(number));
        launch.setObservation("Observation Teste " + number);
        launch.setType(TypeLaunch.values()[number % TypeLaunch.values().length]);
        launch.setCategory(mockCategory(number));
        launch.setPerson(mockPerson(number));
        return launch;
    }

    public LaunchVO mockVO(Integer number) {
        LaunchVO launchVO = new LaunchVO();
        launchVO.setId(number.longValue());
        launchVO.setDescription("Description Teste " + number);
        launchVO.setExpirationDate(LocalDate.now().plusDays(number));
        launchVO.setPaymentDate(LocalDate.now().plusDays(number));// Definindo ExpirationDate corretamente
        launchVO.setValue(BigDecimal.valueOf(number));
        launchVO.setObservation("Observation Teste " + number);
        launchVO.setType(TypeLaunch.values()[number % TypeLaunch.values().length]);
        launchVO.setCategory(mockCategory(number));
        launchVO.setPerson(mockPerson(number));
        return launchVO;
    }

    public Person mockPerson(Integer number) {
        Person person = new Person();
        person.setId(number.longValue());
        person.setName("Name Teste " + number);
        person.setEnabled(true);
        return person;
    }

    private Category mockCategory(Integer number) {
        Category category = new Category();
        category.setId(number.longValue());
        category.setName("Category Teste " + number);
        return category;
    }
}

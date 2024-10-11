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
        List<Launch> lancamentos = new ArrayList<>();
        for (int i = 0; i < 14; i++) {
            lancamentos.add(mockEntity(i));
        }
        return lancamentos;
    }

    public List<LaunchVO> mockVOList() {
        List<LaunchVO> lancamentos = new ArrayList<>();
        for (int i = 0; i < 14; i++) {
            lancamentos.add(mockVO(i));
        }
        return lancamentos;
    }
    
    public Launch mockEntity(Integer number) {
        Launch lancamento = new Launch();
        lancamento.setId(number.longValue());
        lancamento.setDescription("Description Teste " + number);
        lancamento.setExpirationDate(LocalDate.now().plusDays(number));
        lancamento.setExpirationDate(LocalDate.now().plusDays(number));
        lancamento.setValue(BigDecimal.valueOf(number));
        lancamento.setObservation("Observation Teste " + number);
        lancamento.setType(TypeLaunch.values()[number % TypeLaunch.values().length]);
        lancamento.setCategory(mockCategory(number));
        lancamento.setPerson(mockPerson(number));
        return lancamento;
    }

    public LaunchVO mockVO(Integer number) {
        LaunchVO lancamentoVO = new LaunchVO();
        lancamentoVO.setId(number.longValue());
        lancamentoVO.setDescription("Description Teste " + number);
        lancamentoVO.setExpirationDate(LocalDate.now().plusDays(number));
        lancamentoVO.setExpirationDate(LocalDate.now().plusDays(number));
        lancamentoVO.setValue(BigDecimal.valueOf(number));
        lancamentoVO.setObservation("Observation Teste " + number);
        lancamentoVO.setType(TypeLaunch.values()[number % TypeLaunch.values().length]);
        lancamentoVO.setCategory(mockCategory(number));
        lancamentoVO.setPerson(mockPerson(number));
        return lancamentoVO;
    }

    public Person mockPerson(Integer number) {
        Person pessoa = new Person();
        pessoa.setId(number.longValue());
        pessoa.setName("Name Teste " + number);
        pessoa.setEnabled(true);
        return pessoa;
    }

    private Category mockCategory(Integer number) {
        Category category = new Category();
        category.setId(number.longValue());
        category.setName("Category Teste " + number);
        return category;
    }
}

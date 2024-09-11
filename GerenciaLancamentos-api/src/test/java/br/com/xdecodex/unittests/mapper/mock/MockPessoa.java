package br.com.xdecodex.unittests.mapper.mock;

import java.util.ArrayList;
import java.util.List;

import br.com.xdecodex.data.vo.v1.PessoaVO;
import br.com.xdecodex.model.Pessoa;

public class MockPessoa {

    public Pessoa mockEntity() {
        return mockEntity(0);
    }
    
    public PessoaVO mockVO() {
        return mockVO(0);
    }
 
    public List<Pessoa> mockEntityList() {
        List<Pessoa> pessoas = new ArrayList<>();
        for (int i = 0; i < 14; i++) {
            pessoas.add(mockEntity(i));
        }
        return pessoas;
    }

    public List<PessoaVO> mockVOList() {
        List<PessoaVO> pessoas = new ArrayList<>();
        for (int i = 0; i < 14; i++) {
            pessoas.add(mockVO(i));
        }
        return pessoas;
    }
    
    public Pessoa mockEntity(Integer number) {
        Pessoa pessoa = new Pessoa();
        pessoa.setCodigo(number.longValue());
        pessoa.setNome("Nome Teste" + number);
        pessoa.setEndereco(null); // Pode ser ajustado caso deseje mockar o endereço também
        return pessoa;
    }

    public PessoaVO mockVO(Integer number) {
        PessoaVO pessoaVO = new PessoaVO();
        pessoaVO.setCodigo(number.longValue());
        pessoaVO.setNome("Nome Teste" + number);
        pessoaVO.setEndereco(null); // Pode ser ajustado caso deseje mockar o endereço também
        return pessoaVO;
    }
}
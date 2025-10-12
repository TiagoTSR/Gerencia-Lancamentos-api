package br.com.xdecodex.unittests.mapper.mocks;

import java.util.ArrayList;
import java.util.List;

import br.com.xdecodex.data.vo.v1.PessoaVO;
import br.com.xdecodex.model.Pessoa;
import br.com.xdecodex.model.Endereco;

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
        pessoa.setId(number.longValue());
        pessoa.setNome("Nome Teste " + number);

        Endereco endereco = new Endereco();
        endereco.setLogradouro("Endereço Teste " + number);
        endereco.setNumero("Número Teste " + number);
        endereco.setComplemento("Complemento Teste " + number);
        endereco.setBairro("Bairro Teste " + number);
        endereco.setCep("CEP Teste " + number);
        endereco.setCidade(number.longValue());

        pessoa.setEndereco(endereco);
        pessoa.setEnabled(true);
        return pessoa;
    }

    public PessoaVO mockVO(Integer number) {
        PessoaVO pessoaVO = new PessoaVO();
        pessoaVO.setId(number.longValue());
        pessoaVO.setNome("Nome Teste " + number);

        Endereco endereco = new Endereco();
        endereco.setLogradouro("Endereço Teste " + number);
        endereco.setNumero("Número Teste " + number);
        endereco.setComplemento("Complemento Teste " + number);
        endereco.setBairro("Bairro Teste " + number);
        endereco.setCep("CEP Teste " + number);
        endereco.setCidade(number.longValue());

        pessoaVO.setEndereco(endereco);
        pessoaVO.setEnabled(true);
        return pessoaVO;
    }
}

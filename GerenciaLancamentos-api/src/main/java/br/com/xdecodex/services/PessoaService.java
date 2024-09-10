package br.com.xdecodex.services;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import java.util.logging.Logger;

import org.springframework.stereotype.Service;

import br.com.xdecodex.model.Endereco;
import br.com.xdecodex.model.Pessoa;

@Service
public class PessoaServices {

    private final AtomicLong counter = new AtomicLong();
    private Logger logger = Logger.getLogger(PessoaServices.class.getName());

    public List<Pessoa> findAll() {

        logger.info("Finding all people!");

        List<Pessoa> Pessoas = new ArrayList<>();
        for (int i = 0; i < 8; i++) {
            Pessoa Pessoa = mockPessoa(i);
            Pessoas.add(Pessoa);
        }
        return Pessoas;
    }

    public Pessoa findById(String id) {

        logger.info("Finding one Pessoa!");

        Pessoa pessoa = new Pessoa();
        pessoa.setCodigo(counter.incrementAndGet());
        pessoa.setNome("Tiago");

        Endereco endereco = new Endereco();
        endereco.setLogradouro("Rua Exemplo");
        endereco.setNumero("123");
        endereco.setComplemento("Apto 1");
        endereco.setBairro("Vila Velha");
        endereco.setCep("29100-000");
        endereco.setCidade("Vila Velha");
        endereco.setEstado("Espírito Santo");

        pessoa.setEndereco(endereco);
        pessoa.setAtivo(true);

        return pessoa;
    }

    public Pessoa create(Pessoa Pessoa) {

        logger.info("Creating one Pessoa!");
        return Pessoa;
    }

    public Pessoa update(Pessoa Pessoa) {

        logger.info("Updating one Pessoa!");
        return Pessoa;
    }

    public void delete(String id) {

        logger.info("Deleting one Pessoa!");
    }

    private Pessoa mockPessoa(int i) {
        Pessoa pessoa = new Pessoa();
        pessoa.setCodigo(counter.incrementAndGet());
        pessoa.setNome("Pessoa name " + i);

        // precisa instanciar a classe caso seja Embedded para setar as propriedades
        Endereco endereco = new Endereco();
        endereco.setLogradouro("Rua Exemplo " + i);
        endereco.setNumero("123");
        endereco.setComplemento("Apto " + i);
        endereco.setBairro("Bairro Exemplo");
        endereco.setCep("12345-678");
        endereco.setCidade("Cidade Exemplo");
        endereco.setEstado("Estado Exemplo");

        pessoa.setEndereco(endereco);
        pessoa.setAtivo(true);

        return pessoa;
    }

}

package br.com.xdecodex.services;

import java.util.List;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import br.com.xdecodex.model.Pessoa;
import br.com.xdecodex.repositories.PessoaRepository;

@Service
public class PessoaService {

    @Autowired
    private PessoaRepository pessoaRepository;

    public List<Pessoa> findAll() {
        return pessoaRepository.findAll();
    }

    public Pessoa findById(Long id) {
        return pessoaRepository.findById(id)
            .orElseThrow(() -> new EmptyResultDataAccessException("Pessoa não encontrada", 1));
    }

    public Pessoa create(Pessoa pessoa) {
        return pessoaRepository.save(pessoa);
    }

    public Pessoa update(Long id, Pessoa pessoa) {
        Pessoa pessoaSalva = findById(id);
        BeanUtils.copyProperties(pessoa, pessoaSalva, "id");
        return pessoaRepository.save(pessoaSalva);
    }


    public void updateAtivo(Long id, Boolean ativo) {
        Pessoa pessoaSalva = findById(id);
        pessoaSalva.setAtivo(ativo);
        pessoaRepository.save(pessoaSalva);
    }

    public boolean delete(Long id) {
        if (!pessoaRepository.existsById(id)) {
            return false;
        }
        pessoaRepository.deleteById(id);
        return true;
    }
}

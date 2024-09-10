package br.com.xdecodex.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.xdecodex.exceptions.PessoaInexistenteOuInativaException;
import br.com.xdecodex.model.Lancamento;
import br.com.xdecodex.model.Pessoa;
import br.com.xdecodex.repositories.LancamentoRepository;
import br.com.xdecodex.repositories.PessoaRepository;

@Service
public class LancamentoService {

    @Autowired
    private PessoaRepository pessoaRepository;

    @Autowired
    private LancamentoRepository lancamentoRepository;

    public List<Lancamento> findAll() {
        return lancamentoRepository.findAll();
    }

    public Lancamento findById(Long id) {
        Optional<Lancamento> lancamento = lancamentoRepository.findById(id);
        return lancamento.orElse(null);
    }

    public Lancamento save(Lancamento lancamento) {
        Pessoa pessoa = pessoaRepository.findById(lancamento.getPessoa().getCodigo()).orElse(null);
        if (pessoa == null || pessoa.isInativo()) {
            throw new PessoaInexistenteOuInativaException();
        }
        return lancamentoRepository.save(lancamento);
    }

    public Lancamento update(Lancamento lancamento) {
        if (lancamento.getCodigo() == null || !lancamentoRepository.existsById(lancamento.getCodigo())) {
            throw new IllegalArgumentException("Lançamento não encontrado para atualização");
        }
        return lancamentoRepository.save(lancamento);
    }

    public boolean delete(Long id) {
        if (!lancamentoRepository.existsById(id)) {
            return false;
        }
        lancamentoRepository.deleteById(id);
        return true;
    }
}

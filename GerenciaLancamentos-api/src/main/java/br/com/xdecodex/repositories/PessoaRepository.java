package br.com.xdecodex.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.xdecodex.model.Pessoa;

public interface PessoaRepository extends JpaRepository<Pessoa, Long>{

}

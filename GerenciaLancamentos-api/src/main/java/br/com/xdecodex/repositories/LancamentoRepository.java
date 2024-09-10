package br.com.xdecodex.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.xdecodex.model.Lancamento;

public interface LancamentoRepository extends JpaRepository<Lancamento, Long> {

}

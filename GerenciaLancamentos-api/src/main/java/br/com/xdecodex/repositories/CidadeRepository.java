package br.com.xdecodex.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.xdecodex.model.Cidade;

public interface CidadeRepository extends JpaRepository<Cidade, Long> {

	List<Cidade> findByEstadoId(Long estadoId);

}
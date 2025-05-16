package br.com.xdecodex.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import br.com.xdecodex.model.Usuario;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
	
	@Query("SELECT u FROM Usuario u WHERE u.userName =:userName")
	Usuario findByUsername(@Param("userName") String userName);
	
	public Optional<Usuario> findByEmail(String email);

	public List<Usuario> findByPermissoesDescricao(String permissaoDescricao);
}

package br.com.xdecodex.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import br.com.xdecodex.model.Launch;

public interface LaunchRepository extends JpaRepository<Launch, Long> {
	
	@Query("SELECT l FROM Launch l WHERE l.description LIKE LOWER(CONCAT ('%',:description,'%'))")
	Page<Launch> findLaunchsByDescription(@Param("description") String description, Pageable pageable);

}

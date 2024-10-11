package br.com.xdecodex.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import br.com.xdecodex.model.Category;
import br.com.xdecodex.model.Launch;

public interface CategoryRepository extends JpaRepository<Category, Long> {
	
	@Query("SELECT c FROM Category c WHERE c.name LIKE LOWER(CONCAT ('%',:name,'%'))")
	Page<Launch> findCategorysByName(@Param("name") String name, Pageable pageable);
}
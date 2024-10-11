package br.com.xdecodex.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import br.com.xdecodex.model.Person;

public interface PersonRepository extends JpaRepository<Person, Long> {
	
    @Query("SELECT p FROM Person p WHERE LOWER(p.name) LIKE LOWER(CONCAT('%', :name, '%'))")
    Page<Person> findPersonsByName(@Param("name") String names, Pageable pageable);

}

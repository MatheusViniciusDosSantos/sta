package br.com.sta.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import br.com.sta.domain.AutorTrabalho;

public interface AutorTrabalhoRepository extends JpaRepository<AutorTrabalho, Long> {
	@Query(value = "select p from AutorTrabalho p where p.autor.id=?1")
	Page<AutorTrabalho> findByAutor(Long id);

	@Query(value = "select p from AutorTrabalho p where p.trabalho.id=?1")
	Page<AutorTrabalho> findByTrabalho(Long id);
}
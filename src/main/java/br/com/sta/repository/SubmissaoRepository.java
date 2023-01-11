package br.com.sta.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import br.com.sta.domain.Submissao;

public interface SubmissaoRepository extends JpaRepository<Submissao, Long> {
	@Query(value = "select p from Submissao p where p.resultado like %?1%")
	Page<Submissao> findByResultado(String resultado, Pageable page);
}


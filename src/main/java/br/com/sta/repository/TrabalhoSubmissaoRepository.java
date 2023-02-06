package br.com.sta.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import br.com.sta.domain.TrabalhoSubmissao;

public interface TrabalhoSubmissaoRepository extends JpaRepository<TrabalhoSubmissao, Long> {
	@Query(value = "select p from TrabalhoSubmissao p where p.trabalho.id=?1")
	Page<TrabalhoSubmissao> findByTrabalho(Long id, Pageable page);

	@Query(value = "select p from TrabalhoSubmissao p where p.submissao.id=?1")
	Page<TrabalhoSubmissao> findBySubmissao(Long id, Pageable page);
}
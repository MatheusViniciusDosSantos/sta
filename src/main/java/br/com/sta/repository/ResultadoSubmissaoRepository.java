package br.com.sta.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import br.com.sta.domain.ResultadoSubmissao;

public interface ResultadoSubmissaoRepository extends JpaRepository<ResultadoSubmissao, Long> {
	@Query(value = "select p from ResultadoSubmissao p where p.criterio.id=?1")
	Page<ResultadoSubmissao> findByCriterio(Long id, Pageable page);

	@Query(value = "select p from ResultadoSubmissao p where p.avaliadorSubmissao.id=?1")
	Page<ResultadoSubmissao> findByAvaliadorSubmissao(Long id, Pageable page);
}
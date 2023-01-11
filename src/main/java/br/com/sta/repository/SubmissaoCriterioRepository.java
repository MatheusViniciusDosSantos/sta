package br.com.sta.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import br.com.sta.domain.SubmissaoCriterio;

public interface SubmissaoCriterioRepository extends JpaRepository<SubmissaoCriterio, Long> {
	@Query(value = "select p from SubmissaoCriterio p where p.submissao.id=?1")
	Page<SubmissaoCriterio> findBySubmissao(Long id, Pageable page);

	@Query(value = "select p from SubmissaoCriterio p where p.criterio.id=?1")
	Page<SubmissaoCriterio> findByCriterio(Long id, Pageable page);
}
package br.com.sta.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import br.com.sta.domain.Criterio;

public interface CriterioRepository extends JpaRepository<Criterio, Long> {
	@Query(value = "select p from Criterio p where p.descricao like %?1%")
	Page<Criterio> findByDescricao(String descricao, Pageable page);
}


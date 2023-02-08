package br.com.sta.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import br.com.sta.domain.AvaliadorSubmissao;

public interface AvaliadorSubmissaoRepository extends JpaRepository<AvaliadorSubmissao, Long> {
	@Query(value = "select p from AvaliadorSubmissao p where p.avaliador.id=?1")
	Page<AvaliadorSubmissao> findByAvaliador(Long id, Pageable page);

	@Query(value = "select p from AvaliadorSubmissao p where p.traabalhoSubmissao.id=?1")
	Page<AvaliadorSubmissao> findByTrabalhoSubmissao(Long id, Pageable page);
}
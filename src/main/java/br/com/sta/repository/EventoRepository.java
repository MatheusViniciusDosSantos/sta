package br.com.sta.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import br.com.sta.domain.Evento;

public interface EventoRepository extends JpaRepository<Evento, Long> {
	@Query(value = "select p from Evento p where p.nome like %?1%")
	Page<Evento> findByNome(String nome, Pageable page);

	@Query(value = "select p from Evento p where p.organizador.id=?1")
	Page<Evento> findByOrganizador(Long id, Pageable page);
}


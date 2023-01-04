package br.com.sta.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import br.com.sta.domain.Usuario;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
	@Query(value = "select p from Usuario p where p.nome like %?1%")
	Page<Usuario> findByNome(String nome, Pageable page);
	
	@Query(value = "select p from Usuario p where p.cpf=?1")
	Usuario findByCpf(String cpf);
	
	@Query(value = "select p from Usuario p where p.email=?1")
	Usuario findByEmail(String email);
	
	Usuario findByEmailAndCodigoRecuperacaoSenha(String email, String codigoRecuperacaoSenha);
}


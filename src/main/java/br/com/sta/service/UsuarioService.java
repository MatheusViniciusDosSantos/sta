package br.com.sta.service;

import java.time.Instant;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import br.com.sta.domain.Usuario;
import br.com.sta.dto.UsuarioDTO;
import br.com.sta.exception.BadResourceException;
import br.com.sta.exception.ResourceAlreadyExistsException;
import br.com.sta.exception.ResourceNotFoundException;
import br.com.sta.repository.UsuarioRepository;

@Service
public class UsuarioService {
	
	@Autowired
	private UsuarioRepository usuarioRepository;
	
	private boolean existsById(Long id) {
		return usuarioRepository.existsById(id);
	}
	
	public Usuario findById(Long id) throws ResourceNotFoundException {
		Usuario usuario = usuarioRepository.findById(id).orElse(null);
		
		if(usuario == null) {
			throw new ResourceNotFoundException("Usuario não encontrado com o id: " + id);
		} else {
			return usuario;
		}
	}
	
	public Page<UsuarioDTO> findAll(Pageable pageable) {
		
		return new UsuarioDTO().converterListaUsuarioDTO(usuarioRepository.findAll(pageable)) ;
	}
	
	public Page<UsuarioDTO> findAllByNome(String descricao, Pageable page) {
		Page<Usuario> usuarios = usuarioRepository.findByNome(descricao, page);
		
		
		return new UsuarioDTO().converterListaUsuarioDTO(usuarios);
	}
	
	public Usuario save(Usuario usuario) throws BadResourceException, ResourceAlreadyExistsException {
		if(!usuario.getNome().isEmpty()) {
			usuario.setSenha(new BCryptPasswordEncoder().encode(usuario.getSenha()));
			if(existsById(usuario.getId())) {
				throw new ResourceAlreadyExistsException("Usuario com id: " + usuario.getId() + " já existe.");
			}			
			usuario.setStatus('A');
			usuario.setDataCadastro(Instant.now());
			Usuario usuarioNovo = usuarioRepository.save(usuario);			
			return usuarioNovo;
		} else {
			BadResourceException exe = new BadResourceException("Erro ao salvar usuario");
			exe.addErrorMessage("Usuario esta vazio ou nulo");
			throw exe;
		}
		
		
	}
	
	public void update(Usuario usuario) throws BadResourceException, ResourceNotFoundException {
		if (!usuario.getNome().isEmpty()) {
			if (!existsById(usuario.getId())) {
				throw new ResourceNotFoundException("Usuario não encontrado com o id: " + usuario.getId());
			}
			usuario.setDataUltimaAlteracao(Instant.now());
		} else {
			BadResourceException exe = new BadResourceException("Erro ao salvar usuario");
			exe.addErrorMessage("Usuario esta vazio ou nulo");
			throw exe;
		}
	}
	
	public void deleteById(Long id) throws ResourceNotFoundException {
		if(!existsById(id)) {
			throw new ResourceNotFoundException("Usuario não encontrado com o id: " + id);
		} else {
			usuarioRepository.deleteById(id);
		}
	
	}  public Long count() {
		return usuarioRepository.count();
	}
	
}

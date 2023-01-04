package br.com.sta.service;

import java.util.Calendar;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import br.com.sta.domain.Organizador;
import br.com.sta.dto.OrganizadorDTO;
import br.com.sta.exception.BadResourceException;
import br.com.sta.exception.ResourceAlreadyExistsException;
import br.com.sta.exception.ResourceNotFoundException;
import br.com.sta.repository.OrganizadorRepository;

@Service
public class OrganizadorService {
	
	@Autowired
	private OrganizadorRepository organizadorRepository;
	
	private boolean existsById(Long id) {
		return organizadorRepository.existsById(id);
	}
	
	public Organizador findById(Long id) throws ResourceNotFoundException {
		Organizador organizador = organizadorRepository.findById(id).orElse(null);
		
		if(organizador == null) {
			throw new ResourceNotFoundException("Organizador não encontrado com o id: " + id);
		} else {
			return organizador;
		}
	}
	
	public Page<OrganizadorDTO> findAll(Pageable pageable) {
		
		return new OrganizadorDTO().converterListaOrganizadorDTO(organizadorRepository.findAll(pageable)) ;
	}
	
	public Page<OrganizadorDTO> findAllByNome(String descricao, Pageable page) {
		Page<Organizador> organizadores = organizadorRepository.findByNome(descricao, page);
		
		
		return new OrganizadorDTO().converterListaOrganizadorDTO(organizadores);
	}
	
	public Organizador save(Organizador organizador) throws BadResourceException, ResourceAlreadyExistsException {
		if(!organizador.getNome().isEmpty()) {
			organizador.setSenha(new BCryptPasswordEncoder().encode(organizador.getSenha()));
			if(existsById(organizador.getId())) {
				throw new ResourceAlreadyExistsException("Organizador com id: " + organizador.getId() + " já existe.");
			}			
			organizador.setStatus('A');
			organizador.setDataCadastro(Calendar.getInstance().getTime());
			return organizadorRepository.save(organizador);
		} else {
			BadResourceException exe = new BadResourceException("Erro ao salvar organizador");
			exe.addErrorMessage("Organizador esta vazio ou nulo");
			throw exe;
		}
		
		
	}
	
	public void update(Organizador organizador) throws BadResourceException, ResourceNotFoundException {
		if (!organizador.getNome().isEmpty()) {
			if (!existsById(organizador.getId())) {
				throw new ResourceNotFoundException("Organizador não encontrado com o id: " + organizador.getId());
			}
			organizador.setDataUltimaAlteracao(Calendar.getInstance().getTime());
		} else {
			BadResourceException exe = new BadResourceException("Erro ao salvar organizador");
			exe.addErrorMessage("Organizador esta vazio ou nulo");
			throw exe;
		}
	}
	
	public void deleteById(Long id) throws ResourceNotFoundException {
		if(!existsById(id)) {
			throw new ResourceNotFoundException("Organizador não encontrado com o id: " + id);
		} else {
			organizadorRepository.deleteById(id);
		}
	
	}  public Long count() {
		return organizadorRepository.count();
	}
	
}

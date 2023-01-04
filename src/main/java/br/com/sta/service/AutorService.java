package br.com.sta.service;

import java.time.Instant;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import br.com.sta.domain.Autor;
import br.com.sta.dto.AutorDTO;
import br.com.sta.exception.BadResourceException;
import br.com.sta.exception.ResourceAlreadyExistsException;
import br.com.sta.exception.ResourceNotFoundException;
import br.com.sta.repository.AutorRepository;

@Service
public class AutorService {
	
	@Autowired
	private AutorRepository autorRepository;
	
	private boolean existsById(Long id) {
		return autorRepository.existsById(id);
	}
	
	public Autor findById(Long id) throws ResourceNotFoundException {
		Autor autor = autorRepository.findById(id).orElse(null);
		
		if(autor == null) {
			throw new ResourceNotFoundException("Autor não encontrado com o id: " + id);
		} else {
			return autor;
		}
	}
	
	public Page<AutorDTO> findAll(Pageable pageable) {
		
		return new AutorDTO().converterListaAutorDTO(autorRepository.findAll(pageable)) ;
	}
	
	public Page<AutorDTO> findAllByNome(String descricao, Pageable page) {
		Page<Autor> autores = autorRepository.findByNome(descricao, page);
		
		
		return new AutorDTO().converterListaAutorDTO(autores);
	}
	
	public Autor save(Autor autor) throws BadResourceException, ResourceAlreadyExistsException {
		if(!autor.getNome().isEmpty()) {
			if(existsById(autor.getId())) {
				throw new ResourceAlreadyExistsException("Autor com id: " + autor.getId() + " já existe.");
			}			
			autor.setStatus('A');
			autor.setDataCadastro(Instant.now());
			return autorRepository.save(autor);
		} else {
			BadResourceException exe = new BadResourceException("Erro ao salvar autor");
			exe.addErrorMessage("Autor esta vazio ou nulo");
			throw exe;
		}
		
		
	}
	
	public void update(Autor autor) throws BadResourceException, ResourceNotFoundException {
		if (!autor.getNome().isEmpty()) {
			if (!existsById(autor.getId())) {
				throw new ResourceNotFoundException("Autor não encontrado com o id: " + autor.getId());
			}
			autor.setDataUltimaAlteracao(Instant.now());
		} else {
			BadResourceException exe = new BadResourceException("Erro ao salvar autor");
			exe.addErrorMessage("Autor esta vazio ou nulo");
			throw exe;
		}
	}
	
	public void deleteById(Long id) throws ResourceNotFoundException {
		if(!existsById(id)) {
			throw new ResourceNotFoundException("Autor não encontrado com o id: " + id);
		} else {
			autorRepository.deleteById(id);
		}
	
	}  public Long count() {
		return autorRepository.count();
	}
	
}

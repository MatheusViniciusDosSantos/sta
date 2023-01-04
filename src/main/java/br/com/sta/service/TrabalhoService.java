package br.com.sta.service;

import java.time.Instant;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import br.com.sta.domain.Trabalho;
import br.com.sta.dto.TrabalhoDTO;
import br.com.sta.exception.BadResourceException;
import br.com.sta.exception.ResourceAlreadyExistsException;
import br.com.sta.exception.ResourceNotFoundException;
import br.com.sta.repository.TrabalhoRepository;

@Service
public class TrabalhoService {
	
	@Autowired
	private TrabalhoRepository trabalhoRepository;
	
	private boolean existsById(Long id) {
		return trabalhoRepository.existsById(id);
	}
	
	public Trabalho findById(Long id) throws ResourceNotFoundException {
		Trabalho trabalho = trabalhoRepository.findById(id).orElse(null);
		
		if(trabalho == null) {
			throw new ResourceNotFoundException("Trabalho não encontrado com o id: " + id);
		} else {
			return trabalho;
		}
	}
	
	public Page<TrabalhoDTO> findAll(Pageable pageable) {
		
		return new TrabalhoDTO().converterListaTrabalhoDTO(trabalhoRepository.findAll(pageable)) ;
	}
	
	public Page<TrabalhoDTO> findAllByTitulo(String descricao, Pageable page) {
		Page<Trabalho> trabalhos = trabalhoRepository.findByTitulo(descricao, page);
		
		
		return new TrabalhoDTO().converterListaTrabalhoDTO(trabalhos);
	}
	
	public Trabalho save(Trabalho trabalho) throws BadResourceException, ResourceAlreadyExistsException {
		if(!trabalho.getTitulo().isEmpty()) {
			if(existsById(trabalho.getId())) {
				throw new ResourceAlreadyExistsException("Trabalho com id: " + trabalho.getId() + " já existe.");
			}			
			trabalho.setStatus('A');
			trabalho.setDataCadastro(Instant.now());
			Trabalho trabalhoNovo = trabalhoRepository.save(trabalho);			
			return trabalhoNovo;
		} else {
			BadResourceException exe = new BadResourceException("Erro ao salvar trabalho");
			exe.addErrorMessage("Trabalho esta vazio ou nulo");
			throw exe;
		}
		
		
	}
	
	public void update(Trabalho trabalho) throws BadResourceException, ResourceNotFoundException {
		if (!trabalho.getTitulo().isEmpty()) {
			if (!existsById(trabalho.getId())) {
				throw new ResourceNotFoundException("Trabalho não encontrado com o id: " + trabalho.getId());
			}
			trabalho.setDataUltimaAlteracao(Instant.now());
		} else {
			BadResourceException exe = new BadResourceException("Erro ao salvar trabalho");
			exe.addErrorMessage("Trabalho esta vazio ou nulo");
			throw exe;
		}
	}
	
	public void deleteById(Long id) throws ResourceNotFoundException {
		if(!existsById(id)) {
			throw new ResourceNotFoundException("Trabalho não encontrado com o id: " + id);
		} else {
			trabalhoRepository.deleteById(id);
		}
	
	}  public Long count() {
		return trabalhoRepository.count();
	}
	
}

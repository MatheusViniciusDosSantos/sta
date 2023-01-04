package br.com.sta.service;

import java.util.Calendar;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import br.com.sta.domain.AutorTrabalho;
import br.com.sta.dto.AutorTrabalhoDTO;
import br.com.sta.exception.BadResourceException;
import br.com.sta.exception.ResourceAlreadyExistsException;
import br.com.sta.exception.ResourceNotFoundException;
import br.com.sta.repository.AutorTrabalhoRepository;

@Service
public class AutorTrabalhoService {
	
	@Autowired
	private AutorTrabalhoRepository autorTrabalhoRepository;
	
	private boolean existsById(Long id) {
		return autorTrabalhoRepository.existsById(id);
	}
	
	public AutorTrabalho findById(Long id) throws ResourceNotFoundException {
		AutorTrabalho autorTrabalho = autorTrabalhoRepository.findById(id).orElse(null);
		
		if(autorTrabalho == null) {
			throw new ResourceNotFoundException("AutorTrabalho não encontrado com o id: " + id);
		} else {
			return autorTrabalho;
		}
	}
	
	public Page<AutorTrabalhoDTO> findAll(Pageable pageable) {
		
		return new AutorTrabalhoDTO().converterListaAutorTrabalhoDTO(autorTrabalhoRepository.findAll(pageable)) ;
	}
	
	public Page<AutorTrabalhoDTO> findAllByIdAutor(Long id, Pageable page) {
		Page<AutorTrabalho> autorTrabalhos = autorTrabalhoRepository.findByAutor(id, page);
		return new AutorTrabalhoDTO().converterListaAutorTrabalhoDTO(autorTrabalhos);
	}

	public Page<AutorTrabalhoDTO> findAllByIdTrabalho(Long id, Pageable page) {
		Page<AutorTrabalho> autorTrabalhos = autorTrabalhoRepository.findByTrabalho(id, page);
		return new AutorTrabalhoDTO().converterListaAutorTrabalhoDTO(autorTrabalhos);
	}
	
	public AutorTrabalho save(AutorTrabalho autorTrabalho) throws BadResourceException, ResourceAlreadyExistsException {
		if (autorTrabalho.getAutor() != null && autorTrabalho.getTrabalho() != null) {
			if(existsById(autorTrabalho.getId())) {
				throw new ResourceAlreadyExistsException("AutorTrabalho com id: " + autorTrabalho.getId() + " já existe.");
			}			
			autorTrabalho.setStatus('A');
			autorTrabalho.setDataCadastro(Calendar.getInstance().getTime());
			AutorTrabalho autorTrabalhoNovo = autorTrabalhoRepository.save(autorTrabalho);			
			return autorTrabalhoNovo;
		} else {
			BadResourceException exe = new BadResourceException("Erro ao salvar autorTrabalho");
			exe.addErrorMessage("AutorTrabalho esta vazio ou nulo");
			throw exe;
		}		
	}
	
	public void update(AutorTrabalho autorTrabalho) throws BadResourceException, ResourceNotFoundException {
		if (autorTrabalho.getAutor() != null && autorTrabalho.getTrabalho() != null) {
			if (!existsById(autorTrabalho.getId())) {
				throw new ResourceNotFoundException("AutorTrabalho não encontrado com o id: " + autorTrabalho.getId());
			}
			autorTrabalho.setDataUltimaAlteracao(Calendar.getInstance().getTime());
		} else {
			BadResourceException exe = new BadResourceException("Erro ao salvar autorTrabalho");
			exe.addErrorMessage("AutorTrabalho esta vazio ou nulo");
			throw exe;
		}
	}
	
	public void deleteById(Long id) throws ResourceNotFoundException {
		if(!existsById(id)) {
			throw new ResourceNotFoundException("AutorTrabalho não encontrado com o id: " + id);
		} else {
			autorTrabalhoRepository.deleteById(id);
		}
	
	}  public Long count() {
		return autorTrabalhoRepository.count();
	}
	
}

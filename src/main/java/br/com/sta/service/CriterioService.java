package br.com.sta.service;

import java.util.Calendar;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import br.com.sta.domain.Criterio;
import br.com.sta.dto.CriterioDTO;
import br.com.sta.exception.BadResourceException;
import br.com.sta.exception.ResourceAlreadyExistsException;
import br.com.sta.exception.ResourceNotFoundException;
import br.com.sta.repository.CriterioRepository;

@Service
public class CriterioService {
	
	@Autowired
	private CriterioRepository criterioRepository;
	
	private boolean existsById(Long id) {
		return criterioRepository.existsById(id);
	}
	
	public Criterio findById(Long id) throws ResourceNotFoundException {
		Criterio criterio = criterioRepository.findById(id).orElse(null);
		
		if(criterio == null) {
			throw new ResourceNotFoundException("Criterio não encontrado com o id: " + id);
		} else {
			return criterio;
		}
	}
	
	public Page<CriterioDTO> findAll(Pageable pageable) {
		
		return new CriterioDTO().converterListaCriterioDTO(criterioRepository.findAll(pageable)) ;
	}
	
	public Page<CriterioDTO> findAllByDescricao(String descricao, Pageable page) {
		Page<Criterio> criterios = criterioRepository.findByDescricao(descricao, page);
		
		
		return new CriterioDTO().converterListaCriterioDTO(criterios);
	}
	
	public Criterio save(Criterio criterio) throws BadResourceException, ResourceAlreadyExistsException {
		if(!criterio.getDescricao().isEmpty()) {
			if(existsById(criterio.getId())) {
				throw new ResourceAlreadyExistsException("Criterio com id: " + criterio.getId() + " já existe.");
			}			
			criterio.setStatus('A');
			criterio.setDataCadastro(Calendar.getInstance().getTime());
			Criterio criterioNovo = criterioRepository.save(criterio);
			return criterioNovo;
		} else {
			BadResourceException exe = new BadResourceException("Erro ao salvar criterio");
			exe.addErrorMessage("Criterio esta vazio ou nulo");
			throw exe;
		}
		
		
	}
	
	public void update(Criterio criterio) throws BadResourceException, ResourceNotFoundException {
		if (!criterio.getDescricao().isEmpty()) {
			if (!existsById(criterio.getId())) {
				throw new ResourceNotFoundException("Criterio não encontrado com o id: " + criterio.getId());
			}
			criterio.setDataUltimaAlteracao(Calendar.getInstance().getTime());
			criterioRepository.save(criterio);
		} else {
			BadResourceException exe = new BadResourceException("Erro ao salvar criterio");
			exe.addErrorMessage("Criterio esta vazio ou nulo");
			throw exe;
		}
	}
	
	public void deleteById(Long id) throws ResourceNotFoundException {
		if(!existsById(id)) {
			throw new ResourceNotFoundException("Criterio não encontrado com o id: " + id);
		} else {
			criterioRepository.deleteById(id);
		}
	
	}  public Long count() {
		return criterioRepository.count();
	}
	
}

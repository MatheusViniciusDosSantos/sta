package br.com.sta.service;

import java.util.Calendar;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import br.com.sta.domain.SubmissaoCriterio;
import br.com.sta.dto.SubmissaoCriterioDTO;
import br.com.sta.exception.BadResourceException;
import br.com.sta.exception.ResourceAlreadyExistsException;
import br.com.sta.exception.ResourceNotFoundException;
import br.com.sta.repository.SubmissaoCriterioRepository;

@Service
public class SubmissaoCriterioService {
	
	@Autowired
	private SubmissaoCriterioRepository submissaoCriterioRepository;
	
	private boolean existsById(Long id) {
		return submissaoCriterioRepository.existsById(id);
	}
	
	public SubmissaoCriterio findById(Long id) throws ResourceNotFoundException {
		SubmissaoCriterio submissaoCriterio = submissaoCriterioRepository.findById(id).orElse(null);
		
		if(submissaoCriterio == null) {
			throw new ResourceNotFoundException("SubmissaoCriterio não encontrado com o id: " + id);
		} else {
			return submissaoCriterio;
		}
	}
	
	public Page<SubmissaoCriterioDTO> findAll(Pageable pageable) {
		
		return new SubmissaoCriterioDTO().converterListaSubmissaoCriterioDTO(submissaoCriterioRepository.findAll(pageable)) ;
	}
	
	public Page<SubmissaoCriterioDTO> findAllByIdSubmissao(Long id, Pageable page) {
		Page<SubmissaoCriterio> submissaoCriterios = submissaoCriterioRepository.findBySubmissao(id, page);
		return new SubmissaoCriterioDTO().converterListaSubmissaoCriterioDTO(submissaoCriterios);
	}

	public Page<SubmissaoCriterioDTO> findAllByIdCriterio(Long id, Pageable page) {
		Page<SubmissaoCriterio> submissaoCriterios = submissaoCriterioRepository.findByCriterio(id, page);
		return new SubmissaoCriterioDTO().converterListaSubmissaoCriterioDTO(submissaoCriterios);
	}
	
	public SubmissaoCriterio save(SubmissaoCriterio submissaoCriterio) throws BadResourceException, ResourceAlreadyExistsException {
		if (submissaoCriterio.getSubmissao() != null && submissaoCriterio.getCriterio() != null) {
			if(existsById(submissaoCriterio.getId())) {
				throw new ResourceAlreadyExistsException("SubmissaoCriterio com id: " + submissaoCriterio.getId() + " já existe.");
			}			
			submissaoCriterio.setStatus('A');
			submissaoCriterio.setDataCadastro(Calendar.getInstance().getTime());
			SubmissaoCriterio submissaoCriterioNovo = submissaoCriterioRepository.save(submissaoCriterio);			
			return submissaoCriterioNovo;
		} else {
			BadResourceException exe = new BadResourceException("Erro ao salvar submissaoCriterio");
			exe.addErrorMessage("SubmissaoCriterio esta vazio ou nulo");
			throw exe;
		}		
	}
	
	public void update(SubmissaoCriterio submissaoCriterio) throws BadResourceException, ResourceNotFoundException {
		if (submissaoCriterio.getSubmissao() != null && submissaoCriterio.getCriterio() != null) {
			if (!existsById(submissaoCriterio.getId())) {
				throw new ResourceNotFoundException("SubmissaoCriterio não encontrado com o id: " + submissaoCriterio.getId());
			}
			submissaoCriterio.setDataUltimaAlteracao(Calendar.getInstance().getTime());
		} else {
			BadResourceException exe = new BadResourceException("Erro ao salvar submissaoCriterio");
			exe.addErrorMessage("SubmissaoCriterio esta vazio ou nulo");
			throw exe;
		}
	}
	
	public void deleteById(Long id) throws ResourceNotFoundException {
		if(!existsById(id)) {
			throw new ResourceNotFoundException("SubmissaoCriterio não encontrado com o id: " + id);
		} else {
			submissaoCriterioRepository.deleteById(id);
		}
	
	}  public Long count() {
		return submissaoCriterioRepository.count();
	}
	
}

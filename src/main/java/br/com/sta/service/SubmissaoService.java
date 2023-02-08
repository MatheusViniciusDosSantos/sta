package br.com.sta.service;

import java.util.Calendar;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import br.com.sta.domain.Submissao;
import br.com.sta.dto.SubmissaoDTO;
import br.com.sta.exception.BadResourceException;
import br.com.sta.exception.ResourceAlreadyExistsException;
import br.com.sta.exception.ResourceNotFoundException;
import br.com.sta.repository.SubmissaoRepository;

@Service
public class SubmissaoService {
	
	@Autowired
	private SubmissaoRepository submissaoRepository;
	
	private boolean existsById(Long id) {
		return submissaoRepository.existsById(id);
	}
	
	public Submissao findById(Long id) throws ResourceNotFoundException {
		Submissao submissao = submissaoRepository.findById(id).orElse(null);
		
		if(submissao == null) {
			throw new ResourceNotFoundException("Submissao não encontrado com o id: " + id);
		} else {
			return submissao;
		}
	}
	
	public Page<SubmissaoDTO> findAll(Pageable pageable) {
		
		return new SubmissaoDTO().converterListaSubmissaoDTO(submissaoRepository.findAll(pageable)) ;
	}
	
	public Page<SubmissaoDTO> findAllByResultado(String descricao, Pageable page) {
		Page<Submissao> submissoes = submissaoRepository.findByResultado(descricao, page);
		return new SubmissaoDTO().converterListaSubmissaoDTO(submissoes);
	}
	
	public Submissao save(Submissao submissao) throws BadResourceException, ResourceAlreadyExistsException {
		if(!submissao.getResultado().isEmpty()) {
			if(existsById(submissao.getId())) {
				throw new ResourceAlreadyExistsException("Submissao com id: " + submissao.getId() + " já existe.");
			}			
			submissao.setStatus('A');
			submissao.setDataCadastro(Calendar.getInstance().getTime());
			Submissao submissaoNovo = submissaoRepository.save(submissao);
			return submissaoNovo;
		} else {
			BadResourceException exe = new BadResourceException("Erro ao salvar submissao");
			exe.addErrorMessage("Submissao esta vazia ou nula");
			throw exe;
		}
		
		
	}
	
	public void update(Submissao submissao) throws BadResourceException, ResourceNotFoundException {
		if (!submissao.getResultado().isEmpty()) {
			if (!existsById(submissao.getId())) {
				throw new ResourceNotFoundException("Submissao não encontrada com o id: " + submissao.getId());
			}
			submissao.setDataUltimaAlteracao(Calendar.getInstance().getTime());
			submissaoRepository.save(submissao);
		} else {
			BadResourceException exe = new BadResourceException("Erro ao salvar submissao");
			exe.addErrorMessage("Submissao esta vazia ou nula");
			throw exe;
		}
	}
	
	public void deleteById(Long id) throws ResourceNotFoundException {
		if(!existsById(id)) {
			throw new ResourceNotFoundException("Submissao não encontrada com o id: " + id);
		} else {
			submissaoRepository.deleteById(id);
		}
	
	}  public Long count() {
		return submissaoRepository.count();
	}
	
}

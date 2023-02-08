package br.com.sta.service;

import java.util.Calendar;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import br.com.sta.domain.TrabalhoSubmissao;
import br.com.sta.dto.TrabalhoSubmissaoDTO;
import br.com.sta.exception.BadResourceException;
import br.com.sta.exception.ResourceAlreadyExistsException;
import br.com.sta.exception.ResourceNotFoundException;
import br.com.sta.repository.TrabalhoSubmissaoRepository;

@Service
public class TrabalhoSubmissaoService {
	
	@Autowired
	private TrabalhoSubmissaoRepository trabalhoSubmissaoRepository;
	
	private boolean existsById(Long id) {
		return trabalhoSubmissaoRepository.existsById(id);
	}
	
	public TrabalhoSubmissao findById(Long id) throws ResourceNotFoundException {
		TrabalhoSubmissao trabalhoSubmissao = trabalhoSubmissaoRepository.findById(id).orElse(null);
		
		if(trabalhoSubmissao == null) {
			throw new ResourceNotFoundException("TrabalhoSubmissao não encontrado com o id: " + id);
		} else {
			return trabalhoSubmissao;
		}
	}
	
	public Page<TrabalhoSubmissaoDTO> findAll(Pageable pageable) {
		
		return new TrabalhoSubmissaoDTO().converterListaTrabalhoSubmissaoDTO(trabalhoSubmissaoRepository.findAll(pageable)) ;
	}
	
	public Page<TrabalhoSubmissaoDTO> findAllByIdTrabalho(Long id, Pageable page) {
		Page<TrabalhoSubmissao> trabalhoSubmissaos = trabalhoSubmissaoRepository.findByTrabalho(id, page);
		return new TrabalhoSubmissaoDTO().converterListaTrabalhoSubmissaoDTO(trabalhoSubmissaos);
	}

	public Page<TrabalhoSubmissaoDTO> findAllByIdSubmissao(Long id, Pageable page) {
		Page<TrabalhoSubmissao> trabalhoSubmissaos = trabalhoSubmissaoRepository.findBySubmissao(id, page);
		return new TrabalhoSubmissaoDTO().converterListaTrabalhoSubmissaoDTO(trabalhoSubmissaos);
	}
	
	public TrabalhoSubmissao save(TrabalhoSubmissao trabalhoSubmissao) throws BadResourceException, ResourceAlreadyExistsException {
		if (trabalhoSubmissao.getTrabalho() != null && trabalhoSubmissao.getSubmissao() != null) {
			if(existsById(trabalhoSubmissao.getId())) {
				throw new ResourceAlreadyExistsException("TrabalhoSubmissao com id: " + trabalhoSubmissao.getId() + " já existe.");
			}			
			trabalhoSubmissao.setStatus('A');
			trabalhoSubmissao.setDataCadastro(Calendar.getInstance().getTime());
			TrabalhoSubmissao trabalhoSubmissaoNovo = trabalhoSubmissaoRepository.save(trabalhoSubmissao);
			return trabalhoSubmissaoNovo;
		} else {
			BadResourceException exe = new BadResourceException("Erro ao salvar trabalhoSubmissao");
			exe.addErrorMessage("TrabalhoSubmissao esta vazio ou nulo");
			throw exe;
		}		
	}
	
	public void update(TrabalhoSubmissao trabalhoSubmissao) throws BadResourceException, ResourceNotFoundException {
		if (trabalhoSubmissao.getTrabalho() != null && trabalhoSubmissao.getSubmissao() != null) {
			if (!existsById(trabalhoSubmissao.getId())) {
				throw new ResourceNotFoundException("TrabalhoSubmissao não encontrado com o id: " + trabalhoSubmissao.getId());
			}
			trabalhoSubmissao.setDataUltimaAlteracao(Calendar.getInstance().getTime());
			trabalhoSubmissaoRepository.save(trabalhoSubmissao);
		} else {
			BadResourceException exe = new BadResourceException("Erro ao salvar trabalhoSubmissao");
			exe.addErrorMessage("TrabalhoSubmissao esta vazio ou nulo");
			throw exe;
		}
	}
	
	public void deleteById(Long id) throws ResourceNotFoundException {
		if(!existsById(id)) {
			throw new ResourceNotFoundException("TrabalhoSubmissao não encontrado com o id: " + id);
		} else {
			trabalhoSubmissaoRepository.deleteById(id);
		}
	
	}  public Long count() {
		return trabalhoSubmissaoRepository.count();
	}
	
}

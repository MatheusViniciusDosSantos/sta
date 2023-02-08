package br.com.sta.service;

import java.util.Calendar;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import br.com.sta.domain.AvaliadorSubmissao;
import br.com.sta.dto.AvaliadorSubmissaoDTO;
import br.com.sta.exception.BadResourceException;
import br.com.sta.exception.ResourceAlreadyExistsException;
import br.com.sta.exception.ResourceNotFoundException;
import br.com.sta.repository.AvaliadorSubmissaoRepository;

@Service
public class AvaliadorSubmissaoService {
	
	@Autowired
	private AvaliadorSubmissaoRepository avaliadorSubmissaoRepository;
	
	private boolean existsById(Long id) {
		return avaliadorSubmissaoRepository.existsById(id);
	}
	
	public AvaliadorSubmissao findById(Long id) throws ResourceNotFoundException {
		AvaliadorSubmissao avaliadorSubmissao = avaliadorSubmissaoRepository.findById(id).orElse(null);
		
		if(avaliadorSubmissao == null) {
			throw new ResourceNotFoundException("AvaliadorSubmissao não encontrado com o id: " + id);
		} else {
			return avaliadorSubmissao;
		}
	}
	
	public Page<AvaliadorSubmissaoDTO> findAll(Pageable pageable) {
		
		return new AvaliadorSubmissaoDTO().converterListaAvaliadorSubmissaoDTO(avaliadorSubmissaoRepository.findAll(pageable)) ;
	}
	
	public Page<AvaliadorSubmissaoDTO> findAllByIdAvaliador(Long id, Pageable page) {
		Page<AvaliadorSubmissao> avaliadorSubmissaos = avaliadorSubmissaoRepository.findByAvaliador(id, page);
		return new AvaliadorSubmissaoDTO().converterListaAvaliadorSubmissaoDTO(avaliadorSubmissaos);
	}

	public Page<AvaliadorSubmissaoDTO> findAllByIdTrabalhoSubmissao(Long id, Pageable page) {
		Page<AvaliadorSubmissao> avaliadorSubmissaos = avaliadorSubmissaoRepository.findByTrabalhoSubmissao(id, page);
		return new AvaliadorSubmissaoDTO().converterListaAvaliadorSubmissaoDTO(avaliadorSubmissaos);
	}
	
	public AvaliadorSubmissao save(AvaliadorSubmissao avaliadorSubmissao) throws BadResourceException, ResourceAlreadyExistsException {
		if (avaliadorSubmissao.getAvaliador() != null && avaliadorSubmissao.getTrabalhoSubmissao() != null) {
			if(existsById(avaliadorSubmissao.getId())) {
				throw new ResourceAlreadyExistsException("AvaliadorSubmissao com id: " + avaliadorSubmissao.getId() + " já existe.");
			}			
			avaliadorSubmissao.setStatus('A');
			avaliadorSubmissao.setDataCadastro(Calendar.getInstance().getTime());
			AvaliadorSubmissao avaliadorSubmissaoNovo = avaliadorSubmissaoRepository.save(avaliadorSubmissao);
			return avaliadorSubmissaoNovo;
		} else {
			BadResourceException exe = new BadResourceException("Erro ao salvar avaliadorSubmissao");
			exe.addErrorMessage("AvaliadorSubmissao esta vazio ou nulo");
			throw exe;
		}		
	}
	
	public void update(AvaliadorSubmissao avaliadorSubmissao) throws BadResourceException, ResourceNotFoundException {
		if (avaliadorSubmissao.getAvaliador() != null && avaliadorSubmissao.getTrabalhoSubmissao() != null) {
			if (!existsById(avaliadorSubmissao.getId())) {
				throw new ResourceNotFoundException("AvaliadorSubmissao não encontrado com o id: " + avaliadorSubmissao.getId());
			}
			avaliadorSubmissao.setDataUltimaAlteracao(Calendar.getInstance().getTime());
			avaliadorSubmissaoRepository.save(avaliadorSubmissao);
		} else {
			BadResourceException exe = new BadResourceException("Erro ao salvar avaliadorSubmissao");
			exe.addErrorMessage("AvaliadorSubmissao esta vazio ou nulo");
			throw exe;
		}
	}
	
	public void deleteById(Long id) throws ResourceNotFoundException {
		if(!existsById(id)) {
			throw new ResourceNotFoundException("AvaliadorSubmissao não encontrado com o id: " + id);
		} else {
			avaliadorSubmissaoRepository.deleteById(id);
		}
	
	}  public Long count() {
		return avaliadorSubmissaoRepository.count();
	}
	
}

package br.com.sta.service;

import java.util.Calendar;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import br.com.sta.domain.ResultadoSubmissao;
import br.com.sta.dto.ResultadoSubmissaoDTO;
import br.com.sta.exception.BadResourceException;
import br.com.sta.exception.ResourceAlreadyExistsException;
import br.com.sta.exception.ResourceNotFoundException;
import br.com.sta.repository.ResultadoSubmissaoRepository;

@Service
public class ResultadoSubmissaoService {
	
	@Autowired
	private ResultadoSubmissaoRepository resultadoSubmissaoRepository;
	
	private boolean existsById(Long id) {
		return resultadoSubmissaoRepository.existsById(id);
	}
	
	public ResultadoSubmissao findById(Long id) throws ResourceNotFoundException {
		ResultadoSubmissao resultadoSubmissao = resultadoSubmissaoRepository.findById(id).orElse(null);
		
		if(resultadoSubmissao == null) {
			throw new ResourceNotFoundException("ResultadoSubmissao não encontrado com o id: " + id);
		} else {
			return resultadoSubmissao;
		}
	}
	
	public Page<ResultadoSubmissaoDTO> findAll(Pageable pageable) {
		
		return new ResultadoSubmissaoDTO().converterListaResultadoSubmissaoDTO(resultadoSubmissaoRepository.findAll(pageable)) ;
	}
	
	public Page<ResultadoSubmissaoDTO> findAllByIdCriterio(Long id, Pageable page) {
		Page<ResultadoSubmissao> resultadoSubmissoes = resultadoSubmissaoRepository.findByCriterio(id, page);
		return new ResultadoSubmissaoDTO().converterListaResultadoSubmissaoDTO(resultadoSubmissoes);
	}

	public Page<ResultadoSubmissaoDTO> findAllByIdAvaliadorSubmissao(Long id, Pageable page) {
		Page<ResultadoSubmissao> resultadoSubmissoes = resultadoSubmissaoRepository.findByAvaliadorSubmissao(id, page);
		return new ResultadoSubmissaoDTO().converterListaResultadoSubmissaoDTO(resultadoSubmissoes);
	}
	
	public ResultadoSubmissao save(ResultadoSubmissao resultadoSubmissao) throws BadResourceException, ResourceAlreadyExistsException {
		if (resultadoSubmissao.getCriterio() != null && resultadoSubmissao.getAvaliadorSubmissao() != null) {
			if(existsById(resultadoSubmissao.getId())) {
				throw new ResourceAlreadyExistsException("ResultadoSubmissao com id: " + resultadoSubmissao.getId() + " já existe.");
			}			
			resultadoSubmissao.setStatus('A');
			resultadoSubmissao.setDataCadastro(Calendar.getInstance().getTime());
			ResultadoSubmissao resultadoSubmissaoNovo = resultadoSubmissaoRepository.save(resultadoSubmissao);
			return resultadoSubmissaoNovo;
		} else {
			BadResourceException exe = new BadResourceException("Erro ao salvar resultadoSubmissao");
			exe.addErrorMessage("ResultadoSubmissao esta vazio ou nulo");
			throw exe;
		}		
	}
	
	public void update(ResultadoSubmissao resultadoSubmissao) throws BadResourceException, ResourceNotFoundException {
		if (resultadoSubmissao.getCriterio() != null && resultadoSubmissao.getAvaliadorSubmissao() != null) {
			if (!existsById(resultadoSubmissao.getId())) {
				throw new ResourceNotFoundException("ResultadoSubmissao não encontrado com o id: " + resultadoSubmissao.getId());
			}
			resultadoSubmissao.setDataUltimaAlteracao(Calendar.getInstance().getTime());
			resultadoSubmissaoRepository.save(resultadoSubmissao);
		} else {
			BadResourceException exe = new BadResourceException("Erro ao salvar resultadoSubmissao");
			exe.addErrorMessage("ResultadoSubmissao esta vazio ou nulo");
			throw exe;
		}
	}
	
	public void deleteById(Long id) throws ResourceNotFoundException {
		if(!existsById(id)) {
			throw new ResourceNotFoundException("ResultadoSubmissao não encontrado com o id: " + id);
		} else {
			resultadoSubmissaoRepository.deleteById(id);
		}
	
	}  public Long count() {
		return resultadoSubmissaoRepository.count();
	}
	
}

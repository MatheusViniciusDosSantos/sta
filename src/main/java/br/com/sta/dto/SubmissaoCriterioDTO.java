package br.com.sta.dto;

import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;

import br.com.sta.domain.SubmissaoCriterio;
import lombok.Data;

@Data
public class SubmissaoCriterioDTO {
	private long id;
	private SubmissaoDTO submissaoDTO;
	private CriterioDTO criterioDTO;
	
	public SubmissaoCriterioDTO converter(SubmissaoCriterio submissaoCriterio) {
		SubmissaoCriterioDTO submissaoCriterioDTO = new SubmissaoCriterioDTO();
		BeanUtils.copyProperties(submissaoCriterio, submissaoCriterioDTO);
		submissaoCriterioDTO.setSubmissaoDTO(submissaoDTO.converter(submissaoCriterio.getSubmissao()));
		submissaoCriterioDTO.setCriterioDTO(criterioDTO.converter(submissaoCriterio.getCriterio()));
		return submissaoCriterioDTO;
	}
	
	public Page<SubmissaoCriterioDTO> converterListaSubmissaoCriterioDTO(Page<SubmissaoCriterio> pageSubmissaoCriterio) {
		Page<SubmissaoCriterioDTO> listaDTO = pageSubmissaoCriterio.map(this::converter);
		return listaDTO;
	}
	
}

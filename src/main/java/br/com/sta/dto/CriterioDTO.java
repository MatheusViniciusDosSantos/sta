package br.com.sta.dto;

import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;

import br.com.sta.domain.Criterio;
import lombok.Data;

@Data
public class CriterioDTO {
	private long id;
	private String descricao;
	
	public CriterioDTO converter(Criterio criterio) {
		CriterioDTO criterioDTO = new CriterioDTO();
		BeanUtils.copyProperties(criterio, criterioDTO);
		return criterioDTO;
	}
	
	public Page<CriterioDTO> converterListaCriterioDTO(Page<Criterio> pageCriterio) {
		Page<CriterioDTO> listaDTO = pageCriterio.map(this::converter);
		return listaDTO;
	}
	
}

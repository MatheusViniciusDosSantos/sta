package br.com.sta.dto;

import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;

import br.com.sta.domain.AreaAvaliador;
import lombok.Data;

@Data
public class AreaAvaliadorDTO {
	private long id;
	private AreaDTO areaDTO;
	private AvaliadorDTO avaliadorDTO;
	
	public AreaAvaliadorDTO converter(AreaAvaliador areaAvaliador) {
		AreaAvaliadorDTO areaAvaliadorDTO = new AreaAvaliadorDTO();
		BeanUtils.copyProperties(areaAvaliador, areaAvaliadorDTO);
		areaAvaliadorDTO.setAreaDTO(areaDTO.converter(areaAvaliador.getArea()));
		areaAvaliadorDTO.setAvaliadorDTO(avaliadorDTO.converter(areaAvaliador.getAvaliador()));
		return areaAvaliadorDTO;
	}
	
	public Page<AreaAvaliadorDTO> converterListaAreaAvaliadorDTO(Page<AreaAvaliador> pageAreaAvaliador) {
		Page<AreaAvaliadorDTO> listaDTO = pageAreaAvaliador.map(this::converter);
		return listaDTO;
	}
	
}

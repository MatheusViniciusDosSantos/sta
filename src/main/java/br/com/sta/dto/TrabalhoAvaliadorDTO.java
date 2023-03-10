package br.com.sta.dto;

import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;

import br.com.sta.domain.TrabalhoAvaliador;
import lombok.Data;

@Data
public class TrabalhoAvaliadorDTO {
	private long id;
	private TrabalhoDTO trabalhoDTO;
	private AvaliadorDTO avaliadorDTO;
	private ResultadoSubmissaoDTO resultadoSubmissaoDTO;
	
	public TrabalhoAvaliadorDTO converter(TrabalhoAvaliador trabalhoAvaliador) {
		TrabalhoAvaliadorDTO trabalhoAvaliadorDTO = new TrabalhoAvaliadorDTO();
		BeanUtils.copyProperties(trabalhoAvaliador, trabalhoAvaliadorDTO);
		trabalhoAvaliadorDTO.setTrabalhoDTO(trabalhoDTO.converter(trabalhoAvaliador.getTrabalho()));
		trabalhoAvaliadorDTO.setAvaliadorDTO(avaliadorDTO.converter(trabalhoAvaliador.getAvaliador()));
		trabalhoAvaliadorDTO.setResultadoSubmissaoDTO(resultadoSubmissaoDTO.converter(trabalhoAvaliador.getResultadoSubmissao()));
		return trabalhoAvaliadorDTO;
	}
	
	public Page<TrabalhoAvaliadorDTO> converterListaTrabalhoAvaliadorDTO(Page<TrabalhoAvaliador> pageTrabalhoAvaliador) {
		Page<TrabalhoAvaliadorDTO> listaDTO = pageTrabalhoAvaliador.map(this::converter);
		return listaDTO;
	}
	
}

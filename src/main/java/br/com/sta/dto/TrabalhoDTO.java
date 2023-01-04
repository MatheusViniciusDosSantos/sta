package br.com.sta.dto;

import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;

import br.com.sta.domain.Trabalho;
import lombok.Data;

@Data
public class TrabalhoDTO {
	private long id;
	private String titulo;
	private CategoriaDTO categoriaDTO;
	
	public TrabalhoDTO converter(Trabalho trabalho) {
		TrabalhoDTO trabalhoDTO = new TrabalhoDTO();
		BeanUtils.copyProperties(trabalho, trabalhoDTO);
		trabalhoDTO.setCategoriaDTO(categoriaDTO.converter(trabalho.getCategoria()));
		return trabalhoDTO;
	}
	
	public Page<TrabalhoDTO> converterListaTrabalhoDTO(Page<Trabalho> pageTrabalho) {
		Page<TrabalhoDTO> listaDTO = pageTrabalho.map(this::converter);
		return listaDTO;
	}
	
}

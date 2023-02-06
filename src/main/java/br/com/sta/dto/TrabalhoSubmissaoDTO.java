package br.com.sta.dto;

import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;

import br.com.sta.domain.TrabalhoSubmissao;
import lombok.Data;

@Data
public class TrabalhoSubmissaoDTO {
	private long id;
	private TrabalhoDTO trabalhoDTO;
	private SubmissaoDTO submissaoDTO;
	
	public TrabalhoSubmissaoDTO converter(TrabalhoSubmissao trabalhoSubmissao) {
		TrabalhoSubmissaoDTO trabalhoSubmissaoDTO = new TrabalhoSubmissaoDTO();
		BeanUtils.copyProperties(trabalhoSubmissao, trabalhoSubmissaoDTO);
		trabalhoSubmissaoDTO.setTrabalhoDTO(trabalhoDTO.converter(trabalhoSubmissao.getTrabalho()));
		trabalhoSubmissaoDTO.setSubmissaoDTO(submissaoDTO.converter(trabalhoSubmissao.getSubmissao()));
		return trabalhoSubmissaoDTO;
	}
	
	public Page<TrabalhoSubmissaoDTO> converterListaTrabalhoSubmissaoDTO(Page<TrabalhoSubmissao> pageTrabalhoSubmissao) {
		Page<TrabalhoSubmissaoDTO> listaDTO = pageTrabalhoSubmissao.map(this::converter);
		return listaDTO;
	}
	
}

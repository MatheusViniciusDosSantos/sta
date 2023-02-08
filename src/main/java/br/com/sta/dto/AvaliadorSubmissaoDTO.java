package br.com.sta.dto;

import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;

import br.com.sta.domain.AvaliadorSubmissao;
import lombok.Data;

@Data
public class AvaliadorSubmissaoDTO {
	private long id;
	private AvaliadorDTO avaliadorDTO;
	private TrabalhoSubmissaoDTO trabalhoSubmissaoDTO;
	
	public AvaliadorSubmissaoDTO converter(AvaliadorSubmissao avaliadorSubmissao) {
		AvaliadorSubmissaoDTO avaliadorSubmissaoDTO = new AvaliadorSubmissaoDTO();
		BeanUtils.copyProperties(avaliadorSubmissao, avaliadorSubmissaoDTO);
		avaliadorSubmissaoDTO.setAvaliadorDTO(avaliadorDTO.converter(avaliadorSubmissao.getAvaliador()));
		avaliadorSubmissaoDTO.setTrabalhoSubmissaoDTO(trabalhoSubmissaoDTO.converter(avaliadorSubmissao.getTrabalhoSubmissao()));
		return avaliadorSubmissaoDTO;
	}
	
	public Page<AvaliadorSubmissaoDTO> converterListaAvaliadorSubmissaoDTO(Page<AvaliadorSubmissao> pageAvaliadorSubmissao) {
		Page<AvaliadorSubmissaoDTO> listaDTO = pageAvaliadorSubmissao.map(this::converter);
		return listaDTO;
	}
	
}

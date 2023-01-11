package br.com.sta.dto;

import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;

import br.com.sta.domain.Submissao;
import lombok.Data;

@Data
public class SubmissaoDTO {
	private long id;
	private String resultado;
	
	public SubmissaoDTO converter(Submissao submissao) {
		SubmissaoDTO submissaoDTO = new SubmissaoDTO();
		BeanUtils.copyProperties(submissao, submissaoDTO);
		return submissaoDTO;
	}
	
	public Page<SubmissaoDTO> converterListaSubmissaoDTO(Page<Submissao> pageSubmissao) {
		Page<SubmissaoDTO> listaDTO = pageSubmissao.map(this::converter);
		return listaDTO;
	}
	
}

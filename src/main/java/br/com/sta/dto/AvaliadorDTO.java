package br.com.sta.dto;

import java.util.Date;

import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;

import br.com.sta.domain.Avaliador;
import lombok.Data;

@Data
public class AvaliadorDTO {
	private long id;
	private String nome;
	private String cpf;
	private String email;
	private Date dataCadastro;
	
	public AvaliadorDTO converter(Avaliador avaliador) {
		AvaliadorDTO avaliadorDTO = new AvaliadorDTO();
		BeanUtils.copyProperties(avaliador, avaliadorDTO);
		return avaliadorDTO;
	}
	
	public Page<AvaliadorDTO> converterListaAvaliadorDTO(Page<Avaliador> pageAvaliador) {
		Page<AvaliadorDTO> listaDTO = pageAvaliador.map(this::converter);
		return listaDTO;
	}
	
}

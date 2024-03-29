package br.com.sta.dto;

import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;

import br.com.sta.domain.Permissao;
import lombok.Data;

@Data
public class PermissaoDTO {
	private long id;
	private String descricao;
	
	public PermissaoDTO converter(Permissao permissao) {
		PermissaoDTO permissaoDTO = new PermissaoDTO();
		BeanUtils.copyProperties(permissao, permissaoDTO);
		return permissaoDTO;
	}
	
	public Page<PermissaoDTO> converterListaPermissaoDTO(Page<Permissao> pagePermissao) {
		Page<PermissaoDTO> listaDTO = pagePermissao.map(this::converter);
		return listaDTO;
	}
	
}

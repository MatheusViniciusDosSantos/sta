package br.com.sta.dto;

import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;

import br.com.sta.domain.ResultadoSubmissao;
import lombok.Data;

@Data
public class ResultadoSubmissaoDTO {
	private long id;
	private CriterioDTO criterioDTO;
	private AvaliadorSubmissaoDTO avaliadorSubmissaoDTO;
	private String resultado;
	
	public ResultadoSubmissaoDTO converter(ResultadoSubmissao resultadoSubmissao) {
		ResultadoSubmissaoDTO resultadoSubmissaoDTO = new ResultadoSubmissaoDTO();
		BeanUtils.copyProperties(resultadoSubmissao, resultadoSubmissaoDTO);
		resultadoSubmissaoDTO.setCriterioDTO(criterioDTO.converter(resultadoSubmissao.getCriterio()));
		resultadoSubmissaoDTO.setAvaliadorSubmissaoDTO(avaliadorSubmissaoDTO.converter(resultadoSubmissao.getAvaliadorSubmissao()));
		return resultadoSubmissaoDTO;
	}
	
	public Page<ResultadoSubmissaoDTO> converterListaResultadoSubmissaoDTO(Page<ResultadoSubmissao> pageResultadoSubmissao) {
		Page<ResultadoSubmissaoDTO> listaDTO = pageResultadoSubmissao.map(this::converter);
		return listaDTO;
	}
	
}

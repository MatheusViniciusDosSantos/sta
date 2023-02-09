package br.com.sta.domain;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Table(name = "resultado_submissao")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@EqualsAndHashCode(callSuper=true)
@Data
public class ResultadoSubmissao extends Auditoria implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private long id;

	@ManyToOne
	@Schema(description = "Criterio", example = "")
	@JoinColumn(name = "id_criterio")
	private Criterio criterio;

	@ManyToOne
	@Schema(description = "AvaliadorSubmissao", example = "")
	@JoinColumn(name = "id_avaliador_submissao")
	private AvaliadorSubmissao avaliadorSubmissao;

	private String resultado;
	
	public ResultadoSubmissao() {}

}

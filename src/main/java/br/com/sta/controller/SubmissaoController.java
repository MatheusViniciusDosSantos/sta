package br.com.sta.controller;

import java.net.URI;
import java.net.URISyntaxException;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import br.com.sta.domain.Submissao;
import br.com.sta.dto.SubmissaoDTO;
import br.com.sta.exception.BadResourceException;
import br.com.sta.exception.ResourceAlreadyExistsException;
import br.com.sta.exception.ResourceNotFoundException;
import br.com.sta.service.SubmissaoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;

import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;

@RestController
@RequestMapping("/api")
@Tag(name = "submissao", description = "API de Submissao")
public class SubmissaoController {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	// private final int ROW_PER_PAGE = 5;
	
	@Autowired
	private SubmissaoService submissaoService;
	
	@Operation(summary = "Busca submissoes", description = "Buscar todos os submissoes, buscar submissoes por descricao", tags = {"submissao"})
	@GetMapping(value = "/submissao")
	@CrossOrigin("http://localhost:3000")
	public ResponseEntity<Page<SubmissaoDTO>> findAll(
			@Parameter(description = "Descrição para pesquisa", allowEmptyValue = true)
			@RequestBody(required=false) String descricao,
			@Parameter(description = "Paginação", example = "{\"page\":0,\"size\":1}", allowEmptyValue = true)
			 Pageable pageable)	{
		if(descricao.isEmpty()) {
			return ResponseEntity.ok(submissaoService.findAll(pageable));
		} else {
			return ResponseEntity.ok(submissaoService.findAllByResultado(descricao, pageable));
		}
	}
	
	@Operation(summary = "Busca ID", description = "Buscar submissao por ID", tags = {"submissao"})
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Sucesso",
					content = @Content(schema = @Schema(implementation = Submissao.class))),
			@ApiResponse(responseCode = "404", description = "Submissao não encontrado")
	})
	@GetMapping(value = "/submissao/{id}")
	@CrossOrigin("http://localhost:3000")
	public ResponseEntity<Submissao> findSubmissaoById(@PathVariable long id) {
		try {
			Submissao submissao = submissaoService.findById(id);
			return ResponseEntity.ok(submissao);
		} catch (ResourceNotFoundException ex) {
			logger.error(ex.getMessage());
			
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, ex.getMessage(), ex);
		}
	
	}
	
	@Operation(summary = "Adicionar submissao", description = "Adicionar novo submissao informado no banco de dados", tags = {"submissao"})
	@PostMapping(value = "/submissao")
	@CrossOrigin("http://localhost:3000")
	public ResponseEntity<SubmissaoDTO> addSubmissao(@RequestBody Submissao submissao) throws URISyntaxException {
		try {
			Submissao novoSubmissao = submissaoService.save(submissao);
			
			return ResponseEntity.created(new URI("/api/submissao" + novoSubmissao.getId())).body(new SubmissaoDTO().converter(submissao));
		} catch (ResourceAlreadyExistsException ex) {
			logger.error(ex.getMessage());
			return ResponseEntity.status(HttpStatus.CONFLICT).build();
		} catch (BadResourceException ex) {
			logger.error(ex.getMessage());
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
		}
	}
	
	@Operation(summary = "Alterar Submissao", description = "Alterar valores do submissao com id selecionado", tags = {"submissao"})
	@PutMapping(value = "/submissao/{id}")
	@CrossOrigin("http://localhost:3000")
	public ResponseEntity<Submissao> updateSubmissao(@Valid @RequestBody Submissao submissao, 
			@PathVariable long id) {
		try {
			submissao.setId(id);
			submissaoService.update(submissao);
			return ResponseEntity.ok().build();
		} catch (ResourceNotFoundException ex) {
			logger.error(ex.getMessage());
			return ResponseEntity.notFound().build();
		} catch (BadResourceException ex) {
			logger.error(ex.getMessage());
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
		}
		
	}
	
	@Operation(summary = "Deletar submissao", description = "Deletar submissao com o ID informado", tags = {"submissao"})
	@DeleteMapping(path = "/submissao/{id}")
	@CrossOrigin("http://localhost:3000")
	public ResponseEntity<Void> deleteSubmissaoById(@PathVariable long id) {
		try {
			submissaoService.deleteById(id);
			return ResponseEntity.ok().build();
		} catch (ResourceNotFoundException ex) {
			logger.error(ex.getMessage());
			throw new ResponseStatusException(
					HttpStatus.NOT_FOUND, ex.getMessage(), ex);
		}
	}
}

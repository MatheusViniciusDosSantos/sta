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

import br.com.sta.domain.TrabalhoSubmissao;
import br.com.sta.dto.TrabalhoSubmissaoDTO;
import br.com.sta.exception.BadResourceException;
import br.com.sta.exception.ResourceAlreadyExistsException;
import br.com.sta.exception.ResourceNotFoundException;
import br.com.sta.service.TrabalhoSubmissaoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;

import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;

@RestController
@RequestMapping("/api")
@Tag(name = "trabalhoSubmissao", description = "API de TrabalhoSubmissao")
public class TrabalhoSubmissaoController {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	// private final int ROW_PER_PAGE = 5;

	@Autowired
	private TrabalhoSubmissaoService trabalhoSubmissaoService;

	@Operation(summary = "Busca trabalhoSubmissaos", description = "Buscar todos os trabalhoSubmissaos, buscar trabalhoSubmissaos por descricao", tags = {
			"trabalhoSubmissao" })
	@GetMapping(value = "/trabalhoSubmissao")
	@CrossOrigin("http://localhost:3000")
	public ResponseEntity<Page<TrabalhoSubmissaoDTO>> findAll(
			@Parameter(description = "Descrição para pesquisa", allowEmptyValue = true) @RequestBody(required = false) String descricao,
			@Parameter(description = "Paginação", example = "{\"page\":0,\"size\":1}", allowEmptyValue = true) Pageable pageable) {
		return ResponseEntity.ok(trabalhoSubmissaoService.findAll(pageable));
	}

	@Operation(summary = "Busca ID", description = "Buscar trabalhoSubmissao por ID", tags = { "trabalhoSubmissao" })
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Sucesso", content = @Content(schema = @Schema(implementation = TrabalhoSubmissao.class))),
			@ApiResponse(responseCode = "404", description = "TrabalhoSubmissao não encontrado")
	})
	@GetMapping(value = "/trabalhoSubmissao/{id}")
	@CrossOrigin("http://localhost:3000")
	public ResponseEntity<TrabalhoSubmissao> findTrabalhoSubmissaoById(@PathVariable long id) {
		try {
			TrabalhoSubmissao trabalhoSubmissao = trabalhoSubmissaoService.findById(id);
			return ResponseEntity.ok(trabalhoSubmissao);
		} catch (ResourceNotFoundException ex) {
			logger.error(ex.getMessage());

			throw new ResponseStatusException(HttpStatus.NOT_FOUND, ex.getMessage(), ex);
		}

	}

	@Operation(summary = "Busca ID", description = "Buscar trabalhoSubmissao por ID do trabalho", tags = { "trabalhoSubmissao" })
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Sucesso", content = @Content(schema = @Schema(implementation = TrabalhoSubmissao.class))),
			@ApiResponse(responseCode = "404", description = "TrabalhoSubmissao não encontrado para este trabalho")
	})
	@GetMapping(value = "/trabalhoSubmissao/trabalho/{id}")
	@CrossOrigin("http://localhost:3000")
	public ResponseEntity<Page<TrabalhoSubmissaoDTO>> findTrabalhoSubmissaoByIdTrabalho(@PathVariable long id,
			@Parameter(description = "Paginação", example = "{\"page\":0,\"size\":1}", allowEmptyValue = true) Pageable pageable) {
		Page<TrabalhoSubmissaoDTO> trabalhoSubmissaos = trabalhoSubmissaoService.findAllByIdTrabalho(id, pageable);
		return ResponseEntity.ok(trabalhoSubmissaos);
	}

	@Operation(summary = "Busca ID", description = "Buscar trabalhoSubmissao por ID da submissao", tags = {
			"trabalhoSubmissao" })
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Sucesso", content = @Content(schema = @Schema(implementation = TrabalhoSubmissao.class))),
			@ApiResponse(responseCode = "404", description = "TrabalhoSubmissao não encontrado para este trabalho")
	})
	@GetMapping(value = "/trabalhoSubmissao/submissao/{id}")
	@CrossOrigin("http://localhost:3000")
	public ResponseEntity<Page<TrabalhoSubmissaoDTO>> findTrabalhoSubmissaoByIdSubmissao(@PathVariable long id,
			@Parameter(description = "Paginação", example = "{\"page\":0,\"size\":1}", allowEmptyValue = true) Pageable pageable) {
		Page<TrabalhoSubmissaoDTO> trabalhoSubmissaos = trabalhoSubmissaoService.findAllByIdSubmissao(id, pageable);
		return ResponseEntity.ok(trabalhoSubmissaos);
	}

	@Operation(summary = "Adicionar trabalhoSubmissao", description = "Adicionar novo trabalhoSubmissao informado no banco de dados", tags = {
			"trabalhoSubmissao" })
	@PostMapping(value = "/trabalhoSubmissao")
	@CrossOrigin("http://localhost:3000")
	public ResponseEntity<TrabalhoSubmissaoDTO> addTrabalhoSubmissao(@RequestBody TrabalhoSubmissao trabalhoSubmissao)
			throws URISyntaxException {
		try {
			TrabalhoSubmissao novoTrabalhoSubmissao = trabalhoSubmissaoService.save(trabalhoSubmissao);

			return ResponseEntity.created(new URI("/api/trabalhoSubmissao" + novoTrabalhoSubmissao.getId()))
					.body(new TrabalhoSubmissaoDTO().converter(trabalhoSubmissao));
		} catch (ResourceAlreadyExistsException ex) {
			logger.error(ex.getMessage());
			return ResponseEntity.status(HttpStatus.CONFLICT).build();
		} catch (BadResourceException ex) {
			logger.error(ex.getMessage());
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
		}
	}

	@Operation(summary = "Alterar TrabalhoSubmissao", description = "Alterar valores do trabalhoSubmissao com id selecionado", tags = {
			"trabalhoSubmissao" })
	@PutMapping(value = "/trabalhoSubmissao/{id}")
	@CrossOrigin("http://localhost:3000")
	public ResponseEntity<TrabalhoSubmissao> updateTrabalhoSubmissao(@Valid @RequestBody TrabalhoSubmissao trabalhoSubmissao,
			@PathVariable long id) {
		try {
			trabalhoSubmissao.setId(id);
			trabalhoSubmissaoService.update(trabalhoSubmissao);
			return ResponseEntity.ok().build();
		} catch (ResourceNotFoundException ex) {
			logger.error(ex.getMessage());
			return ResponseEntity.notFound().build();
		} catch (BadResourceException ex) {
			logger.error(ex.getMessage());
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
		}

	}

	@Operation(summary = "Deletar trabalhoSubmissao", description = "Deletar trabalhoSubmissao com o ID informado", tags = {
			"trabalhoSubmissao" })
	@DeleteMapping(path = "/trabalhoSubmissao/{id}")
	@CrossOrigin("http://localhost:3000")
	public ResponseEntity<Void> deleteTrabalhoSubmissaoById(@PathVariable long id) {
		try {
			trabalhoSubmissaoService.deleteById(id);
			return ResponseEntity.ok().build();
		} catch (ResourceNotFoundException ex) {
			logger.error(ex.getMessage());
			throw new ResponseStatusException(
					HttpStatus.NOT_FOUND, ex.getMessage(), ex);
		}
	}
}

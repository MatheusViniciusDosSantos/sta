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

import br.com.sta.domain.SubmissaoCriterio;
import br.com.sta.dto.SubmissaoCriterioDTO;
import br.com.sta.exception.BadResourceException;
import br.com.sta.exception.ResourceAlreadyExistsException;
import br.com.sta.exception.ResourceNotFoundException;
import br.com.sta.service.SubmissaoCriterioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;

import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;

@RestController
@RequestMapping("/api")
@Tag(name = "submissaoCriterio", description = "API de SubmissaoCriterio")
public class SubmissaoCriterioController {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	// private final int ROW_PER_PAGE = 5;

	@Autowired
	private SubmissaoCriterioService submissaoCriterioService;

	@Operation(summary = "Busca submissaoCriterios", description = "Buscar todos os submissaoCriterios, buscar submissaoCriterios por descricao", tags = {
			"submissaoCriterio" })
	@GetMapping(value = "/submissaoCriterio")
	@CrossOrigin("http://localhost:3000")
	public ResponseEntity<Page<SubmissaoCriterioDTO>> findAll(
			@Parameter(description = "Descrição para pesquisa", allowEmptyValue = true) @RequestBody(required = false) String descricao,
			@Parameter(description = "Paginação", example = "{\"page\":0,\"size\":1}", allowEmptyValue = true) Pageable pageable) {
		return ResponseEntity.ok(submissaoCriterioService.findAll(pageable));
	}

	@Operation(summary = "Busca ID", description = "Buscar submissaoCriterio por ID", tags = { "submissaoCriterio" })
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Sucesso", content = @Content(schema = @Schema(implementation = SubmissaoCriterio.class))),
			@ApiResponse(responseCode = "404", description = "SubmissaoCriterio não encontrado")
	})
	@GetMapping(value = "/submissaoCriterio/{id}")
	@CrossOrigin("http://localhost:3000")
	public ResponseEntity<SubmissaoCriterio> findSubmissaoCriterioById(@PathVariable long id) {
		try {
			SubmissaoCriterio submissaoCriterio = submissaoCriterioService.findById(id);
			return ResponseEntity.ok(submissaoCriterio);
		} catch (ResourceNotFoundException ex) {
			logger.error(ex.getMessage());

			throw new ResponseStatusException(HttpStatus.NOT_FOUND, ex.getMessage(), ex);
		}

	}

	@Operation(summary = "Busca ID", description = "Buscar submissaoCriterio por ID do submissao", tags = { "submissaoCriterio" })
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Sucesso", content = @Content(schema = @Schema(implementation = SubmissaoCriterio.class))),
			@ApiResponse(responseCode = "404", description = "SubmissaoCriterio não encontrado para este submissao")
	})
	@GetMapping(value = "/submissaoCriterio/submissao/{id}")
	@CrossOrigin("http://localhost:3000")
	public ResponseEntity<Page<SubmissaoCriterioDTO>> findSubmissaoCriterioByIdSubmissao(@PathVariable long id,
			@Parameter(description = "Paginação", example = "{\"page\":0,\"size\":1}", allowEmptyValue = true) Pageable pageable) {
		Page<SubmissaoCriterioDTO> submissaoCriterios = submissaoCriterioService.findAllByIdSubmissao(id, pageable);
		return ResponseEntity.ok(submissaoCriterios);
	}

	@Operation(summary = "Busca ID", description = "Buscar submissaoCriterio por ID do criterio", tags = {
			"submissaoCriterio" })
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Sucesso", content = @Content(schema = @Schema(implementation = SubmissaoCriterio.class))),
			@ApiResponse(responseCode = "404", description = "SubmissaoCriterio não encontrado para este submissao")
	})
	@GetMapping(value = "/submissaoCriterio/criterio/{id}")
	@CrossOrigin("http://localhost:3000")
	public ResponseEntity<Page<SubmissaoCriterioDTO>> findSubmissaoCriterioByIdCriterio(@PathVariable long id,
			@Parameter(description = "Paginação", example = "{\"page\":0,\"size\":1}", allowEmptyValue = true) Pageable pageable) {
		Page<SubmissaoCriterioDTO> submissaoCriterios = submissaoCriterioService.findAllByIdCriterio(id, pageable);
		return ResponseEntity.ok(submissaoCriterios);
	}

	@Operation(summary = "Adicionar submissaoCriterio", description = "Adicionar novo submissaoCriterio informado no banco de dados", tags = {
			"submissaoCriterio" })
	@PostMapping(value = "/submissaoCriterio")
	@CrossOrigin("http://localhost:3000")
	public ResponseEntity<SubmissaoCriterioDTO> addSubmissaoCriterio(@RequestBody SubmissaoCriterio submissaoCriterio)
			throws URISyntaxException {
		try {
			SubmissaoCriterio novoSubmissaoCriterio = submissaoCriterioService.save(submissaoCriterio);

			return ResponseEntity.created(new URI("/api/submissaoCriterio" + novoSubmissaoCriterio.getId()))
					.body(new SubmissaoCriterioDTO().converter(submissaoCriterio));
		} catch (ResourceAlreadyExistsException ex) {
			logger.error(ex.getMessage());
			return ResponseEntity.status(HttpStatus.CONFLICT).build();
		} catch (BadResourceException ex) {
			logger.error(ex.getMessage());
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
		}
	}

	@Operation(summary = "Alterar SubmissaoCriterio", description = "Alterar valores do submissaoCriterio com id selecionado", tags = {
			"submissaoCriterio" })
	@PutMapping(value = "/submissaoCriterio/{id}")
	@CrossOrigin("http://localhost:3000")
	public ResponseEntity<SubmissaoCriterio> updateSubmissaoCriterio(@Valid @RequestBody SubmissaoCriterio submissaoCriterio,
			@PathVariable long id) {
		try {
			submissaoCriterio.setId(id);
			submissaoCriterioService.update(submissaoCriterio);
			return ResponseEntity.ok().build();
		} catch (ResourceNotFoundException ex) {
			logger.error(ex.getMessage());
			return ResponseEntity.notFound().build();
		} catch (BadResourceException ex) {
			logger.error(ex.getMessage());
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
		}

	}

	@Operation(summary = "Deletar submissaoCriterio", description = "Deletar submissaoCriterio com o ID informado", tags = {
			"submissaoCriterio" })
	@DeleteMapping(path = "/submissaoCriterio/{id}")
	@CrossOrigin("http://localhost:3000")
	public ResponseEntity<Void> deleteSubmissaoCriterioById(@PathVariable long id) {
		try {
			submissaoCriterioService.deleteById(id);
			return ResponseEntity.ok().build();
		} catch (ResourceNotFoundException ex) {
			logger.error(ex.getMessage());
			throw new ResponseStatusException(
					HttpStatus.NOT_FOUND, ex.getMessage(), ex);
		}
	}
}

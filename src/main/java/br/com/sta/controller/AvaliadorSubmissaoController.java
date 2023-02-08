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

import br.com.sta.domain.AvaliadorSubmissao;
import br.com.sta.dto.AvaliadorSubmissaoDTO;
import br.com.sta.exception.BadResourceException;
import br.com.sta.exception.ResourceAlreadyExistsException;
import br.com.sta.exception.ResourceNotFoundException;
import br.com.sta.service.AvaliadorSubmissaoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;

import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;

@RestController
@RequestMapping("/api")
@Tag(name = "avaliadorSubmissao", description = "API de AvaliadorSubmissao")
public class AvaliadorSubmissaoController {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	// private final int ROW_PER_PAGE = 5;

	@Autowired
	private AvaliadorSubmissaoService avaliadorSubmissaoService;

	@Operation(summary = "Busca avaliadorSubmissaos", description = "Buscar todos os avaliadorSubmissaos, buscar avaliadorSubmissaos por descricao", tags = {
			"avaliadorSubmissao" })
	@GetMapping(value = "/avaliadorSubmissao")
	@CrossOrigin("http://localhost:3000")
	public ResponseEntity<Page<AvaliadorSubmissaoDTO>> findAll(
			@Parameter(description = "Descrição para pesquisa", allowEmptyValue = true) @RequestBody(required = false) String descricao,
			@Parameter(description = "Paginação", example = "{\"page\":0,\"size\":1}", allowEmptyValue = true) Pageable pageable) {
		return ResponseEntity.ok(avaliadorSubmissaoService.findAll(pageable));
	}

	@Operation(summary = "Busca ID", description = "Buscar avaliadorSubmissao por ID", tags = { "avaliadorSubmissao" })
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Sucesso", content = @Content(schema = @Schema(implementation = AvaliadorSubmissao.class))),
			@ApiResponse(responseCode = "404", description = "AvaliadorSubmissao não encontrado")
	})
	@GetMapping(value = "/avaliadorSubmissao/{id}")
	@CrossOrigin("http://localhost:3000")
	public ResponseEntity<AvaliadorSubmissao> findAvaliadorSubmissaoById(@PathVariable long id) {
		try {
			AvaliadorSubmissao avaliadorSubmissao = avaliadorSubmissaoService.findById(id);
			return ResponseEntity.ok(avaliadorSubmissao);
		} catch (ResourceNotFoundException ex) {
			logger.error(ex.getMessage());

			throw new ResponseStatusException(HttpStatus.NOT_FOUND, ex.getMessage(), ex);
		}

	}

	@Operation(summary = "Busca ID", description = "Buscar avaliadorSubmissao por ID do avaliador", tags = { "avaliadorSubmissao" })
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Sucesso", content = @Content(schema = @Schema(implementation = AvaliadorSubmissao.class))),
			@ApiResponse(responseCode = "404", description = "AvaliadorSubmissao não encontrado para este avaliador")
	})
	@GetMapping(value = "/avaliadorSubmissao/avaliador/{id}")
	@CrossOrigin("http://localhost:3000")
	public ResponseEntity<Page<AvaliadorSubmissaoDTO>> findAvaliadorSubmissaoByIdAvaliador(@PathVariable long id,
			@Parameter(description = "Paginação", example = "{\"page\":0,\"size\":1}", allowEmptyValue = true) Pageable pageable) {
		Page<AvaliadorSubmissaoDTO> avaliadorSubmissaos = avaliadorSubmissaoService.findAllByIdAvaliador(id, pageable);
		return ResponseEntity.ok(avaliadorSubmissaos);
	}

	@Operation(summary = "Busca ID", description = "Buscar avaliadorSubmissao por ID da submissao", tags = {
			"avaliadorSubmissao" })
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Sucesso", content = @Content(schema = @Schema(implementation = AvaliadorSubmissao.class))),
			@ApiResponse(responseCode = "404", description = "AvaliadorSubmissao não encontrado para este avaliador")
	})
	@GetMapping(value = "/avaliadorSubmissao/trabalhoSubmissao/{id}")
	@CrossOrigin("http://localhost:3000")
	public ResponseEntity<Page<AvaliadorSubmissaoDTO>> findAvaliadorSubmissaoByIdSubmissao(@PathVariable long id,
			@Parameter(description = "Paginação", example = "{\"page\":0,\"size\":1}", allowEmptyValue = true) Pageable pageable) {
		Page<AvaliadorSubmissaoDTO> avaliadorSubmissaos = avaliadorSubmissaoService.findAllByIdTrabalhoSubmissao(id, pageable);
		return ResponseEntity.ok(avaliadorSubmissaos);
	}

	@Operation(summary = "Adicionar avaliadorSubmissao", description = "Adicionar novo avaliadorSubmissao informado no banco de dados", tags = {
			"avaliadorSubmissao" })
	@PostMapping(value = "/avaliadorSubmissao")
	@CrossOrigin("http://localhost:3000")
	public ResponseEntity<AvaliadorSubmissaoDTO> addAvaliadorSubmissao(@RequestBody AvaliadorSubmissao avaliadorSubmissao)
			throws URISyntaxException {
		try {
			AvaliadorSubmissao novoAvaliadorSubmissao = avaliadorSubmissaoService.save(avaliadorSubmissao);

			return ResponseEntity.created(new URI("/api/avaliadorSubmissao" + novoAvaliadorSubmissao.getId()))
					.body(new AvaliadorSubmissaoDTO().converter(avaliadorSubmissao));
		} catch (ResourceAlreadyExistsException ex) {
			logger.error(ex.getMessage());
			return ResponseEntity.status(HttpStatus.CONFLICT).build();
		} catch (BadResourceException ex) {
			logger.error(ex.getMessage());
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
		}
	}

	@Operation(summary = "Alterar AvaliadorSubmissao", description = "Alterar valores do avaliadorSubmissao com id selecionado", tags = {
			"avaliadorSubmissao" })
	@PutMapping(value = "/avaliadorSubmissao/{id}")
	@CrossOrigin("http://localhost:3000")
	public ResponseEntity<AvaliadorSubmissao> updateAvaliadorSubmissao(@Valid @RequestBody AvaliadorSubmissao avaliadorSubmissao,
			@PathVariable long id) {
		try {
			avaliadorSubmissao.setId(id);
			avaliadorSubmissaoService.update(avaliadorSubmissao);
			return ResponseEntity.ok().build();
		} catch (ResourceNotFoundException ex) {
			logger.error(ex.getMessage());
			return ResponseEntity.notFound().build();
		} catch (BadResourceException ex) {
			logger.error(ex.getMessage());
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
		}

	}

	@Operation(summary = "Deletar avaliadorSubmissao", description = "Deletar avaliadorSubmissao com o ID informado", tags = {
			"avaliadorSubmissao" })
	@DeleteMapping(path = "/avaliadorSubmissao/{id}")
	@CrossOrigin("http://localhost:3000")
	public ResponseEntity<Void> deleteAvaliadorSubmissaoById(@PathVariable long id) {
		try {
			avaliadorSubmissaoService.deleteById(id);
			return ResponseEntity.ok().build();
		} catch (ResourceNotFoundException ex) {
			logger.error(ex.getMessage());
			throw new ResponseStatusException(
					HttpStatus.NOT_FOUND, ex.getMessage(), ex);
		}
	}
}

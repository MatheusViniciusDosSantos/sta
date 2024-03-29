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

import br.com.sta.domain.AreaAvaliador;
import br.com.sta.dto.AreaAvaliadorDTO;
import br.com.sta.exception.BadResourceException;
import br.com.sta.exception.ResourceAlreadyExistsException;
import br.com.sta.exception.ResourceNotFoundException;
import br.com.sta.service.AreaAvaliadorService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;

import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;

@RestController
@RequestMapping("/api")
@Tag(name = "areaAvaliador", description = "API de AreaAvaliador")
public class AreaAvaliadorController {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	// private final int ROW_PER_PAGE = 5;

	@Autowired
	private AreaAvaliadorService areaAvaliadorService;

	@Operation(summary = "Busca areaAvaliadors", description = "Buscar todos os areaAvaliadors, buscar areaAvaliadors por descricao", tags = {
			"areaAvaliador" })
	@GetMapping(value = "/areaAvaliador")
	@CrossOrigin("http://localhost:3000")
	public ResponseEntity<Page<AreaAvaliadorDTO>> findAll(
			@Parameter(description = "Descrição para pesquisa", allowEmptyValue = true) @RequestBody(required = false) String descricao,
			@Parameter(description = "Paginação", example = "{\"page\":0,\"size\":1}", allowEmptyValue = true) Pageable pageable) {
		return ResponseEntity.ok(areaAvaliadorService.findAll(pageable));
	}

	@Operation(summary = "Busca ID", description = "Buscar areaAvaliador por ID", tags = { "areaAvaliador" })
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Sucesso", content = @Content(schema = @Schema(implementation = AreaAvaliador.class))),
			@ApiResponse(responseCode = "404", description = "AreaAvaliador não encontrado")
	})
	@GetMapping(value = "/areaAvaliador/{id}")
	@CrossOrigin("http://localhost:3000")
	public ResponseEntity<AreaAvaliador> findAreaAvaliadorById(@PathVariable long id) {
		try {
			AreaAvaliador areaAvaliador = areaAvaliadorService.findById(id);
			return ResponseEntity.ok(areaAvaliador);
		} catch (ResourceNotFoundException ex) {
			logger.error(ex.getMessage());

			throw new ResponseStatusException(HttpStatus.NOT_FOUND, ex.getMessage(), ex);
		}

	}

	@Operation(summary = "Busca ID", description = "Buscar areaAvaliador por ID do area", tags = { "areaAvaliador" })
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Sucesso", content = @Content(schema = @Schema(implementation = AreaAvaliador.class))),
			@ApiResponse(responseCode = "404", description = "AreaAvaliador não encontrado para este area")
	})
	@GetMapping(value = "/areaAvaliador/area/{id}")
	@CrossOrigin("http://localhost:3000")
	public ResponseEntity<Page<AreaAvaliadorDTO>> findAreaAvaliadorByIdArea(@PathVariable long id,
			@Parameter(description = "Paginação", example = "{\"page\":0,\"size\":1}", allowEmptyValue = true) Pageable pageable) {
		Page<AreaAvaliadorDTO> areaAvaliadors = areaAvaliadorService.findAllByIdArea(id, pageable);
		return ResponseEntity.ok(areaAvaliadors);
	}

	@Operation(summary = "Busca ID", description = "Buscar areaAvaliador por ID do avaliador", tags = {
			"areaAvaliador" })
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Sucesso", content = @Content(schema = @Schema(implementation = AreaAvaliador.class))),
			@ApiResponse(responseCode = "404", description = "AreaAvaliador não encontrado para este area")
	})
	@GetMapping(value = "/areaAvaliador/avaliador/{id}")
	@CrossOrigin("http://localhost:3000")
	public ResponseEntity<Page<AreaAvaliadorDTO>> findAreaAvaliadorByIdAvaliador(@PathVariable long id,
			@Parameter(description = "Paginação", example = "{\"page\":0,\"size\":1}", allowEmptyValue = true) Pageable pageable) {
		Page<AreaAvaliadorDTO> areaAvaliadors = areaAvaliadorService.findAllByIdAvaliador(id, pageable);
		return ResponseEntity.ok(areaAvaliadors);
	}

	@Operation(summary = "Adicionar areaAvaliador", description = "Adicionar novo areaAvaliador informado no banco de dados", tags = {
			"areaAvaliador" })
	@PostMapping(value = "/areaAvaliador")
	@CrossOrigin("http://localhost:3000")
	public ResponseEntity<AreaAvaliadorDTO> addAreaAvaliador(@RequestBody AreaAvaliador areaAvaliador)
			throws URISyntaxException {
		try {
			AreaAvaliador novoAreaAvaliador = areaAvaliadorService.save(areaAvaliador);

			return ResponseEntity.created(new URI("/api/areaAvaliador" + novoAreaAvaliador.getId()))
					.body(new AreaAvaliadorDTO().converter(areaAvaliador));
		} catch (ResourceAlreadyExistsException ex) {
			logger.error(ex.getMessage());
			return ResponseEntity.status(HttpStatus.CONFLICT).build();
		} catch (BadResourceException ex) {
			logger.error(ex.getMessage());
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
		}
	}

	@Operation(summary = "Alterar AreaAvaliador", description = "Alterar valores do areaAvaliador com id selecionado", tags = {
			"areaAvaliador" })
	@PutMapping(value = "/areaAvaliador/{id}")
	@CrossOrigin("http://localhost:3000")
	public ResponseEntity<AreaAvaliador> updateAreaAvaliador(@Valid @RequestBody AreaAvaliador areaAvaliador,
			@PathVariable long id) {
		try {
			areaAvaliador.setId(id);
			areaAvaliadorService.update(areaAvaliador);
			return ResponseEntity.ok().build();
		} catch (ResourceNotFoundException ex) {
			logger.error(ex.getMessage());
			return ResponseEntity.notFound().build();
		} catch (BadResourceException ex) {
			logger.error(ex.getMessage());
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
		}

	}

	@Operation(summary = "Deletar areaAvaliador", description = "Deletar areaAvaliador com o ID informado", tags = {
			"areaAvaliador" })
	@DeleteMapping(path = "/areaAvaliador/{id}")
	@CrossOrigin("http://localhost:3000")
	public ResponseEntity<Void> deleteAreaAvaliadorById(@PathVariable long id) {
		try {
			areaAvaliadorService.deleteById(id);
			return ResponseEntity.ok().build();
		} catch (ResourceNotFoundException ex) {
			logger.error(ex.getMessage());
			throw new ResponseStatusException(
					HttpStatus.NOT_FOUND, ex.getMessage(), ex);
		}
	}
}

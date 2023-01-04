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

import br.com.sta.domain.Criterio;
import br.com.sta.dto.CriterioDTO;
import br.com.sta.exception.BadResourceException;
import br.com.sta.exception.ResourceAlreadyExistsException;
import br.com.sta.exception.ResourceNotFoundException;
import br.com.sta.service.CriterioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;

import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;

@RestController
@RequestMapping("/api")
@Tag(name = "criterio", description = "API de Criterio")
public class CriterioController {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	// private final int ROW_PER_PAGE = 5;
	
	@Autowired
	private CriterioService criterioService;
	
	@Operation(summary = "Busca criterios", description = "Buscar todos os criterios, buscar criterios por descricao", tags = {"criterio"})
	@GetMapping(value = "/criterio")
	@CrossOrigin("http://localhost:3000")
	public ResponseEntity<Page<CriterioDTO>> findAll(
			@Parameter(description = "Descrição para pesquisa", allowEmptyValue = true)
			@RequestBody(required=false) String descricao,
			@Parameter(description = "Paginação", example = "{\"page\":0,\"size\":1}", allowEmptyValue = true)
			 Pageable pageable)	{
		if(descricao.isEmpty()) {
			return ResponseEntity.ok(criterioService.findAll(pageable));
		} else {
			return ResponseEntity.ok(criterioService.findAllByDescricao(descricao, pageable));
		}
	}
	
	@Operation(summary = "Busca ID", description = "Buscar criterio por ID", tags = {"criterio"})
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Sucesso",
					content = @Content(schema = @Schema(implementation = Criterio.class))),
			@ApiResponse(responseCode = "404", description = "Criterio não encontrado")
	})
	@GetMapping(value = "/criterio/{id}")
	@CrossOrigin("http://localhost:3000")
	public ResponseEntity<Criterio> findCriterioById(@PathVariable long id) {
		try {
			Criterio criterio = criterioService.findById(id);
			return ResponseEntity.ok(criterio);
		} catch (ResourceNotFoundException ex) {
			logger.error(ex.getMessage());
			
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, ex.getMessage(), ex);
		}
	
	}
	
	@Operation(summary = "Adicionar criterio", description = "Adicionar novo criterio informado no banco de dados", tags = {"criterio"})
	@PostMapping(value = "/criterio")
	@CrossOrigin("http://localhost:3000")
	public ResponseEntity<CriterioDTO> addCriterio(@RequestBody Criterio criterio) throws URISyntaxException {
		try {
			Criterio novoCriterio = criterioService.save(criterio);
			
			return ResponseEntity.created(new URI("/api/criterio" + novoCriterio.getId())).body(new CriterioDTO().converter(criterio));
		} catch (ResourceAlreadyExistsException ex) {
			logger.error(ex.getMessage());
			return ResponseEntity.status(HttpStatus.CONFLICT).build();
		} catch (BadResourceException ex) {
			logger.error(ex.getMessage());
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
		}
	}
	
	@Operation(summary = "Alterar Criterio", description = "Alterar valores do criterio com id selecionado", tags = {"criterio"})
	@PutMapping(value = "/criterio/{id}")
	@CrossOrigin("http://localhost:3000")
	public ResponseEntity<Criterio> updateCriterio(@Valid @RequestBody Criterio criterio, 
			@PathVariable long id) {
		try {
			criterio.setId(id);
			criterioService.update(criterio);
			return ResponseEntity.ok().build();
		} catch (ResourceNotFoundException ex) {
			logger.error(ex.getMessage());
			return ResponseEntity.notFound().build();
		} catch (BadResourceException ex) {
			logger.error(ex.getMessage());
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
		}
		
	}
	
	@Operation(summary = "Deletar criterio", description = "Deletar criterio com o ID informado", tags = {"criterio"})
	@DeleteMapping(path = "/criterio/{id}")
	@CrossOrigin("http://localhost:3000")
	public ResponseEntity<Void> deleteCriterioById(@PathVariable long id) {
		try {
			criterioService.deleteById(id);
			return ResponseEntity.ok().build();
		} catch (ResourceNotFoundException ex) {
			logger.error(ex.getMessage());
			throw new ResponseStatusException(
					HttpStatus.NOT_FOUND, ex.getMessage(), ex);
		}
	}
}

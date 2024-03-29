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

import br.com.sta.domain.PermissaoUsuario;
import br.com.sta.dto.PermissaoUsuarioDTO;
import br.com.sta.exception.BadResourceException;
import br.com.sta.exception.ResourceAlreadyExistsException;
import br.com.sta.exception.ResourceNotFoundException;
import br.com.sta.service.PermissaoUsuarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;

import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;

@RestController
@RequestMapping("/api")
@Tag(name = "permissaoUsuario", description = "API de PermissaoUsuario")
public class PermissaoUsuarioController {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	// private final int ROW_PER_PAGE = 5;

	@Autowired
	private PermissaoUsuarioService permissaoUsuarioService;

	@Operation(summary = "Busca permissaoUsuarios", description = "Buscar todos os permissaoUsuarios, buscar permissaoUsuarios por descricao", tags = {
			"permissaoUsuario" })
	@GetMapping(value = "/permissaoUsuario")
	@CrossOrigin("http://localhost:3000")
	public ResponseEntity<Page<PermissaoUsuarioDTO>> findAll(
			@Parameter(description = "Descrição para pesquisa", allowEmptyValue = true) @RequestBody(required = false) String descricao,
			@Parameter(description = "Paginação", example = "{\"page\":0,\"size\":1}", allowEmptyValue = true) Pageable pageable) {
		return ResponseEntity.ok(permissaoUsuarioService.findAll(pageable));
	}

	@Operation(summary = "Busca ID", description = "Buscar permissaoUsuario por ID", tags = { "permissaoUsuario" })
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Sucesso", content = @Content(schema = @Schema(implementation = PermissaoUsuario.class))),
			@ApiResponse(responseCode = "404", description = "PermissaoUsuario não encontrado")
	})
	@GetMapping(value = "/permissaoUsuario/{id}")
	@CrossOrigin("http://localhost:3000")
	public ResponseEntity<PermissaoUsuario> findPermissaoUsuarioById(@PathVariable long id) {
		try {
			PermissaoUsuario permissaoUsuario = permissaoUsuarioService.findById(id);
			return ResponseEntity.ok(permissaoUsuario);
		} catch (ResourceNotFoundException ex) {
			logger.error(ex.getMessage());

			throw new ResponseStatusException(HttpStatus.NOT_FOUND, ex.getMessage(), ex);
		}

	}

	@Operation(summary = "Busca ID", description = "Buscar permissaoUsuario por ID do permissao", tags = { "permissaoUsuario" })
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Sucesso", content = @Content(schema = @Schema(implementation = PermissaoUsuario.class))),
			@ApiResponse(responseCode = "404", description = "PermissaoUsuario não encontrado para este permissao")
	})
	@GetMapping(value = "/permissaoUsuario/permissao/{id}")
	@CrossOrigin("http://localhost:3000")
	public ResponseEntity<Page<PermissaoUsuarioDTO>> findPermissaoUsuarioByIdPermissao(@PathVariable long id,
			@Parameter(description = "Paginação", example = "{\"page\":0,\"size\":1}", allowEmptyValue = true) Pageable pageable) {
		Page<PermissaoUsuarioDTO> permissaoUsuarios = permissaoUsuarioService.findAllByIdPermissao(id, pageable);
		return ResponseEntity.ok(permissaoUsuarios);
	}

	@Operation(summary = "Busca ID", description = "Buscar permissaoUsuario por ID do usuario", tags = {
			"permissaoUsuario" })
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Sucesso", content = @Content(schema = @Schema(implementation = PermissaoUsuario.class))),
			@ApiResponse(responseCode = "404", description = "PermissaoUsuario não encontrado para este permissao")
	})
	@GetMapping(value = "/permissaoUsuario/usuario/{id}")
	@CrossOrigin("http://localhost:3000")
	public ResponseEntity<Page<PermissaoUsuarioDTO>> findPermissaoUsuarioByIdUsuario(@PathVariable long id,
			@Parameter(description = "Paginação", example = "{\"page\":0,\"size\":1}", allowEmptyValue = true) Pageable pageable) {
		Page<PermissaoUsuarioDTO> permissaoUsuarios = permissaoUsuarioService.findAllByIdUsuario(id, pageable);
		return ResponseEntity.ok(permissaoUsuarios);
	}

	@Operation(summary = "Adicionar permissaoUsuario", description = "Adicionar novo permissaoUsuario informado no banco de dados", tags = {
			"permissaoUsuario" })
	@PostMapping(value = "/permissaoUsuario")
	@CrossOrigin("http://localhost:3000")
	public ResponseEntity<PermissaoUsuarioDTO> addPermissaoUsuario(@RequestBody PermissaoUsuario permissaoUsuario)
			throws URISyntaxException {
		try {
			PermissaoUsuario novoPermissaoUsuario = permissaoUsuarioService.save(permissaoUsuario);

			return ResponseEntity.created(new URI("/api/permissaoUsuario" + novoPermissaoUsuario.getId()))
					.body(new PermissaoUsuarioDTO().converter(permissaoUsuario));
		} catch (ResourceAlreadyExistsException ex) {
			logger.error(ex.getMessage());
			return ResponseEntity.status(HttpStatus.CONFLICT).build();
		} catch (BadResourceException ex) {
			logger.error(ex.getMessage());
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
		}
	}

	@Operation(summary = "Alterar PermissaoUsuario", description = "Alterar valores do permissaoUsuario com id selecionado", tags = {
			"permissaoUsuario" })
	@PutMapping(value = "/permissaoUsuario/{id}")
	@CrossOrigin("http://localhost:3000")
	public ResponseEntity<PermissaoUsuario> updatePermissaoUsuario(@Valid @RequestBody PermissaoUsuario permissaoUsuario,
			@PathVariable long id) {
		try {
			permissaoUsuario.setId(id);
			permissaoUsuarioService.update(permissaoUsuario);
			return ResponseEntity.ok().build();
		} catch (ResourceNotFoundException ex) {
			logger.error(ex.getMessage());
			return ResponseEntity.notFound().build();
		} catch (BadResourceException ex) {
			logger.error(ex.getMessage());
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
		}

	}

	@Operation(summary = "Deletar permissaoUsuario", description = "Deletar permissaoUsuario com o ID informado", tags = {
			"permissaoUsuario" })
	@DeleteMapping(path = "/permissaoUsuario/{id}")
	@CrossOrigin("http://localhost:3000")
	public ResponseEntity<Void> deletePermissaoUsuarioById(@PathVariable long id) {
		try {
			permissaoUsuarioService.deleteById(id);
			return ResponseEntity.ok().build();
		} catch (ResourceNotFoundException ex) {
			logger.error(ex.getMessage());
			throw new ResponseStatusException(
					HttpStatus.NOT_FOUND, ex.getMessage(), ex);
		}
	}
}

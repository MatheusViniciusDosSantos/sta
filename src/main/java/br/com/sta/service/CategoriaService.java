package br.com.sta.service;

import java.time.Instant;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import br.com.sta.domain.Categoria;
import br.com.sta.dto.CategoriaDTO;
import br.com.sta.exception.BadResourceException;
import br.com.sta.exception.ResourceAlreadyExistsException;
import br.com.sta.exception.ResourceNotFoundException;
import br.com.sta.repository.CategoriaRepository;

@Service
public class CategoriaService {
	
	@Autowired
	private CategoriaRepository categoriaRepository;
	
	private boolean existsById(Long id) {
		return categoriaRepository.existsById(id);
	}
	
	public Categoria findById(Long id) throws ResourceNotFoundException {
		Categoria categoria = categoriaRepository.findById(id).orElse(null);
		
		if(categoria == null) {
			throw new ResourceNotFoundException("Categoria não encontrado com o id: " + id);
		} else {
			return categoria;
		}
	}
	
	public Page<CategoriaDTO> findAll(Pageable pageable) {
		
		return new CategoriaDTO().converterListaCategoriaDTO(categoriaRepository.findAll(pageable)) ;
	}
	
	public Page<CategoriaDTO> findAllByDescricao(String descricao, Pageable page) {
		Page<Categoria> categorias = categoriaRepository.findByDescricao(descricao, page);
		
		
		return new CategoriaDTO().converterListaCategoriaDTO(categorias);
	}
	
	public Categoria save(Categoria categoria) throws BadResourceException, ResourceAlreadyExistsException {
		if(!categoria.getDescricao().isEmpty()) {
			if(existsById(categoria.getId())) {
				throw new ResourceAlreadyExistsException("Categoria com id: " + categoria.getId() + " já existe.");
			}			
			categoria.setStatus('A');
			categoria.setDataCadastro(Instant.now());
			Categoria categoriaNovo = categoriaRepository.save(categoria);			
			return categoriaNovo;
		} else {
			BadResourceException exe = new BadResourceException("Erro ao salvar categoria");
			exe.addErrorMessage("Categoria esta vazia ou nula");
			throw exe;
		}
		
		
	}
	
	public void update(Categoria categoria) throws BadResourceException, ResourceNotFoundException {
		if (!categoria.getDescricao().isEmpty()) {
			if (!existsById(categoria.getId())) {
				throw new ResourceNotFoundException("Categoria não encontrada com o id: " + categoria.getId());
			}
			categoria.setDataUltimaAlteracao(Instant.now());
		} else {
			BadResourceException exe = new BadResourceException("Erro ao salvar categoria");
			exe.addErrorMessage("Categoria esta vazia ou nula");
			throw exe;
		}
	}
	
	public void deleteById(Long id) throws ResourceNotFoundException {
		if(!existsById(id)) {
			throw new ResourceNotFoundException("Categoria não encontrada com o id: " + id);
		} else {
			categoriaRepository.deleteById(id);
		}
	
	}  public Long count() {
		return categoriaRepository.count();
	}
	
}
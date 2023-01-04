package br.com.sta.service;

import java.time.Instant;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import br.com.sta.domain.Area;
import br.com.sta.dto.AreaDTO;
import br.com.sta.exception.BadResourceException;
import br.com.sta.exception.ResourceAlreadyExistsException;
import br.com.sta.exception.ResourceNotFoundException;
import br.com.sta.repository.AreaRepository;

@Service
public class AreaService {
	
	@Autowired
	private AreaRepository areaRepository;
	
	private boolean existsById(Long id) {
		return areaRepository.existsById(id);
	}
	
	public Area findById(Long id) throws ResourceNotFoundException {
		Area area = areaRepository.findById(id).orElse(null);
		
		if(area == null) {
			throw new ResourceNotFoundException("Area não encontrado com o id: " + id);
		} else {
			return area;
		}
	}
	
	public Page<AreaDTO> findAll(Pageable pageable) {
		
		return new AreaDTO().converterListaAreaDTO(areaRepository.findAll(pageable)) ;
	}
	
	public Page<AreaDTO> findAllByDescricao(String descricao, Pageable page) {
		Page<Area> areas = areaRepository.findByDescricao(descricao, page);
		
		
		return new AreaDTO().converterListaAreaDTO(areas);
	}
	
	public Area save(Area area) throws BadResourceException, ResourceAlreadyExistsException {
		if(!area.getDescricao().isEmpty()) {
			if(existsById(area.getId())) {
				throw new ResourceAlreadyExistsException("Area com id: " + area.getId() + " já existe.");
			}			
			area.setStatus('A');
			area.setDataCadastro(Instant.now());
			Area areaNovo = areaRepository.save(area);			
			return areaNovo;
		} else {
			BadResourceException exe = new BadResourceException("Erro ao salvar area");
			exe.addErrorMessage("Area esta vazia ou nula");
			throw exe;
		}
		
		
	}
	
	public void update(Area area) throws BadResourceException, ResourceNotFoundException {
		if (!area.getDescricao().isEmpty()) {
			if (!existsById(area.getId())) {
				throw new ResourceNotFoundException("Area não encontrada com o id: " + area.getId());
			}
			area.setDataUltimaAlteracao(Instant.now());
		} else {
			BadResourceException exe = new BadResourceException("Erro ao salvar area");
			exe.addErrorMessage("Area esta vazia ou nula");
			throw exe;
		}
	}
	
	public void deleteById(Long id) throws ResourceNotFoundException {
		if(!existsById(id)) {
			throw new ResourceNotFoundException("Area não encontrada com o id: " + id);
		} else {
			areaRepository.deleteById(id);
		}
	
	}  public Long count() {
		return areaRepository.count();
	}
	
}

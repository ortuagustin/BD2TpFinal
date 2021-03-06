package ar.edu.unlp.pasae.tp_integrador.controllers;

import java.util.Collection;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import ar.edu.unlp.pasae.tp_integrador.dtos.PathologyDTO;
import ar.edu.unlp.pasae.tp_integrador.dtos.PathologyRequestDTO;
import ar.edu.unlp.pasae.tp_integrador.services.PathologyService;

@RestController
@RequestMapping("/pathologies")
public class PathologyController {
	@Autowired
	PathologyService pathologiesService;

	@GetMapping(path = "/", produces = "application/json")
	public Page<PathologyDTO> indexPageable(
			@RequestParam(value="newestPage", defaultValue="0") int page,
			@RequestParam(value="newestSizePerPage", defaultValue="10") int sizePerPage,
			@RequestParam(value="newestSortField", defaultValue="name") String sortField,
			@RequestParam(value="newestSortOrder", defaultValue="asc") String sortOrder,
			@RequestParam(value="search", defaultValue="") String search
			) {
		return this.getPathologysService().list(page, sizePerPage, sortField, sortOrder, search);
	}

	@GetMapping(path = "/all", produces = "application/json")
	public Collection<PathologyDTO> index() {
		return this.getPathologysService().list().collect(Collectors.toList());
	}

	@GetMapping(path = "/{id}", produces = "application/json")
	public PathologyDTO show(@PathVariable(value = "id") Long id) {
		return this.getPathologysService().find(id);
	}

	@DeleteMapping(path = "/{id}")
	public void delete(@PathVariable(value = "id") Long id) {
		this.getPathologysService().delete(id);
	}

	@PutMapping(path = "/", consumes = "application/json", produces = "application/json")
	public PathologyDTO create(@RequestBody @Valid PathologyRequestDTO pathology) {
		return this.getPathologysService().create(pathology);
	}

	@PatchMapping(path = "/{id}", consumes = "application/json")
	public PathologyDTO update(@PathVariable(value = "id") Long id, @RequestBody @Valid PathologyRequestDTO pathology) {
		return this.getPathologysService().update(id, pathology);
	}

	private PathologyService getPathologysService() {
		return this.pathologiesService;
	}
}
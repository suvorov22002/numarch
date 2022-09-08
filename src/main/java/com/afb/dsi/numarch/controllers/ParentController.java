package com.afb.dsi.numarch.controllers;

import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.afb.dsi.numarch.dtos.NumArchUtils;
import com.afb.dsi.numarch.dtos.ParentDTO;
import com.afb.dsi.numarch.dtos.PropertiesDTO;
import com.afb.dsi.numarch.entities.Parent;
import com.afb.dsi.numarch.services.IParentService;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@CrossOrigin
@RestController
@RequestMapping("/rest/api/numarch/parent")
public class ParentController {
	
	public static final Logger logger = LoggerFactory.getLogger(PropertiesController.class);
	
	@Autowired
	private IParentService parentservice;
	
	/**
	 * @param name
	 * @return
	 */
	@GetMapping("/findbyname/{name}")
	@ApiOperation(value="Rechercher un parent par son nom", response = PropertiesDTO.class)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Ok: successfull research"),
			@ApiResponse(code = 204, message = "No Content: no result founded"),
	})
	public ResponseEntity<ParentDTO> findByName(@PathVariable("name") String name) {

		//UriComponentsBuilder uriComponentBuilder
		Parent parentResponse = parentservice.findByName(name);

		if (parentResponse != null) {
			ParentDTO parentDTO = NumArchUtils.mapParentToParentDTO(parentResponse);
			return new ResponseEntity<ParentDTO>(parentDTO, HttpStatus.OK);
		}

		return new ResponseEntity<ParentDTO>(HttpStatus.NO_CONTENT);
	}
	
	/**
	 * Ajouter un nouveau Type Evidence dans la BD. Si la propriete existe déjà, on
	retourne un reference indiquant que la création n'a pas abouti.
	 * @param typeproprieteDTORequest
	 * @return
	 */
	@PostMapping("/add")
	@ApiOperation(value = "Ajouter un parent", response = ParentDTO.class)
	@ApiResponses(value = { @ApiResponse(code = 409, message = "Conflit: le parent existe deja"),
			@ApiResponse(code = 201, message = "cree : le parenta ete enregistre avec succes"),
			@ApiResponse(code = 304, message = "Erreur : le parent n'a pa ete cree") })
	public ResponseEntity<ParentDTO> createNew(@RequestBody ParentDTO
			parentDTORequest) {
		return parentservice.saveParent(parentDTORequest);
	}
	
	/**
	 * Retourne la liste de tous les typeEvidences
	 * @param 
	 * @return
	 */
	@GetMapping("/getall")
	@ApiOperation(value="Obtenir tous les parents", response = List.class)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Ok: successfully listed"),
			@ApiResponse(code = 204, message = "No Content: no result founded"),
	})
	public ResponseEntity<List<ParentDTO>> getAlls() {
		//, UriComponentsBuilder uriComponentBuilder

		List<Parent> parents = parentservice.getAllParent();
		if (parents != null && !CollectionUtils.isEmpty(parents)) {
			List<ParentDTO> parentsDTOs = parents.stream().map(parent -> {
				return NumArchUtils.mapParentToParentDTO(parent);
			}).collect(Collectors.toList());
			return new ResponseEntity<List<ParentDTO>>(parentsDTOs, HttpStatus.OK);
		}
		return new ResponseEntity<List<ParentDTO>>(HttpStatus.NO_CONTENT);
	}
}

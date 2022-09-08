package com.afb.dsi.numarch.controllers;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
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

import com.afb.dsi.numarch.dtos.PropertiesDTO;
import com.afb.dsi.numarch.entities.Properties;
import com.afb.dsi.numarch.services.IPropertiesService;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@CrossOrigin
@RestController
@RequestMapping("/rest/api/numarch/properties")
public class PropertiesController {
	
	public static final Logger logger = LoggerFactory.getLogger(PropertiesController.class);
	
	@Autowired
	private IPropertiesService propertiesService;
	
	/**
	 * @param name
	 * @return
	 */
	@GetMapping("/findbyname/{name}")
	@ApiOperation(value="Rechercher un Type propriete par son nom", response = PropertiesDTO.class)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Ok: successfull research"),
			@ApiResponse(code = 204, message = "No Content: no result founded"),
	})
	public ResponseEntity<PropertiesDTO> findByName(@PathVariable("name") String name) {

		//UriComponentsBuilder uriComponentBuilder
		Properties propertiesResponse = propertiesService.findByName(name);

		if (propertiesResponse != null) {
			PropertiesDTO propertiesDTO = mapPropertiesToPropertiesDTO(propertiesResponse);
			return new ResponseEntity<PropertiesDTO>(propertiesDTO, HttpStatus.OK);
		}

		return new ResponseEntity<PropertiesDTO>(HttpStatus.NO_CONTENT);
	}
	
	/**
	 * Ajouter un nouveau Type Evidence dans la BD. Si la propriete existe déjà, on
	retourne un reference indiquant que la création n'a pas abouti.
	 * @param typeproprieteDTORequest
	 * @return
	 */
	@PostMapping("/add")
	@ApiOperation(value = "Ajouter une propriete", response = PropertiesDTO.class)
	@ApiResponses(value = { @ApiResponse(code = 409, message = "Conflit: la propriete existe deja"),
			@ApiResponse(code = 201, message = "cree : la propriete a ete enregistre avec succes"),
			@ApiResponse(code = 304, message = "Erreur : le Type document n'a pa ete cree") })
	public ResponseEntity<PropertiesDTO> createNew(@RequestBody PropertiesDTO
			propertiesDTORequest) {
		return propertiesService.saveProperties(propertiesDTORequest);
	}


	/**
	 * Retourne la liste de tous les typeEvidences
	 * @param 
	 * @return
	 */
	@GetMapping("/getall")
	@ApiOperation(value="Obtenir plusieurs types Properties", response = List.class)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Ok: successfully listed"),
			@ApiResponse(code = 204, message = "No Content: no result founded"),
	})
	public ResponseEntity<List<PropertiesDTO>> getAlls() {
		//, UriComponentsBuilder uriComponentBuilder

		List<Properties> properties = propertiesService.getAllProperties();
		if (properties != null && !CollectionUtils.isEmpty(properties)) {
			List<PropertiesDTO> propertiesDTOs = properties.stream().map(propertie -> {
				return mapPropertiesToPropertiesDTO(propertie);
			}).collect(Collectors.toList());
			return new ResponseEntity<List<PropertiesDTO>>(propertiesDTOs, HttpStatus.OK);
		}
		return new ResponseEntity<List<PropertiesDTO>>(HttpStatus.NO_CONTENT);
	}

	
	
	
	/**
	 * Transforme une entity TypeEvidence en un POJO TypeEvidenceDTO
	 *
	 * @param typeEvidence
	 * @return
	 */
	private PropertiesDTO mapPropertiesToPropertiesDTO(Properties typeEvidence) {
		ModelMapper mapper = new ModelMapper();
		PropertiesDTO typeEvidenceDTO = mapper.map(typeEvidence, PropertiesDTO.class);
		return typeEvidenceDTO;
	}


	/**
	 * Transforme un POJO TypeEvidenceDTO en une entity TypeEvidence
	 *
	 * @param typeEvidenceDTO
	 * @return
	 */
	private Properties mapPropertiesDTOToProperties(PropertiesDTO typeEvidenceDTO) {
		ModelMapper mapper = new ModelMapper();
		Properties typeEvidence = mapper.map(typeEvidenceDTO, Properties.class);
		return typeEvidence;
	}


}

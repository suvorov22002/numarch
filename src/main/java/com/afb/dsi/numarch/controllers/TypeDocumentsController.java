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
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.afb.dsi.numarch.dtos.TypeDocumentsDTO;
import com.afb.dsi.numarch.entities.TypeDocuments;
import com.afb.dsi.numarch.services.ITypeDocumentsService;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@CrossOrigin
@RestController
@RequestMapping("/rest/api/numarch/typedocument")
public class TypeDocumentsController {
	
	public static final Logger logger = LoggerFactory.getLogger(TypeDocumentsController.class);
	
	@Autowired
	private ITypeDocumentsService typeDocumentsService;
	
	@PostMapping("/add")
	@ApiOperation(value = "Ajouter un Type Documents", response = TypeDocumentsDTO.class)
	@ApiResponses(value = { @ApiResponse(code = 409, message = "Conflit: le Type Documents existe deja"),
			@ApiResponse(code = 201, message = "cree : le Type Documents a ete enregistre avec succes"),
			@ApiResponse(code = 304, message = "Erreur : le Type Documents n'a pa ete cree") })
	public ResponseEntity<TypeDocumentsDTO> createNew(@RequestBody TypeDocumentsDTO
			typeDocumentsDTORequest) {
		
		TypeDocuments existingDocuments = typeDocumentsService.findByName(typeDocumentsDTORequest.getName());
		if (existingDocuments != null) {
			return new ResponseEntity<TypeDocumentsDTO>(HttpStatus.CONFLICT);
		}
		TypeDocuments typeDocumentsRequest = mapTypeDocumentsDTOToTypeDocuments(typeDocumentsDTORequest);
		TypeDocuments typeDocumentsResponse = typeDocumentsService.saveTypeDocuments(typeDocumentsRequest);
		if (typeDocumentsResponse != null) {
			TypeDocumentsDTO typeDocumentsDTO = mapTypeDocumentsToTypeDocumentsDTO(typeDocumentsResponse);
			return new ResponseEntity<TypeDocumentsDTO>(typeDocumentsDTO, HttpStatus.CREATED);
		}
		return new ResponseEntity<TypeDocumentsDTO>(HttpStatus.NOT_MODIFIED);
	
	}
	
	@PutMapping("/update")
	@ApiOperation(value = "Update/Modifier Type Documents existant", response = TypeDocumentsDTO.class)
	@ApiResponses(value = { @ApiResponse(code = 404, message = "Not Found : la ressource demandee n existe pas"),
			@ApiResponse(code = 200, message = "Ok: le Type Documents modifie avec succes"),
			@ApiResponse(code = 304, message = "Not Modified: La modification du Type Documents n a pas abouti") })
	public ResponseEntity<TypeDocumentsDTO> update(@RequestBody TypeDocumentsDTO
			typeDocumentsDTORequest) {
		
		if (!typeDocumentsService.checkIfIdexists(typeDocumentsDTORequest.getId())) {
			return new ResponseEntity<TypeDocumentsDTO>(HttpStatus.NOT_FOUND);
		}
		TypeDocuments typeDocumentsRequest = mapTypeDocumentsDTOToTypeDocuments(typeDocumentsDTORequest);
		TypeDocuments typeDocumentsResponse = typeDocumentsService.updateTypeDocuments(typeDocumentsRequest);
		if (typeDocumentsResponse != null) {
			TypeDocumentsDTO typeEvidenceDTO = mapTypeDocumentsToTypeDocumentsDTO(typeDocumentsResponse);
			return new ResponseEntity<TypeDocumentsDTO>(typeEvidenceDTO, HttpStatus.OK);
		}
		return new ResponseEntity<TypeDocumentsDTO>(HttpStatus.NOT_MODIFIED);
	}
	
	/**
	 * Supprime un Type evidence dans la BD. Si le Type Evidence n'est pas retrouvé, on
		retourne le Statut HTTP NO_CONTENT.
	 * @param typeEvidenceId
	 * @return
	 */
	@DeleteMapping("/delete/{id}")
	@ApiOperation(value = "Supprimer un Type Evidence", response = String.class)
	@ApiResponse(code = 204, message = "No Content: Suppression du Type Evidence avec succes")
	public ResponseEntity<String> deleteTypeDocuments(@PathVariable Long id) {
		typeDocumentsService.deleteDocument(id);
		return new ResponseEntity<String>(HttpStatus.NO_CONTENT);
	}
	
	/**
	 * @param name
	 * @return
	 */
	@GetMapping("/findbyname/{name}")
	@ApiOperation(value="Search a TypeDocuments by it name", response = TypeDocumentsDTO.class)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Ok: successfull research"),
			@ApiResponse(code = 204, message = "No Content: no result founded"),
	})
	public ResponseEntity<TypeDocumentsDTO> findByReference(@PathVariable("name") String name) {

		//UriComponentsBuilder uriComponentBuilder
		TypeDocuments typeDocumentResponse = typeDocumentsService.findByName(name);

		if (typeDocumentResponse != null) {
			TypeDocumentsDTO activiteDTO = mapTypeDocumentsToTypeDocumentsDTO(typeDocumentResponse);
			return new ResponseEntity<TypeDocumentsDTO>(activiteDTO, HttpStatus.OK);
		}

		return new ResponseEntity<TypeDocumentsDTO>(HttpStatus.NO_CONTENT);
	}
	
	@PostMapping("/findtypedocument")
	@ApiOperation(value="Recherche des TypeDocuments a l aide de plusieurs parametres", response = List.class)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Ok: successfully listed"),
			@ApiResponse(code = 204, message = "No Content: no result founded"),
	})
	public ResponseEntity<List<TypeDocumentsDTO>> findTypeDocuments(@RequestBody TypeDocumentsDTO
			typeDocumentsDTORequest) {
		//, UriComponentsBuilder uriComponentBuilder

		List<TypeDocuments> typeDocuments = typeDocumentsService.findTypeDocuments(typeDocumentsDTORequest);
		if (typeDocuments != null && !CollectionUtils.isEmpty(typeDocuments)) {
			List<TypeDocumentsDTO> typeDocumentsDTOs = typeDocuments.stream().map(typeDocument -> {
				return mapTypeDocumentsToTypeDocumentsDTO(typeDocument);
			}).collect(Collectors.toList());
			return new ResponseEntity<List<TypeDocumentsDTO>>(typeDocumentsDTOs, HttpStatus.OK);
		}
		return new ResponseEntity<List<TypeDocumentsDTO>>(HttpStatus.NO_CONTENT);
	}



	/**
	 * Retourne la liste de tous les activites
	 * @param 
	 * @return
	 */
	@GetMapping("/getall")
	@ApiOperation(value="Obtenir toutes les typeDocuments", response = List.class)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Ok: successfully listed"),
			@ApiResponse(code = 204, message = "No Content: no result founded"),
	})
	public ResponseEntity<List<TypeDocumentsDTO>> getAlls() {
		//, UriComponentsBuilder uriComponentBuilder

		List<TypeDocuments> typeDocuments = typeDocumentsService.getAllTypeDocuments();
		if (typeDocuments != null && !CollectionUtils.isEmpty(typeDocuments)) {
			List<TypeDocumentsDTO> typeDocumentsDTOs = typeDocuments.stream().map(typeDocument -> {
				return mapTypeDocumentsToTypeDocumentsDTO(typeDocument);
			}).collect(Collectors.toList());
			return new ResponseEntity<List<TypeDocumentsDTO>>(typeDocumentsDTOs, HttpStatus.OK);
		}
		return new ResponseEntity<List<TypeDocumentsDTO>>(HttpStatus.NO_CONTENT);
	}

	
	
	
	
	/**
	 * Transforme une entity TypeEvidence en un POJO TypeEvidenceDTO
	 *
	 * @param typeEvidence
	 * @return
	 */
	private TypeDocumentsDTO mapTypeDocumentsToTypeDocumentsDTO(TypeDocuments typeEvidence) {
		ModelMapper mapper = new ModelMapper();
		TypeDocumentsDTO typeEvidenceDTO = mapper.map(typeEvidence, TypeDocumentsDTO.class);
		return typeEvidenceDTO;
	}


	/**
	 * Transforme un POJO TypeEvidenceDTO en une entity TypeEvidence
	 *
	 * @param typeEvidenceDTO
	 * @return
	 */
	private TypeDocuments mapTypeDocumentsDTOToTypeDocuments(TypeDocumentsDTO typeDocumentsDTO) {
		ModelMapper mapper = new ModelMapper();
		TypeDocuments typeEvidence = mapper.map(typeDocumentsDTO, TypeDocuments.class);
		return typeEvidence;
	}

}

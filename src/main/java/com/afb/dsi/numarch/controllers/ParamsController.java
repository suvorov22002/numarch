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
import com.afb.dsi.numarch.dtos.ParamsDTO;
import com.afb.dsi.numarch.dtos.ParentDTO;
import com.afb.dsi.numarch.dtos.PropertiesDTO;
import com.afb.dsi.numarch.entities.Params;
import com.afb.dsi.numarch.entities.Parent;
import com.afb.dsi.numarch.services.IParamsService;
import com.afb.dsi.numarch.services.IParentService;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@CrossOrigin
@RestController
@RequestMapping("/rest/api/numarch/params")
public class ParamsController {
	
	public static final Logger logger = LoggerFactory.getLogger(PropertiesController.class);
	
	@Autowired
	private IParamsService paramservice;
	
	/**
	 * @param name
	 * @return
	 */
	@GetMapping("/findbycode/{code}")
	@ApiOperation(value="Rechercher un param par son code", response = PropertiesDTO.class)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Ok: successfull research"),
			@ApiResponse(code = 204, message = "No Content: no result founded"),
	})
	public ResponseEntity<ParamsDTO> findByCode(@PathVariable("code") String code) {

		//UriComponentsBuilder uriComponentBuilder
		Params paramsResponse = paramservice.findByCode(code);

		if (paramsResponse != null) {
			ParamsDTO paramDTO = NumArchUtils.mapParamsToParamsDTO(paramsResponse);
			return new ResponseEntity<ParamsDTO>(paramDTO, HttpStatus.OK);
		}

		return new ResponseEntity<ParamsDTO>(HttpStatus.NO_CONTENT);
	}
	
	/**
	 * Ajouter un nouveau Type Evidence dans la BD. Si la propriete existe déjà, on
	retourne un reference indiquant que la création n'a pas abouti.
	 * @param typeproprieteDTORequest
	 * @return
	 */
	@PostMapping("/add")
	@ApiOperation(value = "Ajouter un params", response = ParamsDTO.class)
	@ApiResponses(value = { @ApiResponse(code = 409, message = "Conflit: le params existe deja"),
			@ApiResponse(code = 201, message = "cree : le params ete enregistre avec succes"),
			@ApiResponse(code = 304, message = "Erreur : le params n'a pa ete cree") })
	public ResponseEntity<ParamsDTO> createNew(@RequestBody ParamsDTO
			paramsDTORequest) {
		return paramservice.saveParams(paramsDTORequest);
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
	public ResponseEntity<List<ParamsDTO>> getAlls() {
		//, UriComponentsBuilder uriComponentBuilder

		List<Params> params = paramservice.getAllParams();
		if (params != null && !CollectionUtils.isEmpty(params)) {
			List<ParamsDTO> paramsDTO = params.stream().map(param -> {
				return NumArchUtils.mapParamsToParamsDTO(param);
			}).collect(Collectors.toList());
			return new ResponseEntity<List<ParamsDTO>>(paramsDTO, HttpStatus.OK);
		}
		return new ResponseEntity<List<ParamsDTO>>(HttpStatus.NO_CONTENT);
	}
	
}

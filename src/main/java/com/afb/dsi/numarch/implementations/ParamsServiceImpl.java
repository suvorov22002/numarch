package com.afb.dsi.numarch.implementations;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.afb.dsi.numarch.dtos.NumArchUtils;
import com.afb.dsi.numarch.dtos.ParamsDTO;
import com.afb.dsi.numarch.entities.Params;
import com.afb.dsi.numarch.enumerations.MessageResponse;
import com.afb.dsi.numarch.repository.ParamsRepository;
import com.afb.dsi.numarch.services.IParamsService;

import lombok.extern.slf4j.Slf4j;

@Service("paramservice")
@Slf4j
@Transactional
public class ParamsServiceImpl implements IParamsService{

	@Autowired
	ParamsRepository paramrepository;
	
	@Override
	public Params findByCode(String code) {
		return paramrepository.findByCode(code);
	}

	@Override
	public ResponseEntity<ParamsDTO> saveParams(ParamsDTO paramsDTORequest) {
		
		Params ParamsResponse;
		ParamsDTO paramsDTO = new ParamsDTO();
		
		try {
			
			if (paramsDTORequest == null) {
				paramsDTO.setCodeResponse(Integer.toString(HttpStatus.PRECONDITION_FAILED.value()));
				paramsDTO.setMessage(HttpStatus.PRECONDITION_FAILED.getReasonPhrase() + " : " + MessageResponse.NULL_OBJET);
				return new ResponseEntity<ParamsDTO>(paramsDTO, HttpStatus.PRECONDITION_FAILED);
			}
			
			if (StringUtils.isBlank(paramsDTORequest.getCode())) {
				paramsDTO.setCodeResponse(Integer.toString(HttpStatus.PRECONDITION_FAILED.value()));
				paramsDTO.setMessage(HttpStatus.PRECONDITION_FAILED.getReasonPhrase() + " : " + MessageResponse.NULL_PROPERTY + "(Code)");
				return new ResponseEntity<ParamsDTO>(paramsDTO, HttpStatus.PRECONDITION_FAILED);
			}
			
			
			Params existingProperties = findByCode(paramsDTORequest.getCode()); 
			
			if (existingProperties != null) {
				paramsDTO.setCodeResponse(Integer.toString(HttpStatus.CONFLICT.value()));
				paramsDTO.setMessage(HttpStatus.CONFLICT.getReasonPhrase() + " : " + MessageResponse.ELEMENT_ALREADY_EXIST);
				return new ResponseEntity<ParamsDTO>(paramsDTO, HttpStatus.CONFLICT);
			}
			
			
			Params paramsRequest = NumArchUtils.mapParamsDTOToParams(paramsDTORequest);
			
			ParamsResponse = paramrepository.save(paramsRequest);
			
			if (ParamsResponse != null) {
				paramsDTO = NumArchUtils.mapParamsToParamsDTO(ParamsResponse);
				paramsDTO.setCodeResponse(Integer.toString(HttpStatus.OK.value()));
				paramsDTO.setMessage(HttpStatus.OK.getReasonPhrase() + " : " + MessageResponse.SUCCESSFULL_OPERATION);
				return new ResponseEntity<ParamsDTO>(paramsDTO, HttpStatus.OK);
			}
			
			paramsDTO.setCodeResponse(Integer.toString(HttpStatus.NOT_IMPLEMENTED.value()));
			paramsDTO.setMessage(HttpStatus.NOT_IMPLEMENTED.getReasonPhrase() + " : " + MessageResponse.FAILED_OPERATION);
			return new ResponseEntity<ParamsDTO>(paramsDTO, HttpStatus.NOT_IMPLEMENTED);
			
		}catch (Exception e) {
			paramsDTO.setCodeResponse(Integer.toString(HttpStatus.INTERNAL_SERVER_ERROR.value()));
			paramsDTO.setMessage(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase());
			paramsDTO.setError(e.getMessage());
			return new ResponseEntity<ParamsDTO>(paramsDTO, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
	}
	
	
	
	@Override
	public ResponseEntity<ParamsDTO> editParams(ParamsDTO paramsDTORequest) {
		
		Params ParamsResponse;
		ParamsDTO paramsDTO = new ParamsDTO();
		
		try {
				
				if (paramsDTORequest == null) {
					paramsDTO.setCodeResponse(Integer.toString(HttpStatus.PRECONDITION_FAILED.value()));
					paramsDTO.setMessage(HttpStatus.PRECONDITION_FAILED.getReasonPhrase() + " : " + MessageResponse.NULL_OBJET);
					return new ResponseEntity<ParamsDTO>(paramsDTO, HttpStatus.PRECONDITION_FAILED);
				}
				
				if (StringUtils.isBlank(paramsDTORequest.getCode())) {
					paramsDTO.setCodeResponse(Integer.toString(HttpStatus.PRECONDITION_FAILED.value()));
					paramsDTO.setMessage(HttpStatus.PRECONDITION_FAILED.getReasonPhrase() + " : " + MessageResponse.NULL_PROPERTY + "(Code)");
					return new ResponseEntity<ParamsDTO>(paramsDTO, HttpStatus.PRECONDITION_FAILED);
				}
				
				Params existingProperties = findByCode(paramsDTORequest.getCode()); 
				
				if (existingProperties != null) {
					existingProperties.setCode(paramsDTORequest.getCode());
					existingProperties.setLibelle(paramsDTORequest.getLibelle());
					existingProperties.setValeur(paramsDTORequest.getValeur());
					existingProperties.setActif(true);
					
					//Params paramsRequest = NumArchUtils.mapParamsDTOToParams(paramsDTORequest);
					
					log.info("exist id: "+existingProperties.getId());
					ParamsResponse = paramrepository.saveAndFlush(existingProperties);
					
					paramsDTO = NumArchUtils.mapParamsToParamsDTO(ParamsResponse);
					paramsDTO.setCodeResponse(Integer.toString(HttpStatus.OK.value()));
					paramsDTO.setMessage(HttpStatus.OK.getReasonPhrase() + " : " + MessageResponse.SUCCESSFULL_OPERATION);
					return new ResponseEntity<ParamsDTO>(paramsDTO, HttpStatus.OK);
				}
				
				paramsDTO.setCodeResponse(Integer.toString(HttpStatus.NOT_IMPLEMENTED.value()));
				paramsDTO.setMessage(HttpStatus.NOT_IMPLEMENTED.getReasonPhrase() + " : " + MessageResponse.FAILED_OPERATION);
				return new ResponseEntity<ParamsDTO>(paramsDTO, HttpStatus.NOT_IMPLEMENTED);
		}
		catch(Exception e) {
			paramsDTO.setCodeResponse(Integer.toString(HttpStatus.INTERNAL_SERVER_ERROR.value()));
			paramsDTO.setMessage(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase());
			paramsDTO.setError(e.getMessage());
			return new ResponseEntity<ParamsDTO>(paramsDTO, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@Override
	public List<Params> saveListParams(List<Params> params) {
		return paramrepository.saveAll(params);
	}

	@Override
	public List<Params> getAllParams() {
		return paramrepository.findAll();
	}

}

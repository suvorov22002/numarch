package com.afb.dsi.numarch.implementations;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.afb.dsi.numarch.dtos.NumArchUtils;
import com.afb.dsi.numarch.dtos.PropertiesDTO;
import com.afb.dsi.numarch.entities.Properties;
import com.afb.dsi.numarch.enumerations.DataType;
import com.afb.dsi.numarch.enumerations.MessageResponse;
import com.afb.dsi.numarch.repository.PropertiesRepository;
import com.afb.dsi.numarch.services.IPropertiesService;

@Service("propertiesservice")
@Transactional
public class PropertiesServiceImpl implements IPropertiesService {
	

	@Autowired
	private PropertiesRepository propertiesRepository;
	
	@PersistenceContext
	private EntityManager entityManager;
	
	
	@Override
	public Properties findByName(String name) {
		return propertiesRepository.findByName(name);
	}

	@Override
	public ResponseEntity<PropertiesDTO> saveProperties(PropertiesDTO propertiesDTORequest) {
		
		PropertiesDTO propertiesDTO = new PropertiesDTO();
		
		try {
			if (propertiesDTORequest == null) {
				propertiesDTO.setCodeResponse(Integer.toString(HttpStatus.PRECONDITION_FAILED.value()));
				propertiesDTO.setMessage(HttpStatus.PRECONDITION_FAILED.getReasonPhrase() + " : " + MessageResponse.NULL_OBJET);
				return new ResponseEntity<PropertiesDTO>(propertiesDTO, HttpStatus.PRECONDITION_FAILED);
			}			
			if (StringUtils.isBlank(propertiesDTORequest.getName())) {
				propertiesDTO.setCodeResponse(Integer.toString(HttpStatus.PRECONDITION_FAILED.value()));
				propertiesDTO.setMessage(HttpStatus.PRECONDITION_FAILED.getReasonPhrase() + " : " + MessageResponse.NULL_PROPERTY + "(Name)");
				return new ResponseEntity<PropertiesDTO>(propertiesDTO, HttpStatus.PRECONDITION_FAILED);
			}
			if (StringUtils.isBlank(propertiesDTORequest.getTitle())) {
				propertiesDTO.setCodeResponse(Integer.toString(HttpStatus.PRECONDITION_FAILED.value()));
				propertiesDTO.setMessage(HttpStatus.PRECONDITION_FAILED.getReasonPhrase() + " : " + MessageResponse.NULL_PROPERTY + "(Title)");
				return new ResponseEntity<PropertiesDTO>(propertiesDTO, HttpStatus.PRECONDITION_FAILED);
			}
			
			
			Properties existingProperties = findByName(propertiesDTORequest.getName()); 
			if (existingProperties != null) {
				propertiesDTO.setCodeResponse(Integer.toString(HttpStatus.CONFLICT.value()));
				propertiesDTO.setMessage(HttpStatus.CONFLICT.getReasonPhrase() + " : " + MessageResponse.ELEMENT_ALREADY_EXIST);
				return new ResponseEntity<PropertiesDTO>(propertiesDTO, HttpStatus.CONFLICT);
			}
			Properties PropertiesRequest = NumArchUtils.mapPropertiesDTOToProperties(propertiesDTORequest);
			if(propertiesDTORequest.getDataType() == null) PropertiesRequest.setDataType(DataType.TEXT);
			if(propertiesDTORequest.getMandatory() == null) PropertiesRequest.setMandatory(Boolean.TRUE);			
			Properties PropertiesResponse = propertiesRepository.save(PropertiesRequest);
			
			if (PropertiesResponse != null) {
				propertiesDTO = NumArchUtils.mapPropertiesToPropertiesDTO(PropertiesResponse);
				propertiesDTO.setCodeResponse(Integer.toString(HttpStatus.OK.value()));
				propertiesDTO.setMessage(HttpStatus.OK.getReasonPhrase() + " : " + MessageResponse.SUCCESSFULL_OPERATION);
				return new ResponseEntity<PropertiesDTO>(propertiesDTO, HttpStatus.OK);
			}
			
			propertiesDTO.setCodeResponse(Integer.toString(HttpStatus.NOT_IMPLEMENTED.value()));
			propertiesDTO.setMessage(HttpStatus.NOT_IMPLEMENTED.getReasonPhrase() + " : " + MessageResponse.FAILED_OPERATION);
			return new ResponseEntity<PropertiesDTO>(propertiesDTO, HttpStatus.NOT_IMPLEMENTED);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			propertiesDTO.setCodeResponse(Integer.toString(HttpStatus.INTERNAL_SERVER_ERROR.value()));
			propertiesDTO.setMessage(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase());
			propertiesDTO.setError(e.getMessage());
			return new ResponseEntity<PropertiesDTO>(propertiesDTO, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
	}

	@Override
	public List<Properties> saveListProperties(List<Properties> properties) {
		return propertiesRepository.saveAll(properties);
	}

	@Override
	public Properties updateProperties(Properties properties) {
		return propertiesRepository.saveAndFlush(properties);
	}

	@Override
	public void deleteProperties(Long id) {
		propertiesRepository.deleteById(id);
	}

	@Override
	public List<Properties> getAllProperties() {
		// TODO Auto-generated method stub
		return propertiesRepository.findAll();
	}

}

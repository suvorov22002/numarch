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
import com.afb.dsi.numarch.dtos.ParentDTO;
import com.afb.dsi.numarch.entities.Parent;
import com.afb.dsi.numarch.enumerations.MessageResponse;
import com.afb.dsi.numarch.repository.ParentRepository;
import com.afb.dsi.numarch.services.IParentService;

@Service("parentservice")
@Transactional
public class ParentServiceImpl implements IParentService {
	
	@Autowired
	ParentRepository parenrepository;
	
	@PersistenceContext
	private EntityManager entityManager;
	
	@Override
	public Parent findByName(String name) {
		return parenrepository.findByName(name);
	}

	@Override
	public ResponseEntity<ParentDTO> saveParent(ParentDTO parentDTORequest) {
		
		ParentDTO parentDTO = new ParentDTO();
		
		try {
			if (parentDTORequest == null) {
				parentDTO.setCodeResponse(Integer.toString(HttpStatus.PRECONDITION_FAILED.value()));
				parentDTO.setMessage(HttpStatus.PRECONDITION_FAILED.getReasonPhrase() + " : " + MessageResponse.NULL_OBJET);
				return new ResponseEntity<ParentDTO>(parentDTO, HttpStatus.PRECONDITION_FAILED);
			}			
			if (StringUtils.isBlank(parentDTORequest.getName())) {
				parentDTO.setCodeResponse(Integer.toString(HttpStatus.PRECONDITION_FAILED.value()));
				parentDTO.setMessage(HttpStatus.PRECONDITION_FAILED.getReasonPhrase() + " : " + MessageResponse.NULL_PROPERTY + "(Name)");
				return new ResponseEntity<ParentDTO>(parentDTO, HttpStatus.PRECONDITION_FAILED);
			}
			if (StringUtils.isBlank(parentDTORequest.getName())) {
				parentDTO.setCodeResponse(Integer.toString(HttpStatus.PRECONDITION_FAILED.value()));
				parentDTO.setMessage(HttpStatus.PRECONDITION_FAILED.getReasonPhrase() + " : " + MessageResponse.NULL_PROPERTY + "(Name)");
				return new ResponseEntity<ParentDTO>(parentDTO, HttpStatus.PRECONDITION_FAILED);
			}
			
			
			Parent existingProperties = findByName(parentDTORequest.getName()); 
			if (existingProperties != null) {
				parentDTO.setCodeResponse(Integer.toString(HttpStatus.CONFLICT.value()));
				parentDTO.setMessage(HttpStatus.CONFLICT.getReasonPhrase() + " : " + MessageResponse.ELEMENT_ALREADY_EXIST);
				return new ResponseEntity<ParentDTO>(parentDTO, HttpStatus.CONFLICT);
			}
			Parent parentRequest = NumArchUtils.mapParentDTOToParent(parentDTORequest);
			
		
			Parent ParentResponse = parenrepository.save(parentRequest);
			
			if (ParentResponse != null) {
				parentDTO = NumArchUtils.mapParentToParentDTO(ParentResponse);
				parentDTO.setCodeResponse(Integer.toString(HttpStatus.OK.value()));
				parentDTO.setMessage(HttpStatus.OK.getReasonPhrase() + " : " + MessageResponse.SUCCESSFULL_OPERATION);
				return new ResponseEntity<ParentDTO>(parentDTO, HttpStatus.OK);
			}
			
			parentDTO.setCodeResponse(Integer.toString(HttpStatus.NOT_IMPLEMENTED.value()));
			parentDTO.setMessage(HttpStatus.NOT_IMPLEMENTED.getReasonPhrase() + " : " + MessageResponse.FAILED_OPERATION);
			return new ResponseEntity<ParentDTO>(parentDTO, HttpStatus.NOT_IMPLEMENTED);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			parentDTO.setCodeResponse(Integer.toString(HttpStatus.INTERNAL_SERVER_ERROR.value()));
			parentDTO.setMessage(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase());
			parentDTO.setError(e.getMessage());
			return new ResponseEntity<ParentDTO>(parentDTO, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@Override
	public List<Parent> saveListParent(List<Parent> parent) {
		return parenrepository.saveAll(parent);
	}

	@Override
	public List<Parent> getAllParent() {
		return parenrepository.findAll();
	}

}

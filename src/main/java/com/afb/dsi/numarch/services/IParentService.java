package com.afb.dsi.numarch.services;

import java.util.List;

import org.springframework.http.ResponseEntity;

import com.afb.dsi.numarch.dtos.ParentDTO;
import com.afb.dsi.numarch.entities.Parent;
import com.afb.dsi.numarch.entities.Properties;

public interface IParentService {
	
	public Parent findByName(String name);
	
	public ResponseEntity<ParentDTO> saveParent(ParentDTO parentDTORequest);
	
	public List<Parent> saveListParent(List<Parent> parent);
	
	public List<Parent> getAllParent();
}

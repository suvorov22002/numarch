package com.afb.dsi.numarch.services;

import java.util.List;

import org.springframework.http.ResponseEntity;

import com.afb.dsi.numarch.dtos.PropertiesDTO;
import com.afb.dsi.numarch.entities.Properties;

public interface IPropertiesService {
	
	public Properties findByName(String name);
	
	public ResponseEntity<PropertiesDTO> saveProperties(PropertiesDTO propertiesDTORequest);
	
	public List<Properties> saveListProperties(List<Properties> properties);

	public Properties updateProperties(Properties properties);

	public void deleteProperties(Long id);
	
	public List<Properties> getAllProperties();
	
}

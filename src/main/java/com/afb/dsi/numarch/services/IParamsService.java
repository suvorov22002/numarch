package com.afb.dsi.numarch.services;

import java.util.List;

import org.springframework.http.ResponseEntity;

import com.afb.dsi.numarch.dtos.ParamsDTO;
import com.afb.dsi.numarch.entities.Params;

public interface IParamsService {
	
	public Params findByCode(String code);
	
	public ResponseEntity<ParamsDTO> saveParams(ParamsDTO ParamsDTORequest);
	
	public ResponseEntity<ParamsDTO> editParams(ParamsDTO ParamsDTORequest);
	
	public List<Params> saveListParams(List<Params> params);
	
	public List<Params> getAllParams();
}


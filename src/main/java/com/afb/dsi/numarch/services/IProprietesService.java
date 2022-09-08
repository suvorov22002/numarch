package com.afb.dsi.numarch.services;

import java.util.List;

import com.afb.dsi.numarch.entities.Proprietes;

public interface IProprietesService {
	
	public List<Proprietes> findByDocument(Long id);
}

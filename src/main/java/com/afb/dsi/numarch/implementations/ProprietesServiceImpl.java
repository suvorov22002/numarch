package com.afb.dsi.numarch.implementations;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.afb.dsi.numarch.entities.Proprietes;
import com.afb.dsi.numarch.repository.ProprietesRepository;
import com.afb.dsi.numarch.services.IProprietesService;

@Service("proprietesservice")
@Transactional
public class ProprietesServiceImpl implements IProprietesService {

	@Autowired
	ProprietesRepository proprierepository;
	
	@Override
	public List<Proprietes> findByDocument(Long id) {
		return proprierepository.findByDocument(id);
	}

}

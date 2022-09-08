package com.afb.dsi.numarch.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.afb.dsi.numarch.entities.Params;

public interface ParamsRepository extends JpaRepository<Params, Long>{
	public Params findByCode(String code);
}

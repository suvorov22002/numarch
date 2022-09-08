package com.afb.dsi.numarch.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.afb.dsi.numarch.entities.Properties;

@Repository
public interface PropertiesRepository extends JpaRepository<Properties, Long>{
	
	public Properties findByName(String code);
	
	public Properties findByNameAndMandatory(String code, boolean mandatory);
	
	public List<Properties> findByTitleLike(String title);
}

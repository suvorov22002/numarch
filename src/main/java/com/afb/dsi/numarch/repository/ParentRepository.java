package com.afb.dsi.numarch.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.afb.dsi.numarch.entities.Parent;
import com.afb.dsi.numarch.entities.Properties;

public interface ParentRepository extends JpaRepository<Parent, Long> {
	public Parent findByName(String code);
}

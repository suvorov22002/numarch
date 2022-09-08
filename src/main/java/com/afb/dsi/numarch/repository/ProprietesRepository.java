package com.afb.dsi.numarch.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.afb.dsi.numarch.entities.Proprietes;

@Repository
public interface ProprietesRepository extends JpaRepository<Proprietes, Long>{
	
	@Query("SELECT p FROM Proprietes AS p WHERE p.docx.id = ?1 ")
	public List<Proprietes> findByDocument(Long documentId); 
	
}

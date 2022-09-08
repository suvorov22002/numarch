package com.afb.dsi.numarch.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.afb.dsi.numarch.entities.Documents;

@Repository
public interface DocumentsRepository extends JpaRepository<Documents, Long>{
	
	@Query("SELECT a FROM Documents AS a WHERE a.id = ?1 ")
	public Documents findByIdDocuments(Long id); 
	
	@Query("SELECT a FROM Documents AS a WHERE a.typeDocument.id = ?1 ")
	public List<Documents> findByDocuments(Long typeDocumentId); 
	
	@Query("SELECT a FROM Documents AS a ORDER BY a.dateEnvoi DESC ")
	public List<Documents> findAlls(); 
	
	public Documents findByReference(String reference);
	
	public Documents findByName(String code);
	
}

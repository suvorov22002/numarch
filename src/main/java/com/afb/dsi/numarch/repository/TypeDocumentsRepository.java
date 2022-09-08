package com.afb.dsi.numarch.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.afb.dsi.numarch.entities.TypeDocuments;

@Repository
public interface TypeDocumentsRepository extends JpaRepository<TypeDocuments, Long>{
	
	public TypeDocuments findByName(String name);
	
	public TypeDocuments findByNameAndParent(String name, String parent);
	
	public List<TypeDocuments> findByTitleLike(String title);
}

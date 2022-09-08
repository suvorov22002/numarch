package com.afb.dsi.numarch.services;

import java.util.List;
import java.util.Optional;

import com.afb.dsi.numarch.dtos.TypeDocumentsDTO;
import com.afb.dsi.numarch.entities.TypeDocuments;

public interface ITypeDocumentsService {
	
	public void deleteTypeDocuments(Long id);
	
	public Optional<TypeDocuments> findById(Long id);
	
	public TypeDocuments saveTypeDocuments(TypeDocuments documents);
	
	public boolean checkIfIdexists(Long id);
	
	public void deleteDocument(Long id);
	
	public TypeDocuments updateTypeDocuments(TypeDocuments typeDocuments);
	
	public TypeDocuments findByName(String name);
	
	public List<TypeDocuments> findTypeDocuments(TypeDocumentsDTO typeDocumentsDTO);
	
	public List<TypeDocuments> getAllTypeDocuments();
	
	public List<TypeDocuments> listAllTypeAcs();


}

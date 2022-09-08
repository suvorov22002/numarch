package com.afb.dsi.numarch.services;

import java.io.File;
import java.util.List;
import java.util.Optional;

import com.afb.dsi.numarch.dtos.DocumentsDTO;
import com.afb.dsi.numarch.entities.Documents;
import com.afb.dsi.numarch.entities.Properties;

public interface IDocumentsService {
	
	public Documents saveDocuments(DocumentsDTO documentsDTO);
	
	public List<Documents> saveListDocuments(List<Documents> documents);

	public Documents updateDocuments(Documents documents);

	public void deleteDocuments(Long id);
	
	public Optional<Documents> findById(Long id);
	
	public List<Documents> getAllDocuments();

	public boolean checkIfIdexists(Long id);
	
	public List<Documents> findByDocuments(Long documentsId);   
	
	public List<Documents> findDocuments(DocumentsDTO documents);
	
	public Documents sendToAcs(Documents docx, File file);

	public List<Documents> getAllDocument();
	
	public List<Documents> findAllReportDocs(Boolean traiter);
	
	public Documents findByName(String name);
	
	public Documents findByIdDocuments(Long id); 
	
	public Documents findByReference(String ref); 
}

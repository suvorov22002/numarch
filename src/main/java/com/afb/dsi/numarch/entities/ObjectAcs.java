package com.afb.dsi.numarch.entities;

import java.io.Serializable;

import com.afb.dsi.numarch.dtos.DocumentsDTO;
import com.fasterxml.jackson.annotation.JsonIgnore;

public class ObjectAcs implements Serializable{
	
	
	/**
	 * 
	 */
	@JsonIgnore
	private static final long serialVersionUID = 1L;
	private DocumentsDTO doc;
	private String file;
	
	public ObjectAcs(String file, DocumentsDTO doc) {
		super();
		this.file = file;
		this.doc = doc;
	}

	public ObjectAcs() {
		super();
	}

	public String getFile() {
		return file;
	}

	public void setFile(String file) {
		this.file = file;
	}

	public DocumentsDTO getDoc() {
		return doc;
	}

	public void setDoc(DocumentsDTO doc) {
		this.doc = doc;
	}
}

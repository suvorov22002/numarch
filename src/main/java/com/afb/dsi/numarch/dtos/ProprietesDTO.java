package com.afb.dsi.numarch.dtos;

import java.io.Serializable;

import com.afb.dsi.numarch.entities.Documents;
import com.fasterxml.jackson.annotation.JsonIgnore;

public class ProprietesDTO implements Serializable {

	/**
	 * 
	 */
	@JsonIgnore
	private static final long serialVersionUID = 1L;
	
	private String properties;
	
	private String valeur;
	
	private Documents docx;

	public ProprietesDTO() {
		super();	// TODO Auto-generated constructor stub
	}

	public ProprietesDTO(String properties, String valeur, Documents docx) {
		super();
		this.properties = properties;
		this.valeur = valeur;
		this.docx = docx;
	}

	public String getProperties() {
		return properties;
	}

	public void setProperties(String properties) {
		this.properties = properties;
	}

	public String getValeur() {
		return valeur;
	}

	public void setValeur(String valeur) {
		this.valeur = valeur;
	}

	public Documents getDocx() {
		return docx;
	}

	public void setDocx(Documents docx) {
		this.docx = docx;
	}
	
}

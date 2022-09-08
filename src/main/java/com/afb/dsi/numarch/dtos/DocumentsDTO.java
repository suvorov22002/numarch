package com.afb.dsi.numarch.dtos;

import java.io.Serializable;
import java.util.Date;

import com.afb.dsi.numarch.entities.TypeDocuments;
import com.fasterxml.jackson.annotation.JsonIgnore;

public class DocumentsDTO implements Serializable {

	/**
	 * 
	 */
	@JsonIgnore
	private static final long serialVersionUID = 1L;
	
	private Long id;
	
	private String reference;
	
	private String name;
	
	private String olname;
	
	private Date dateEnvoi = new Date();
	
	private TypeDocuments typeDocument;
	
	private String proprietes; //split des proprietes
	
	private boolean  traiter;
	
	private String url;
	
	private String uti;
	
	private String base64Str;
	
	private String categ;

	
	public DocumentsDTO() {
		super();
	}
	
	public DocumentsDTO(Long id, String reference, String name, String olname, Date dateEnvoi, TypeDocuments typeDocument,
			String proprietes, boolean traiter, String url,  String uti, String base64Str,  String categ) {
		super();
		this.id = id;
		this.reference = reference;
		this.name = name;
		this.olname = olname;
		this.dateEnvoi = dateEnvoi;
		this.typeDocument = typeDocument;
		this.proprietes = proprietes;
		this.traiter = traiter;
		this.url = url;
		this.uti = uti;
		this.base64Str = base64Str;
		this.categ = categ;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getReference() {
		return reference;
	}

	public void setReference(String reference) {
		this.reference = reference;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getOlname() {
		return olname;
	}

	public void setOlname(String olname) {
		this.olname = olname;
	}

	public Date getDateEnvoi() {
		return dateEnvoi;
	}

	public void setDateEnvoi(Date dateEnvoi) {
		this.dateEnvoi = dateEnvoi;
	}

	public TypeDocuments getTypeDocument() {
		return typeDocument;
	}

	public void setTypeDocument(TypeDocuments typeDocument) {
		this.typeDocument = typeDocument;
	}

	public String getProprietes() {
		return proprietes;
	}

	public void setProprietes(String proprietes) {
		this.proprietes = proprietes;
	}

	public boolean isTraiter() {
		return traiter;
	}

	public void setTraiter(boolean traiter) {
		this.traiter = traiter;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}
	
	public String getUti() {
		return uti;
	}

	public void setUti(String uti) {
		this.uti = uti;
	}

	public String getBase64Str() {
		return base64Str;
	}

	public void setBase64Str(String base64Str) {
		this.base64Str = base64Str;
	}

	public String getCateg() {
		return categ;
	}

	public void setCateg(String categ) {
		this.categ = categ;
	}
	
	
}

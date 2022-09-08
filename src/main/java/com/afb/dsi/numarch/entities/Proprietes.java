package com.afb.dsi.numarch.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "NUMARCH_PROPRIETES")
public class Proprietes extends AbstractValidFrom {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Column(name = "properties")
	private String properties;
	
	@Column(name = "valeur")
	private String valeur;
	
	@ManyToOne
	@JoinColumn(name="documents", referencedColumnName="id")
	private Documents docx;

	public Proprietes() {
		super();
	}

	public Proprietes(String properties, String valeur, Documents docx) {
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

package com.afb.dsi.numarch.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "NUMARCH_PARAMS")
public class Params extends AbstractValidFrom{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Column(name = "code")
	private String code;
	
	@Column(name = "libelle")
	private String libelle;
	
	@Column(name = "valeur")
	private String valeur;
	
	@Column(name = "actif")
	private boolean actif;

	public Params() {
		super();
	}

	public Params(String code, String libelle, String valeur, boolean actif) {
		super();
		this.code = code;
		this.libelle = libelle;
		this.valeur = valeur;
		this.actif = actif;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getLibelle() {
		return libelle;
	}

	public void setLibelle(String libelle) {
		this.libelle = libelle;
	}

	public String getValeur() {
		return valeur;
	}

	public void setValeur(String valeur) {
		this.valeur = valeur;
	}

	public boolean isActif() {
		return actif;
	}

	public void setActif(boolean actif) {
		this.actif = actif;
	}
	
}

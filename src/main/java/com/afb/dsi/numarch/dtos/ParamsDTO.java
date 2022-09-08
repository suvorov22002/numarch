package com.afb.dsi.numarch.dtos;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class ParamsDTO extends ResponseBase{

	/**
	 * 
	 */
	@JsonIgnore
	private static final long serialVersionUID = 1L;
	
	private String code;
	private String libelle;
	private String valeur;
	private boolean actif;
	
	public ParamsDTO() {
		super();
	}

	public ParamsDTO(String codeResponse, String error, String code, String libelle, String valeur, boolean actif) {
		super(codeResponse, error);
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

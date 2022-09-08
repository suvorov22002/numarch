package com.afb.dsi.numarch.enumerations;

public enum MessageResponse {
	

	ELEMENT_ALREADY_EXIST("Element deja enregistre"), 
	
	NULL_OBJET("Element vide ou nulle"), 
	
	NULL_PROPERTY("Champ vide ou nulle"), 
	
	SUCCESSFULL_OPERATION("Opération réussie"), 
	
	FAILED_OPERATION("Opération en échec"), 
	

	
	/** SUCCESS. */
	SUCCESS("SUCCESS"), 

	/** ERROR. */
	ERROR("ERROR"); 
	
	private final String value;
	
	private MessageResponse(String value) {
		this.value = value;
	}
	
	/**
	 * Obtention de la valeur 
	 * @return	Valeur
	 */
	public String value() {
		return value;
	}

	/**
	 * Obtention de la valeur 
	 * @return	Valeur
	 */
	public String getValue() {
		return value;
	}
}

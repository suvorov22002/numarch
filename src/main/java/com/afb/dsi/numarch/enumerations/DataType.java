package com.afb.dsi.numarch.enumerations;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public enum DataType {
	
	/**
	 * TEXT
	 */
	TEXT("TEXT"),
	
	
	/**
	 * DATE
	 */
	DATE("DATE"),
	
	
	/**
	 * CONTENT
	 */
	CONTENT("CONTENT"),
	
	
	/**
	 * NUMBER
	 */
	NUMBER("NUMBER"),
	
	
	/**
	 * COMBOBOX
	 */
	COMBOBOX("COMBOBOX");



	/**
	 * Valeur de la constante (Code)
	 */
	private final String value;

	/**
	 * Liste des Types d'entr�es
	 */
	private static final List<DataType> types = new ArrayList<DataType>();
	

	static { 
		types.add(COMBOBOX); 
		types.add(CONTENT); 
		types.add(DATE); 
		types.add(NUMBER);
		Collections.sort(types);
	}
	
	

	/**
	 * Constructeur avec initialisation des parametres
	 * @param value	Valeur du type d'entr�es
	 */
	private DataType(String value) {

		// Initialisation de la valeur
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

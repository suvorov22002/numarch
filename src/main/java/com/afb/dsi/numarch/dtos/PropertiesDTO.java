package com.afb.dsi.numarch.dtos;

import com.afb.dsi.numarch.enumerations.DataType;
import com.fasterxml.jackson.annotation.JsonIgnore;

public class PropertiesDTO extends ResponseBase {

	/**
	 * 
	 */
	@JsonIgnore
	private static final long serialVersionUID = 1L;
	
	private String name;
	
	private String title;
	
	private String description;
	
	private DataType dataType;
	
	private String defaultValue;
	
	private Boolean multiValued;
	
	private Boolean mandatory;
	
	private Boolean enforced;
	
	private Boolean protect;
	
	private Boolean indexed;

	public PropertiesDTO(String name, String title, String description, DataType dataType, String defaultValue,
			Boolean multiValue, Boolean mandatory, Boolean enforced, Boolean protect, Boolean indexed) {
		super();
		this.name = name;
		this.title = title;
		this.description = description;
		this.dataType = dataType;
		this.defaultValue = defaultValue;
		this.multiValued = multiValue;
		this.mandatory = mandatory;
		this.enforced = enforced;
		this.protect = protect;
		this.indexed = indexed;
	}

	public PropertiesDTO() {
		super();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public DataType getDataType() {
		return dataType;
	}

	public void setDataType(DataType dataType) {
		this.dataType = dataType;
	}

	public String getDefaultValue() {
		return defaultValue;
	}

	public void setDefaultValue(String defaultValue) {
		this.defaultValue = defaultValue;
	}

	public Boolean getMultiValued() {
		return multiValued;
	}

	public void setMultiValued(Boolean multiValue) {
		this.multiValued = multiValue;
	}

	public Boolean getMandatory() {
		return mandatory;
	}

	public void setMandatory(Boolean mandatory) {
		this.mandatory = mandatory;
	}

	public Boolean getEnforced() {
		return enforced;
	}

	public void setEnforced(Boolean enforced) {
		this.enforced = enforced;
	}

	public Boolean getProtect() {
		return protect;
	}

	public void setProtect(Boolean protect) {
		this.protect = protect;
	}

	public Boolean getIndexed() {
		return indexed;
	}

	public void setIndexed(Boolean indexed) {
		this.indexed = indexed;
	}
	
}

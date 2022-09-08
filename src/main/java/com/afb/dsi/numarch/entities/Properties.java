package com.afb.dsi.numarch.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;

import com.afb.dsi.numarch.enumerations.DataType;

@Entity
@Table(name = "NUMARCH_PROPERTIES")
public class Properties extends AbstractValidFrom {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Column(name = "name", unique = true, nullable = false)
	private String name;
	
	@Column(name = "title")
	private String title;
	
	@Column(name = "description")
	private String description;
	
	@Column(name = "date_type", nullable = false)
	@Enumerated(EnumType.STRING)
	private DataType dataType;
	
	@Column(name = "defaultValue")
	private String defaultValue;
	
	@Column(name = "multiValue", columnDefinition = "boolean default false")
	private Boolean multiValued = false;
	
	@Column(name = "mandatory", columnDefinition = "boolean default false")
	private Boolean mandatory = false;
	
	@Column(name = "enforced", columnDefinition = "boolean default false")
	private Boolean enforced = false;
	
	@Column(name = "protect", columnDefinition = "boolean default false")
	private Boolean protect = false;
	
	@Column(name = "indexed", columnDefinition = "boolean default false")
	private Boolean indexed = false;

	public Properties() {
		super();
	}

	public Properties(String name, String title, String description, DataType dataType, String defaultValue,
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

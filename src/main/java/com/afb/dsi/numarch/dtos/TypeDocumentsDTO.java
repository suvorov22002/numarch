package com.afb.dsi.numarch.dtos;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class TypeDocumentsDTO extends ResponseBase {
	
	/**
	 * 
	 */
	@JsonIgnore
	private static final long serialVersionUID = 1L;
	
	private Long id;
	private String name;
	private String description;
	private Boolean isAspect;
	private Boolean isContainer;
	private String parent;
	private String propertie;
	private String title;
	private String defaultAspects;
	
	public TypeDocumentsDTO() {
		super();
	}

	public TypeDocumentsDTO(String codeResponse, String error, Long id, String name, String description,
			Boolean isAspect, Boolean isContainer, String parent, String propertie, String title, String defaultAspects) {
		super(codeResponse, error);
		this.id = id;
		this.name = name;
		this.description = description;
		this.isAspect = isAspect;
		this.isContainer = isContainer;
		this.parent = parent;
		this.propertie = propertie;
		this.title = title;
		this.defaultAspects = defaultAspects;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Boolean getIsAspect() {
		return isAspect;
	}

	public void setIsAspect(Boolean isAspect) {
		this.isAspect = isAspect;
	}

	public Boolean getIsContainer() {
		return isContainer;
	}

	public void setIsContainer(Boolean isContainer) {
		this.isContainer = isContainer;
	}

	public String getParent() {
		return parent;
	}

	public void setParent(String parent) {
		this.parent = parent;
	}

	public String getPropertie() {
		return propertie;
	}

	public void setPropertie(String propertie) {
		this.propertie = propertie;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDefaultAspects() {
		return defaultAspects;
	}

	public void setDefaultAspects(String defaultAspects) {
		this.defaultAspects = defaultAspects;
	}
	
	

}

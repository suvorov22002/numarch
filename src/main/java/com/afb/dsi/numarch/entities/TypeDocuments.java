package com.afb.dsi.numarch.entities;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name = "NUMARCH_TYPE_DOCUMENTS")
public class TypeDocuments extends AbstractValidFrom {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Column(name = "name", unique = true, nullable = false)
	private String name;
	
	@Column(name = "description")
	private String description;
	
	@Column(name = "title")
	private String title;
	
	@Column(name = "aspect", columnDefinition = "boolean default false")
	private Boolean isAspect = false;
	
	@Column(name = "container", columnDefinition = "boolean default false")
	private Boolean isContainer = false;
	
	@Column(name = "parent")
	private String parent; //set only parent name
	
	@Column(name = "propertie", length = 512)
	private String propertie;
	
//	@Transient
//	private List<Properties> properties;
	
	@Column(name = "defaultaspects", length = 512)
	private String defaultAspects;
	
	@Transient
	private Object associations;
	
	@Transient
	private Object childassociations;
	
	@Column(name = "url", length = 512)
	private String url;

	public TypeDocuments() {
		super();
	}

	public TypeDocuments(String name, String description, String title, Boolean isAspect, Boolean isContainer,
			String parent, String propertie, List<Properties> properties, String defaultAspects, Object associations,
			Object childassociations, String url) {
		super();
		this.name = name;
		this.description = description;
		this.title = title;
		this.isAspect = isAspect;
		this.isContainer = isContainer;
		this.parent = parent;
		this.propertie = propertie;
	//	this.properties = properties;
		this.defaultAspects = defaultAspects;
		this.associations = associations;
		this.childassociations = childassociations;
		this.url = url;
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

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
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

//	public String getPropertie() {
//		propertie = "";
//		for(Properties p : properties) {
//			if(StringUtils.equals(propertie, "")) {
//				propertie = propertie + p.getName();
//			}
//			else {
//				propertie = propertie + "/" + p.getName();
//			}
//			
//		}
//		return propertie;
//	}

	public void setPropertie(String propertie) {
		this.propertie = propertie;
	}

//	public List<Properties> getProperties() {
//		return properties;
//	}
//
//	public void setProperties(List<Properties> properties) {
//		this.properties = properties;
//	}

	public String getDefaultAspects() {
		return defaultAspects;
	}

	public void setDefaultAspects(String defaultAspects) {
		this.defaultAspects = defaultAspects;
	}

	public Object getAssociations() {
		return associations;
	}

	public void setAssociations(Object associations) {
		this.associations = associations;
	}

	public Object getChildassociations() {
		return childassociations;
	}

	public void setChildassociations(Object childassociations) {
		this.childassociations = childassociations;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

}

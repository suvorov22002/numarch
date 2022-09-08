package com.afb.dsi.numarch.dtos;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class ParentDTO extends ResponseBase {

	/**
	 * 
	 */
	@JsonIgnore
	private static final long serialVersionUID = 1L;
	
	private String name;
	private String title;
	private String url;
	
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
	
	public String getUrl() {
		return url;
	}
	
	public void setUrl(String url) {
		this.url = url;
	}
	
	public ParentDTO(String name, String title, String url) {
		super();
		this.name = name;
		this.title = title;
		this.url = url;
	}
	
	public ParentDTO() {
		super();
	}
	
}

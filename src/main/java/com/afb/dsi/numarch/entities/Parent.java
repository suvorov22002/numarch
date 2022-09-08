package com.afb.dsi.numarch.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "NUMARCH_PARENT")
public class Parent extends AbstractValidFrom{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Column(name = "name", unique = true, nullable = false)
	private String name;
	
	@Column(name = "title")
	private String title;
	
	@Column(name = "url")
	private String url;

	public Parent(String name, String title, String url) {
		super();
		this.name = name;
		this.title = title;
		this.url = url;
	}

	public Parent() {
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

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}
	
}

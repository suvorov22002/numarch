package com.afb.dsi.numarch.entities;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.apache.commons.lang3.StringUtils;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "NUMARCH_DOCUMENTS")
public class Documents extends AbstractValidFrom {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Column(name = "reference", unique = true, nullable = false)
	private String reference;
	
	@Column(name = "traiter")
	private Boolean traiter;
	
	@Column(name = "name")
	private String name;
	
	@Column(name = "oldname")
	private String olname;
	
	@Column(name = "url")
	private String url;
	
	@Column(name = "uti")
	private String uti;
	
	@Column(name = "base64Str")
	private String base64Str;
	
	@Column(name = "categ")
	private String categ;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "date_sai")
	private Date dateEnvoi = new Date();
	
	@ManyToOne
	@JoinColumn(name = "type_document")
	private TypeDocuments typeDocument;
	
	@JsonIgnore
	@OneToMany(mappedBy = "docx", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	private List<Proprietes> docPropriete = new ArrayList<Proprietes>();

	
	@Transient
	private String proprietes;

	public Documents(String reference, Boolean traiter, String name, String olname, Date dateEnvoi, TypeDocuments typeDocument,
			List<Proprietes> docPropriete, String proprietes, String url, String uti, String base64Str, String categ) {
		super();
		this.reference = reference;
		this.traiter = traiter;
		this.name = name;
		this.olname = olname;
		this.dateEnvoi = dateEnvoi;
		this.typeDocument = typeDocument;
		this.docPropriete = docPropriete;
		this.proprietes = proprietes;
		this.url = url;
		this.uti = uti;
		this.base64Str = base64Str;
		this.categ = categ;
	}

	public String getOlname() {
		return olname;
	}

	public void setOlname(String olname) {
		this.olname = olname;
	}

	public Documents() {
		super();
	}

	public String getReference() {
		return reference;
	}

	public void setReference(String reference) {
		this.reference = reference;
	}

	public TypeDocuments getTypeDocument() {
		return typeDocument;
	}

	public void setTypeDocument(TypeDocuments typeDocument) {
		this.typeDocument = typeDocument;
	}

	public Boolean getTraiter() {
		return traiter;
	}

	public void setTraiter(Boolean traiter) {
		this.traiter = traiter;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Date getDateEnvoi() {
		return dateEnvoi;
	}

	public void setDateEnvoi(Date dateEnvoi) {
		this.dateEnvoi = dateEnvoi;
	}

	public List<Proprietes> getDocPropriete() {
		return docPropriete;
	}

	public void setDocPropriete(List<Proprietes> docPropriete) {
		this.docPropriete = docPropriete;
	}

	public String getProprietes() {
		if(!docPropriete.isEmpty()) {
			for(Proprietes e : docPropriete) {
				if(StringUtils.isBlank(proprietes)) {
					proprietes = e.getProperties()+";"+e.getValeur();
				}
				else {
					proprietes = proprietes + "/" + e.getProperties()+";"+e.getValeur();
				}
			}
		}
		return proprietes;
	}

	public void setProprietes(String proprietes) {
		this.proprietes = proprietes;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getUti() {
		return uti;
	}

	public void setUti(String uti) {
		this.uti = uti;
	}

	public String getBase64Str() {
		return base64Str;
	}

	public void setBase64Str(String base64Str) {
		this.base64Str = base64Str;
	}

	public String getCateg() {
		return categ;
	}

	public void setCateg(String categ) {
		this.categ = categ;
	}
	
}

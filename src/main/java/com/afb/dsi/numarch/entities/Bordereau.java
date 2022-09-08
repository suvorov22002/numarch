package com.afb.dsi.numarch.entities;

public class Bordereau {
	
	private String eve;
	private String age;
	private String ncp;
	private String recip;
	private String cle;
	private String dco;
	private String uti;
	private double mon;
	private String type;
	private String filename;
	private String olfilename;
	private String base64;
	
	public Bordereau(String eve, String age, String ncp, String recip, String cle, String dco, String uti, double mon, String type,  String filename, String olfilename, String base64) {
		super();
		this.eve = eve;
		this.age = age;
		this.ncp = ncp;
		this.recip = recip;
		this.cle = cle;
		this.dco = dco;
		this.uti = uti;
		this.mon = mon;
		this.type = type;
		this.filename = filename;
		this.olfilename = olfilename;
		this.base64 = base64;
	}

	public Bordereau() {
		super();
	}

	public String getEve() {
		return eve;
	}

	public void setEve(String eve) {
		this.eve = eve;
	}

	public String getAge() {
		return age;
	}

	public void setAge(String age) {
		this.age = age;
	}

	public String getNcp() {
		return ncp;
	}

	public void setNcp(String ncp) {
		this.ncp = ncp;
	}

	public String getCle() {
		return cle;
	}

	public void setCle(String cle) {
		this.cle = cle;
	}

	public String getDco() {
		return dco;
	}

	public void setDco(String dco) {
		this.dco = dco;
	}

	public String getUti() {
		return uti;
	}

	public void setUti(String uti) {
		this.uti = uti;
	}

	public double getMon() {
		return mon;
	}

	public void setMon(double mon) {
		this.mon = mon;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getFilename() {
		return filename;
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}

	public String getOlfilename() {
		return olfilename;
	}

	public void setOlfilename(String olfilename) {
		this.olfilename = olfilename;
	}

	public String getRecip() {
		return recip;
	}

	public void setRecip(String recip) {
		this.recip = recip;
	}

	public String getBase64() {
		return base64;
	}

	public void setBase64(String base64) {
		this.base64 = base64;
	}

}

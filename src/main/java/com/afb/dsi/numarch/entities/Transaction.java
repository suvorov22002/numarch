package com.afb.dsi.numarch.entities;

public class Transaction extends Bordereau{
	
	private Boolean decode;
	
	public Transaction() {
		
	}

	public Boolean getDecode() {
		return decode;
	}

	public void setDecode(Boolean decode) {
		this.decode = decode;
	}
	
	public String toString() {
		return "DECODE: "+this.getDecode()+"\nREF: "+this.getEve()+"\nAGENCE: "+this.getAge()+"\nCOMPTE: "+this.getNcp()+"\nDESTINATAIRE: "+this.getRecip()+"\nCLE: "+this.getCle()+"\nDCO: "+this.getDco()+
				"\nUTI: "+this.getUti()+"\nMONTANT: "+this.getMon()+"\nTYPE: "+this.getType()+"\nFILE: "+this.getFilename();
	}
}

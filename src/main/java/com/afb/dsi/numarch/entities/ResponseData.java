package com.afb.dsi.numarch.entities;

import java.util.ArrayList;
import java.util.List;

import com.afb.dsi.numarch.dtos.ResponseBase;

public class ResponseData extends ResponseBase{
	
	private List ltrx;

	public List getLtrx() {
		return ltrx;
	}

	public void setLtrx(List ltrx) {
		this.ltrx = ltrx;
	}
	
}

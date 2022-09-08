package com.afb.dsi.numarch.dtos;

import java.io.Serializable;

import org.apache.commons.lang3.StringUtils;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class ResponseBase implements Serializable {
	
	@JsonIgnore
	private static final long serialVersionUID = 1L;

	private String codeResponse; 

	private String error;
	
	private String message;
	
	
	/**
	 * @param codeResponse
	 * @param error
	 */
	public ResponseBase(String codeResponse, String error) {
		super();
		this.setCodeResponse(codeResponse);
		this.setError(error);
		this.setError(ResponseHolder.mapMessage.get(this.getCodeResponse()));
	}

	public Boolean status() {
		if(StringUtils.equalsIgnoreCase(this.getCodeResponse(), ResponseHolder.SUCESS)) return Boolean.TRUE;
		else return Boolean.FALSE;
	}
	
	public ResponseBase error(String codeResponse) {
		this.setCodeResponse(codeResponse);
		this.setError(ResponseHolder.mapMessage.get(codeResponse));
		return this;
	}

	/**
	 * 
	 */
	public ResponseBase() {
		super();
	}

	/**
	 * @return the codeResponse
	 */
	public String getCodeResponse() {
		return codeResponse;
	}

	/**
	 * @param codeResponse the code to set
	 */
	public void setCodeResponse(String codeResponse) {
		this.codeResponse = codeResponse;
	}

	/**
	 * @return the error
	 */
	public String getError() {
		return error;
	}

	/**
	 * @param error the error to set
	 */
	public void setError(String error) {
		this.error = error;
	}

	/**
	 * @return the message
	 */
	public String getMessage() {
		return message;
	}

	/**
	 * @param message the message to set
	 */
	public void setMessage(String message) {
		this.message = message;
	}
}

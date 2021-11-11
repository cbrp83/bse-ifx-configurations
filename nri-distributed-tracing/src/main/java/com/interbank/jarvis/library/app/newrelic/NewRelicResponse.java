// Copyright (c) 2020 - Interbank 
package com.interbank.jarvis.library.app.newrelic;

import java.io.Serializable;


/**
 * Name: NewRelicResponse
 * @author Edert Ochoa
 * @date 2021-02-01
 * @version 1.0.0
 * 
 * **/
public class NewRelicResponse implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private String requestId;

	/**
	 * @return the requestId
	 */
	public String getRequestId() {
		return requestId;
	}

	/**
	 * @param requestId the requestId to set
	 */
	public void setRequestId(String requestId) {
		this.requestId = requestId;
	}
	
	
}
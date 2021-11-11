// Copyright (c) 2020 - Interbank  
package com.interbank.jarvis.library.app.entities;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

/**
 * Name: PropertiesBean
 * @author Edert Ochoa
 * @date 2021-02-01
 * @version 1.0.0
 * 
 * **/
@Configuration
@PropertySource(value = "classpath:Apps/Common/config/application-config-common.yml", factory = YamlProperties.class)
@ConfigurationProperties("common")
public class PropertiesBean {
	
	private String newrelicLicence;
	private String newrelicURL;
	private String newrelicApiKey;
	
	/**
	 * @return the newrelicApiKey
	 */
	public String getNewrelicApiKey() {
		return newrelicApiKey;
	}
	/**
	 * @param newrelicApiKey the newrelicApiKey to set
	 */
	public void setNewrelicApiKey(String newrelicApiKey) {
		this.newrelicApiKey = newrelicApiKey;
	}
	/**
	 * @return the newrelicURL
	 */
	public String getNewrelicURL() {
		return newrelicURL;
	}
	/**
	 * @param newrelicURL the newrelicURL to set
	 */
	public void setNewrelicURL(String newrelicURL) {
		this.newrelicURL = newrelicURL;
	}
	/**
	 * @return the newrelicLicence
	 */
	public String getNewrelicLicence() {
		return newrelicLicence;
	}
	/**
	 * @param newrelicLicence the newrelicLicence to set
	 */
	public void setNewrelicLicence(String newrelicLicence) {
		this.newrelicLicence = newrelicLicence;
	}
}
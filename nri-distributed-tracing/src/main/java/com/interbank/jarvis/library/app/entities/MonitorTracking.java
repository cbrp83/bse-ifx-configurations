// Copyright (c) 2020 - Interbank 
package com.interbank.jarvis.library.app.entities;

import java.io.Serializable;
import java.util.Base64;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Name: MonitorTracking
 * @author Edert Ochoa
 * @date 2021-02-01
 * @version 1.0.0
 * 
 * */
@Component
public class MonitorTracking implements Serializable {

	private static final Logger logger = LogManager.getLogger(MonitorTracking.class);
	/**
	 * 
	 */
	
	private static final long serialVersionUID = 1L;
	private String appKey;
	private String canalKey;
	private String deviceId;
	private String deviceType;
	private String cardNumber;
	private String serviceId;
	private String ipUsu;
	private String userAgent;
	
	public MonitorTracking() {	}
	
	/**
	 * @return the appKey
	 */
	public String getAppKey() {
		return appKey;
	}
	/**
	 * @param appKey the appKey to set
	 */
	public void setAppKey(String appKey) {
		this.appKey = appKey;
	}
	/**
	 * @return the canalKey
	 */
	public String getCanalKey() {
		return canalKey;
	}
	/**
	 * @param canalKey the canalKey to set
	 */
	public void setCanalKey(String canalKey) {
		this.canalKey = canalKey;
	}
	/**
	 * @return the deviceId
	 */
	public String getDeviceId() {
		return deviceId;
	}
	/**
	 * @param deviceId the deviceId to set
	 */
	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}
	/**
	 * @return the deviceType
	 */
	public String getDeviceType() {
		return deviceType;
	}
	/**
	 * @param deviceType the deviceType to set
	 */
	public void setDeviceType(String deviceType) {
		this.deviceType = deviceType;
	}
	/**
	 * @return the cardNumber
	 */
	public String getCardNumber() {
		return cardNumber;
	}
	/**
	 * @param cardNumber the cardNumber to set
	 */
	public void setCardNumber(String cardNumber) {
		this.cardNumber = cardNumber;
	}
	/**
	 * @return the serviceId
	 */
	public String getServiceId() {
		return serviceId;
	}
	/**
	 * @param serviceId the serviceId to set
	 */
	public void setServiceId(String serviceId) {
		this.serviceId = serviceId;
	}
	/**
	 * @return the ipUsu
	 */
	public String getIpUsu() {
		return ipUsu;
	}
	/**
	 * @param ipUsu the ipUsu to set
	 */
	public void setIpUsu(String ipUsu) {
		this.ipUsu = ipUsu;
	}
	/**
	 * @return the userAgent
	 */
	public String getUserAgent() {
		return userAgent;
	}
	/**
	 * @param userAgent the userAgent to set
	 */
	public void setUserAgent(String userAgent) {
		this.userAgent = userAgent;
	}
	
	public static MonitorTracking getMonitorTrackingData(String monitorTrackingEncode) {
		MonitorTracking mObj = new MonitorTracking();
		try {
			ObjectMapper mapper = new ObjectMapper(); 
			byte[] decodedBytes = Base64.getDecoder().decode(monitorTrackingEncode);
			String decodedString = new String(decodedBytes);
			String cleanDecode = decodedString.replaceAll("User-Agent", "userAgent");
		    mObj = mapper.readValue(cleanDecode, MonitorTracking.class);
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
		return mObj;
	}
	
}

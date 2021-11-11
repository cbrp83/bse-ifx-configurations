// Copyright (c) 2020 - Interbank 
package com.interbank.jarvis.library.app.newrelic;

import java.util.Date;

/**
 * Name: NewRelicMapper
 * @author Edert Ochoa
 * @date 2021-02-01
 * @version 1.0.0
 * 
 * **/
public class NewRelicMapper {

	/***
	 * Name: mapResponse
	 * @param String applicationId
	 * @param String componentName
	 * @param String componentType
	 * @param String traceId
	 * @param String message
	 * @param String operationName
	 * @param String serviceId
	 * @param String level
	 * @param String parentId
	 * @param String spanId
	 * @return NewRelicRequest request
	 * 
	 * */
	public NewRelicRequest mapResponse(String applicationId, String componentName, String componentType,
			String traceId, String message, String operationName, String serviceId, String level, String parentId, String spanId) {
		NewRelicRequest requestNewRelic = new NewRelicRequest();
		requestNewRelic.setCurrentDate(String.valueOf((new Date()).getTime()));
		requestNewRelic.setApplicationId(applicationId);
		requestNewRelic.setComponentName(componentName);
		requestNewRelic.setComponentType(componentType);
		requestNewRelic.setLevel(level);
		requestNewRelic.setTraceId(traceId);
		requestNewRelic.setMessage(message);
		requestNewRelic.setOperationName(operationName);
		requestNewRelic.setServiceId(serviceId);
		requestNewRelic.setParentId(parentId);
		requestNewRelic.setSpanId(spanId);
		return requestNewRelic;
	}
}

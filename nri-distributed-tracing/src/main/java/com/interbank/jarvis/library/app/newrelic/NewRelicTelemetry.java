// Copyright (c) 2020 - Interbank 
package com.interbank.jarvis.library.app.newrelic;

import java.util.ArrayList;
import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;

import com.interbank.jarvis.library.app.entities.PropertiesBean;
import com.newrelic.telemetry.Attributes;
import com.newrelic.telemetry.OkHttpPoster;
import com.newrelic.telemetry.SpanBatchSenderFactory;
import com.newrelic.telemetry.spans.Span;
import com.newrelic.telemetry.spans.SpanBatch;
import com.newrelic.telemetry.spans.SpanBatchSender;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Name: NewRelicTelemery
 * @author Edert Ochoa
 * @date 2021-02-01
 * @date 2020-12-16
 * 
 * **/
@Component
public class NewRelicTelemetry {

	private final Logger logger = LogManager.getLogger(this.getClass());
	
	@Autowired
	private PropertiesBean propertiesBean;
	
	/**
	 * Send Distributed Trace Transaction to New Relic
	 * @param NewRelicRequest request
	 * @param long spanStart
	 * @throws Exception
	 * 
	 * **/
	public void sendTraceTransaction(NewRelicRequest request) throws Exception {
		
		new Thread() {
			@Override
			public void run() {
				try {
					SpanBatchSender sender =
					        SpanBatchSender.create(
					            SpanBatchSenderFactory.fromHttpImplementation(OkHttpPoster::new)
					                .configureWith(propertiesBean.getNewrelicApiKey())
					                .auditLoggingEnabled(true)
					                .build());
					    List<Span> spans = new ArrayList<>();
					    spans.add(
					        Span.builder(request.getSpanId())
					            .traceId(request.getTraceId())
					            .name(request.getOperationName())
					            .parentId(request.getParentId())
					            .durationMs(request.getDurationTime())
					            .timestamp(request.getSpanStartTime())
					            .serviceName(request.getApplicationName())
					            .build());
					    sender.sendBatch(new SpanBatch(spans, getCommonAttributes(request), request.getTraceId()));			
				}catch (Exception e) {
					logger.error("Logs: " + e.getMessage());
				}
			}
		}.start();
	}
	
	 /***
	  * Static attributes
	  * @param NewRelicRequest newRelicRequest
	  * @return Attributes attributes
	  * 
	  * */
	  private static Attributes getCommonAttributes(NewRelicRequest newRelicRequest) {
		Attributes attributes = new Attributes();
		attributes.put("componentType", newRelicRequest.getComponentType());
		attributes.put("componentName", newRelicRequest.getComponentName());
		attributes.put("customerCode", newRelicRequest.getCustomerCode());
		attributes.put("operationName", newRelicRequest.getOperationName());
		attributes.put("applicationId", newRelicRequest.getApplicationId());
	    return attributes;    
	  }
	
}

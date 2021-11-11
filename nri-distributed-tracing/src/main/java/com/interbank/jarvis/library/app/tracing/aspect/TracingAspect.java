package com.interbank.jarvis.library.app.tracing.aspect;

import java.util.Arrays;
import java.util.Date;
import java.util.UUID;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;

import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;
import org.springframework.util.StopWatch.TaskInfo;

import com.interbank.jarvis.library.app.entities.MonitorTracking;
import com.interbank.jarvis.library.app.newrelic.NewRelicRequest;
import com.interbank.jarvis.library.app.newrelic.NewRelicTelemetry;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Name: TracingAspect
 * @author Edert Ochoa
 * @date 2021-02-01
 * @version 1.0.0
 * 
 * **/

@Aspect
@Component
public class TracingAspect {

	public static final Logger logger = LoggerFactory.getLogger(TracingAspect.class);

	@Autowired
	private NewRelicTelemetry newRelicTelemetry;
	

	/**
	 * This method uses Around advice which ensures that an advice can run before
	 * and after the method execution, to and log the execution time of the method
	 * This advice will be be applied to all the method which are annotate with the
	 * annotation @DistributedTracingNR @see
	 * com.interbank.jarvis.app.tracing.DistributedTracingNR
	 * 
	 * Any method where execution times need to be measue, anotate the method
	 * with @DistributedTracingNR example
	 * 
	 * @DistributedTracingNR public void m1();
	 * 
	 * @param proceedingJoinPoint
	 * @return
	 * @throws Throwable
	 */
	@Around("@annotation(com.interbank.jarvis.library.app.tracing.aspect.DistributedTracingNR)")
	public Object methodTimeLogger(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
		MethodSignature methodSignature = (MethodSignature) proceedingJoinPoint.getSignature();

		// Generate startTime and unique spanId for New Relic
		Long spanStartTime = System.currentTimeMillis();
		String spanId = UUID.randomUUID().toString();

		// Get intercepted method details
		String className = methodSignature.getDeclaringType().getSimpleName();
		String operationName = methodSignature.getName();
		String componentType = ((MethodSignature) proceedingJoinPoint.getSignature()).getMethod()
				.getAnnotation(DistributedTracingNR.class).componentType();
		String componentName = ((MethodSignature) proceedingJoinPoint.getSignature()).getMethod()
				.getAnnotation(DistributedTracingNR.class).componentName();
		String applicationName = ((MethodSignature) proceedingJoinPoint.getSignature()).getMethod()
				.getAnnotation(DistributedTracingNR.class).applicationName();

		// Measure method execution time
		StopWatch stopWatch = new StopWatch(className + "->" + operationName);
		stopWatch.start(operationName);

		Object[] signatureArgs = proceedingJoinPoint.getArgs();
		String[] parametersName = methodSignature.getParameterNames();

		int indexMonitorTracking = Arrays.asList(parametersName).indexOf("monitorTracking");
		int indexTraceId = Arrays.asList(parametersName).indexOf("traceId");
		int indexParentId = Arrays.asList(parametersName).indexOf("parentId");
		int indexSpanId = Arrays.asList(parametersName).indexOf("spanId");
		int indexConsumerId = Arrays.asList(parametersName).indexOf("consumerId");

		// Modify args(spanId) from method
		signatureArgs[indexSpanId] = spanId;

		Object result = proceedingJoinPoint.proceed(signatureArgs);
		stopWatch.stop();

		// Calculate duration time
		TaskInfo taskInfo = stopWatch.getLastTaskInfo();
		long durationTime = taskInfo.getTimeMillis();

		// Get parameters
		String monitorTracking = signatureArgs[indexMonitorTracking].toString();
		String traceId = signatureArgs[indexTraceId].toString();
		String parentId = signatureArgs[indexParentId].toString();
		String consumerId = signatureArgs[indexConsumerId].toString();

		// Call NewRelic Telemetry SDK
		NewRelicRequest requestNewRelic = new NewRelicRequest();
		requestNewRelic.setCurrentDate(String.valueOf((new Date()).getTime()));
		requestNewRelic.setApplicationName(applicationName);
		requestNewRelic.setComponentName(componentName);
		requestNewRelic.setComponentType(componentType);
		requestNewRelic.setTraceId(traceId);
		requestNewRelic.setOperationName(operationName);
		requestNewRelic.setParentId(parentId);
		requestNewRelic.setSpanId(spanId);
		requestNewRelic.setApplicationId(consumerId);
		requestNewRelic.setDurationTime(durationTime);
		requestNewRelic.setSpanStartTime(spanStartTime);
		
		callDistributedTracing(requestNewRelic, monitorTracking);

		return result;
	}

	private void callDistributedTracing(NewRelicRequest requestNewRelic,String paramMonitorTracking ) throws Exception {
		
		MonitorTracking monitorTracking = MonitorTracking.getMonitorTrackingData(paramMonitorTracking);
		requestNewRelic.setServiceId(monitorTracking.getServiceId());
		
		newRelicTelemetry.sendTraceTransaction(requestNewRelic);
	}

}

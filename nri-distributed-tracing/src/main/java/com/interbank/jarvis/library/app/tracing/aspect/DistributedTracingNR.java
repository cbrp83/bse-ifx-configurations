package com.interbank.jarvis.library.app.tracing.aspect;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Name: DistributedTracingNR
 * @author Edert Ochoa
 * @date 2021-02-01
 * @version 1.0.0
 * 
 * **/

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface DistributedTracingNR {
	String componentType();
	String componentName();
	String applicationName();
}
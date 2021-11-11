// Copyright (c) 2020 - Interbank 
package com.interbank.jarvis.library.app.entities;

import java.io.IOException;
import java.util.Properties;
import org.springframework.beans.factory.config.YamlPropertiesFactoryBean;
import org.springframework.core.env.PropertiesPropertySource;
import org.springframework.core.env.PropertySource;
import org.springframework.core.io.support.EncodedResource;
import org.springframework.core.io.support.PropertySourceFactory;

/**
 * 
 * YamlProperties
 * @author Edert Ochoa
 * @date 2021-02-01
 * @version 1.0.0
 * @copyright Interbank
 * */
public class YamlProperties implements PropertySourceFactory{

	/**
	 * createPropertySource
	 * @param String name
	 * @param EncodedResource encodedResource
	 * @return PropertySource
	 * 
	 * */
	@Override
	public PropertySource<?> createPropertySource(String name, EncodedResource encodedResource) throws IOException {
		YamlPropertiesFactoryBean factory = new YamlPropertiesFactoryBean();
		factory.setResources(encodedResource.getResource());
		Properties properties = factory.getObject();
		return new PropertiesPropertySource(encodedResource.getResource().getFilename(), properties);
	}
	
}
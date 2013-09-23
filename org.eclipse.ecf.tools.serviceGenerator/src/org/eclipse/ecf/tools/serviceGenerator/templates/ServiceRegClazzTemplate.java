/*******************************************************************************
* Copyright (c) 2013 Salinda Jayawardana. All rights reserved. This
* program and the accompanying materials are made available under the terms of
* the Eclipse Public License v1.0 which accompanies this distribution, and is
* available at http://www.eclipse.org/legal/epl-v10.html
*
******************************************************************************/
package org.eclipse.ecf.tools.serviceGenerator.templates;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.eclipse.core.resources.IProject;
import org.eclipse.ecf.tools.serviceGenerator.utils.FileUtils;
import org.eclipse.ecf.tools.serviceGenerator.utils.TemplateUtilsImpl;
import org.osgi.framework.BundleContext;
import org.osgi.util.tracker.ServiceTracker;

public class ServiceRegClazzTemplate {
	
	public static String createServiceRegisterTemplete(String projectName,String packgeName,String className,
			String interfaceName,String interfacePkg,String serviceImpleClazz,IProject iProject) throws Exception{

		File resourceFile = new TemplateUtilsImpl().getResourceFile("templates/Register.temp");
		String fileContent = FileUtils.getContentAsString(resourceFile);
		if(!"".equals(packgeName)){
		fileContent = fileContent.replace("package", "package " + packgeName + ";");
		}else{
			fileContent = fileContent.replace("package","");
		}
		fileContent = fileContent.replace("ServiceRegisterClassName", className);
		fileContent = fileContent.replace("projectNameoftheActivator", projectName);
		fileContent = fileContent.replace("serviceInterfacePkg", interfacePkg);
		fileContent = fileContent.replace("serviceInterface", interfaceName);
		fileContent = fileContent.replace("serviceImplclassname", serviceImpleClazz);
		createPropertyFile(iProject,"ecf.r_osgi.peer");//set as a default value for the property 
		return fileContent;
	}
	
	
	private static void createPropertyFile(IProject project,String container) throws IOException{
		Properties prop = new Properties();
		File file = project.getFile("service.properties").getLocation().toFile();
		if(!file.exists()){
			file.createNewFile();
		}
		FileOutputStream outputStream = new FileOutputStream(file);
		prop.setProperty("container", container);
		prop.store(outputStream, "remote service runtime properties");
	}

}

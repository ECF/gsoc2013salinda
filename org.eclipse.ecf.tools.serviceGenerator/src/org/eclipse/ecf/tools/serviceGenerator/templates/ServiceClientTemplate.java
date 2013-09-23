/*******************************************************************************
* Copyright (c) 2013 Salinda Jayawardana. All rights reserved. This
* program and the accompanying materials are made available under the terms of
* the Eclipse Public License v1.0 which accompanies this distribution, and is
* available at http://www.eclipse.org/legal/epl-v10.html
*
******************************************************************************/
package org.eclipse.ecf.tools.serviceGenerator.templates;

import java.io.File;

import org.eclipse.ecf.tools.serviceGenerator.utils.FileUtils;
import org.eclipse.ecf.tools.serviceGenerator.utils.TemplateUtilsImpl;
import org.eclipse.ecf.tools.serviceGenerator.utils.TemplateUtil;


public class ServiceClientTemplate {
	
	public static String createServiceConsumerClassTemplete(String projectName,String packgeName,String className,String interfaceName) throws Exception{

		File resourceFile = new TemplateUtilsImpl().getResourceFile("templates/RemoteClient.temp");
		String fileContent = FileUtils.getContentAsString(resourceFile);
		if(!"".equals(packgeName)){
		fileContent = fileContent.replace("package", "package " + packgeName + ";");
		}else{
			fileContent = fileContent.replace("package","");
		}
		fileContent = fileContent.replace("serviceinterfaceName", interfaceName);
		fileContent = fileContent.replace("ServiceConsumerClassName", className);
		fileContent = fileContent.replace("projectNameoftheActivator", projectName);
		return fileContent;
	}
}

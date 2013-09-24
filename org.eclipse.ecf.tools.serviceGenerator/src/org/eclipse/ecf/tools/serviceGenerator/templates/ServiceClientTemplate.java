/*******************************************************************************
* Copyright (c) 2013 Salinda Jayawardana. All rights reserved. This
* program and the accompanying materials are made available under the terms of
* the Eclipse Public License v1.0 which accompanies this distribution, and is
* available at http://www.eclipse.org/legal/epl-v10.html
*
******************************************************************************/
package org.eclipse.ecf.tools.serviceGenerator.templates;

import java.io.File;
import java.net.URL;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.ecf.tools.serviceGenerator.Activator;
import org.eclipse.ecf.tools.serviceGenerator.utils.FileUtils;
import org.osgi.framework.Bundle;
import org.osgi.framework.FrameworkUtil;


public class ServiceClientTemplate {
	
	private static final String CLIENT_TEMPLATE_LOCATION = "templates/RemoteClient.temp";
	
	public static String createServiceConsumerClassTemplete(String projectName,String packgeName,String className,String interfaceName) throws Exception{
		Bundle bundle = FrameworkUtil.getBundle(Activator.class);
		URL fileURL = bundle.getEntry(CLIENT_TEMPLATE_LOCATION);
		File file = new File(FileLocator.resolve(fileURL).toURI());
		String fileContent = FileUtils.getContentAsString(file);
		if(!"".equals(packgeName)){
		fileContent = fileContent.replace("package", "package " + packgeName + ";");
		}else{
			fileContent = fileContent.replace("package","");
		}
		fileContent = fileContent.replace("serviceinterfaceName", interfaceName);
		fileContent = fileContent.replace("ServiceConsumerClassName", className);
		return fileContent;
	}
}

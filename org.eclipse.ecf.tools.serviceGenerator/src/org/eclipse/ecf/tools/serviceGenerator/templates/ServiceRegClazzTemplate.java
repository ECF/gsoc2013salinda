/*******************************************************************************
* Copyright (c) 2013 Salinda Jayawardana. All rights reserved. This
* program and the accompanying materials are made available under the terms of
* the Eclipse Public License v1.0 which accompanies this distribution, and is
* available at http://www.eclipse.org/legal/epl-v10.html
*
******************************************************************************/
package org.eclipse.ecf.tools.serviceGenerator.templates;

import java.util.ArrayList;
import java.util.List;

import org.osgi.framework.BundleContext;
import org.osgi.util.tracker.ServiceTracker;

public class ServiceRegClazzTemplate {
	public static String getClassTemplete(String packgeName,String className,List<String> imports,String interfaceName){
		   
		try{
					StringBuffer buffer = new StringBuffer();
					if(!"".equals(packgeName)){
						buffer.append("package ");
						buffer.append(packgeName);
						buffer.append(";");
						}
					buffer.append("\n");
					for (String strImport : imports) {
						buffer.append("\nimport "+strImport+";");
					}
					buffer.append("\n\n");
					buffer.append("public class " + className +"{ \n\n" );
					//buffer.append(" \n\t");
					buffer.append("private ServiceTracker containerManagerServiceTracker;\n\n");
					
					buffer.append("public void register(String interfaceName, Object classInstance,String containerDescription,BundleContext context) { \n\t\t");
					buffer.append("try {\n\n\t");
					buffer.append("IContainerManager containerManager = getContainerManagerService(context);\n\t");
					buffer.append("IContainer container= containerManager.getContainerFactory().createContainer(containerDescription);\n\t");					 
					buffer.append("IRemoteServiceContainerAdapter containerAdapter = (IRemoteServiceContainerAdapter) container.getAdapter(IRemoteServiceContainerAdapter.class);\n\t");
					buffer.append("containerAdapter.registerRemoteService(new String[] { interfaceName }, classInstance, null);\n\t");
					buffer.append("} catch (Exception e) {\n\n\t");
			               // TODO Auto-generated catch block
					buffer.append("} \n\n\t");
					buffer.append("}\n");
					buffer.append("private IContainerManager getContainerManagerService(BundleContext context) { \n\n\t");
					buffer.append("if (containerManagerServiceTracker == null) {\n\t");
					buffer.append("containerManagerServiceTracker = new ServiceTracker(context, IContainerManager.class.getName(),null);\n\n\t");
					buffer.append("containerManagerServiceTracker.open();");
					buffer.append("}\n\t");
					buffer.append("return (IContainerManager) containerManagerServiceTracker.getService();\n\t");
					buffer.append("}\n\t");
					buffer.append("}\n\t");
					return buffer.toString();
	            }catch (Exception e){
	           
	              return"";
	            }
		}
}

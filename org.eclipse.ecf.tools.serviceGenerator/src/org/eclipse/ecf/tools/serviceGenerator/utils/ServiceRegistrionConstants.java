package org.eclipse.ecf.tools.serviceGenerator.utils;

import java.awt.geom.Area;
import java.util.ArrayList;
import java.util.List;

public class ServiceRegistrionConstants {

	public static final String SERVICE_REG_STR_IMPORTS_RUNTIME_PLATFORM ="org.eclipse.core.runtime.Platform";
	public static final String SERVICE_REG_STR_IMPORTS_CORE_CONTAINER ="org.eclipse.ecf.core.IContainer";
	public static final String SERVICE_REG_STR_IMPORTS_CORE_CONTAINERMANAGER ="org.eclipse.ecf.core.IContainerManager";
	public static final String SERVICE_REG_STR_IMPORTS_CORE_IDENTITY ="org.eclipse.ecf.core.identity.ID";
	public static final String SERVICE_REG_STR_IMPORTS_REMOTE_ADAPTER ="org.eclipse.ecf.remoteservice.IRemoteServiceContainerAdapter"; 
	public static final String SERVICE_REG_STR_IMPORTS_REMOTE_ID ="org.eclipse.ecf.remoteservice.IRemoteServiceID";
	public static final String SERVICE_REG_STR_IMPORTS_REMOTE_REG ="org.eclipse.ecf.remoteservice.IRemoteServiceRegistration";

	static List<String> imports = new  ArrayList<String>();
	static{
		imports.add("org.eclipse.core.runtime.Platform");
		imports.add("org.eclipse.ecf.core.IContainer");
		imports.add("org.eclipse.ecf.core.IContainerManager");
		imports.add("org.eclipse.ecf.core.identity.ID");
		imports.add("org.eclipse.ecf.remoteservice.IRemoteServiceContainerAdapter");
		imports.add("org.eclipse.ecf.remoteservice.IRemoteServiceID");
		imports.add("org.eclipse.ecf.remoteservice.IRemoteServiceRegistration");
		
	}
	public static List<String> getImports(){
			return imports;
	}
}

package org.eclipse.ecf.tools.serviceGenerator.templates;

import java.io.File;

import org.eclipse.ecf.tools.serviceGenerator.utils.FileUtils;
import org.eclipse.ecf.tools.serviceGenerator.utils.TemplateUtil;


public class ServiceClientTemplate {
	
	public static String createServiceConsumerClassTemplete(String packgeName,String className) throws Exception{

		File resourceFile = new ServiceClientTemplateUtils().getResourceFile("templates/RemoteService.temp");
		String fileContent = FileUtils.getContentAsString(resourceFile);
		if(!"".equals(packgeName)){
		fileContent = fileContent.replace("package", "package " + packgeName + ";");
		}else{
			fileContent = fileContent.replace("package","");
		}
		fileContent = fileContent.replace("ServiceConsumerClassName", className);
		return fileContent;
	}


	public static String createManifestFileTemplate(String symbolicName){
		StringBuffer buffer = new StringBuffer();   
        buffer.append("Manifest-Version: 1.0 \n");
        buffer.append("Bundle-ActivationPolicy: lazy \n");
        buffer.append("Bundle-Name: ");
        buffer.append(symbolicName);
        buffer.append("\n");
        buffer.append("Bundle-Version: ");
        buffer.append("1.0");
        buffer.append("\n");
        buffer.append("Bundle-Activator: ");
        buffer.append(symbolicName+".Activator");
        buffer.append("\n");
        buffer.append("Bundle-ManifestVersion: 2");
        buffer.append("\n");
        buffer.append("Carbon-Component: UIBundle");
        buffer.append("\n");
        buffer.append("Import-Package: org.osgi.framework;");
        buffer.append("version=");
        buffer.append("\"");
        buffer.append("1.3.0");
        buffer.append("\"");
        buffer.append("\n");
        buffer.append("Bundle-SymbolicName: ");
        buffer.append(symbolicName);
        buffer.append(";singleton:=true\n");
        return buffer.toString();
	}
}

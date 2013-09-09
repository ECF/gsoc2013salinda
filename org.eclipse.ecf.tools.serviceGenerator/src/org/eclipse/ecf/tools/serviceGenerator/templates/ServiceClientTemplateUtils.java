
package org.eclipse.ecf.tools.serviceGenerator.templates;

import org.eclipse.core.runtime.Platform;
import org.eclipse.ecf.tools.serviceGenerator.Activator;
import org.eclipse.ecf.tools.serviceGenerator.utils.TemplateUtil;
import org.osgi.framework.Bundle;


public class ServiceClientTemplateUtils extends TemplateUtil{
	private static ServiceClientTemplateUtils instance;
	
	protected Bundle getBundle() {
		return Platform.getBundle(Activator.PLUGIN_ID);
	}
	
	public static ServiceClientTemplateUtils getInstance(){
		if (instance==null){
			instance=new ServiceClientTemplateUtils();
		}
		return instance;
	}
}


package org.eclipse.ecf.tools.serviceGenerator.utils;

import org.eclipse.core.runtime.Platform;
import org.eclipse.ecf.tools.serviceGenerator.Activator;
import org.osgi.framework.Bundle;


public class TemplateUtilsImpl extends TemplateUtil{
	private static TemplateUtilsImpl instance;
	
	protected Bundle getBundle() {
		return Platform.getBundle(Activator.PLUGIN_ID);
	}
	
	public static TemplateUtilsImpl getInstance(){
		if (instance==null){
			instance=new TemplateUtilsImpl();
		}
		return instance;
	}
}

//MAK This code appears to be copied from somewhere. Thus, a copyright header is missing crediting the source.
package org.eclipse.ecf.tools.servicegenerator.utils;

import java.io.PrintStream;

import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.service.log.LogService;
import org.osgi.util.tracker.ServiceTracker;

public class Logger extends ServiceTracker implements LogService{

	/** LogService interface class name */
	protected final static String clazz = "org.osgi.service.log.LogService"; //$NON-NLS-1$

	
 
	public Logger(BundleContext context) {
		super(context, clazz, null);
		 
	}

	public void log(int level, String message) {
		log(null, level, message, null);
	}

	public void log(int level, String message, Throwable exception) {
		log(null, level, message, exception);
	}

	public void log(ServiceReference reference, int level, String message) {
		log(reference, level, message, null);
	}

	public synchronized void log(ServiceReference reference, int level,
			String message, Throwable exception) {
		ServiceReference[] references = getServiceReferences();

		if (references != null) {
			int size = references.length;

			for (int i = 0; i < size; i++) {
				LogService service = (LogService) getService(references[i]);
				if (service != null) {
					try {
						service.log(reference, level, message, exception);
					} catch (Exception e) {
						// TODO: consider printing to System Error
					}
				}
			}
			return;
		}
	}
}

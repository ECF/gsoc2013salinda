/*******************************************************************************
* Copyright (c) 2013 Salinda Jayawardana. All rights reserved. This
* program and the accompanying materials are made available under the terms of
* the Eclipse Public License v1.0 which accompanies this distribution, and is
* available at http://www.eclipse.org/legal/epl-v10.html
*
******************************************************************************/
package org.eclipse.ecf.tools.serviceGenerator.utils;

import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.service.log.LogService;
import org.osgi.util.tracker.ServiceTracker;

@SuppressWarnings("rawtypes")
public class Logger extends ServiceTracker implements LogService{

	/** LogService interface class name */
	protected final static String clazz = "org.osgi.service.log.LogService"; //$NON-NLS-1$

	@SuppressWarnings("unchecked")
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

	@SuppressWarnings({ "unchecked" })
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
						
					}
				}
			}
			return;
		}
	}
}

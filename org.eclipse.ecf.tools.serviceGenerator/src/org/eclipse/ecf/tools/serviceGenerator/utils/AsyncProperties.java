/*******************************************************************************
* Copyright (c) 2013 Salinda Jayawardana. All rights reserved. This
* program and the accompanying materials are made available under the terms of
* the Eclipse Public License v1.0 which accompanies this distribution, and is
* available at http://www.eclipse.org/legal/epl-v10.html
*
******************************************************************************/
package org.eclipse.ecf.tools.serviceGenerator.utils;

import java.util.ResourceBundle;


public class AsyncProperties{
private static final String BUNDLE_NAME = "AsyncService";

	public static final String ASYNC_SERVICE_STR_IMPORTS_CALLBACkPROXY="org.eclipse.ecf.remoteservice.IAsyncRemoteServiceProxy";
	public static final String ASYNC_SERVICE_STR_IMPORTS_CALLBACK="org.eclipse.ecf.remoteservice.IAsyncCallback";
	public static final String ASYNC_SERVICE_STR_IMPORTS_IFUTURE="org.eclipse.equinox.concurrent.future.IFuture";
	public static final String ASYNC_SERVICE_STR_IASYNC_REMOTE_SERVICEPROXY="IAsyncRemoteServiceProxy";
	public static  String Service_Gen_Error_Msg;

	static {
		// initialize resource bundle
		ResourceBundle asyncServiceProperties = ResourceBundle.getBundle(BUNDLE_NAME);
        AsyncProperties.Service_Gen_Error_Msg = asyncServiceProperties.getString("Service_Gen_Error_Msg");
	}

	private AsyncProperties() {
	}
}

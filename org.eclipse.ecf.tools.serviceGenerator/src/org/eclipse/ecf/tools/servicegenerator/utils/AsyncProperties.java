package org.eclipse.ecf.tools.servicegenerator.utils;

import org.eclipse.osgi.util.NLS;

public class AsyncProperties extends NLS {
	private static final String BUNDLE_NAME = "AsyncService";
	public static String AsyncService_Str_Impors_callbackProxy;
	public static String AsyncService_Str_Imports_callback;
	public static String AsyncService_Str_Import_IFuture;
	public static String AsyncService_Str_IAsyncRemoteServiceProxy;

	static {
		// initialize resource bundle
		NLS.initializeMessages(BUNDLE_NAME, AsyncProperties.class);
	}

	private AsyncProperties() {
	}
}

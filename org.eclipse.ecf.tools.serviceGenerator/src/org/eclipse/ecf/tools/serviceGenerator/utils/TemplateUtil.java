/*******************************************************************************
* Copyright (c) 2013 Salinda Jayawardana. All rights reserved. This
* program and the accompanying materials are made available under the terms of
* the Eclipse Public License v1.0 which accompanies this distribution, and is
* available at http://www.eclipse.org/legal/epl-v10.html
*
******************************************************************************/
package org.eclipse.ecf.tools.serviceGenerator.utils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.osgi.framework.Bundle;


public abstract class TemplateUtil {
	
	public String getTemplateString(String filename) throws IOException {
		if (filename == null)
			return null;
		URL fileURL = getResourceURL("templates/"+filename);
		if(fileURL != null){
			InputStream openStream = fileURL.openStream();
		    String contentAsString = getContentAsString(openStream);
		    openStream.close();
			return contentAsString;
		}else{
			return null;
		}
	}

	public static String getContentAsString(InputStream dataStream)
			throws IOException {
		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		byte[] b=new byte[1024];
		int read = dataStream.read(b);
		while(read!=-1){
			stream.write(b,0,read);
			read = dataStream.read(b);
		}
		String streamContent = stream.toString();
		stream.close();
		return streamContent;
	}
	
	public URL getResourceURL(String filename) {
		return FileLocator.find(getBundle(), new Path(filename), null);
	}
	
	public URL getResourceURL(IPath path) {
		return FileLocator.find(getBundle(), path, null);
	}

	public File getResourceFile(String filename) throws IOException {
		URL resourceURL = getResourceURL(filename);
		File destinationFile = File.createTempFile("ecf", ".tmp");
		FileUtils.createFile(destinationFile, resourceURL.openStream());
		return destinationFile;
	}

	protected abstract Bundle getBundle();
}

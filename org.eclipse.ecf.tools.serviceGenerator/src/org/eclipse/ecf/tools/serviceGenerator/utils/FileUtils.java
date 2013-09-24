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

public class FileUtils{
      
	public static String getContentAsString(URL url) throws IOException {
		InputStream openStream = url.openStream();
	    String contentAsString = getContentAsString(openStream);
	    openStream.close();
	    return contentAsString;
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
	
	public static String getContentAsString(File file) throws IOException {
		return getContentAsString(file.toURI().toURL());
	}
	
}

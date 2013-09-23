/*******************************************************************************
* Copyright (c) 2013 Salinda Jayawardana. All rights reserved. This
* program and the accompanying materials are made available under the terms of
* the Eclipse Public License v1.0 which accompanies this distribution, and is
* available at http://www.eclipse.org/legal/epl-v10.html
*
******************************************************************************/
package org.eclipse.ecf.tools.serviceGenerator.utils;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Enumeration;
import java.util.jar.Attributes;
import java.util.jar.JarFile;
import java.util.jar.Manifest;

import org.eclipse.osgi.util.ManifestElement;
import org.osgi.framework.BundleException;

public class ManifestEditor {
	
	public static void addPluginDependency(String newbundleHeader,String newbundleHeaderValue,String newbundleAttr,
			String newAttrValue, boolean overwrite,String filepath) throws BundleException, IOException 
			{ 
		   
		    FileInputStream in = new FileInputStream(filepath); 
		    Manifest manifest = new Manifest(in);
			boolean foundHeader = false; 
			boolean hasValuesForPlugin = false; 
			boolean islastHeader=false;
			StringBuilder strBuilder = new StringBuilder(); 
			
			Attributes mainAttrs = manifest.getMainAttributes(); 
			int tempCount =0;
			for (Object entryName : mainAttrs.keySet()) { 
				String values; 
				String header; 
				tempCount++;
				if(entryName instanceof String) { 
					header = (String) entryName; 
					values = mainAttrs.getValue(header); 
				} else if(entryName instanceof Attributes.Name) { 
					header = (String) ((Attributes.Name) entryName).toString(); 
					values = mainAttrs.getValue((Attributes.Name) entryName); 
				} else { 
					throw new BundleException("Bundle Confalsefiguration error: " 
							+ entryName.getClass() + " (" + entryName + ")"); 
				} 
				if(!newbundleHeader.equals(header)) {
						continue; 
				}
			    if(tempCount==mainAttrs.keySet().size()){
			    	islastHeader = true;
			    }

			foundHeader = true; 
			if(values != null){
			 ManifestElement[] elements = ManifestElement.parseHeader(header, values);
			 	for (int i = 0; i < elements.length; i++){
			 		ManifestElement manifestElement = elements[i];
			 		Enumeration<?> keys = manifestElement.getKeys();
			 		Enumeration<?> directiveKeys = manifestElement.getDirectiveKeys();
			 		
			 		StringBuilder valueComponents = new StringBuilder();	
			 		boolean lastElement = i >= elements.length-1;
			 		boolean firstElement = i == 0; 
			 		boolean elementIsRequiredPlugin = false;

			 		for (int j = 0; j < manifestElement.getValueComponents().length; j++){
			 				String pureValue = manifestElement.getValueComponents()[j];
			 				if(newbundleHeaderValue.equalsIgnoreCase(pureValue)){
			 					hasValuesForPlugin = true;
			 					elementIsRequiredPlugin = true;
			 					if(!overwrite)
			 						return;
			 				}
			 				valueComponents.append(pureValue + ";"); 
			 		} 
			if(!elementIsRequiredPlugin){
				if(!firstElement){
	 		    	 strBuilder.append(',');
	 		    	 strBuilder.append('\n'); 
	 		    	 strBuilder.append(' ');
				  }else{
					  strBuilder.append(' ');
				  }
				strBuilder.append(manifestElement.toString());
				continue;
			}else{
			 strBuilder.append((firstElement?"":" ") + valueComponents);
			}
			boolean foundVersionAttr = false;
			if(keys != null){
						while (keys.hasMoreElements()){
								String key = (String) keys.nextElement();
								String value = manifestElement.getAttribute(key); 
								if(newAttrValue!=null && !newAttrValue.isEmpty()){
									if(newbundleAttr.equalsIgnoreCase(key)){
										strBuilder.append(key + "=\"" + newAttrValue+"\"");
										foundVersionAttr = true;
									}else{
										strBuilder.append(key + "=\"" + value);
									}
								}
						}
			}
			if(!foundVersionAttr){
				if(newAttrValue!=null && !newAttrValue.isEmpty()){
					strBuilder.append(newbundleAttr + "=\"" + newAttrValue+"\"");
				}	
			}
			
     		if(directiveKeys != null){
				while (directiveKeys.hasMoreElements()){
					boolean lastDirective = !directiveKeys.hasMoreElements();
					if(!lastDirective){
							strBuilder.append(";");
					}
				}
			}if(!lastElement){
			strBuilder.append(",\n");
			}
		}
	}
   break;
	}
 in.close();		
  writeIntoBundleMF(newbundleHeaderValue, newAttrValue, overwrite, filepath, newbundleHeader,
			newbundleAttr, foundHeader, hasValuesForPlugin, strBuilder,
			manifest);
	}

	private static  void writeIntoBundleMF(String plugin, String version, boolean overwrite,
			String fileName, String importBundleHeader,
			String bundleVersionAttr, boolean foundHeader,
			boolean hasValuesForPlugin, StringBuilder strBuilder,
			Manifest manifest) throws FileNotFoundException, IOException {
		if(!foundHeader){
			   if(version!=null && !version.isEmpty()){
				   manifest.getMainAttributes().putValue(importBundleHeader,  plugin + ";" 
						   + bundleVersionAttr + "=\"" + version+"\"");
			   }else{
				manifest.getMainAttributes().putValue(importBundleHeader, plugin);
			   }
		 }else{
				if(hasValuesForPlugin){
				manifest.getMainAttributes().putValue(importBundleHeader, strBuilder.toString());
				}else{
				String existingValues = strBuilder.toString();
				boolean areExistingValues = existingValues.trim().length() != 0;
				 String newValue = plugin;
				 if(version!=null && !version.isEmpty()){
					 newValue = plugin + ";" + bundleVersionAttr + "=\"" + version+"\"";
				 } 
				//newValue = (areExistingValues)?(existingValues+",\n "+newValue):newValue;
				if(areExistingValues){
					newValue =existingValues+",\n " + newValue;
				}else{
					newValue =" "+newValue;
				}
				manifest.getMainAttributes().putValue(importBundleHeader, newValue);
				}
		 }
		    
			FileOutputStream outputStream = new FileOutputStream(fileName);
			manifest.write(outputStream);
			outputStream.flush();
		    outputStream.close();
	}

	//How to use this
			public static void main(String[] args) throws Exception { 
			try { ManifestEditor editor= new ManifestEditor();
			//	org.eclipse.ecf.remoteservice,org.eclipse.ecf.core,org.eclipse.ecf.core.identity
				String fileName ="/home/salinda/workspace/ecfTool/TestBundle/src/MANIFEST.MF";
				/*ManifestEditor.addPluginDependency("Import-Package",
						"org.eclipse.ecf.remoteservice,\n org.eclipse.ecf.core,\n org.eclipse.ecf.core.identity",
						"version",null, false,fileName); */
				
				
				
				
				
				
				//ManifestEditor.addPluginDependency("Import-Package","org.eclipse.ecf.core","version",null, false,fileName); 
				ManifestEditor.addPluginDependency("Import-Package","org.eclipse.ecf.core.identity","version",null, false,fileName); 
				ManifestEditor.addPluginDependency("Import-Package","org.eclipse.ecf.core.abc","version",null, false,fileName);
				ManifestEditor.addPluginDependency("Import-Package","org.eclipse.ecf.core.def","version",null, false,fileName);
				ManifestEditor.addPluginDependency("Import-Package","org.eclipse.ecf.core.hjk","version",null, false,fileName);
			} 
			catch (Throwable t) { 
			System.err.println("Unexpected Exception: " + t); 
			t.printStackTrace(); 
			} 
		}  
			
}

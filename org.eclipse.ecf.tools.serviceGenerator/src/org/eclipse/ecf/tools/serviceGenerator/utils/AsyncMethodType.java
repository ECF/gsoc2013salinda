/*******************************************************************************
* Copyright (c) 2013 Salinda Jayawardana. All rights reserved. This
* program and the accompanying materials are made available under the terms of
* the Eclipse Public License v1.0 which accompanies this distribution, and is
* available at http://www.eclipse.org/legal/epl-v10.html
*
******************************************************************************/
package org.eclipse.ecf.tools.serviceGenerator.utils;

public enum AsyncMethodType {
	 BOTH(1,"AsyncType.BOTH"),CALLBACK(2,"AsyncType.CALLBACK"),FUTURE(3,"AsyncType.FUTURE");
	 
	 private int code;
	 private String strCode;
	 
	 private AsyncMethodType(int code,String strCode) {
	   this.code = code;
	   this.strCode = strCode;
	 }
	 
	 public int getCode() {
	   return code;
	 }

	public String getStrCode() {
		return strCode;
	}
}

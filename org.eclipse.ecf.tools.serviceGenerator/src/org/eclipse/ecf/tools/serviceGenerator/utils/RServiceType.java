package org.eclipse.ecf.tools.serviceGenerator.utils;

public enum RServiceType {
 ASYNC(1,"Async"),SYNC(2,"sync");
	 
	 private int code;
	 private String strCode;
	 
	 private RServiceType(int code,String strCode) {
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

package org.eclipse.ecf.tools.serviceGenerator.utils;

public enum AsyncMethodType {
	 BOTH(1,"both"),CALLBACK(2,"callback"),FUTURE(3,"future");
	 
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

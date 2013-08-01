package org.eclipse.ecf.tools.serviceGenerator.utils;

public enum Annotaions {
	RService(1,"RemoteService"),ASYNC_METHOD(2,"Async");
	 
	 private int code;
	 private String strCode;
	 
	 private Annotaions(int code,String strCode) {
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

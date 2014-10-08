package com.incito.base.exception;


public class AppException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2031123365205271993L;
	
	/** 定义异常类型 */
	public final static byte TYPE_DATABASE 	= 0x01;
	public final static byte TYPE_HTTP_CODE	= 0x02;
	public final static byte TYPE_HTTP_ERROR= 0x03;
	public final static byte TYPE_XML	 	= 0x04;
	public final static byte TYPE_IO	 	= 0x05;
	public final static byte TYPE_RUN	 	= 0x06;
	public final static byte TYPE_JSON	 	= 0x07;
	
	private byte type;
	private int code;
	
	private AppException(){
		
	}
	
	private AppException(byte type, int code, Exception excp) {
		super(excp);
		this.type = type;
		this.code = code;		
	}
	public int getCode() {
		return this.code;
	}
	public int getType() {
		return this.type;
	}
	

	public static AppException database(int code) {
		return new AppException(TYPE_DATABASE, code ,null);
	}
	
	public static AppException http(int code) {
		return new AppException(TYPE_HTTP_CODE, code, null);
	}
	
	public static AppException http(Exception e) {
		return new AppException(TYPE_HTTP_ERROR, 0 ,e);
	}
	
	public static AppException xml(Exception e) {
		return new AppException(TYPE_XML, 0, e);
	}
	
	public static AppException json(Exception e) {
		return new AppException(TYPE_JSON, 0, e);
	}
	
	public static AppException run(Exception e) {
		return new AppException(TYPE_RUN, 0, e);
	}

	/**
	 * 获取APP异常崩溃处理对象
	 * @return
	 */
	public static AppException getAppExceptionHandler(){
		return new AppException();
	}
	
}

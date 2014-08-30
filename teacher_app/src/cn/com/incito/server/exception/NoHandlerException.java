package cn.com.incito.server.exception;

public class NoHandlerException extends RuntimeException{
    private static final long serialVersionUID = 1L;
    
    /**
	 * @description:
	 */
	public NoHandlerException(){
		super();
	}
	/**
	 * @description: 
	 * @param message
	 */
	public NoHandlerException(String message){
		super(message);
	}
    /**
     * @description: 
     * @param cause
     */
    public NoHandlerException(Throwable cause) {
        super(cause);
    }
	/**
	 * @description: 
	 * @param message
	 * @param cause
	 */
	public NoHandlerException(String message, Throwable cause) {
        super(message, cause);
    }
}

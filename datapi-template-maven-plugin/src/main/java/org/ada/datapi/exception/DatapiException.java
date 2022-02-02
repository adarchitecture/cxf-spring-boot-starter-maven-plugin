package org.ada.datapi.exception;

/**
 * Exception to be thrown in situations when the input
 * to the application has caused a failure. AKA user error,
 * not programmer error
 */
public class DatapiException extends RuntimeException {

    /**
	 * 
	 */
	private static final long serialVersionUID = 2626671100099007838L;

	public DatapiException (String message) {
        super (message);
    }

    public DatapiException (String message, Throwable cause) {
        super (message, cause);
    }

}

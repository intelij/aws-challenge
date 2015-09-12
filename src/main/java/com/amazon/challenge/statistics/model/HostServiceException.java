package com.amazon.challenge.statistics.model;

/**
 * Exception on the service level
 * 
 * @author durrah
 *
 */
public class HostServiceException extends Exception {
	/**
	 * 
	 */
	private static final long serialVersionUID = 2616308545914902389L;

	public HostServiceException() {
		super();
	}

	public HostServiceException(String message) {
		super(message);
	}

	public HostServiceException(Throwable cause) {
		super(cause);
	}

	public HostServiceException(String message, Throwable cause) {
		super(message, cause);
	}
}

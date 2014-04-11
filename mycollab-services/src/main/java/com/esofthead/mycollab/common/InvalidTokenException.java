package com.esofthead.mycollab.common;

/**
 * 
 * @author MyCollab Ltd.
 * @since 4.0
 * 
 */
public class InvalidTokenException extends Exception {
	private static final long serialVersionUID = 1L;

	public InvalidTokenException(Throwable e) {
		super(e);
	}

	public InvalidTokenException(String message) {
		super(message);
	}
}

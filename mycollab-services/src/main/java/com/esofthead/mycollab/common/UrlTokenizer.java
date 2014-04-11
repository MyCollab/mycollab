package com.esofthead.mycollab.common;

import java.util.StringTokenizer;

/**
 * 
 * @author MyCollab Ltd.
 * @since 4.0
 * 
 */
public class UrlTokenizer {

	private StringTokenizer urlTokenizer;

	public UrlTokenizer(String url) {
		String internalVal = (url.startsWith("/")) ? url.substring(1) : url;
		internalVal = UrlEncodeDecoder.decode(internalVal);
		urlTokenizer = new StringTokenizer(internalVal, "/");
	}

	public int getInt() throws InvalidTokenException {
		if (urlTokenizer.hasMoreTokens()) {
			try {
				int intVal = Integer.parseInt(urlTokenizer.nextToken());
				return intVal;
			} catch (NumberFormatException e) {
				throw new InvalidTokenException(e);
			}
		} else {
			throw new InvalidTokenException("Invalid token");
		}
	}

	public String getString() throws InvalidTokenException {
		if (urlTokenizer.hasMoreTokens()) {
			return urlTokenizer.nextToken();
		} else {
			throw new InvalidTokenException("Invalid token");
		}
	}
}

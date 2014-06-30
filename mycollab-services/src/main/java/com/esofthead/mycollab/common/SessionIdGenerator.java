package com.esofthead.mycollab.common;

/**
 * 
 * @author MyCollab Ltd.
 * @since 4.3.0
 * 
 */
public abstract class SessionIdGenerator {
	private static SessionIdGenerator instance;

	abstract public String getSessionIdApp();

	public static void registerSessionIdGenerator(SessionIdGenerator provider) {
		instance = provider;
	}

	public static String getSessionId() {
		return instance.getSessionIdApp();
	}
}

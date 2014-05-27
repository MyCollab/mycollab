package com.esofthead.mycollab.core;

/**
 * 
 * @author MyCollab Ltd.
 * @since 4.1.2
 * 
 */
public enum LanguageSupport {

	ENGLISH("English"), JAPAN("Japan");

	private String language;

	LanguageSupport(String language) {
		this.language = language;
	}

	public String getLanguage() {
		return language;
	}
}

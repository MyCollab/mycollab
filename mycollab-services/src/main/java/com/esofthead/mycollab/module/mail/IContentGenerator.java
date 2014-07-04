package com.esofthead.mycollab.module.mail;

/**
 * 
 * @author MyCollab Ltd.
 * @since 4.3.0
 * 
 */
public interface IContentGenerator {

	/**
	 * 
	 * @param key
	 * @param value
	 */
	void putVariable(String key, Object value);

	/**
	 * 
	 * @param subject
	 * @return
	 */
	String generateSubjectContent(String subject);

	/**
	 * 
	 * @param templateFilePath
	 * @return
	 */
	String generateBodyContent(String templateFilePath);
}

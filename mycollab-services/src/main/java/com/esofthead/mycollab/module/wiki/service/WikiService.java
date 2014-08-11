package com.esofthead.mycollab.module.wiki.service;

import com.esofthead.mycollab.module.wiki.domain.Page;

/**
 * 
 * @author MyCollab Ltd.
 * @since 4.4.0
 *
 */
public interface WikiService {
	/**
	 * 
	 * @param page
	 * @param createdUser
	 */
	void savePage(Page page, String createdUser);
}

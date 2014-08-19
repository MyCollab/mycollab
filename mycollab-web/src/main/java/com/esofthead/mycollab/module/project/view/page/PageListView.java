package com.esofthead.mycollab.module.project.view.page;

import java.util.List;

import com.esofthead.mycollab.module.wiki.domain.WikiResource;
import com.esofthead.mycollab.vaadin.mvp.PageView;

/**
 * 
 * @author MyCollab Ltd.
 * @since 4.4.0
 *
 */
public interface PageListView extends PageView {
	/**
	 * 
	 * @param resources
	 */
	void displayDefaultPages(List<WikiResource> resources);
}

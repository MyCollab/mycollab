/**
 * This file is part of mycollab-web.
 *
 * mycollab-web is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * mycollab-web is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with mycollab-web.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.esofthead.mycollab.module.project.view.bug;

import java.net.MalformedURLException;
import java.net.URL;

import com.esofthead.mycollab.core.MyCollabException;
import com.esofthead.mycollab.vaadin.AppContext;
import com.esofthead.mycollab.vaadin.mvp.AbstractPageView;
import com.esofthead.mycollab.vaadin.mvp.ViewComponent;
import com.vaadin.server.ExternalResource;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.BrowserFrame;
import com.vaadin.ui.Label;

/**
 * 
 * @author MyCollab Ltd.
 * @since 1.0
 * 
 */
@ViewComponent
public class BugAdvertisementViewImpl extends AbstractPageView implements
		BugAdvertisementView {
	private static final long serialVersionUID = 1L;

	public BugAdvertisementViewImpl() {
		this.setWidth("100%");
		this.setHeight("512px");
		URL url = null;
		try {
			url = new URL(AppContext.getSiteUrl()
					+ "assets/ads/bug/bugAds.html");
		} catch (MalformedURLException e) {
			throw new MyCollabException(e);
		}
		BrowserFrame browser = new BrowserFrame("", new ExternalResource(url));
		browser.setWidth("100%");
		browser.setHeight("100%");

		this.addComponent(browser);
	}
}

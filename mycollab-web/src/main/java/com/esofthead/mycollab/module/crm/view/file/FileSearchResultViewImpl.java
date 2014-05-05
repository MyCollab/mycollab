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
package com.esofthead.mycollab.module.crm.view.file;

import com.esofthead.mycollab.eventmanager.EventBus;
import com.esofthead.mycollab.module.crm.events.DocumentEvent;
import com.esofthead.mycollab.module.file.domain.criteria.FileSearchCriteria;
import com.esofthead.mycollab.module.file.view.components.FileSearchResultComponent;
import com.esofthead.mycollab.vaadin.AppContext;
import com.esofthead.mycollab.vaadin.mvp.AbstractPageView;
import com.esofthead.mycollab.vaadin.mvp.ViewComponent;

@ViewComponent
public class FileSearchResultViewImpl extends AbstractPageView implements
		FileSearchResultView {
	private static final long serialVersionUID = 1L;

	private FileSearchResultComponent searchResultComp;

	public FileSearchResultViewImpl() {
		searchResultComp = new FileSearchResultComponent() {
			private static final long serialVersionUID = 1L;

			@Override
			protected void backView() {
				EventBus.getInstance().fireEvent(
						new DocumentEvent.GotoDashboard(
								FileSearchResultViewImpl.this, null));
			}
		};
		this.addComponent(searchResultComp);
		this.setMargin(true);
	}

	@Override
	public void displaySearchResult(FileSearchCriteria searchCriteria) {
		searchResultComp.displaySearchResult(searchCriteria.getRootFolder(),
				searchCriteria.getBaseFolder(), searchCriteria.getFileName());
		AppContext.addFragment("crm/file/search", "Customer: Search Files");
	}

}

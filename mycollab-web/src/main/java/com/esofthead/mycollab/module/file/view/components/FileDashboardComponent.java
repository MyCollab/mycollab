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
package com.esofthead.mycollab.module.file.view.components;

import com.esofthead.mycollab.module.ecm.domain.Folder;
import com.esofthead.mycollab.module.ecm.service.ResourceService;
import com.esofthead.mycollab.module.file.domain.criteria.FileSearchCriteria;
import com.esofthead.mycollab.spring.ApplicationContextUtil;
import com.esofthead.mycollab.vaadin.events.SearchHandler;
import com.esofthead.mycollab.vaadin.ui.MyCollabResource;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Image;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.Reindeer;

/**
 * 
 * @author MyCollab Ltd.
 * @since 1.0
 * 
 */
public class FileDashboardComponent extends VerticalLayout {
	private static final long serialVersionUID = 1L;

	private String rootPath;
	private Folder rootFolder;
	private ResourcesDisplayComponent resourceDisplayComponent;

	private final ResourceService resourceService;

	public FileDashboardComponent(String rootPath) {
		this.rootPath = rootPath;

		this.rootFolder = new Folder(this.rootPath);

		this.setWidth("100%");
		this.setSpacing(true);
		this.resourceService = ApplicationContextUtil
				.getSpringBean(ResourceService.class);

		this.addComponent(constructHeader());

		this.resourceDisplayComponent = new ResourcesDisplayComponent(rootPath,
				rootFolder);
		this.addComponent(resourceDisplayComponent);

	}

	public HorizontalLayout constructHeader() {
		final HorizontalLayout layout = new HorizontalLayout();
		layout.setWidth("100%");
		layout.setSpacing(true);
		layout.setMargin(true);

		final Image titleIcon = new Image(null,
				MyCollabResource.newResource("icons/24/project/file.png"));
		layout.addComponent(titleIcon);
		layout.setComponentAlignment(titleIcon, Alignment.MIDDLE_LEFT);

		final Label searchtitle = new Label("Files");
		searchtitle.setStyleName(Reindeer.LABEL_H2);
		layout.addComponent(searchtitle);
		layout.setComponentAlignment(searchtitle, Alignment.MIDDLE_LEFT);
		layout.setExpandRatio(searchtitle, 1.0f);
		return layout;
	}

	public void displayResources() {
		resourceDisplayComponent.displayComponent(rootFolder, rootPath,
				"Documents");

		resourceDisplayComponent
				.addSearchHandlerToBreadCrumb(new SearchHandler<FileSearchCriteria>() {
					@Override
					public void onSearch(FileSearchCriteria criteria) {
						Folder selectedFolder = null;
						selectedFolder = (Folder) resourceService
								.getResource(criteria.getBaseFolder());
						resourceDisplayComponent
								.constructBodyItemContainer(selectedFolder);
						FileDashboardComponent.this.rootFolder = selectedFolder;
					}
				});
	}
}

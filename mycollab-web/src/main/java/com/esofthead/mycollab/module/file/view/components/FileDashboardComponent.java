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
import com.esofthead.mycollab.module.project.ProjectTypeConstants;
import com.esofthead.mycollab.module.project.ui.ProjectAssetsManager;
import com.esofthead.mycollab.spring.ApplicationContextUtil;
import com.esofthead.mycollab.vaadin.events.SearchHandler;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import org.vaadin.maddon.layouts.MHorizontalLayout;
import org.vaadin.maddon.layouts.MVerticalLayout;

/**
 * 
 * @author MyCollab Ltd.
 * @since 1.0
 * 
 */
public class FileDashboardComponent extends MVerticalLayout {
	private static final long serialVersionUID = 1L;

	private Folder rootFolder;
	private ResourcesDisplayComponent resourceDisplayComponent;

	private ResourceService resourceService;

	public FileDashboardComponent(String rootPath) {
        this.withMargin(false);
        this.setSizeFull();

		this.rootFolder = new Folder(rootPath);
		this.resourceService = ApplicationContextUtil.getSpringBean(ResourceService.class);

		this.resourceDisplayComponent = new ResourcesDisplayComponent(rootFolder);
		this.with(constructHeader(), resourceDisplayComponent);
	}

	public HorizontalLayout constructHeader() {
		MHorizontalLayout layout = new MHorizontalLayout().withMargin(new MarginInfo(true, false, false, false))
                .withWidth("100%");

        Label titleLbl = new Label(ProjectAssetsManager.getAsset(ProjectTypeConstants.FILE).getHtml() + " Files",
                ContentMode.HTML);
        titleLbl.setStyleName("headerName");
        layout.with(titleLbl).withAlign(titleLbl, Alignment.MIDDLE_LEFT).expand(titleLbl);
        return layout;
	}

	public void displayResources() {
		resourceDisplayComponent.displayComponent(rootFolder, "Documents");

		resourceDisplayComponent.addSearchHandlerToBreadCrumb(new SearchHandler<FileSearchCriteria>() {
					@Override
					public void onSearch(FileSearchCriteria criteria) {
						Folder selectedFolder = (Folder) resourceService.getResource(criteria.getBaseFolder());
						resourceDisplayComponent.constructBodyItemContainer(selectedFolder);
						FileDashboardComponent.this.rootFolder = selectedFolder;
					}
				});
	}
}

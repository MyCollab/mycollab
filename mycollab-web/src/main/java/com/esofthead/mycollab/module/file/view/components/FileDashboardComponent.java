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

import com.esofthead.mycollab.common.i18n.GenericI18Enum;
import com.esofthead.mycollab.core.arguments.SearchCriteria;
import com.esofthead.mycollab.module.ecm.domain.Folder;
import com.esofthead.mycollab.module.ecm.service.ResourceService;
import com.esofthead.mycollab.module.file.domain.criteria.FileSearchCriteria;
import com.esofthead.mycollab.spring.ApplicationContextUtil;
import com.esofthead.mycollab.vaadin.AppContext;
import com.esofthead.mycollab.vaadin.events.SearchHandler;
import com.esofthead.mycollab.vaadin.ui.GenericSearchPanel;
import com.esofthead.mycollab.vaadin.ui.MyCollabResource;
import com.esofthead.mycollab.vaadin.ui.UIConstants;
import com.esofthead.mycollab.vaadin.ui.UiUtils;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Image;
import com.vaadin.ui.Label;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;

/**
 * 
 * @author MyCollab Ltd.
 * @since 1.0
 * 
 */
public abstract class FileDashboardComponent extends VerticalLayout {
	private static final long serialVersionUID = 1L;
	private String rootPath;
	private String rootFolderName;

	private final FileSearchPanel fileSearchPanel;
	private ResourceHandlerComponent resourceHandlerComponent;
	private Folder baseFolder;
	private HorizontalLayout resourceContainer;

	private final ResourceService resourceService;

	public FileDashboardComponent() {
		this.setWidth("100%");
		this.setSpacing(true);
		this.resourceService = ApplicationContextUtil
				.getSpringBean(ResourceService.class);
		this.fileSearchPanel = new FileSearchPanel(null);

		this.addComponent(this.fileSearchPanel);

		resourceContainer = new HorizontalLayout();
		resourceContainer.setSizeFull();

		this.resourceHandlerComponent = new ResourceHandlerComponent(
				baseFolder, rootPath, null);
		this.resourceHandlerComponent.setSpacing(true);
		resourceContainer.addComponent(resourceHandlerComponent);
		resourceContainer.setComponentAlignment(resourceHandlerComponent,
				Alignment.TOP_LEFT);
		resourceContainer.setExpandRatio(resourceHandlerComponent, 1.0f);

		this.addComponent(resourceContainer);

	}

	abstract protected void doSearch(FileSearchCriteria searchCriteria);

	private void displayResourcesInTable(final Folder folder) {
		resourceHandlerComponent.displayComponent(folder, rootPath,
				rootFolderName, false);
	}

	public void displayResources(String rootPath, String rootFolderName) {
		this.rootPath = rootPath;
		this.rootFolderName = rootFolderName;

		this.baseFolder = new Folder();
		this.baseFolder.setPath(this.rootPath);

		this.displayResourcesInTable(this.baseFolder);

		resourceHandlerComponent
				.addSearchHandlerToBreadCrumb(new SearchHandler<FileSearchCriteria>() {
					@Override
					public void onSearch(FileSearchCriteria criteria) {
						Folder selectedFolder = null;
						selectedFolder = (Folder) FileDashboardComponent.this.resourceService
								.getResource(criteria.getBaseFolder());
						resourceHandlerComponent
								.constructBodyItemContainer(selectedFolder);
						resourceHandlerComponent
								.gotoFolderBreadCumb(selectedFolder);
						FileDashboardComponent.this.baseFolder = selectedFolder;
						resourceHandlerComponent
								.setCurrentBaseFolder(selectedFolder);
					}
				});
	}

	private class FileSearchPanel extends
			GenericSearchPanel<FileSearchCriteria> {
		private static final long serialVersionUID = 1L;
		protected FileSearchCriteria searchCriteria;
		private ComponentContainer menuBar = null;
		private HorizontalLayout basicSearchBody;

		public FileSearchPanel(final ComponentContainer menuBar) {
			this.menuBar = menuBar;
		}

		@Override
		public void attach() {
			super.attach();
			this.createBasicSearchLayout();
		}

		private void createBasicSearchLayout() {

			this.setCompositionRoot(new FileBasicSearchLayout());
		}

		private HorizontalLayout createSearchTopPanel() {
			final HorizontalLayout layout = new HorizontalLayout();
			layout.setWidth("100%");
			layout.setSpacing(true);
			layout.setMargin(new MarginInfo(true, false, true, false));
			layout.setStyleName(UIConstants.HEADER_VIEW);

			final Image titleIcon = new Image(null,
					MyCollabResource.newResource("icons/24/project/file.png"));
			layout.addComponent(titleIcon);
			layout.setComponentAlignment(titleIcon, Alignment.MIDDLE_LEFT);

			final Label searchtitle = new Label("Files");
			searchtitle.setStyleName(UIConstants.HEADER_TEXT);
			layout.addComponent(searchtitle);
			layout.setComponentAlignment(searchtitle, Alignment.MIDDLE_LEFT);
			layout.setExpandRatio(searchtitle, 1.0f);

			if (this.menuBar != null) {
				UiUtils.addComponent(layout, this.menuBar,
						Alignment.MIDDLE_RIGHT);
			}

			return layout;
		}

		@SuppressWarnings("rawtypes")
		class FileBasicSearchLayout extends BasicSearchLayout {

			@SuppressWarnings("unchecked")
			public FileBasicSearchLayout() {
				super(FileSearchPanel.this);
			}

			private static final long serialVersionUID = 1L;
			private TextField nameField;
			private CheckBox myItemCheckbox;

			@Override
			public ComponentContainer constructHeader() {
				return FileSearchPanel.this.createSearchTopPanel();
			}

			@Override
			public ComponentContainer constructBody() {
				final HorizontalLayout basicSearchBody = new HorizontalLayout();
				basicSearchBody.setSpacing(true);
				basicSearchBody.setMargin(true);
				UiUtils.addComponent(basicSearchBody, new Label("Name:"),
						Alignment.MIDDLE_LEFT);

				this.nameField = new TextField();
				this.nameField.setWidth(UIConstants.DEFAULT_CONTROL_WIDTH);
				UiUtils.addComponent(basicSearchBody, this.nameField,
						Alignment.MIDDLE_CENTER);

				this.myItemCheckbox = new CheckBox(
						AppContext
								.getMessage(GenericI18Enum.SEARCH_MYITEMS_CHECKBOX));
				UiUtils.addComponent(basicSearchBody, this.myItemCheckbox,
						Alignment.MIDDLE_CENTER);

				final Button searchBtn = new Button(
						AppContext
								.getMessage(GenericI18Enum.BUTTON_SEARCH_LABEL));
				searchBtn.setStyleName(UIConstants.THEME_GREEN_LINK);
				searchBtn.setIcon(MyCollabResource
						.newResource("icons/16/search.png"));

				searchBtn.addClickListener(new Button.ClickListener() {
					private static final long serialVersionUID = 1L;

					@Override
					public void buttonClick(final ClickEvent event) {
						FileDashboardComponent.this
								.doSearch((FileSearchCriteria) fillupSearchCriteria());
					}
				});
				UiUtils.addComponent(basicSearchBody, searchBtn,
						Alignment.MIDDLE_LEFT);
				final Button cancelBtn = new Button(
						AppContext
								.getMessage(GenericI18Enum.BUTTON_CLEAR_LABEL));

				cancelBtn.addClickListener(new Button.ClickListener() {
					private static final long serialVersionUID = 1L;

					@Override
					public void buttonClick(final ClickEvent event) {
						FileBasicSearchLayout.this.nameField.setValue("");
					}
				});
				cancelBtn.setStyleName(UIConstants.THEME_GRAY_LINK);
				basicSearchBody.addComponent(cancelBtn);

				return basicSearchBody;
			}

			@Override
			protected SearchCriteria fillupSearchCriteria() {
				FileSearchPanel.this.searchCriteria = new FileSearchCriteria();
				FileSearchPanel.this.searchCriteria.setRootFolder(rootPath);
				FileSearchPanel.this.searchCriteria.setFileName(this.nameField
						.getValue().toString().trim());
				FileSearchPanel.this.searchCriteria.setBaseFolder(baseFolder
						.getPath());

				return FileSearchPanel.this.searchCriteria;
			}
		}
	}
}

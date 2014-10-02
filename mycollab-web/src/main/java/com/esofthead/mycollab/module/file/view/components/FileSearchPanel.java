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

import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.EventListener;

import com.esofthead.mycollab.common.i18n.GenericI18Enum;
import com.esofthead.mycollab.eventmanager.ApplicationEvent;
import com.esofthead.mycollab.module.file.domain.criteria.FileSearchCriteria;
import com.esofthead.mycollab.vaadin.AppContext;
import com.esofthead.mycollab.vaadin.ui.GenericSearchPanel;
import com.esofthead.mycollab.vaadin.ui.MyCollabResource;
import com.esofthead.mycollab.vaadin.ui.UIConstants;
import com.esofthead.mycollab.vaadin.ui.UiUtils;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Image;
import com.vaadin.ui.Label;
import com.vaadin.ui.TextField;
import com.vaadin.ui.themes.Reindeer;
import com.vaadin.util.ReflectTools;

/**
 * 
 * @author MyCollab Ltd.
 * @since 4.5.2
 *
 */
public class FileSearchPanel extends GenericSearchPanel<FileSearchCriteria> {
	private static final long serialVersionUID = 1L;

	private String rootPath;
	private FileSearchCriteria searchCriteria;
	private HorizontalLayout basicSearchBody;

	public HorizontalLayout getBasicSearchBody() {
		return basicSearchBody;
	}

	public FileSearchPanel(String rootPath) {
		this.rootPath = rootPath;
		this.setCompositionRoot(new FileBasicSearchLayout());
	}

	class FileBasicSearchLayout extends BasicSearchLayout<FileSearchCriteria> {

		public FileBasicSearchLayout() {
			super(FileSearchPanel.this);
		}

		private static final long serialVersionUID = 1L;
		private TextField nameField;
		private CheckBox myItemCheckbox;

		@Override
		public ComponentContainer constructHeader() {
			final HorizontalLayout layout = new HorizontalLayout();
			layout.setWidth("100%");
			layout.setSpacing(true);
			layout.setMargin(true);

			final Image titleIcon = new Image(null,
					MyCollabResource
							.newResource("icons/24/ecm/document_preview.png"));
			layout.addComponent(titleIcon);
			layout.setComponentAlignment(titleIcon, Alignment.MIDDLE_LEFT);

			final Label searchtitle = new Label("Files");
			searchtitle.setStyleName(Reindeer.LABEL_H2);
			layout.addComponent(searchtitle);
			layout.setComponentAlignment(searchtitle, Alignment.MIDDLE_LEFT);
			layout.setExpandRatio(searchtitle, 1.0f);
			return layout;
		}

		@Override
		public ComponentContainer constructBody() {
			final HorizontalLayout basicSearchBody = new HorizontalLayout();
			basicSearchBody.setSpacing(true);
			basicSearchBody.setMargin(true);
			UiUtils.addComponent(basicSearchBody, new Label("Name:"),
					Alignment.MIDDLE_LEFT);
			this.addStyleName("file-list-view");
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
					AppContext.getMessage(GenericI18Enum.BUTTON_SEARCH));
			searchBtn.setStyleName(UIConstants.THEME_GREEN_LINK);
			searchBtn.setIcon(MyCollabResource
					.newResource("icons/16/search.png"));

			searchBtn.addClickListener(new Button.ClickListener() {

				private static final long serialVersionUID = 1L;

				@Override
				public void buttonClick(final ClickEvent event) {
					fireEvent(new SearchResourceEvent(FileSearchPanel.this,
							fillupSearchCriteria()));
				}
			});
			UiUtils.addComponent(basicSearchBody, searchBtn,
					Alignment.MIDDLE_LEFT);

			final Button cancelBtn = new Button(
					AppContext.getMessage(GenericI18Enum.BUTTON_CLEAR));

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
		protected FileSearchCriteria fillupSearchCriteria() {
			FileSearchPanel.this.searchCriteria = new FileSearchCriteria();
			FileSearchPanel.this.searchCriteria.setRootFolder(rootPath);
			FileSearchPanel.this.searchCriteria.setFileName(this.nameField
					.getValue().toString().trim());
			FileSearchPanel.this.searchCriteria.setBaseFolder(rootPath);

			return FileSearchPanel.this.searchCriteria;
		}
	}

	public void addSearchResourcesListener(SearchResourceListener listener) {
		this.addListener(SearchResourceEvent.VIEW_IDENTIFIER,
				SearchResourceEvent.class, listener,
				SearchResourceListener.viewInitMethod);
	}

	public static interface SearchResourceListener extends EventListener,
			Serializable {
		public static final Method viewInitMethod = ReflectTools.findMethod(
				SearchResourceListener.class, "searchResources",
				SearchResourceEvent.class);

		void searchResources(SearchResourceEvent event);
	}

	public static class SearchResourceEvent extends ApplicationEvent {
		private static final long serialVersionUID = 1L;

		public static final String VIEW_IDENTIFIER = "searchResources";

		public SearchResourceEvent(Object source, FileSearchCriteria criteria) {
			super(source, criteria);
		}
	}

}

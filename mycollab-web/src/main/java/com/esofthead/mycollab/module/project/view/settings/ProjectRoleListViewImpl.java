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

package com.esofthead.mycollab.module.project.view.settings;

import java.util.Arrays;

import com.esofthead.mycollab.common.localization.GenericI18Enum;
import com.esofthead.mycollab.eventmanager.EventBus;
import com.esofthead.mycollab.module.project.CurrentProjectVariables;
import com.esofthead.mycollab.module.project.LabelLink;
import com.esofthead.mycollab.module.project.ProjectLinkBuilder;
import com.esofthead.mycollab.module.project.ProjectRolePermissionCollections;
import com.esofthead.mycollab.module.project.domain.ProjectRole;
import com.esofthead.mycollab.module.project.domain.SimpleProjectRole;
import com.esofthead.mycollab.module.project.domain.criteria.ProjectRoleSearchCriteria;
import com.esofthead.mycollab.module.project.events.ProjectRoleEvent;
import com.esofthead.mycollab.module.project.localization.PeopleI18nEnum;
import com.esofthead.mycollab.module.project.service.ProjectRoleService;
import com.esofthead.mycollab.spring.ApplicationContextUtil;
import com.esofthead.mycollab.vaadin.AppContext;
import com.esofthead.mycollab.vaadin.events.HasMassItemActionHandlers;
import com.esofthead.mycollab.vaadin.events.HasSearchHandlers;
import com.esofthead.mycollab.vaadin.events.HasSelectableItemHandlers;
import com.esofthead.mycollab.vaadin.events.HasSelectionOptionHandlers;
import com.esofthead.mycollab.vaadin.events.MassItemActionHandler;
import com.esofthead.mycollab.vaadin.mvp.AbstractPageView;
import com.esofthead.mycollab.vaadin.mvp.ViewComponent;
import com.esofthead.mycollab.vaadin.ui.CheckBoxDecor;
import com.esofthead.mycollab.vaadin.ui.DefaultMassItemActionHandlersContainer;
import com.esofthead.mycollab.vaadin.ui.MyCollabResource;
import com.esofthead.mycollab.vaadin.ui.SelectionOptionButton;
import com.esofthead.mycollab.vaadin.ui.UIConstants;
import com.esofthead.mycollab.vaadin.ui.UiUtils;
import com.esofthead.mycollab.vaadin.ui.table.AbstractPagedBeanTable;
import com.esofthead.mycollab.vaadin.ui.table.DefaultPagedBeanTable;
import com.esofthead.mycollab.vaadin.ui.table.TableViewField;
import com.vaadin.data.Property;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Table;
import com.vaadin.ui.VerticalLayout;

/**
 * 
 * @author MyCollab Ltd.
 * @since 1.0
 */
@ViewComponent
public class ProjectRoleListViewImpl extends AbstractPageView implements
		ProjectRoleListView {
	private static final long serialVersionUID = 1L;

	private final ProjectRoleSearchPanel searchPanel;
	private SelectionOptionButton selectOptionButton;
	private DefaultPagedBeanTable<ProjectRoleService, ProjectRoleSearchCriteria, SimpleProjectRole> tableItem;
	private final VerticalLayout listLayout;
	private DefaultMassItemActionHandlersContainer tableActionControls;
	private final Label selectedItemsNumberLabel = new Label();

	public ProjectRoleListViewImpl() {
		this.setMargin(new MarginInfo(false, true, false, true));

		this.searchPanel = new ProjectRoleSearchPanel();
		addComponent(this.searchPanel);

		this.listLayout = new VerticalLayout();
		addComponent(this.listLayout);

		this.generateDisplayTable();
	}

	private void generateDisplayTable() {
		this.tableItem = new DefaultPagedBeanTable<ProjectRoleService, ProjectRoleSearchCriteria, SimpleProjectRole>(
				ApplicationContextUtil.getSpringBean(ProjectRoleService.class),
				SimpleProjectRole.class, new TableViewField("", "selected",
						UIConstants.TABLE_CONTROL_WIDTH), Arrays.asList(
						new TableViewField("Name", "rolename",
								UIConstants.TABLE_EX_LABEL_WIDTH),
						new TableViewField("Description", "description",
								UIConstants.TABLE_EX_LABEL_WIDTH)));

		this.tableItem.addGeneratedColumn("selected",
				new Table.ColumnGenerator() {
					private static final long serialVersionUID = 1L;

					@Override
					public Object generateCell(final Table source,
							final Object itemId, final Object columnId) {
						final CheckBoxDecor cb = new CheckBoxDecor("", false);
						cb.setImmediate(true);
						cb.addValueChangeListener(new ValueChangeListener() {
							private static final long serialVersionUID = 1L;

							@Override
							public void valueChange(
									Property.ValueChangeEvent event) {
								final SimpleProjectRole role = ProjectRoleListViewImpl.this.tableItem
										.getBeanByIndex(itemId);
								ProjectRoleListViewImpl.this.tableItem
										.fireSelectItemEvent(role);
							}
						});

						final ProjectRole role = ProjectRoleListViewImpl.this.tableItem
								.getBeanByIndex(itemId);
						role.setExtraData(cb);
						return cb;
					}
				});

		this.tableItem.addGeneratedColumn("rolename",
				new Table.ColumnGenerator() {
					private static final long serialVersionUID = 1L;

					@Override
					public com.vaadin.ui.Component generateCell(
							final Table source, final Object itemId,
							final Object columnId) {
						final ProjectRole role = ProjectRoleListViewImpl.this.tableItem
								.getBeanByIndex(itemId);
						final LabelLink b = new LabelLink(role.getRolename(),
								ProjectLinkBuilder.generateRolePreviewFullLink(
										role.getProjectid(), role.getId()));
						return b;

					}
				});

		this.listLayout.addComponent(this.constructTableActionControls());
		this.listLayout.addComponent(this.tableItem);
	}

	@Override
	public HasSearchHandlers<ProjectRoleSearchCriteria> getSearchHandlers() {
		return this.searchPanel;
	}

	private ComponentContainer constructTableActionControls() {
		final CssLayout layoutWrapper = new CssLayout();
		layoutWrapper.setWidth("100%");
		final HorizontalLayout layout = new HorizontalLayout();
		layout.setSpacing(true);
		layoutWrapper.addStyleName(UIConstants.TABLE_ACTION_CONTROLS);
		layoutWrapper.addComponent(layout);

		this.selectOptionButton = new SelectionOptionButton(this.tableItem);
		layout.addComponent(this.selectOptionButton);

		final Button deleteBtn = new Button(
				AppContext.getMessage(GenericI18Enum.BUTTON_DELETE));
		deleteBtn.setEnabled(CurrentProjectVariables
				.canAccess(ProjectRolePermissionCollections.ROLES));

		this.tableActionControls = new DefaultMassItemActionHandlersContainer();
		if (CurrentProjectVariables
				.canAccess(ProjectRolePermissionCollections.ROLES)) {
			tableActionControls.addActionItem(
					MassItemActionHandler.DELETE_ACTION,
					MyCollabResource.newResource("icons/16/action/delete.png"),
					"delete");
		}

		tableActionControls.addDownloadActionItem(
				MassItemActionHandler.EXPORT_PDF_ACTION,
				MyCollabResource.newResource("icons/16/action/pdf.png"),
				"export", "export.pdf");
		tableActionControls.addDownloadActionItem(
				MassItemActionHandler.EXPORT_EXCEL_ACTION,
				MyCollabResource.newResource("icons/16/action/excel.png"),
				"export", "export.xlsx");
		tableActionControls.addDownloadActionItem(
				MassItemActionHandler.EXPORT_CSV_ACTION,
				MyCollabResource.newResource("icons/16/action/csv.png"),
				"export", "export.csv");

		layout.addComponent(this.tableActionControls);
		layout.addComponent(this.selectedItemsNumberLabel);
		layout.setComponentAlignment(this.selectedItemsNumberLabel,
				Alignment.MIDDLE_CENTER);
		return layoutWrapper;
	}

	@Override
	public void enableActionControls(final int numOfSelectedItems) {
		this.tableActionControls.setVisible(true);
		this.selectedItemsNumberLabel.setValue("Selected: "
				+ numOfSelectedItems);
	}

	@Override
	public void disableActionControls() {
		this.tableActionControls.setVisible(false);
		this.selectOptionButton.setSelectedChecbox(false);
		this.selectedItemsNumberLabel.setValue("");
	}

	@Override
	public HasSelectionOptionHandlers getOptionSelectionHandlers() {
		return this.selectOptionButton;
	}

	@Override
	public HasMassItemActionHandlers getPopupActionHandlers() {
		return this.tableActionControls;
	}

	@Override
	public HasSelectableItemHandlers<SimpleProjectRole> getSelectableItemHandlers() {
		return this.tableItem;
	}

	@Override
	public AbstractPagedBeanTable<ProjectRoleSearchCriteria, SimpleProjectRole> getPagedBeanTable() {
		return this.tableItem;
	}

	private HorizontalLayout createHeaderRight() {
		final HorizontalLayout layout = new HorizontalLayout();

		final Button createBtn = new Button(
				AppContext.getMessage(PeopleI18nEnum.NEW_ROLE_ACTION),
				new Button.ClickListener() {
					private static final long serialVersionUID = 1L;

					@Override
					public void buttonClick(final Button.ClickEvent event) {
						EventBus.getInstance().fireEvent(
								new ProjectRoleEvent.GotoAdd(this, null));
					}
				});
		createBtn.setEnabled(CurrentProjectVariables
				.canWrite(ProjectRolePermissionCollections.ROLES));
		createBtn.setStyleName(UIConstants.THEME_GREEN_LINK);
		createBtn.setIcon(MyCollabResource
				.newResource("icons/16/addRecord.png"));

		UiUtils.addComponent(layout, createBtn, Alignment.MIDDLE_RIGHT);

		return layout;
	}
}

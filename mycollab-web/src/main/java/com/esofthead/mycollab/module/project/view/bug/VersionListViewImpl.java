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

import java.util.Arrays;
import java.util.Date;
import java.util.GregorianCalendar;

import com.esofthead.mycollab.common.TableViewField;
import com.esofthead.mycollab.common.i18n.GenericI18Enum;
import com.esofthead.mycollab.common.i18n.OptionI18nEnum.StatusI18nEnum;
import com.esofthead.mycollab.module.project.CurrentProjectVariables;
import com.esofthead.mycollab.module.project.LabelLink;
import com.esofthead.mycollab.module.project.ProjectLinkBuilder;
import com.esofthead.mycollab.module.project.ProjectRolePermissionCollections;
import com.esofthead.mycollab.module.project.ProjectTooltipGenerator;
import com.esofthead.mycollab.module.project.i18n.VersionI18nEnum;
import com.esofthead.mycollab.module.tracker.domain.SimpleVersion;
import com.esofthead.mycollab.module.tracker.domain.Version;
import com.esofthead.mycollab.module.tracker.domain.criteria.VersionSearchCriteria;
import com.esofthead.mycollab.module.tracker.service.VersionService;
import com.esofthead.mycollab.spring.ApplicationContextUtil;
import com.esofthead.mycollab.vaadin.AppContext;
import com.esofthead.mycollab.vaadin.events.HasMassItemActionHandlers;
import com.esofthead.mycollab.vaadin.events.HasSearchHandlers;
import com.esofthead.mycollab.vaadin.events.HasSelectableItemHandlers;
import com.esofthead.mycollab.vaadin.events.HasSelectionOptionHandlers;
import com.esofthead.mycollab.vaadin.events.MassItemActionHandler;
import com.esofthead.mycollab.vaadin.mvp.AbstractPageView;
import com.esofthead.mycollab.vaadin.mvp.ViewComponent;
import com.esofthead.mycollab.vaadin.mvp.ViewScope;
import com.esofthead.mycollab.vaadin.ui.CheckBoxDecor;
import com.esofthead.mycollab.vaadin.ui.DefaultMassItemActionHandlersContainer;
import com.esofthead.mycollab.vaadin.ui.MyCollabResource;
import com.esofthead.mycollab.vaadin.ui.SelectionOptionButton;
import com.esofthead.mycollab.vaadin.ui.UIConstants;
import com.esofthead.mycollab.vaadin.ui.table.AbstractPagedBeanTable;
import com.esofthead.mycollab.vaadin.ui.table.DefaultPagedBeanTable;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Component;
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
@ViewComponent(scope=ViewScope.PROTOTYPE)
public class VersionListViewImpl extends AbstractPageView implements
		VersionListView {

	private static final long serialVersionUID = 1L;
	private final VersionSearchPanel componentSearchPanel;
	private SelectionOptionButton selectOptionButton;
	private DefaultPagedBeanTable<VersionService, VersionSearchCriteria, SimpleVersion> tableItem;
	private final VerticalLayout componentListLayout;
	private DefaultMassItemActionHandlersContainer tableActionControls;
	private final Label selectedItemsNumberLabel = new Label();

	public VersionListViewImpl() {

		this.setMargin(new MarginInfo(false, true, false, true));

		this.componentSearchPanel = new VersionSearchPanel();
		this.addComponent(this.componentSearchPanel);

		this.componentListLayout = new VerticalLayout();
		this.addComponent(this.componentListLayout);

		this.generateDisplayTable();
	}

	private void generateDisplayTable() {
		this.tableItem = new DefaultPagedBeanTable<VersionService, VersionSearchCriteria, SimpleVersion>(
				ApplicationContextUtil.getSpringBean(VersionService.class),
				SimpleVersion.class,
				new TableViewField(null, "selected",
						UIConstants.TABLE_CONTROL_WIDTH),
				Arrays.asList(
						new TableViewField(VersionI18nEnum.FORM_NAME,
								"versionname", UIConstants.TABLE_EX_LABEL_WIDTH),
						new TableViewField(GenericI18Enum.FORM_DESCRIPTION,
								"description", UIConstants.TABLE_EX_LABEL_WIDTH),
						new TableViewField(VersionI18nEnum.FORM_DUE_DATE,
								"duedate", UIConstants.TABLE_DATE_TIME_WIDTH)));

		this.tableItem.addGeneratedColumn("selected",
				new Table.ColumnGenerator() {
					private static final long serialVersionUID = 1L;

					@Override
					public Object generateCell(final Table source,
							final Object itemId, final Object columnId) {
						final SimpleVersion version = VersionListViewImpl.this.tableItem
								.getBeanByIndex(itemId);
						final CheckBoxDecor cb = new CheckBoxDecor("", version
								.isSelected());
						cb.setImmediate(true);
						cb.addValueChangeListener(new ValueChangeListener() {
							private static final long serialVersionUID = 1L;

							@Override
							public void valueChange(ValueChangeEvent event) {
								VersionListViewImpl.this.tableItem
										.fireSelectItemEvent(version);

							}
						});

						version.setExtraData(cb);
						return cb;
					}
				});

		this.tableItem.addGeneratedColumn("versionname",
				new Table.ColumnGenerator() {
					private static final long serialVersionUID = 1L;

					@Override
					public Component generateCell(final Table source,
							final Object itemId, final Object columnId) {
						final Version bugVersion = VersionListViewImpl.this.tableItem
								.getBeanByIndex(itemId);
						final LabelLink b = new LabelLink(bugVersion
								.getVersionname(), ProjectLinkBuilder
								.generateBugVersionPreviewFullLink(
										bugVersion.getProjectid(),
										bugVersion.getId()));
						if (bugVersion.getStatus() != null
								&& bugVersion.getStatus().equals(
										StatusI18nEnum.Closed.name())) {
							b.addStyleName(UIConstants.LINK_COMPLETED);
						} else if (bugVersion.getDuedate() != null
								&& (bugVersion.getDuedate()
										.before(new GregorianCalendar()
												.getTime()))) {
							b.addStyleName(UIConstants.LINK_OVERDUE);
						}
						b.setDescription(ProjectTooltipGenerator
								.generateToolTipVersion(
										AppContext.getUserLocale(), bugVersion,
										AppContext.getSiteUrl(),
										AppContext.getTimezoneId()));
						return b;

					}
				});

		this.tableItem.addGeneratedColumn("duedate",
				new Table.ColumnGenerator() {
					private static final long serialVersionUID = 1L;

					@Override
					public Component generateCell(final Table source,
							final Object itemId, final Object columnId) {
						final Version bugVersion = VersionListViewImpl.this.tableItem
								.getBeanByIndex(itemId);
						Date duedate = bugVersion.getDuedate();
						if (duedate != null) {
							return new Label(AppContext.formatDate(duedate));
						} else {
							return new Label("");
						}
					}
				});

		this.tableItem.setWidth("100%");

		this.componentListLayout.addComponent(this
				.constructTableActionControls());
		this.componentListLayout.addComponent(this.tableItem);
	}

	@Override
	public HasSearchHandlers<VersionSearchCriteria> getSearchHandlers() {
		return this.componentSearchPanel;
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

		tableActionControls = new DefaultMassItemActionHandlersContainer();

		if (CurrentProjectVariables
				.canAccess(ProjectRolePermissionCollections.VERSIONS)) {
			tableActionControls.addActionItem(
					MassItemActionHandler.DELETE_ACTION,
					MyCollabResource.newResource("icons/16/action/delete.png"),
					"delete",
					AppContext.getMessage(GenericI18Enum.BUTTON_DELETE_LABEL));
		}

		tableActionControls.addActionItem(MassItemActionHandler.MAIL_ACTION,
				MyCollabResource.newResource("icons/16/action/mail.png"),
				"mail", AppContext.getMessage(GenericI18Enum.BUTTON_MAIL));

		tableActionControls.addDownloadActionItem(
				MassItemActionHandler.EXPORT_PDF_ACTION,
				MyCollabResource.newResource("icons/16/action/pdf.png"),
				"export", "export.pdf",
				AppContext.getMessage(GenericI18Enum.BUTTON_EXPORT_PDF));

		tableActionControls.addDownloadActionItem(
				MassItemActionHandler.EXPORT_EXCEL_ACTION,
				MyCollabResource.newResource("icons/16/action/excel.png"),
				"export", "export.xlsx",
				AppContext.getMessage(GenericI18Enum.BUTTON_EXPORT_EXCEL));

		tableActionControls.addDownloadActionItem(
				MassItemActionHandler.EXPORT_CSV_ACTION,
				MyCollabResource.newResource("icons/16/action/csv.png"),
				"export", "export.csv",
				AppContext.getMessage(GenericI18Enum.BUTTON_EXPORT_CSV));

		layout.addComponent(this.tableActionControls);
		layout.addComponent(this.selectedItemsNumberLabel);
		layout.setComponentAlignment(this.selectedItemsNumberLabel,
				Alignment.MIDDLE_CENTER);
		return layoutWrapper;
	}

	@Override
	public void enableActionControls(final int numOfSelectedItems) {
		this.tableActionControls.setVisible(true);
		this.selectedItemsNumberLabel.setValue(AppContext.getMessage(
				GenericI18Enum.TABLE_SELECTED_ITEM_TITLE, numOfSelectedItems));
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
	public HasSelectableItemHandlers<SimpleVersion> getSelectableItemHandlers() {
		return this.tableItem;
	}

	@Override
	public AbstractPagedBeanTable<VersionSearchCriteria, SimpleVersion> getPagedBeanTable() {
		return this.tableItem;
	}
}

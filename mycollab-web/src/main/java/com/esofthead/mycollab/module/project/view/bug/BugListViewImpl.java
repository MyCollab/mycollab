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

import com.esofthead.mycollab.core.arguments.NumberSearchField;
import com.esofthead.mycollab.core.arguments.SearchField;
import com.esofthead.mycollab.core.utils.LocalizationHelper;
import com.esofthead.mycollab.eventmanager.ApplicationEvent;
import com.esofthead.mycollab.eventmanager.ApplicationEventListener;
import com.esofthead.mycollab.eventmanager.EventBus;
import com.esofthead.mycollab.module.file.resource.SimpleGridExportItemsStreamResource;
import com.esofthead.mycollab.module.project.CurrentProjectVariables;
import com.esofthead.mycollab.module.project.ProjectRolePermissionCollections;
import com.esofthead.mycollab.module.project.events.BugEvent;
import com.esofthead.mycollab.module.project.localization.BugI18nEnum;
import com.esofthead.mycollab.module.tracker.domain.SimpleBug;
import com.esofthead.mycollab.module.tracker.domain.criteria.BugSearchCriteria;
import com.esofthead.mycollab.module.tracker.service.BugService;
import com.esofthead.mycollab.reporting.ReportExportType;
import com.esofthead.mycollab.reporting.RpParameterBuilder;
import com.esofthead.mycollab.spring.ApplicationContextUtil;
import com.esofthead.mycollab.vaadin.events.HasSearchHandlers;
import com.esofthead.mycollab.vaadin.events.HasSelectableItemHandlers;
import com.esofthead.mycollab.vaadin.mvp.AbstractPageView;
import com.esofthead.mycollab.vaadin.mvp.ViewComponent;
import com.esofthead.mycollab.vaadin.resource.StreamResourceFactory;
import com.esofthead.mycollab.vaadin.resource.StreamWrapperFileDownloader;
import com.esofthead.mycollab.vaadin.ui.MyCollabResource;
import com.esofthead.mycollab.vaadin.ui.SplitButton;
import com.esofthead.mycollab.vaadin.ui.UIConstants;
import com.esofthead.mycollab.vaadin.ui.UiUtils;
import com.esofthead.mycollab.vaadin.ui.table.IPagedBeanTable;
import com.esofthead.mycollab.vaadin.ui.table.TableClickEvent;
import com.vaadin.server.StreamResource;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

/**
 * 
 * @author MyCollab Ltd.
 * @since 1.0
 * 
 */
@ViewComponent
public class BugListViewImpl extends AbstractPageView implements BugListView {

	private static final long serialVersionUID = 1L;
	private final BugSearchPanel bugSearchPanel;
	private BugTableDisplay tableItem;
	private final VerticalLayout bugListLayout;
	private SplitButton exportButtonControl;

	public BugListViewImpl() {
		/*super(LocalizationHelper.getMessage(BugI18nEnum.BUG_SEARCH_TITLE), "bug_selected.png");

		this.addHeaderRightContent(createHeaderRight());

		CssLayout contentWrapper = new CssLayout();
		contentWrapper.setStyleName("content-wrapper");*/
		this.setMargin(new MarginInfo(false, true, false, true));

		this.bugSearchPanel = new BugSearchPanel();
		addComponent(this.bugSearchPanel);

		this.bugListLayout = new VerticalLayout();
		addComponent(this.bugListLayout);

		this.generateDisplayTable();
		//this.addComponent(contentWrapper);

	}

	private void generateDisplayTable() {

		this.tableItem = new BugTableDisplay(BugListView.VIEW_DEF_ID,
				BugTableFieldDef.action, Arrays.asList(
						BugTableFieldDef.summary, BugTableFieldDef.assignUser,
						BugTableFieldDef.severity, BugTableFieldDef.resolution,
						BugTableFieldDef.duedate));

		this.tableItem
		.addTableListener(new ApplicationEventListener<TableClickEvent>() {
			private static final long serialVersionUID = 1L;

			@Override
			public Class<? extends ApplicationEvent> getEventType() {
				return TableClickEvent.class;
			}

			@Override
			public void handle(final TableClickEvent event) {
				final SimpleBug bug = (SimpleBug) event.getData();
				if ("summary".equals(event.getFieldName())) {
					EventBus.getInstance().fireEvent(
							new BugEvent.GotoRead(BugListViewImpl.this,
									bug.getId()));
				}
			}
		});

		this.bugListLayout.addComponent(this.constructTableActionControls());
		this.bugListLayout.addComponent(this.tableItem);
	}

	@Override
	public HasSearchHandlers<BugSearchCriteria> getSearchHandlers() {
		return this.bugSearchPanel;
	}

	private ComponentContainer constructTableActionControls() {
		final CssLayout layoutWrapper = new CssLayout();
		layoutWrapper.setWidth("100%");
		final HorizontalLayout layout = new HorizontalLayout();
		layout.setSpacing(true);
		layout.setWidth("100%");
		layoutWrapper.addStyleName(UIConstants.TABLE_ACTION_CONTROLS);
		layoutWrapper.addComponent(layout);

		final Label lbEmpty = new Label("");
		layout.addComponent(lbEmpty);
		layout.setExpandRatio(lbEmpty, 1.0f);

		HorizontalLayout buttonControls = new HorizontalLayout();
		buttonControls.setSpacing(true);
		layout.addComponent(buttonControls);

		Button customizeViewBtn = new Button("", new Button.ClickListener() {
			private static final long serialVersionUID = 1L;

			@Override
			public void buttonClick(ClickEvent event) {
				UI.getCurrent().addWindow(
						new BugListCustomizeWindow(BugListView.VIEW_DEF_ID,
								tableItem));

			}
		});
		customizeViewBtn.setIcon(MyCollabResource
				.newResource("icons/16/customize.png"));
		customizeViewBtn.setDescription("Layout Options");
		customizeViewBtn.setStyleName(UIConstants.THEME_GRAY_LINK);
		buttonControls.addComponent(customizeViewBtn);

		Button exportBtn = new Button("Export", new Button.ClickListener() {
			private static final long serialVersionUID = 1L;

			@Override
			public void buttonClick(ClickEvent event) {
				exportButtonControl.setPopupVisible(true);

			}
		});
		exportButtonControl = new SplitButton(exportBtn);
		exportButtonControl.addStyleName(UIConstants.THEME_GRAY_LINK);
		exportButtonControl.setIcon(MyCollabResource
				.newResource("icons/16/export.png"));

		VerticalLayout popupButtonsControl = new VerticalLayout();
		exportButtonControl.setContent(popupButtonsControl);

		Button exportPdfBtn = new Button("Pdf");

		StreamWrapperFileDownloader fileDownloader = new StreamWrapperFileDownloader(
				new StreamResourceFactory() {

					@Override
					public StreamResource getStreamResource() {
						String title = "Bugs of Project "
								+ ((CurrentProjectVariables.getProject() != null && CurrentProjectVariables
								.getProject().getName() != null) ? CurrentProjectVariables
										.getProject().getName() : "");
						BugSearchCriteria searchCriteria = new BugSearchCriteria();
						searchCriteria.setProjectId(new NumberSearchField(
								SearchField.AND, CurrentProjectVariables
								.getProject().getId()));

						StreamResource res = new StreamResource(
								new SimpleGridExportItemsStreamResource.AllItems<BugSearchCriteria, SimpleBug>(
										title,
										new RpParameterBuilder(tableItem
												.getDisplayColumns()),
												ReportExportType.PDF,
												ApplicationContextUtil
												.getSpringBean(BugService.class),
												searchCriteria, SimpleBug.class),
								"export.pdf");
						return res;
					}

				});
		fileDownloader.extend(exportPdfBtn);
		exportPdfBtn.setIcon(MyCollabResource
				.newResource("icons/16/filetypes/pdf.png"));
		exportPdfBtn.setStyleName("link");
		popupButtonsControl.addComponent(exportPdfBtn);

		Button exportExcelBtn = new Button("Excel");
		StreamWrapperFileDownloader excelDownloader = new StreamWrapperFileDownloader(
				new StreamResourceFactory() {

					@Override
					public StreamResource getStreamResource() {
						String title = "Bugs of Project "
								+ ((CurrentProjectVariables.getProject() != null && CurrentProjectVariables
								.getProject().getName() != null) ? CurrentProjectVariables
										.getProject().getName() : "");
						BugSearchCriteria searchCriteria = new BugSearchCriteria();
						searchCriteria.setProjectId(new NumberSearchField(
								SearchField.AND, CurrentProjectVariables
								.getProject().getId()));

						StreamResource res = new StreamResource(
								new SimpleGridExportItemsStreamResource.AllItems<BugSearchCriteria, SimpleBug>(
										title,
										new RpParameterBuilder(tableItem
												.getDisplayColumns()),
												ReportExportType.EXCEL,
												ApplicationContextUtil
												.getSpringBean(BugService.class),
												searchCriteria, SimpleBug.class),
								"export.xlsx");
						return res;
					}
				});
		excelDownloader.extend(exportExcelBtn);
		exportExcelBtn.setIcon(MyCollabResource
				.newResource("icons/16/filetypes/excel.png"));
		exportExcelBtn.setStyleName("link");
		popupButtonsControl.addComponent(exportExcelBtn);

		Button exportCsvBtn = new Button("CSV");

		StreamWrapperFileDownloader csvFileDownloader = new StreamWrapperFileDownloader(
				new StreamResourceFactory() {

					@Override
					public StreamResource getStreamResource() {
						String title = "Bugs of Project "
								+ ((CurrentProjectVariables.getProject() != null && CurrentProjectVariables
								.getProject().getName() != null) ? CurrentProjectVariables
										.getProject().getName() : "");
						BugSearchCriteria searchCriteria = new BugSearchCriteria();
						searchCriteria.setProjectId(new NumberSearchField(
								SearchField.AND, CurrentProjectVariables
								.getProject().getId()));

						StreamResource res = new StreamResource(
								new SimpleGridExportItemsStreamResource.AllItems<BugSearchCriteria, SimpleBug>(
										title,
										new RpParameterBuilder(tableItem
												.getDisplayColumns()),
												ReportExportType.CSV,
												ApplicationContextUtil
												.getSpringBean(BugService.class),
												searchCriteria, SimpleBug.class),
								"export.csv");
						return res;
					}
				});
		csvFileDownloader.extend(exportCsvBtn);

		exportCsvBtn.setIcon(MyCollabResource
				.newResource("icons/16/filetypes/csv.png"));
		exportCsvBtn.setStyleName("link");
		popupButtonsControl.addComponent(exportCsvBtn);

		buttonControls.addComponent(exportButtonControl);
		return layoutWrapper;
	}

	@Override
	public HasSelectableItemHandlers<SimpleBug> getSelectableItemHandlers() {
		return this.tableItem;
	}

	@Override
	public IPagedBeanTable<BugSearchCriteria, SimpleBug> getPagedBeanTable() {
		return this.tableItem;
	}

	@Override
	public void setTitle(final String title) {
		if (this.bugSearchPanel != null) {
			this.bugSearchPanel.setBugTitle(title);
		}
	}

	private HorizontalLayout createHeaderRight() {
		final HorizontalLayout layout = new HorizontalLayout();
		final Button createAccountBtn = new Button(
				LocalizationHelper.getMessage(BugI18nEnum.NEW_BUG_ACTION),
				new Button.ClickListener() {
					private static final long serialVersionUID = 1L;

					@Override
					public void buttonClick(final ClickEvent event) {
						EventBus.getInstance().fireEvent(
								new BugEvent.GotoAdd(this, null));
					}
				});
		createAccountBtn.setEnabled(CurrentProjectVariables
				.canWrite(ProjectRolePermissionCollections.BUGS));
		createAccountBtn.setStyleName(UIConstants.THEME_BLUE_LINK);
		createAccountBtn.setIcon(MyCollabResource
				.newResource("icons/16/addRecord.png"));
		UiUtils.addComponent(layout, createAccountBtn, Alignment.MIDDLE_LEFT);

		return layout;
	}
}

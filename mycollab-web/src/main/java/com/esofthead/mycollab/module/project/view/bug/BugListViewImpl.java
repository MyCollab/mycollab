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

import org.vaadin.hene.popupbutton.PopupButton;

import com.esofthead.mycollab.common.i18n.FileI18nEnum;
import com.esofthead.mycollab.core.arguments.NumberSearchField;
import com.esofthead.mycollab.core.arguments.SearchField;
import com.esofthead.mycollab.eventmanager.EventBusFactory;
import com.esofthead.mycollab.module.file.resource.SimpleGridExportItemsStreamResource;
import com.esofthead.mycollab.module.project.CurrentProjectVariables;
import com.esofthead.mycollab.module.project.events.BugEvent;
import com.esofthead.mycollab.module.tracker.domain.SimpleBug;
import com.esofthead.mycollab.module.tracker.domain.criteria.BugSearchCriteria;
import com.esofthead.mycollab.module.tracker.service.BugService;
import com.esofthead.mycollab.reporting.ReportExportType;
import com.esofthead.mycollab.reporting.RpParameterBuilder;
import com.esofthead.mycollab.spring.ApplicationContextUtil;
import com.esofthead.mycollab.vaadin.AppContext;
import com.esofthead.mycollab.vaadin.events.HasMassItemActionHandlers;
import com.esofthead.mycollab.vaadin.events.HasSearchHandlers;
import com.esofthead.mycollab.vaadin.events.HasSelectableItemHandlers;
import com.esofthead.mycollab.vaadin.events.HasSelectionOptionHandlers;
import com.esofthead.mycollab.vaadin.mvp.AbstractPageView;
import com.esofthead.mycollab.vaadin.mvp.ViewComponent;
import com.esofthead.mycollab.vaadin.mvp.ViewScope;
import com.esofthead.mycollab.vaadin.resource.StreamResourceFactory;
import com.esofthead.mycollab.vaadin.resource.StreamWrapperFileDownloader;
import com.esofthead.mycollab.vaadin.ui.MyCollabResource;
import com.esofthead.mycollab.vaadin.ui.UIConstants;
import com.esofthead.mycollab.vaadin.ui.table.AbstractPagedBeanTable;
import com.esofthead.mycollab.vaadin.ui.table.IPagedBeanTable.TableClickEvent;
import com.esofthead.mycollab.vaadin.ui.table.IPagedBeanTable.TableClickListener;
import com.vaadin.server.StreamResource;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.ComponentContainer;
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
@ViewComponent(scope=ViewScope.PROTOTYPE)
public class BugListViewImpl extends AbstractPageView implements BugListView {

	private static final long serialVersionUID = 1L;
	private final BugSearchPanel bugSearchPanel;
	private BugTableDisplay tableItem;
	private final VerticalLayout bugListLayout;
	private PopupButton exportButtonControl;

	public BugListViewImpl() {

		this.setMargin(new MarginInfo(false, true, false, true));

		this.bugSearchPanel = new BugSearchPanel();
		this.bugListLayout = new VerticalLayout();
		this.generateDisplayTable();
		this.bugSearchPanel.addRightComponent(constructTableActionControls());
		addComponent(this.bugSearchPanel);
		addComponent(this.bugListLayout);

	}

	private void generateDisplayTable() {

		this.tableItem = new BugTableDisplay(BugListView.VIEW_DEF_ID,
				BugTableFieldDef.action, Arrays.asList(
						BugTableFieldDef.summary, BugTableFieldDef.assignUser,
						BugTableFieldDef.severity, BugTableFieldDef.resolution,
						BugTableFieldDef.duedate));

		this.tableItem.addTableListener(new TableClickListener() {
			private static final long serialVersionUID = 1L;

			@Override
			public void itemClick(final TableClickEvent event) {
				final SimpleBug bug = (SimpleBug) event.getData();
				if ("summary".equals(event.getFieldName())) {
					EventBusFactory.getInstance().post(
							new BugEvent.GotoRead(BugListViewImpl.this, bug
									.getId()));
				}
			}
		});
		this.bugListLayout.addComponent(this.tableItem);
	}

	@Override
	public HasSearchHandlers<BugSearchCriteria> getSearchHandlers() {
		return this.bugSearchPanel;
	}

	private ComponentContainer constructTableActionControls() {
		final HorizontalLayout layout = new HorizontalLayout();
		layout.setSpacing(true);
		layout.setWidth("100%");

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
				.newResource("icons/16/option_white.png"));
		customizeViewBtn.setDescription("Layout Options");
		customizeViewBtn.setStyleName(UIConstants.THEME_GRAY_LINK);
		buttonControls.addComponent(customizeViewBtn);

		exportButtonControl = new PopupButton();
		exportButtonControl.addStyleName(UIConstants.THEME_GRAY_LINK);
		exportButtonControl.setIcon(MyCollabResource
				.newResource("icons/16/export.png"));
		exportButtonControl.setDescription(AppContext
				.getMessage(FileI18nEnum.EXPORT_FILE));

		VerticalLayout popupButtonsControl = new VerticalLayout();
		exportButtonControl.setContent(popupButtonsControl);

		Button exportPdfBtn = new Button(
				AppContext.getMessage(FileI18nEnum.PDF));

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

		Button exportExcelBtn = new Button(
				AppContext.getMessage(FileI18nEnum.EXCEL));
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

		Button exportCsvBtn = new Button(
				AppContext.getMessage(FileI18nEnum.CSV));

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
		return layout;
	}

	@Override
	public HasSelectableItemHandlers<SimpleBug> getSelectableItemHandlers() {
		return this.tableItem;
	}

	// @Override
	// public IPagedBeanTable<BugSearchCriteria, SimpleBug> getPagedBeanTable()
	// {
	// return this.tableItem;
	// }

	@Override
	public void setTitle(final String title) {
		if (this.bugSearchPanel != null) {
			this.bugSearchPanel.setBugTitle(title);
		}
	}

	@Override
	public void enableActionControls(int numOfSelectedItem) {
		throw new UnsupportedOperationException(
				"This view doesn't support this operation");
	}

	@Override
	public void disableActionControls() {
		throw new UnsupportedOperationException(
				"This view doesn't support this operation");
	}

	@Override
	public HasSelectionOptionHandlers getOptionSelectionHandlers() {
		throw new UnsupportedOperationException(
				"This view doesn't support this operation");
	}

	@Override
	public HasMassItemActionHandlers getPopupActionHandlers() {
		throw new UnsupportedOperationException(
				"This view doesn't support this operation");
	}

	@Override
	public AbstractPagedBeanTable<BugSearchCriteria, SimpleBug> getPagedBeanTable() {
		return this.tableItem;
	}

}

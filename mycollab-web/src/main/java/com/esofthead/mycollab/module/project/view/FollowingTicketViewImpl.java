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
package com.esofthead.mycollab.module.project.view;

import java.util.Arrays;
import java.util.GregorianCalendar;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;

import com.esofthead.mycollab.common.domain.criteria.MonitorSearchCriteria;
import com.esofthead.mycollab.common.i18n.FollowerI18nEnum;
import com.esofthead.mycollab.core.arguments.SetSearchField;
import com.esofthead.mycollab.core.arguments.StringSearchField;
import com.esofthead.mycollab.eventmanager.EventBusFactory;
import com.esofthead.mycollab.module.project.ProjectTypeConstants;
import com.esofthead.mycollab.module.project.domain.FollowingTicket;
import com.esofthead.mycollab.module.project.events.ProjectEvent;
import com.esofthead.mycollab.module.project.i18n.OptionI18nEnum.BugStatus;
import com.esofthead.mycollab.module.project.service.ProjectFollowingTicketService;
import com.esofthead.mycollab.module.project.view.parameters.BugScreenData;
import com.esofthead.mycollab.module.project.view.parameters.ProjectScreenData;
import com.esofthead.mycollab.module.project.view.parameters.TaskScreenData;
import com.esofthead.mycollab.reporting.ExportItemsStreamResource;
import com.esofthead.mycollab.reporting.ReportExportType;
import com.esofthead.mycollab.reporting.RpParameterBuilder;
import com.esofthead.mycollab.reporting.SimpleGridExportItemsStreamResource;
import com.esofthead.mycollab.shell.events.ShellEvent;
import com.esofthead.mycollab.spring.ApplicationContextUtil;
import com.esofthead.mycollab.vaadin.AppContext;
import com.esofthead.mycollab.vaadin.mvp.AbstractPageView;
import com.esofthead.mycollab.vaadin.mvp.PageActionChain;
import com.esofthead.mycollab.vaadin.mvp.ViewComponent;
import com.esofthead.mycollab.vaadin.resources.LazyStreamSource;
import com.esofthead.mycollab.vaadin.ui.ButtonLink;
import com.esofthead.mycollab.vaadin.ui.MyCollabResource;
import com.esofthead.mycollab.vaadin.ui.SplitButton;
import com.esofthead.mycollab.vaadin.ui.UIConstants;
import com.esofthead.mycollab.vaadin.ui.UserLink;
import com.esofthead.mycollab.vaadin.ui.table.AbstractPagedBeanTable;
import com.vaadin.server.FileDownloader;
import com.vaadin.server.StreamResource;
import com.vaadin.server.StreamResource.StreamSource;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Image;
import com.vaadin.ui.Label;
import com.vaadin.ui.Table;
import com.vaadin.ui.Table.ColumnGenerator;
import com.vaadin.ui.VerticalLayout;

/**
 * 
 * @author MyCollab Ltd.
 * @since 1.0
 * 
 */
@ViewComponent
public class FollowingTicketViewImpl extends AbstractPageView implements
		FollowingTicketView {
	private static final long serialVersionUID = 1L;

	private SplitButton exportButtonControl;
	private final FollowingTicketTable ticketTable;
	private MonitorSearchCriteria searchCriteria;

	public FollowingTicketViewImpl() {
		this.setWidth("100%");

		final VerticalLayout headerWrapper = new VerticalLayout();
		headerWrapper.setWidth("100%");
		headerWrapper.setStyleName("projectfeed-hdr-wrapper");

		HorizontalLayout controlBtns = new HorizontalLayout();

		final HorizontalLayout header = new HorizontalLayout();
		header.setWidth("100%");
		header.setSpacing(true);

		final Image timeIcon = new Image(null,
				MyCollabResource.newResource("icons/24/follow.png"));
		header.addComponent(timeIcon);

		final Label layoutHeader = new Label("My Following Tickets");
		layoutHeader.addStyleName("h2");
		header.addComponent(layoutHeader);
		header.setComponentAlignment(layoutHeader, Alignment.MIDDLE_LEFT);
		header.setExpandRatio(layoutHeader, 1.0f);

		final VerticalLayout contentWrapper = new VerticalLayout();

		contentWrapper.setWidth("100%");
		contentWrapper.addStyleName("content-wrapper");

		headerWrapper.addComponent(header);
		this.addComponent(headerWrapper);
		contentWrapper.addComponent(controlBtns);
		this.addComponent(contentWrapper);

		final Button backBtn = new Button(
				AppContext
						.getMessage(FollowerI18nEnum.BUTTON_BACK_TO_WORKBOARD));
		backBtn.addClickListener(new Button.ClickListener() {
			private static final long serialVersionUID = 1L;

			@Override
			public void buttonClick(final ClickEvent event) {
				EventBusFactory.getInstance().post(
						new ShellEvent.GotoProjectModule(
								FollowingTicketViewImpl.this, null));

			}
		});

		backBtn.addStyleName(UIConstants.THEME_GREEN_LINK);
		backBtn.setIcon(MyCollabResource.newResource("icons/16/back.png"));

		controlBtns.setMargin(new MarginInfo(true, false, true, false));
		controlBtns.setWidth("100%");
		controlBtns.addComponent(backBtn);
		controlBtns.setExpandRatio(backBtn, 1.0f);

		Button exportBtn = new Button("Export", new Button.ClickListener() {
			private static final long serialVersionUID = 1L;

			@Override
			public void buttonClick(ClickEvent event) {
				exportButtonControl.setPopupVisible(true);

			}
		});
		exportButtonControl = new SplitButton(exportBtn);
		exportButtonControl.setWidthUndefined();
		exportButtonControl.addStyleName(UIConstants.THEME_GRAY_LINK);
		exportButtonControl.setIcon(MyCollabResource
				.newResource("icons/16/export.png"));

		VerticalLayout popupButtonsControl = new VerticalLayout();
		exportButtonControl.setContent(popupButtonsControl);

		Button exportPdfBtn = new Button("Pdf");
		FileDownloader pdfDownloader = new FileDownloader(
				constructStreamResource(ReportExportType.PDF));
		pdfDownloader.extend(exportPdfBtn);
		exportPdfBtn.setIcon(MyCollabResource
				.newResource("icons/16/filetypes/pdf.png"));
		exportPdfBtn.setStyleName("link");
		popupButtonsControl.addComponent(exportPdfBtn);

		Button exportExcelBtn = new Button("Excel");
		FileDownloader excelDownloader = new FileDownloader(
				constructStreamResource(ReportExportType.EXCEL));
		excelDownloader.extend(exportExcelBtn);
		exportExcelBtn.setIcon(MyCollabResource
				.newResource("icons/16/filetypes/excel.png"));
		exportExcelBtn.setStyleName("link");
		popupButtonsControl.addComponent(exportExcelBtn);

		controlBtns.addComponent(exportButtonControl);

		this.ticketTable = new FollowingTicketTable();
		this.ticketTable.addStyleName("full-border-table");
		this.ticketTable.setMargin(new MarginInfo(true, false, false, false));
		contentWrapper.addComponent(this.ticketTable);
	}

	private StreamResource constructStreamResource(
			final ReportExportType exportType) {

		LazyStreamSource streamSource = new LazyStreamSource() {
			private static final long serialVersionUID = 1L;

			@Override
			protected StreamSource buildStreamSource() {
				return new SimpleGridExportItemsStreamResource.AllItems<MonitorSearchCriteria, FollowingTicket>(
						"Following Tickets Report",
						new RpParameterBuilder(ticketTable.getDisplayColumns()),
						exportType,
						ApplicationContextUtil
								.getSpringBean(ProjectFollowingTicketService.class),
						searchCriteria, FollowingTicket.class);
			}
		};

		StreamResource res = new StreamResource(streamSource,
				ExportItemsStreamResource.getDefaultExportFileName(exportType));
		return res;
	}

	@Override
	public void displayFollowingTicket(final List<Integer> prjKeys) {
		if (CollectionUtils.isNotEmpty(prjKeys)) {
			searchCriteria = new MonitorSearchCriteria();
			searchCriteria.setExtraTypeIds(new SetSearchField<Integer>(prjKeys
					.toArray(new Integer[0])));
			searchCriteria.setUser(new StringSearchField(AppContext
					.getUsername()));
			this.ticketTable.setSearchCriteria(searchCriteria);
		}
	}

	private class FollowingTicketTable extends
			AbstractPagedBeanTable<MonitorSearchCriteria, FollowingTicket> {

		private static final long serialVersionUID = 1L;
		private ProjectFollowingTicketService projectFollowingTicketService;

		public FollowingTicketTable() {
			super(FollowingTicket.class, Arrays.asList(
					FollowingTicketFieldDef.summary,
					FollowingTicketFieldDef.project,
					FollowingTicketFieldDef.assignee,
					FollowingTicketFieldDef.createdDate));

			this.projectFollowingTicketService = ApplicationContextUtil
					.getSpringBean(ProjectFollowingTicketService.class);

			this.addGeneratedColumn("summary", new ColumnGenerator() {
				private static final long serialVersionUID = 1L;

				@Override
				public Object generateCell(final Table source,
						final Object itemId, final Object columnId) {
					final FollowingTicket ticket = FollowingTicketTable.this
							.getBeanByIndex(itemId);
					final ButtonLink ticketLink = new ButtonLink(ticket
							.getSummary());

					if (ProjectTypeConstants.BUG.equals(ticket.getType())) {
						ticketLink.setIcon(MyCollabResource
								.newResource("icons/16/project/bug.png"));

						if (BugStatus.Verified.name()
								.equals(ticket.getStatus())) {
							ticketLink.addStyleName(UIConstants.LINK_COMPLETED);
						} else if (ticket.getDueDate() != null
								&& ticket.getDueDate().before(
										new GregorianCalendar().getTime())) {
							ticketLink.addStyleName(UIConstants.LINK_OVERDUE);
						}

						ticketLink.addClickListener(new Button.ClickListener() {
							private static final long serialVersionUID = 1L;

							@Override
							public void buttonClick(final ClickEvent event) {
								final int projectId = ticket.getProjectId();
								final int bugId = ticket.getTypeId();
								final PageActionChain chain = new PageActionChain(
										new ProjectScreenData.Goto(projectId),
										new BugScreenData.Read(bugId));
								EventBusFactory.getInstance().post(
										new ProjectEvent.GotoMyProject(this,
												chain));
							}
						});
					} else if (ProjectTypeConstants.TASK.equals(ticket
							.getType())) {
						ticketLink.setIcon(MyCollabResource
								.newResource("icons/16/project/task.png"));

						if ("Closed".equals(ticket.getStatus())) {
							ticketLink.addStyleName(UIConstants.LINK_COMPLETED);
						} else {
							if ("Pending".equals(ticket.getStatus())) {
								ticketLink
										.addStyleName(UIConstants.LINK_PENDING);
							} else if (ticket.getDueDate() != null
									&& ticket.getDueDate().before(
											new GregorianCalendar().getTime())) {
								ticketLink
										.addStyleName(UIConstants.LINK_OVERDUE);
							}
						}

						ticketLink.addClickListener(new Button.ClickListener() {
							private static final long serialVersionUID = 1L;

							@Override
							public void buttonClick(final ClickEvent event) {
								final int projectId = ticket.getProjectId();
								final int taskId = ticket.getTypeId();
								final PageActionChain chain = new PageActionChain(
										new ProjectScreenData.Goto(projectId),
										new TaskScreenData.Read(taskId));
								EventBusFactory.getInstance().post(
										new ProjectEvent.GotoMyProject(this,
												chain));

							}
						});
					}

					return ticketLink;
				}
			});

			this.addGeneratedColumn("projectName", new ColumnGenerator() {
				private static final long serialVersionUID = 1L;

				@Override
				public Object generateCell(final Table source,
						final Object itemId, final Object columnId) {
					final FollowingTicket ticket = FollowingTicketTable.this
							.getBeanByIndex(itemId);
					final ButtonLink projectLink = new ButtonLink(ticket
							.getProjectName());
					projectLink.addClickListener(new Button.ClickListener() {
						private static final long serialVersionUID = 1L;

						@Override
						public void buttonClick(final ClickEvent event) {
							final int projectId = ticket.getProjectId();
							final PageActionChain chain = new PageActionChain(
									new ProjectScreenData.Goto(projectId));
							EventBusFactory.getInstance()
									.post(new ProjectEvent.GotoMyProject(this,
											chain));
						}
					});
					projectLink.setIcon(MyCollabResource
							.newResource("icons/16/project/project.png"));
					return projectLink;
				}
			});

			this.addGeneratedColumn("assignUser", new ColumnGenerator() {
				private static final long serialVersionUID = 1L;

				@Override
				public Object generateCell(final Table source,
						final Object itemId, final Object columnId) {
					final FollowingTicket ticket = FollowingTicketTable.this
							.getBeanByIndex(itemId);
					final UserLink userLink = new UserLink(ticket
							.getAssignUser(), ticket.getAssignUserAvatarId(),
							ticket.getAssignUserFullName());
					return userLink;
				}
			});

			this.addGeneratedColumn("monitorDate", new ColumnGenerator() {
				private static final long serialVersionUID = 1L;

				@Override
				public Object generateCell(final Table source,
						final Object itemId, final Object columnId) {
					final FollowingTicket ticket = FollowingTicketTable.this
							.getBeanByIndex(itemId);
					Label lbl = new Label();
					lbl.setValue(AppContext.formatDate(ticket.getMonitorDate()));
					return lbl;
				}
			});
		}

		@Override
		protected int queryTotalCount() {
			return this.projectFollowingTicketService
					.getTotalCount(this.searchRequest.getSearchCriteria());
		}

		@Override
		protected List<FollowingTicket> queryCurrentData() {
			return this.projectFollowingTicketService
					.findPagableListByCriteria(this.searchRequest);
		}

	}
}

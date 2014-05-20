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
package com.esofthead.mycollab.module.project.view.task;

import java.util.GregorianCalendar;
import java.util.List;

import org.jsoup.Jsoup;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.vaadin.dialogs.ConfirmDialog;
import org.vaadin.hene.popupbutton.PopupButton;

import com.esofthead.mycollab.common.localization.GenericI18Enum;
import com.esofthead.mycollab.configuration.SiteConfiguration;
import com.esofthead.mycollab.core.utils.StringUtils;
import com.esofthead.mycollab.eventmanager.EventBus;
import com.esofthead.mycollab.module.project.CurrentProjectVariables;
import com.esofthead.mycollab.module.project.LabelLink;
import com.esofthead.mycollab.module.project.ProjectLinkBuilder;
import com.esofthead.mycollab.module.project.ProjectLinkUtils;
import com.esofthead.mycollab.module.project.ProjectResources;
import com.esofthead.mycollab.module.project.ProjectRolePermissionCollections;
import com.esofthead.mycollab.module.project.domain.SimpleTask;
import com.esofthead.mycollab.module.project.domain.criteria.TaskSearchCriteria;
import com.esofthead.mycollab.module.project.events.TaskEvent;
import com.esofthead.mycollab.module.project.service.ProjectTaskService;
import com.esofthead.mycollab.module.project.view.settings.component.ProjectUserLink;
import com.esofthead.mycollab.module.user.UserLinkUtils;
import com.esofthead.mycollab.spring.ApplicationContextUtil;
import com.esofthead.mycollab.vaadin.AppContext;
import com.esofthead.mycollab.vaadin.ui.ConfirmDialogExt;
import com.esofthead.mycollab.vaadin.ui.MyCollabResource;
import com.esofthead.mycollab.vaadin.ui.ProgressPercentageIndicator;
import com.esofthead.mycollab.vaadin.ui.UIConstants;
import com.esofthead.mycollab.vaadin.ui.UserAvatarControlFactory;
import com.esofthead.mycollab.vaadin.ui.table.DefaultPagedBeanTable;
import com.esofthead.mycollab.vaadin.ui.table.TableClickEvent;
import com.esofthead.mycollab.vaadin.ui.table.TableViewField;
import com.hp.gagawa.java.elements.A;
import com.hp.gagawa.java.elements.Div;
import com.hp.gagawa.java.elements.H3;
import com.hp.gagawa.java.elements.Img;
import com.hp.gagawa.java.elements.Td;
import com.hp.gagawa.java.elements.Tr;
import com.vaadin.server.Sizeable;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Table;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

public class TaskSearchTableDisplay
		extends
		DefaultPagedBeanTable<ProjectTaskService, TaskSearchCriteria, SimpleTask> {
	private static final long serialVersionUID = 1L;
	private static Logger log = LoggerFactory
			.getLogger(TaskSearchTableDisplay.class);

	public TaskSearchTableDisplay(List<TableViewField> displayColumns) {
		this(null, displayColumns);
	}

	public TaskSearchTableDisplay(TableViewField requiredColumn,
			List<TableViewField> displayColumns) {
		super(ApplicationContextUtil.getSpringBean(ProjectTaskService.class),
				SimpleTask.class, requiredColumn, displayColumns);

		this.addGeneratedColumn("taskname", new Table.ColumnGenerator() {
			private static final long serialVersionUID = 1L;

			@Override
			public com.vaadin.ui.Component generateCell(Table source,
					final Object itemId, Object columnId) {
				final SimpleTask task = TaskSearchTableDisplay.this
						.getBeanByIndex(itemId);

				CssLayout taskName = new CssLayout();

				String taskname = "[%s-%s] %s";
				taskname = String.format(taskname, CurrentProjectVariables
						.getProject().getShortname(), task.getTaskkey(), task
						.getTaskname());
				LabelLink b = new LabelLink(taskname, ProjectLinkBuilder
						.generateTaskPreviewFullLink(task.getProjectid(),
								task.getId()));
				b.setDescription(generateToolTip(task));

				if (StringUtils.isNotNullOrEmpty(task.getPriority())) {
					b.setIconLink(ProjectResources
							.getIconResourceLink12ByTaskPriority(task
									.getPriority()));

				}

				if (task.getPercentagecomplete() != null
						&& 100d == task.getPercentagecomplete()) {
					b.addStyleName(UIConstants.LINK_COMPLETED);
				} else {
					if ("Pending".equals(task.getStatus())) {
						b.addStyleName(UIConstants.LINK_PENDING);
					} else if ((task.getEnddate() != null && (task.getEnddate()
							.before(new GregorianCalendar().getTime())))
							|| (task.getActualenddate() != null && (task
									.getActualenddate()
									.before(new GregorianCalendar().getTime())))
							|| (task.getDeadline() != null && (task
									.getDeadline()
									.before(new GregorianCalendar().getTime())))) {
						b.addStyleName(UIConstants.LINK_OVERDUE);
					}
				}

				taskName.addComponent(b);
				taskName.setWidth("100%");
				taskName.setHeight(SIZE_UNDEFINED, Sizeable.Unit.PIXELS);
				return taskName;

			}
		});

		this.addGeneratedColumn("percentagecomplete",
				new Table.ColumnGenerator() {
					private static final long serialVersionUID = 1L;

					@Override
					public com.vaadin.ui.Component generateCell(Table source,
							final Object itemId, Object columnId) {
						final SimpleTask task = TaskSearchTableDisplay.this
								.getBeanByIndex(itemId);
						Double percomp = (task.getPercentagecomplete() == null) ? new Double(
								0) : task.getPercentagecomplete();
						ProgressPercentageIndicator progress = new ProgressPercentageIndicator(
								percomp);
						progress.setWidth("100px");
						return progress;
					}
				});

		this.addGeneratedColumn("startdate", new Table.ColumnGenerator() {
			private static final long serialVersionUID = 1L;

			@Override
			public com.vaadin.ui.Component generateCell(Table source,
					final Object itemId, Object columnId) {
				final SimpleTask task = TaskSearchTableDisplay.this
						.getBeanByIndex(itemId);
				return new Label(AppContext.formatDate(task.getStartdate()));

			}
		});

		this.addGeneratedColumn("deadline", new Table.ColumnGenerator() {
			private static final long serialVersionUID = 1L;

			@Override
			public com.vaadin.ui.Component generateCell(Table source,
					final Object itemId, Object columnId) {
				final SimpleTask task = TaskSearchTableDisplay.this
						.getBeanByIndex(itemId);
				return new Label(AppContext.formatDate(task.getDeadline()));

			}
		});

		this.addGeneratedColumn("id", new Table.ColumnGenerator() {
			private static final long serialVersionUID = 1L;

			@Override
			public com.vaadin.ui.Component generateCell(Table source,
					final Object itemId, Object columnId) {
				final SimpleTask task = TaskSearchTableDisplay.this
						.getBeanByIndex(itemId);
				PopupButton taskSettingPopupBtn = new PopupButton();
				VerticalLayout filterBtnLayout = new VerticalLayout();
				filterBtnLayout.setMargin(true);
				filterBtnLayout.setSpacing(true);
				filterBtnLayout.setWidth("100px");

				Button editButton = new Button("Edit",
						new Button.ClickListener() {
							private static final long serialVersionUID = 1L;

							@Override
							public void buttonClick(ClickEvent event) {
								EventBus.getInstance().fireEvent(
										new TaskEvent.GotoEdit(
												TaskSearchTableDisplay.this,
												task));
							}
						});
				editButton.setEnabled(CurrentProjectVariables
						.canWrite(ProjectRolePermissionCollections.TASKS));
				editButton.setStyleName("link");
				filterBtnLayout.addComponent(editButton);

				if ((task.getPercentagecomplete() != null && task
						.getPercentagecomplete() != 100)
						|| task.getPercentagecomplete() == null) {
					Button closeBtn = new Button("Close",
							new Button.ClickListener() {
								private static final long serialVersionUID = 1L;

								@Override
								public void buttonClick(Button.ClickEvent event) {
									task.setStatus("Closed");
									task.setPercentagecomplete(100d);

									ProjectTaskService projectTaskService = ApplicationContextUtil
											.getSpringBean(ProjectTaskService.class);
									projectTaskService.updateWithSession(task,
											AppContext.getUsername());

									fireTableEvent(new TableClickEvent(
											TaskSearchTableDisplay.this, task,
											"closeTask"));
								}
							});
					closeBtn.setStyleName("link");
					closeBtn.setEnabled(CurrentProjectVariables
							.canWrite(ProjectRolePermissionCollections.TASKS));
					filterBtnLayout.addComponent(closeBtn);
				} else {
					Button reOpenBtn = new Button("ReOpen",
							new Button.ClickListener() {
								private static final long serialVersionUID = 1L;

								@Override
								public void buttonClick(Button.ClickEvent event) {
									task.setStatus("Open");
									task.setPercentagecomplete(0d);

									ProjectTaskService projectTaskService = ApplicationContextUtil
											.getSpringBean(ProjectTaskService.class);
									projectTaskService.updateWithSession(task,
											AppContext.getUsername());
									fireTableEvent(new TableClickEvent(
											TaskSearchTableDisplay.this, task,
											"reopenTask"));
								}
							});
					reOpenBtn.setStyleName("link");
					reOpenBtn.setEnabled(CurrentProjectVariables
							.canWrite(ProjectRolePermissionCollections.TASKS));
					filterBtnLayout.addComponent(reOpenBtn);
				}

				if (!"Pending".equals(task.getStatus())) {
					if (!"Closed".equals(task.getStatus())) {
						Button pendingBtn = new Button("Pending",
								new Button.ClickListener() {
									private static final long serialVersionUID = 1L;

									@Override
									public void buttonClick(ClickEvent event) {
										task.setStatus("Pending");
										task.setPercentagecomplete(0d);

										ProjectTaskService projectTaskService = ApplicationContextUtil
												.getSpringBean(ProjectTaskService.class);
										projectTaskService.updateWithSession(
												task, AppContext.getUsername());
										fireTableEvent(new TableClickEvent(
												TaskSearchTableDisplay.this,
												task, "pendingTask"));
									}
								});
						pendingBtn.setStyleName("link");
						pendingBtn.setEnabled(CurrentProjectVariables
								.canWrite(ProjectRolePermissionCollections.TASKS));
						filterBtnLayout.addComponent(pendingBtn);
					}
				} else {
					Button reOpenBtn = new Button("ReOpen",
							new Button.ClickListener() {
								private static final long serialVersionUID = 1L;

								@Override
								public void buttonClick(Button.ClickEvent event) {
									task.setStatus("Open");
									task.setPercentagecomplete(0d);

									ProjectTaskService projectTaskService = ApplicationContextUtil
											.getSpringBean(ProjectTaskService.class);
									projectTaskService.updateWithSession(task,
											AppContext.getUsername());

									fireTableEvent(new TableClickEvent(
											TaskSearchTableDisplay.this, task,
											"reopenTask"));
								}
							});
					reOpenBtn.setStyleName("link");
					reOpenBtn.setEnabled(CurrentProjectVariables
							.canWrite(ProjectRolePermissionCollections.TASKS));
					filterBtnLayout.addComponent(reOpenBtn);
				}

				Button deleteBtn = new Button(AppContext
						.getMessage(GenericI18Enum.BUTTON_DELETE_LABEL),
						new Button.ClickListener() {
							private static final long serialVersionUID = 1L;

							@Override
							public void buttonClick(ClickEvent event) {
								ConfirmDialogExt.show(
										UI.getCurrent(),
										AppContext
												.getMessage(
														GenericI18Enum.DELETE_DIALOG_TITLE,
														SiteConfiguration
																.getSiteName()),
										AppContext
												.getMessage(GenericI18Enum.DELETE_SINGLE_ITEM_DIALOG_MESSAGE),
										AppContext
												.getMessage(GenericI18Enum.BUTTON_YES_LABEL),
										AppContext
												.getMessage(GenericI18Enum.BUTTON_NO_LABEL),
										new ConfirmDialog.Listener() {
											private static final long serialVersionUID = 1L;

											@Override
											public void onClose(
													ConfirmDialog dialog) {
												if (dialog.isConfirmed()) {
													ProjectTaskService projectTaskService = ApplicationContextUtil
															.getSpringBean(ProjectTaskService.class);
													projectTaskService.removeWithSession(
															task.getId(),
															AppContext
																	.getUsername(),
															AppContext
																	.getAccountId());
													fireTableEvent(new TableClickEvent(
															TaskSearchTableDisplay.this,
															task, "deleteTask"));
												}
											}
										});
							}
						});
				deleteBtn.setStyleName("link");
				deleteBtn.setEnabled(CurrentProjectVariables
						.canAccess(ProjectRolePermissionCollections.TASKS));
				filterBtnLayout.addComponent(deleteBtn);

				taskSettingPopupBtn.setIcon(MyCollabResource
						.newResource("icons/16/item_settings.png"));
				taskSettingPopupBtn.setStyleName("link");
				taskSettingPopupBtn.setContent(filterBtnLayout);
				return taskSettingPopupBtn;
			}
		});

		this.addGeneratedColumn("assignUserFullName",
				new Table.ColumnGenerator() {
					private static final long serialVersionUID = 1L;

					@Override
					public com.vaadin.ui.Component generateCell(Table source,
							final Object itemId, Object columnId) {
						final SimpleTask task = TaskSearchTableDisplay.this
								.getBeanByIndex(itemId);
						return new ProjectUserLink(task.getAssignuser(), task
								.getAssignUserAvatarId(), task
								.getAssignUserFullName(), true, true);

					}
				});
		this.setWidth("100%");
	}

	private String generateToolTip(SimpleTask task) {
		try {
			Div div = new Div();
			H3 taksName = new H3();
			taksName.appendText(Jsoup.parse(task.getTaskname()).html());
			div.appendChild(taksName);

			com.hp.gagawa.java.elements.Table table = new com.hp.gagawa.java.elements.Table();
			table.setStyle("padding-left:10px; width :500px; color: #5a5a5a; font-size:11px;");
			Tr trRow1 = new Tr();
			trRow1.appendChild(
					new Td().setStyle(
							"width: 70px; vertical-align: top; text-align: right;")
							.appendText("Start Date:")).appendChild(
					new Td().appendText(AppContext.formatDate(task
							.getStartdate())));
			trRow1.appendChild(
					new Td().setStyle(
							"width: 110px; vertical-align: top; text-align: right;")
							.appendText("Actual Start Date:")).appendChild(
					new Td().appendText(AppContext.formatDate(task
							.getActualstartdate())));

			Tr trRow2 = new Tr();
			trRow2.appendChild(
					new Td().setStyle(
							"width: 70px; vertical-align: top; text-align: right;")
							.appendText("End Date:"))
					.appendChild(
							new Td().appendText(AppContext.formatDate(task
									.getEnddate())));
			trRow2.appendChild(
					new Td().setStyle(
							"width: 110px; vertical-align: top; text-align: right;")
							.appendText("Actual End Date:")).appendChild(
					new Td().appendText(AppContext.formatDate(task
							.getActualenddate())));

			Tr trRow3 = new Tr();
			trRow3.appendChild(
					new Td().setStyle(
							"width: 70px; vertical-align: top; text-align: right;")
							.appendText("Deadline:")).appendChild(
					new Td().appendText(AppContext.formatDate(task
							.getDeadline())));
			trRow3.appendChild(
					new Td().setStyle(
							"width: 110px; vertical-align: top; text-align: right;")
							.appendText("Priority:")).appendChild(
					new Td().appendText(StringUtils.getStringFieldValue(task
							.getPriority())));

			Tr trRow4 = new Tr();
			trRow4.appendChild(
					new Td().setStyle(
							"width: 70px; vertical-align: top; text-align: right;")
							.appendText("Assignee:"))
					.appendChild(
							new Td().setStyle(
									"width: 150px;word-wrap: break-word; white-space: normal;vertical-align: top; word-break: break-all;")
									.appendChild(
											new A().setHref(
													(task.getAssignuser() != null) ? UserLinkUtils
															.generatePreviewFullUserLink(
																	AppContext
																			.getSiteUrl(),
																	task.getAssignuser())
															: "")
													.appendChild(
															new Img(
																	"",
																	UserAvatarControlFactory
																			.getAvatarLink(
																					task.getAssignUserAvatarId(),
																					16)))
													.appendText(
															StringUtils
																	.getStringFieldValue(task
																			.getAssignUserFullName()))));
			trRow4.appendChild(
					new Td().setStyle(
							"width: 110px; vertical-align: top; text-align: right;")
							.appendText("TaskGroup:"))
					.appendChild(
							new Td().setStyle(
									"width: 200px;word-wrap: break-word; white-space: normal;vertical-align: top; word-break: break-all;")
									.appendChild(
											new A().setHref(
													(task.getTasklistid() != null) ? AppContext
															.getSiteUrl()
															+ "#"
															+ ProjectLinkUtils
																	.generateTaskGroupPreviewLink(
																			task.getProjectid(),
																			task.getTasklistid())
															: "")
													.appendText(
															StringUtils
																	.getStringFieldValue(task
																			.getTaskListName()))));

			Tr trRow5 = new Tr();
			trRow5.appendChild(
					new Td().setStyle(
							"width: 70px; vertical-align: top; text-align: right;")
							.appendText("Complete(%):"))
					.appendChild(
							new Td().setStyle(
									"word-wrap: break-word; white-space: normal;vertical-align: top; word-break: break-all;")
									.appendText(
											StringUtils.getStringFieldValue(task
													.getPercentagecomplete())));
			Tr trRow6 = new Tr();

			Td trRow6_value = new Td()
					.setStyle(
							"word-wrap: break-word; white-space: normal;vertical-align: top; word-break: break-all;")
					.appendText(
							StringUtils.trimHtmlTags(task.getNotes()));
			trRow6_value.setAttribute("colspan", "3");

			trRow6.appendChild(
					new Td().setStyle(
							"width: 70px; vertical-align: top; text-align: right;")
							.appendText("Notes:")).appendChild(trRow6_value);

			table.appendChild(trRow1);
			table.appendChild(trRow2);
			table.appendChild(trRow3);
			table.appendChild(trRow4);
			table.appendChild(trRow5);
			table.appendChild(trRow6);
			div.appendChild(table);
			return div.write();
		} catch (Exception e) {
			log.error("Error while generate tooltip for Version", e);
			return "";
		}
	}
}

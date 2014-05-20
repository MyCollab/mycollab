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

package com.esofthead.mycollab.module.crm.view.activity;

import java.util.GregorianCalendar;
import java.util.List;

import org.jsoup.Jsoup;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.esofthead.mycollab.core.utils.StringUtils;
import com.esofthead.mycollab.module.crm.CrmLinkGenerator;
import com.esofthead.mycollab.module.crm.data.CrmLinkBuilder;
import com.esofthead.mycollab.module.crm.domain.SimpleActivity;
import com.esofthead.mycollab.module.crm.domain.criteria.ActivitySearchCriteria;
import com.esofthead.mycollab.module.crm.service.EventService;
import com.esofthead.mycollab.module.project.LabelLink;
import com.esofthead.mycollab.module.user.UserLinkUtils;
import com.esofthead.mycollab.spring.ApplicationContextUtil;
import com.esofthead.mycollab.vaadin.AppContext;
import com.esofthead.mycollab.vaadin.ui.CheckBoxDecor;
import com.esofthead.mycollab.vaadin.ui.UIConstants;
import com.esofthead.mycollab.vaadin.ui.UserAvatarControlFactory;
import com.esofthead.mycollab.vaadin.ui.table.DefaultPagedBeanTable;
import com.esofthead.mycollab.vaadin.ui.table.TableViewField;
import com.hp.gagawa.java.elements.A;
import com.hp.gagawa.java.elements.Div;
import com.hp.gagawa.java.elements.H3;
import com.hp.gagawa.java.elements.Img;
import com.hp.gagawa.java.elements.Td;
import com.hp.gagawa.java.elements.Tr;
import com.vaadin.data.Property;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.ui.Label;
import com.vaadin.ui.Table;

/**
 * 
 * @author MyCollab Ltd.
 * @since 2.0
 */
public class ActivityTableDisplay
		extends
		DefaultPagedBeanTable<EventService, ActivitySearchCriteria, SimpleActivity> {
	private static final long serialVersionUID = 1L;

	private static Logger log = LoggerFactory
			.getLogger(ActivityTableDisplay.class);

	public ActivityTableDisplay(List<TableViewField> displayColumns) {
		this(null, displayColumns);
	}

	public ActivityTableDisplay(TableViewField requireColumn,
			List<TableViewField> displayColumns) {
		super(ApplicationContextUtil.getSpringBean(EventService.class),
				SimpleActivity.class, requireColumn, displayColumns);

		this.addGeneratedColumn("selected", new Table.ColumnGenerator() {
			private static final long serialVersionUID = 1L;

			@Override
			public Object generateCell(final Table source, final Object itemId,
					Object columnId) {
				final CheckBoxDecor cb = new CheckBoxDecor("", false);
				cb.setImmediate(true);
				cb.addValueChangeListener(new Property.ValueChangeListener() {
					private static final long serialVersionUID = 1L;

					@Override
					public void valueChange(ValueChangeEvent event) {
						SimpleActivity simpleEvent = ActivityTableDisplay.this
								.getBeanByIndex(itemId);
						ActivityTableDisplay.this
								.fireSelectItemEvent(simpleEvent);
					}
				});

				SimpleActivity simpleEvent = ActivityTableDisplay.this
						.getBeanByIndex(itemId);
				simpleEvent.setExtraData(cb);
				return cb;
			}
		});

		this.addGeneratedColumn("startDate", new Table.ColumnGenerator() {
			private static final long serialVersionUID = 1L;

			@Override
			public com.vaadin.ui.Component generateCell(Table source,
					Object itemId, Object columnId) {

				final SimpleActivity event = ActivityTableDisplay.this
						.getBeanByIndex(itemId);
				Label l = new Label();
				l.setValue(AppContext.formatDateTime(event.getStartDate()));
				return l;
			}
		});

		this.addGeneratedColumn("endDate", new Table.ColumnGenerator() {
			private static final long serialVersionUID = 1L;

			@Override
			public com.vaadin.ui.Component generateCell(Table source,
					Object itemId, Object columnId) {
				final SimpleActivity event = ActivityTableDisplay.this
						.getBeanByIndex(itemId);
				Label l = new Label();
				l.setValue(AppContext.formatDateTime(event.getEndDate()));
				return l;
			}
		});

		this.addGeneratedColumn("subject", new Table.ColumnGenerator() {
			private static final long serialVersionUID = 1L;

			@Override
			public com.vaadin.ui.Component generateCell(Table source,
					final Object itemId, Object columnId) {
				final SimpleActivity simpleEvent = ActivityTableDisplay.this
						.getBeanByIndex(itemId);
				
				final LabelLink b = new LabelLink(simpleEvent.getSubject(),
						CrmLinkBuilder.generateActivityPreviewLinkFull(simpleEvent.getEventType(),simpleEvent.getId()));

				if ("Held".equals(simpleEvent.getStatus())) {
					b.addStyleName(UIConstants.LINK_COMPLETED);
				} else {
					if (simpleEvent.getEndDate() != null
							&& (simpleEvent.getEndDate()
									.before(new GregorianCalendar().getTime()))) {
						b.addStyleName(UIConstants.LINK_OVERDUE);
					}
				}
				b.setDescription(generateToolTip(simpleEvent));
				return b;

			}
		});
	}

	private String generateToolTip(SimpleActivity event) {
		try {
			if (event.getEventType().equals("Event")) {
				return generateToolTipMeeting(event);
			} else if (event.getEventType().equals("Call")) {
				return generateToolTipCall(event);
			} else if (event.getEventType().equals("Task")) {
				return generateToolTipTask(event);
			}
			return null;
		} catch (Exception e) {
			return "";
		}
	}

	private String generateToolTipMeeting(SimpleActivity meeting) {
		try {
			Div div = new Div();
			H3 eventName = new H3();
			eventName.appendText(meeting.getSubject());
			div.appendChild(eventName);

			com.hp.gagawa.java.elements.Table table = new com.hp.gagawa.java.elements.Table();
			table.setStyle("padding-left:10px; width :500px; color: #5a5a5a; font-size:11px;");
			Tr trRow1 = new Tr();
			trRow1.appendChild(
					new Td().setStyle(
							"width: 100px; vertical-align: top; text-align: right;")
							.appendText("Start Date & Time:"))
					.appendChild(
							new Td().setStyle(
									"word-wrap: break-word; white-space: normal;vertical-align: top; word-break: break-all;")
									.appendText(
											AppContext.formatDateTime(meeting
													.getStartDate())));
			trRow1.appendChild(
					new Td().setStyle(
							"width: 90px; vertical-align: top; text-align: right;")
							.appendText("Status:"))
					.appendChild(
							new Td().setStyle(
									"width:110px; vertical-align: top; text-align: left;")
									.appendText(
											(meeting.getStatus() != null) ? meeting
													.getStatus() : ""));

			Tr trRow2 = new Tr();
			trRow2.appendChild(
					new Td().setStyle(
							"width: 100px; vertical-align: top; text-align: right;")
							.appendText("End Date & Time:"))
					.appendChild(
							new Td().setStyle(
									"word-wrap: break-word; white-space: normal;vertical-align: top; word-break: break-all;")
									.appendText(
											AppContext.formatDateTime(meeting
													.getEndDate())));
			trRow2.appendChild(
					new Td().setStyle(
							"width: 90px; vertical-align: top; text-align: right;")
							.appendText("Location:"))
					.appendChild(
							new Td().setStyle(
									"word-wrap: break-word; white-space: normal;vertical-align: top; word-break: break-all;")
									.appendText(
											(meeting.getMeetingLocation() != null) ? meeting
													.getMeetingLocation() : ""));
			Tr trRow3 = new Tr();
			Td trRow3_value = new Td()
					.setStyle(
							"word-wrap: break-word; white-space: normal;vertical-align: top; word-break: break-all;")
					.appendText(
							StringUtils.trimHtmlTags(meeting
									.getDescription()));
			trRow3_value.setAttribute("colspan", "3");

			trRow3.appendChild(
					new Td().setStyle(
							"width: 70px; vertical-align: top; text-align: right;")
							.appendText("Description:")).appendChild(
					trRow3_value);

			table.appendChild(trRow1);
			table.appendChild(trRow2);
			table.appendChild(trRow3);
			div.appendChild(table);
			return div.write();
		} catch (Exception e) {
			return "";
		}
	}

	private String generateToolTipCall(SimpleActivity call) {
		try {
			Div div = new Div();
			H3 callName = new H3();
			callName.appendText(call.getSubject());
			div.appendChild(callName);

			com.hp.gagawa.java.elements.Table table = new com.hp.gagawa.java.elements.Table();
			table.setStyle("padding-left:10px; width :500px; color: #5a5a5a; font-size:11px;");
			Tr trRow1 = new Tr();
			trRow1.appendChild(
					new Td().setStyle(
							"width: 120px; vertical-align: top; text-align: right;")
							.appendText("Start Date & Time:"))
					.appendChild(
							new Td().setStyle(
									"word-wrap: break-word; white-space: normal;vertical-align: top; word-break: break-all;")
									.appendText(
											AppContext.formatDateTime(call
													.getStartDate())));
			trRow1.appendChild(
					new Td().setStyle(
							"width: 110px; vertical-align: top; text-align: right;")
							.appendText("Status:"))
					.appendChild(
							new Td().setStyle(
									"word-wrap: break-word; white-space: normal;vertical-align: top; word-break: break-all;")
									.appendText(
											(call.getStatus() != null) ? call
													.getStatus() : ""));

			Tr trRow2 = new Tr();
			trRow2.appendChild(
					new Td().setStyle(
							"width: 90px; vertical-align: top; text-align: right;")
							.appendText("Duration:"))
					.appendChild(
							new Td().setStyle(
									"word-wrap: break-word; white-space: normal;vertical-align: top; word-break: break-all;")
									.appendText(
											(call.getCallDuration() != null) ? call
													.getCallDuration()
													.toString() : ""));
			trRow2.appendChild(
					new Td().setStyle(
							"width: 110px; vertical-align: top; text-align: right;")
							.appendText("Purpose:"))
					.appendChild(
							new Td().setStyle(
									"word-wrap: break-word; white-space: normal;vertical-align: top; word-break: break-all;")
									.appendText(
											(call.getCallPurpose() != null) ? call
													.getCallPurpose() : ""));
			Tr trRow3 = new Tr();
			Td trRow3_value = new Td()
					.setStyle(
							"word-wrap: break-word; white-space: normal;vertical-align: top; word-break: break-all;")
					.appendText(
							StringUtils.trimHtmlTags(call
									.getDescription()));
			trRow3_value.setAttribute("colspan", "3");

			trRow3.appendChild(
					new Td().setStyle(
							"width: 70px; vertical-align: top; text-align: right;")
							.appendText("Description:")).appendChild(
					trRow3_value);

			Tr trRow4 = new Tr();
			Td trRow4_value = new Td()
					.setStyle(
							"word-wrap: break-word; white-space: normal;vertical-align: top; word-break: break-all;")
					.appendText(
							StringUtils.trimHtmlTags(call
									.getCallResult()));
			trRow4_value.setAttribute("colspan", "3");

			trRow4.appendChild(
					new Td().setStyle(
							"width: 70px; vertical-align: top; text-align: right;")
							.appendText("Result:")).appendChild(trRow4_value);

			table.appendChild(trRow1);
			table.appendChild(trRow2);
			table.appendChild(trRow4);
			div.appendChild(table);
			return div.write();
		} catch (Exception e) {
			return "";
		}
	}

	private String generateToolTipTask(SimpleActivity event) {
		try {
			Div div = new Div();
			H3 eventName = new H3();
			eventName.appendText(Jsoup.parse(event.getSubject()).html());
			div.appendChild(eventName);

			com.hp.gagawa.java.elements.Table table = new com.hp.gagawa.java.elements.Table();
			table.setStyle("padding-left:10px; width :500px; color: #5a5a5a; font-size:11px;");
			Tr trRow1 = new Tr();
			trRow1.appendChild(
					new Td().setStyle(
							"width: 100px; vertical-align: top; text-align: right;")
							.appendText("Start Date:"))
					.appendChild(
							new Td().setStyle(
									"word-wrap: break-word; white-space: normal;vertical-align: top; word-break: break-all;")
									.appendText(
											AppContext.formatDateTime(event
													.getStartDate())));
			trRow1.appendChild(
					new Td().setStyle(
							"width: 90px; vertical-align: top; text-align: right;")
							.appendText("Status:"))
					.appendChild(
							new Td().setStyle(
									"word-wrap: break-word; white-space: normal;vertical-align: top; word-break: break-all;")
									.setStyle(
											"width:110px; vertical-align: top; text-align: left;")
									.appendText(
											StringUtils
													.getStringFieldValue(event
															.getStatus())));

			Tr trRow2 = new Tr();
			trRow2.appendChild(
					new Td().setStyle(
							"width: 100px; vertical-align: top; text-align: right;")
							.appendText("Due Date:"))
					.appendChild(
							new Td().setStyle(
									"word-wrap: break-word; white-space: normal;vertical-align: top; word-break: break-all;")
									.appendText(
											AppContext.formatDateTime(event
													.getEndDate())));
			trRow2.appendChild(
					new Td().setStyle(
							"width: 90px; vertical-align: top; text-align: right;")
							.appendText("Contact:"))
					.appendChild(
							new Td().setStyle(
									"width:110px; vertical-align: top; text-align: left;")
									.appendChild(
											new A().setHref(
													(event.getContactId() != null) ? AppContext
															.getSiteUrl()
															+ "#"
															+ CrmLinkGenerator
																	.generateContactPreviewLink(event
																			.getContactId())
															: "")
													.appendText(
															StringUtils
																	.getStringFieldValue(event
																			.getContactFullName()))));

			Tr trRow3 = new Tr();
			trRow3.appendChild(
					new Td().setStyle(
							"width: 100px; vertical-align: top; text-align: right;")
							.appendText("Priority:"))
					.appendChild(
							new Td().setStyle(
									"word-wrap: break-word; white-space: normal;vertical-align: top; word-break: break-all;")
									.appendText(
											StringUtils
													.getStringFieldValue(event
															.getPriority())));
			trRow3.appendChild(
					new Td().setStyle(
							"width: 90px; vertical-align: top; text-align: right;")
							.appendText("Assignee:"))
					.appendChild(
							new Td().setStyle(
									"width: 150px;word-wrap: break-word; white-space: normal;vertical-align: top; word-break: break-all;")
									.appendChild(
											new A().setHref(
													(event.getAssignUser() != null) ? UserLinkUtils
															.generatePreviewFullUserLink(
																	AppContext
																			.getSiteUrl(),
																	event.getAssignUser())
															: "")
													.appendChild(
															new Img(
																	"",
																	UserAvatarControlFactory
																			.getAvatarLink(
																					event.getAssignUserAvatarId(),
																					16)))
													.appendText(
															StringUtils
																	.getStringFieldValue(event
																			.getAssignUserFullName()))));

			Tr trRow4 = new Tr();
			Td trRow4_value = new Td()
					.setStyle(
							"word-wrap: break-word; white-space: normal;vertical-align: top; word-break: break-all;")
					.appendText(
							StringUtils.trimHtmlTags(event
									.getDescription()));
			trRow4_value.setAttribute("colspan", "3");

			trRow4.appendChild(
					new Td().setStyle(
							"width: 70px; vertical-align: top; text-align: right;")
							.appendText("Description:")).appendChild(
					trRow4_value);

			table.appendChild(trRow1);
			table.appendChild(trRow2);
			table.appendChild(trRow3);
			div.appendChild(table);
			return div.write();
		} catch (Exception e) {
			log.error("Error while generate Event tooltip", e);
			return "";
		}
	}
}

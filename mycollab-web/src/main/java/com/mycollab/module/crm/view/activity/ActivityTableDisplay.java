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

package com.mycollab.module.crm.view.activity;

import com.hp.gagawa.java.elements.*;
import com.mycollab.common.TableViewField;
import com.mycollab.configuration.StorageFactory;
import com.mycollab.core.utils.StringUtils;
import com.mycollab.module.crm.CrmLinkGenerator;
import com.mycollab.module.crm.CrmTypeConstants;
import com.mycollab.module.crm.CrmLinkBuilder;
import com.mycollab.module.crm.domain.SimpleActivity;
import com.mycollab.module.crm.domain.criteria.ActivitySearchCriteria;
import com.mycollab.module.crm.service.EventService;
import com.mycollab.module.crm.ui.CrmAssetsManager;
import com.mycollab.module.user.AccountLinkGenerator;
import com.mycollab.spring.AppContextUtil;
import com.mycollab.vaadin.MyCollabUI;
import com.mycollab.vaadin.UserUIContext;
import com.mycollab.vaadin.ui.ELabel;
import com.mycollab.vaadin.web.ui.CheckBoxDecor;
import com.mycollab.vaadin.web.ui.LabelLink;
import com.mycollab.vaadin.web.ui.WebThemes;
import com.mycollab.vaadin.web.ui.table.DefaultPagedBeanTable;
import com.vaadin.server.FontAwesome;
import org.jsoup.Jsoup;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * @author MyCollab Ltd.
 * @since 2.0
 */
public class ActivityTableDisplay extends DefaultPagedBeanTable<EventService, ActivitySearchCriteria, SimpleActivity> {
    private static final long serialVersionUID = 1L;

    private static final Logger LOG = LoggerFactory.getLogger(ActivityTableDisplay.class);

    public ActivityTableDisplay(List<TableViewField> displayColumns) {
        this(null, displayColumns);
    }

    public ActivityTableDisplay(TableViewField requireColumn, List<TableViewField> displayColumns) {
        super(AppContextUtil.getSpringBean(EventService.class), SimpleActivity.class, requireColumn, displayColumns);

        this.addGeneratedColumn("selected", (source, itemId, columnId) -> {
            final SimpleActivity simpleEvent = getBeanByIndex(itemId);
            final CheckBoxDecor cb = new CheckBoxDecor("", simpleEvent.isSelected());
            cb.setImmediate(true);
            cb.addValueChangeListener(valueChangeEvent -> fireSelectItemEvent(simpleEvent));
            simpleEvent.setExtraData(cb);
            return cb;
        });

        this.addGeneratedColumn("startDate", (source, itemId, columnId) -> {
            SimpleActivity event = getBeanByIndex(itemId);
            return new ELabel(UserUIContext.formatPrettyTime(event.getStartDate()))
                    .withDescription(UserUIContext.formatDateTime(event.getStartDate()));
        });

        this.addGeneratedColumn("endDate", (source, itemId, columnId) -> {
            SimpleActivity event = getBeanByIndex(itemId);
            return new ELabel(UserUIContext.formatPrettyTime(event.getEndDate())).withDescription(UserUIContext.formatDateTime(event.getEndDate()));
        });

        this.addGeneratedColumn("subject", (source, itemId, columnId) -> {
            SimpleActivity simpleEvent = getBeanByIndex(itemId);

            LabelLink b = new LabelLink(simpleEvent.getSubject(), CrmLinkBuilder.generateActivityPreviewLinkFull(
                    simpleEvent.getEventType(), simpleEvent.getId()));
            FontAwesome iconLink = CrmAssetsManager.getAsset(simpleEvent.getEventType());
            b.setIconLink(iconLink);

            if (simpleEvent.isCompleted()) {
                b.addStyleName(WebThemes.LINK_COMPLETED);
            } else if (simpleEvent.isOverdue()) {
                b.addStyleName(WebThemes.LINK_OVERDUE);
            }
            b.setDescription(generateToolTip(simpleEvent));
            return b;
        });
    }

    private static String generateToolTip(SimpleActivity event) {
        if (CrmTypeConstants.MEETING.equals(event.getEventType())) {
            return generateToolTipMeeting(event);
        } else if (CrmTypeConstants.CALL.equals(event.getEventType())) {
            return generateToolTipCall(event);
        } else if (CrmTypeConstants.TASK.equals(event.getEventType())) {
            return generateToolTipTask(event);
        }
        return "";
    }

    private static String generateToolTipMeeting(SimpleActivity meeting) {
        try {
            Div div = new Div();
            H3 eventName = new H3();
            eventName.appendText(meeting.getSubject());
            div.appendChild(eventName);

            com.hp.gagawa.java.elements.Table table = new com.hp.gagawa.java.elements.Table();
            table.setStyle("padding-left:10px; width :500px; color: #5a5a5a; font-size:11px;");
            Tr trRow1 = new Tr();
            trRow1.appendChild(
                    new Td().setStyle("width: 100px; vertical-align: top; text-align: right;")
                            .appendText("Start Date & Time:"))
                    .appendChild(new Td().setStyle("word-wrap: break-word; white-space: normal;vertical-align: top;")
                                    .appendText(UserUIContext.formatDateTime(meeting.getStartDate())));
            trRow1.appendChild(
                    new Td().setStyle("width: 90px; vertical-align: top; text-align: right;").appendText("Status:"))
                    .appendChild(new Td().setStyle("width:110px; vertical-align: top; text-align: left;")
                                    .appendText((meeting.getStatus() != null) ? meeting.getStatus() : ""));

            Tr trRow2 = new Tr();
            trRow2.appendChild(
                    new Td().setStyle("width: 100px; vertical-align: top; text-align: right;").appendText("End Date & Time:"))
                    .appendChild(new Td().setStyle("word-wrap: break-word; white-space: normal;vertical-align: top;")
                                    .appendText(UserUIContext.formatDateTime(meeting.getEndDate())));
            trRow2.appendChild(new Td().setStyle("width: 90px; vertical-align: top; text-align: right;")
                            .appendText("Location:"))
                    .appendChild(new Td().setStyle("word-wrap: break-word; white-space: normal;vertical-align: top;")
                                    .appendText((meeting.getMeetingLocation() != null) ? meeting.getMeetingLocation() : ""));
            Tr trRow3 = new Tr();
            Td trRow3_value = new Td().setStyle("word-wrap: break-word; white-space: normal;vertical-align: top;")
                    .appendText(StringUtils.trimHtmlTags(meeting.getDescription()));
            trRow3_value.setAttribute("colspan", "3");

            trRow3.appendChild(new Td().setStyle("width: 70px; vertical-align: top; text-align: right;").appendText("Description:"))
                    .appendChild(trRow3_value);

            table.appendChild(trRow1);
            table.appendChild(trRow2);
            table.appendChild(trRow3);
            div.appendChild(table);
            return div.write();
        } catch (Exception e) {
            return "";
        }
    }

    private static String generateToolTipCall(SimpleActivity call) {
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
                                    "word-wrap: break-word; white-space: normal;vertical-align: top;")
                                    .appendText(
                                            UserUIContext.formatDateTime(call
                                                    .getStartDate())));
            trRow1.appendChild(new Td().setStyle(
                    "width: 110px; vertical-align: top; text-align: right;")
                    .appendText("Status:"))
                    .appendChild(
                            new Td().setStyle(
                                    "word-wrap: break-word; white-space: normal;vertical-align: top;")
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
                                    "word-wrap: break-word; white-space: normal;vertical-align: top;")
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
                                    "word-wrap: break-word; white-space: normal;vertical-align: top;")
                                    .appendText(
                                            (call.getCallPurpose() != null) ? call
                                                    .getCallPurpose() : ""));
            Tr trRow3 = new Tr();
            Td trRow3_value = new Td()
                    .setStyle(
                            "word-wrap: break-word; white-space: normal;vertical-align: top;")
                    .appendText(StringUtils.trimHtmlTags(call.getDescription()));
            trRow3_value.setAttribute("colspan", "3");

            trRow3.appendChild(
                    new Td().setStyle(
                            "width: 70px; vertical-align: top; text-align: right;")
                            .appendText("Description:")).appendChild(
                    trRow3_value);

            Tr trRow4 = new Tr();
            Td trRow4_value = new Td()
                    .setStyle(
                            "word-wrap: break-word; white-space: normal;vertical-align: top;")
                    .appendText(StringUtils.trimHtmlTags(call.getCallResult()));
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

    private static String generateToolTipTask(SimpleActivity event) {
        try {
            Div div = new Div();
            H3 eventName = new H3();
            eventName.appendText(Jsoup.parse(event.getSubject()).html());
            div.appendChild(eventName);

            com.hp.gagawa.java.elements.Table table = new com.hp.gagawa.java.elements.Table();
            table.setStyle("padding-left:10px; width :500px; color: #5a5a5a; font-size:11px;");
            Tr trRow1 = new Tr();
            trRow1.appendChild(new Td().setStyle("width: 100px; vertical-align: top; text-align: right;").appendText("Start Date:"))
                    .appendChild(new Td().setStyle("word-wrap: break-word; white-space: normal;vertical-align: top;")
                            .appendText(UserUIContext.formatDateTime(event.getStartDate())));
            trRow1.appendChild(new Td().setStyle("width: 90px; vertical-align: top; text-align: right;").appendText("Status:"))
                    .appendChild(new Td().setStyle(
                            "word-wrap: break-word; white-space: normal;vertical-align: top;")
                            .setStyle("width:110px; vertical-align: top; text-align: left;")
                            .appendText(StringUtils.trimHtmlTags(event.getStatus())));

            Tr trRow2 = new Tr();
            trRow2.appendChild(new Td().setStyle("width: 100px; vertical-align: top; text-align: right;")
                    .appendText("Due Date:")).appendChild(
                    new Td().setStyle("word-wrap: break-word; white-space: normal;vertical-align: top;")
                            .appendText(UserUIContext.formatDateTime(event.getEndDate())));
            trRow2.appendChild(new Td().setStyle("width: 90px; vertical-align: top; text-align: right;")
                    .appendText("Contact:"))
                    .appendChild(new Td().setStyle("width:110px; vertical-align: top; text-align: left;")
                            .appendChild(new A().setHref((event.getContactId() != null) ? MyCollabUI.getSiteUrl() + "#"
                                    + CrmLinkGenerator.generateContactPreviewLink(event.getContactId()) : "")
                                    .appendText(StringUtils.trimHtmlTags(event.getContactFullName()))));

            Tr trRow3 = new Tr();
            trRow3.appendChild(
                    new Td().setStyle("width: 100px; vertical-align: top; text-align: right;").appendText("Priority:"))
                    .appendChild(new Td().setStyle("word-wrap: break-word; white-space: normal;vertical-align: top;")
                            .appendText(StringUtils.trimHtmlTags(event.getPriority())));
            trRow3.appendChild(new Td().setStyle("width: 90px; vertical-align: top; text-align: right;")
                    .appendText("Assignee:"))
                    .appendChild(
                            new Td().setStyle("width: 150px;word-wrap: break-word; white-space: normal;vertical-align: top;")
                                    .appendChild(new A().setHref((event.getAssignUser() != null) ? AccountLinkGenerator.generatePreviewFullUserLink(
                                            MyCollabUI.getSiteUrl(), event.getAssignUser()) : "")
                                            .appendChild(new Img("", StorageFactory.getAvatarPath(event.getAssignUserAvatarId(), 16)))
                                            .appendText(StringUtils.trimHtmlTags(event.getAssignUserFullName()))));

            Tr trRow4 = new Tr();
            Td trRow4_value = new Td().setStyle("word-wrap: break-word; white-space: normal;vertical-align: top;")
                    .appendText(StringUtils.trimHtmlTags(event.getDescription()));
            trRow4_value.setAttribute("colspan", "3");

            trRow4.appendChild(new Td().setStyle("width: 70px; vertical-align: top; text-align: right;")
                    .appendText("Description:")).appendChild(trRow4_value);

            table.appendChild(trRow1);
            table.appendChild(trRow2);
            table.appendChild(trRow3);
            div.appendChild(table);
            return div.write();
        } catch (Exception e) {
            LOG.error("Error while generate Event tooltip", e);
            return "";
        }
    }
}

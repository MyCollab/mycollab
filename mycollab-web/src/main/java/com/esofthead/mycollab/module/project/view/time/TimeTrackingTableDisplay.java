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
package com.esofthead.mycollab.module.project.view.time;

import com.esofthead.mycollab.common.TableViewField;
import com.esofthead.mycollab.common.i18n.OptionI18nEnum;
import com.esofthead.mycollab.core.utils.BeanUtility;
import com.esofthead.mycollab.core.utils.DateTimeUtils;
import com.esofthead.mycollab.html.DivLessFormatter;
import com.esofthead.mycollab.module.project.ProjectLinkBuilder;
import com.esofthead.mycollab.module.project.ProjectResources;
import com.esofthead.mycollab.module.project.ProjectTypeConstants;
import com.esofthead.mycollab.module.project.domain.SimpleItemTimeLogging;
import com.esofthead.mycollab.module.project.domain.criteria.ItemTimeLoggingSearchCriteria;
import com.esofthead.mycollab.module.project.i18n.OptionI18nEnum.BugStatus;
import com.esofthead.mycollab.module.project.service.ItemTimeLoggingService;
import com.esofthead.mycollab.module.project.view.settings.component.ProjectUserLink;
import com.esofthead.mycollab.spring.ApplicationContextUtil;
import com.esofthead.mycollab.utils.TooltipHelper;
import com.esofthead.mycollab.vaadin.AppContext;
import com.esofthead.mycollab.vaadin.ui.LabelLink;
import com.esofthead.mycollab.vaadin.ui.MyCollabResource;
import com.esofthead.mycollab.vaadin.ui.UIConstants;
import com.esofthead.mycollab.vaadin.ui.WebResourceIds;
import com.esofthead.mycollab.vaadin.ui.table.DefaultPagedBeanTable;
import com.hp.gagawa.java.elements.A;
import com.hp.gagawa.java.elements.Img;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.*;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Table.ColumnGenerator;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.UUID;

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
public class TimeTrackingTableDisplay
        extends
        DefaultPagedBeanTable<ItemTimeLoggingService, ItemTimeLoggingSearchCriteria, SimpleItemTimeLogging> {
    private static final long serialVersionUID = 1L;

    private static final Logger LOG = LoggerFactory
            .getLogger(TimeTrackingTableDisplay.class);

    public TimeTrackingTableDisplay(List<TableViewField> displayColumns) {
        super(ApplicationContextUtil
                        .getSpringBean(ItemTimeLoggingService.class),
                SimpleItemTimeLogging.class, displayColumns);

        this.addGeneratedColumn("logUserFullName", new Table.ColumnGenerator() {
            private static final long serialVersionUID = 1L;

            @Override
            public com.vaadin.ui.Component generateCell(final Table source,
                                                        final Object itemId, final Object columnId) {
                final SimpleItemTimeLogging timeItem = TimeTrackingTableDisplay.this
                        .getBeanByIndex(itemId);

                return new ProjectUserLink(timeItem.getLoguser(), timeItem
                        .getLogUserAvatarId(), timeItem.getLogUserFullName());

            }
        });

        this.addGeneratedColumn("summary", new Table.ColumnGenerator() {
                    private static final long serialVersionUID = 1L;

                    @Override
                    public com.vaadin.ui.Component generateCell(final Table source,
                                                                final Object itemId, final Object columnId) {
                        SimpleItemTimeLogging itemLogging = TimeTrackingTableDisplay.this
                                .getBeanByIndex(itemId);

                        try {
                            VerticalLayout summaryWrapper = new VerticalLayout();

                            String type = itemLogging.getType();

                            if (type == null) {
                                return new Label(itemLogging.getNote(),
                                        ContentMode.HTML);
                            } else {
                                Label timeTrackingLink = new Label(buildItemValue(itemLogging), ContentMode.HTML);
                                timeTrackingLink.addStyleName("link");
                                timeTrackingLink.addStyleName(UIConstants.WORD_WRAP);
                                timeTrackingLink.setWidth("100%");

                                if (ProjectTypeConstants.BUG.equals(type)) {
                                    if (BugStatus.Verified.name().equals(
                                            itemLogging.getStatus())) {
                                        timeTrackingLink
                                                .addStyleName(UIConstants.LINK_COMPLETED);
                                    } else if (itemLogging.getDueDate() != null
                                            && (itemLogging.getDueDate()
                                            .before(DateTimeUtils.getCurrentDateWithoutMS()))) {
                                        timeTrackingLink
                                                .addStyleName(UIConstants.LINK_OVERDUE);
                                    }
                                } else if (type.equals(ProjectTypeConstants.TASK)) {
                                    if (itemLogging.getPercentageComplete() != null
                                            && 100d == itemLogging.getPercentageComplete()) {
                                        timeTrackingLink
                                                .addStyleName(UIConstants.LINK_COMPLETED);
                                    } else {
                                        if (OptionI18nEnum.StatusI18nEnum.Pending.name().equals(itemLogging.getStatus())) {
                                            timeTrackingLink
                                                    .addStyleName(UIConstants.LINK_PENDING);
                                        } else if (itemLogging.getDueDate() != null
                                                && (itemLogging.getDueDate()
                                                .before(DateTimeUtils.getCurrentDateWithoutMS()))) {
                                            timeTrackingLink
                                                    .addStyleName(UIConstants.LINK_OVERDUE);
                                        }
                                    }
                                } else {
                                    if (OptionI18nEnum.StatusI18nEnum.Closed.name().equals(itemLogging.getStatus())) {
                                        timeTrackingLink
                                                .addStyleName(UIConstants.LINK_COMPLETED);
                                    } else if (itemLogging.getDueDate() != null
                                            && (itemLogging.getDueDate()
                                            .before(DateTimeUtils.getCurrentDateWithoutMS()))) {
                                        timeTrackingLink
                                                .addStyleName(UIConstants.LINK_OVERDUE);
                                    }
                                }
                                summaryWrapper.addComponent(timeTrackingLink);

                                if (StringUtils.isNotBlank(itemLogging.getNote())) {
                                    summaryWrapper.addComponent(new Label(itemLogging
                                            .getNote(), ContentMode.HTML));
                                }

                                return summaryWrapper;
                            }


                        } catch (Exception e) {
                            LOG.error(
                                    "Error: " + BeanUtility.printBeanObj(itemLogging),
                                    e);
                            return new Label("");
                        }

                    }
                }

        );

        this.

                addGeneratedColumn("projectName", new ColumnGenerator() {
                            private static final long serialVersionUID = 1L;

                            @Override
                            public Object generateCell(Table source, Object itemId,
                                                       Object columnId) {
                                final SimpleItemTimeLogging itemLogging = TimeTrackingTableDisplay.this
                                        .getBeanByIndex(itemId);

                                LabelLink b = new LabelLink(itemLogging.getProjectName(),
                                        ProjectLinkBuilder.generateProjectFullLink(itemLogging
                                                .getProjectid()));
                                b.setIconLink(MyCollabResource
                                        .newResourceLink(WebResourceIds._16_project_project));
                                return b;
                            }
                        }

                );

        this.

                addGeneratedColumn("isbillable", new ColumnGenerator() {
                            private static final long serialVersionUID = 1L;

                            @Override
                            public Object generateCell(Table source, Object itemId,
                                                       Object columnId) {
                                final SimpleItemTimeLogging timeLogging = TimeTrackingTableDisplay.this
                                        .getBeanByIndex(itemId);
                                Button icon = new Button();
                                if (timeLogging.getIsbillable()) {
                                    icon.setIcon(MyCollabResource
                                            .newResource(WebResourceIds._16_yes));
                                } else {
                                    icon.setIcon(MyCollabResource
                                            .newResource(WebResourceIds._16_no));
                                }
                                icon.setStyleName("link");
                                return icon;
                            }
                        }

                );

        this.

                addGeneratedColumn("logforday", new ColumnGenerator() {
                            private static final long serialVersionUID = 1L;

                            @Override
                            public com.vaadin.ui.Component generateCell(final Table source,
                                                                        final Object itemId, final Object columnId) {
                                final SimpleItemTimeLogging timeLogging = TimeTrackingTableDisplay.this
                                        .getBeanByIndex(itemId);
                                final Label l = new Label();
                                l.setValue(AppContext.formatDate(timeLogging.getLogforday()));
                                return l;
                            }
                        }

                );

        this.

                addGeneratedColumn("id", new Table.ColumnGenerator() {
                            private static final long serialVersionUID = 1L;

                            @Override
                            public Object generateCell(Table source, Object itemId,
                                                       Object columnId) {
                                final SimpleItemTimeLogging itemLogging = TimeTrackingTableDisplay.this
                                        .getBeanByIndex(itemId);

                                HorizontalLayout layout = new HorizontalLayout();
                                layout.setSpacing(true);
                                Button editBtn = new Button("", new Button.ClickListener() {
                                    private static final long serialVersionUID = 1L;

                                    @Override
                                    public void buttonClick(ClickEvent event) {
                                        fireTableEvent(new TableClickEvent(
                                                TimeTrackingTableDisplay.this, itemLogging,
                                                "edit"));

                                    }
                                });
                                editBtn.setStyleName("link");
                                editBtn.setIcon(MyCollabResource
                                        .newResource(WebResourceIds._16_edit));
                                layout.addComponent(editBtn);

                                Button deleteBtn = new Button("", new Button.ClickListener() {
                                    private static final long serialVersionUID = 1L;

                                    @Override
                                    public void buttonClick(ClickEvent event) {
                                        fireTableEvent(new TableClickEvent(
                                                TimeTrackingTableDisplay.this, itemLogging,
                                                "delete"));

                                    }
                                });
                                deleteBtn.setStyleName("link");
                                deleteBtn.setIcon(MyCollabResource
                                        .newResource(WebResourceIds._16_delete));
                                layout.addComponent(deleteBtn);
                                return layout;
                            }

                        }

                );

        this.

                setWidth("100%");
    }

    private String buildItemValue(SimpleItemTimeLogging itemLogging) {
        String type = itemLogging.getType();
        if (type == null) {
            return itemLogging.getNote();
        }

        String uid = UUID.randomUUID().toString();
        DivLessFormatter div = new DivLessFormatter();
        Img image = new Img("", ProjectResources.getResourceLink(itemLogging
                .getType()));
        A itemLink = new A();
        itemLink.setId("tag" + uid);

        if (ProjectTypeConstants.TASK.equals(itemLogging.getType())
                || ProjectTypeConstants.BUG.equals(itemLogging.getType())) {
            itemLink.setHref(ProjectLinkBuilder.generateProjectItemLink(
                    itemLogging.getProjectShortName(),
                    itemLogging.getProjectid(), itemLogging.getType(),
                    itemLogging.getExtraTypeId() + ""));
        } else {
            itemLink.setHref(ProjectLinkBuilder.generateProjectItemLink(
                    itemLogging.getProjectShortName(),
                    itemLogging.getProjectid(), itemLogging.getType(),
                    itemLogging.getTypeid() + ""));
        }

        String arg17 = "'" + uid + "'";
        String arg18 = "'" + itemLogging.getType() + "'";
        String arg19 = "'" + itemLogging.getTypeid() + "'";
        String arg20 = "'" + AppContext.getSiteUrl() + "tooltip/'";
        String arg21 = "'" + AppContext.getAccountId() + "'";
        String arg22 = "'" + AppContext.getSiteUrl() + "'";
        String arg23 = AppContext.getSession().getTimezone();
        String arg24 = "'" + AppContext.getUserLocale().toString() + "'";

        String mouseOverFunc = String.format(
                "return overIt(%s,%s,%s,%s,%s,%s,%s,%s);", arg17, arg18, arg19,
                arg20, arg21, arg22, arg23, arg24);
        itemLink.setAttribute("onmouseover", mouseOverFunc);
        itemLink.appendText(itemLogging.getSummary());

        div.appendChild(image, DivLessFormatter.EMPTY_SPACE(), itemLink, DivLessFormatter.EMPTY_SPACE(),
                TooltipHelper.buildDivTooltipEnable(uid));
        return div.write();
    }
}

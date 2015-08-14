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
import com.esofthead.mycollab.module.project.ProjectTypeConstants;
import com.esofthead.mycollab.module.project.domain.SimpleItemTimeLogging;
import com.esofthead.mycollab.module.project.domain.criteria.ItemTimeLoggingSearchCriteria;
import com.esofthead.mycollab.module.project.i18n.OptionI18nEnum.BugStatus;
import com.esofthead.mycollab.module.project.service.ItemTimeLoggingService;
import com.esofthead.mycollab.module.project.ui.ProjectAssetsManager;
import com.esofthead.mycollab.module.project.view.settings.component.ProjectUserLink;
import com.esofthead.mycollab.spring.ApplicationContextUtil;
import com.esofthead.mycollab.utils.TooltipHelper;
import com.esofthead.mycollab.vaadin.AppContext;
import com.esofthead.mycollab.vaadin.ui.FontIconLabel;
import com.esofthead.mycollab.vaadin.ui.LabelLink;
import com.esofthead.mycollab.vaadin.ui.UIConstants;
import com.esofthead.mycollab.vaadin.ui.table.DefaultPagedBeanTable;
import com.hp.gagawa.java.elements.A;
import com.hp.gagawa.java.elements.Text;
import com.vaadin.server.FontAwesome;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Label;
import com.vaadin.ui.Table;
import com.vaadin.ui.Table.ColumnGenerator;
import com.vaadin.ui.VerticalLayout;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.vaadin.viritin.layouts.MHorizontalLayout;

import java.util.List;
import java.util.UUID;

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
public class TimeTrackingTableDisplay
        extends DefaultPagedBeanTable<ItemTimeLoggingService, ItemTimeLoggingSearchCriteria, SimpleItemTimeLogging> {
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
                                timeTrackingLink.addStyleName(UIConstants.THEME_LINK);
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

        this.addGeneratedColumn("projectName", new ColumnGenerator() {
                            private static final long serialVersionUID = 1L;

                            @Override
                            public Object generateCell(Table source, Object itemId,
                                                       Object columnId) {
                                final SimpleItemTimeLogging itemLogging = TimeTrackingTableDisplay.this
                                        .getBeanByIndex(itemId);

                                LabelLink b = new LabelLink(itemLogging.getProjectName(),
                                        ProjectLinkBuilder.generateProjectFullLink(itemLogging
                                                .getProjectid()));
                                b.setIconLink(ProjectAssetsManager.getAsset(ProjectTypeConstants.PROJECT));
                                return b;
                            }
                        }

                );

        this.addGeneratedColumn("isbillable", new ColumnGenerator() {
                            private static final long serialVersionUID = 1L;

                            @Override
                            public Object generateCell(Table source, Object itemId,
                                                       Object columnId) {
                                final SimpleItemTimeLogging timeLogging = TimeTrackingTableDisplay.this
                                        .getBeanByIndex(itemId);
                                FontIconLabel icon;
                                if (timeLogging.getIsbillable()) {
                                    icon = new FontIconLabel(FontAwesome.CHECK);
                                } else {
                                    icon = new FontIconLabel(FontAwesome.TIMES);
                                }
                                return icon;
                            }
                        }

                );

        this.addGeneratedColumn("logforday", new ColumnGenerator() {
                            private static final long serialVersionUID = 1L;

                            @Override
                            public com.vaadin.ui.Component generateCell(final Table source,
                                                                        final Object itemId, final Object columnId) {
                                final SimpleItemTimeLogging timeLogging = TimeTrackingTableDisplay.this
                                        .getBeanByIndex(itemId);
                                return new Label(AppContext.formatDate(timeLogging.getLogforday()));
                            }
                        }

                );

        this.addGeneratedColumn("id", new Table.ColumnGenerator() {
                            private static final long serialVersionUID = 1L;

                            @Override
                            public Object generateCell(Table source, Object itemId,
                                                       Object columnId) {
                                final SimpleItemTimeLogging itemLogging = TimeTrackingTableDisplay.this
                                        .getBeanByIndex(itemId);

                                MHorizontalLayout layout = new MHorizontalLayout();
                                Button editBtn = new Button("", new Button.ClickListener() {
                                    private static final long serialVersionUID = 1L;

                                    @Override
                                    public void buttonClick(ClickEvent event) {
                                        fireTableEvent(new TableClickEvent(
                                                TimeTrackingTableDisplay.this, itemLogging,
                                                "edit"));

                                    }
                                });
                                editBtn.addStyleName(UIConstants.BUTTON_ICON_ONLY);
                                editBtn.setIcon(FontAwesome.EDIT);

                                Button deleteBtn = new Button("", new Button.ClickListener() {
                                    private static final long serialVersionUID = 1L;

                                    @Override
                                    public void buttonClick(ClickEvent event) {
                                        fireTableEvent(new TableClickEvent(
                                                TimeTrackingTableDisplay.this, itemLogging,
                                                "delete"));

                                    }
                                });
                                deleteBtn.setIcon(FontAwesome.TRASH_O);
                                deleteBtn.addStyleName(UIConstants.BUTTON_ICON_ONLY);
                                layout.with(editBtn, deleteBtn);
                                return layout;
                            }

                        }

                );

        this.setWidth("100%");
    }

    private String buildItemValue(SimpleItemTimeLogging itemLogging) {
        String type = itemLogging.getType();
        if (type == null) {
            return itemLogging.getNote();
        }

        String uid = UUID.randomUUID().toString();
        DivLessFormatter div = new DivLessFormatter();
        Text image = new Text(ProjectAssetsManager.getAsset(itemLogging
                .getType()).getHtml());
        A itemLink = new A().setId("tag" + uid);

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

        itemLink.setAttribute("onmouseover", TooltipHelper.projectHoverJsFunction(uid, itemLogging.getType(), itemLogging.getTypeid() + ""));
        itemLink.setAttribute("onmouseleave", TooltipHelper.itemMouseLeaveJsFunction(uid));
        itemLink.appendText(itemLogging.getSummary());

        div.appendChild(image, DivLessFormatter.EMPTY_SPACE(), itemLink, DivLessFormatter.EMPTY_SPACE(),
                TooltipHelper.buildDivTooltipEnable(uid));
        return div.write();
    }
}

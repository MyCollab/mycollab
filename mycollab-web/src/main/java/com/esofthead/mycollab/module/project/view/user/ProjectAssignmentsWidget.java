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
package com.esofthead.mycollab.module.project.view.user;

import com.esofthead.mycollab.common.i18n.GenericI18Enum;
import com.esofthead.mycollab.configuration.Storage;
import com.esofthead.mycollab.core.arguments.DateSearchField;
import com.esofthead.mycollab.core.arguments.SearchField;
import com.esofthead.mycollab.core.arguments.SetSearchField;
import com.esofthead.mycollab.core.arguments.StringSearchField;
import com.esofthead.mycollab.core.utils.DateTimeUtils;
import com.esofthead.mycollab.core.utils.StringUtils;
import com.esofthead.mycollab.html.DivLessFormatter;
import com.esofthead.mycollab.module.project.CurrentProjectVariables;
import com.esofthead.mycollab.module.project.ProjectLinkBuilder;
import com.esofthead.mycollab.module.project.ProjectTypeConstants;
import com.esofthead.mycollab.module.project.domain.ProjectGenericTask;
import com.esofthead.mycollab.module.project.domain.criteria.ProjectGenericTaskSearchCriteria;
import com.esofthead.mycollab.module.project.i18n.ProjectCommonI18nEnum;
import com.esofthead.mycollab.module.project.i18n.TaskI18nEnum;
import com.esofthead.mycollab.module.project.service.ProjectGenericTaskService;
import com.esofthead.mycollab.module.project.ui.ProjectAssetsManager;
import com.esofthead.mycollab.spring.ApplicationContextUtil;
import com.esofthead.mycollab.utils.TooltipHelper;
import com.esofthead.mycollab.vaadin.AppContext;
import com.esofthead.mycollab.vaadin.ui.AbstractBeanPagedList;
import com.esofthead.mycollab.vaadin.ui.DefaultBeanPagedList;
import com.esofthead.mycollab.vaadin.ui.SafeHtmlLabel;
import com.hp.gagawa.java.elements.A;
import com.hp.gagawa.java.elements.Div;
import com.hp.gagawa.java.elements.Img;
import com.hp.gagawa.java.elements.Text;
import com.vaadin.data.Property;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.*;
import org.vaadin.maddon.layouts.MHorizontalLayout;
import org.vaadin.maddon.layouts.MVerticalLayout;

import java.util.UUID;

/**
 * @author MyCollab Ltd.
 * @since 4.1
 */
public class ProjectAssignmentsWidget extends MVerticalLayout {
    private static final long serialVersionUID = 1L;

    private ProjectGenericTaskSearchCriteria searchCriteria;

    private Label titleLbl;
    private DefaultBeanPagedList<ProjectGenericTaskService, ProjectGenericTaskSearchCriteria, ProjectGenericTask> taskList;

    public ProjectAssignmentsWidget() {
        withSpacing(false).withMargin(new MarginInfo(true, false, true, false));
        titleLbl = new Label(AppContext.getMessage(ProjectCommonI18nEnum.WIDGET_OPEN_ASSIGNMENTS_TITLE, 0));

        final CheckBox overdueSelection = new CheckBox("Overdue");
        overdueSelection.addValueChangeListener(new Property.ValueChangeListener() {
            @Override
            public void valueChange(Property.ValueChangeEvent valueChangeEvent) {
                boolean isOverdueOption = overdueSelection.getValue();
                if (isOverdueOption) {
                    searchCriteria.setDueDate(new DateSearchField(DateTimeUtils.getCurrentDateWithoutMS()));
                } else {
                    searchCriteria.setDueDate(null);
                }
                updateSearchResult();
            }
        });

        final CheckBox myItemsSelection = new CheckBox("My Items");
        myItemsSelection.addValueChangeListener(new Property.ValueChangeListener() {
            @Override
            public void valueChange(Property.ValueChangeEvent valueChangeEvent) {
                boolean isMyItemsOption = myItemsSelection.getValue();
                if (isMyItemsOption) {
                    searchCriteria.setAssignUser(new StringSearchField(AppContext.getUsername()));
                } else {
                    searchCriteria.setAssignUser(null);
                }
                updateSearchResult();
            }
        });

        MHorizontalLayout header = new MHorizontalLayout().withMargin(new MarginInfo(false, true, false, true))
                .withHeight("34px").with(titleLbl, overdueSelection, myItemsSelection)
                .withAlign(titleLbl, Alignment.MIDDLE_LEFT).withAlign(overdueSelection, Alignment.MIDDLE_RIGHT)
                .withAlign(myItemsSelection, Alignment.MIDDLE_RIGHT).expand(titleLbl);
        header.addStyleName("panel-header");

        taskList = new DefaultBeanPagedList<>(ApplicationContextUtil.getSpringBean(ProjectGenericTaskService.class),
                new TaskRowDisplayHandler(), 10);
        this.with(header, taskList);
    }

    public void showOpenAssignments() {
        searchCriteria = new ProjectGenericTaskSearchCriteria();
        searchCriteria.setIsOpenned(new SearchField());
        searchCriteria.setProjectIds(new SetSearchField<>(CurrentProjectVariables.getProjectId()));
        updateSearchResult();
    }

    private void updateSearchResult() {
        taskList.setSearchCriteria(searchCriteria);
        titleLbl.setValue(AppContext.getMessage(ProjectCommonI18nEnum.WIDGET_OPEN_ASSIGNMENTS_TITLE, taskList.getTotalCount()));
    }

    public static class TaskRowDisplayHandler implements DefaultBeanPagedList.RowDisplayHandler<ProjectGenericTask> {

        @Override
        public Component generateRow(AbstractBeanPagedList host, ProjectGenericTask genericTask, int rowIndex) {
            CssLayout layout = new CssLayout();
            layout.setWidth("100%");
            layout.setStyleName("list-row");

            Div itemDiv = buildItemValue(genericTask);

            Label taskLbl = new Label(itemDiv.write(), ContentMode.HTML);
            if (genericTask.isOverdue()) {
                taskLbl.addStyleName("overdue");
            }

            layout.addComponent(taskLbl);

            Label descLbl;
            if (org.apache.commons.lang3.StringUtils.isBlank(genericTask.getDescription())) {
                descLbl = new Label("<<No Description>>");
            } else {
                descLbl = new SafeHtmlLabel(StringUtils.trim(genericTask.getDescription(), 250, true));
            }
            layout.addComponent(descLbl);

            Div footerDiv = new Div().setCSSClass("activity-date");

            if (genericTask.getDueDate() != null) {
                footerDiv.appendChild(new Text(AppContext.getMessage(TaskI18nEnum.OPT_DUE_DATE,
                        AppContext.formatPrettyTime(genericTask.getDueDatePlusOne())))).
                        setTitle(AppContext.formatDate(genericTask.getDueDate()));
            } else {
                footerDiv.appendChild(new Text(AppContext.getMessage(TaskI18nEnum.OPT_DUE_DATE, "Undefined")));
            }


            if (genericTask.getAssignUser() != null) {
                footerDiv.appendChild(buildAssigneeValue(genericTask));
            }

            layout.addComponent(new Label(footerDiv.write(), ContentMode.HTML));
            return layout;
        }

        private Div buildItemValue(ProjectGenericTask task) {
            String uid = UUID.randomUUID().toString();
            Div div = new DivLessFormatter();
            Text image = new Text(ProjectAssetsManager.getAsset(task.getType()).getHtml());
            A itemLink = new A().setId("tag" + uid);
            if (ProjectTypeConstants.TASK.equals(task.getType()) || ProjectTypeConstants.BUG.equals(task.getType())) {
                itemLink.setHref(ProjectLinkBuilder.generateProjectItemLink(task.getProjectShortName(),
                        task.getProjectId(), task.getType(), task.getExtraTypeId() + ""));
            } else {
                itemLink.setHref(ProjectLinkBuilder.generateProjectItemLink(task.getProjectShortName(),
                        task.getProjectId(), task.getType(), task.getTypeId() + ""));
            }

            itemLink.setAttribute("onmouseover", TooltipHelper.projectHoverJsFunction(uid, task.getType(), task.getTypeId() + ""));
            itemLink.setAttribute("onmouseleave", TooltipHelper.itemMouseLeaveJsFunction(uid));
            itemLink.appendText(task.getName());

            div.appendChild(image, DivLessFormatter.EMPTY_SPACE(), itemLink, DivLessFormatter.EMPTY_SPACE(),
                    TooltipHelper.buildDivTooltipEnable(uid));
            return div;
        }

        private Div buildAssigneeValue(ProjectGenericTask task) {
            String uid = UUID.randomUUID().toString();
            Div div = new DivLessFormatter();
            Img userAvatar = new Img("", Storage.getAvatarPath(task.getAssignUserAvatarId(), 16));
            A userLink = new A().setId("tag" + uid).setHref(ProjectLinkBuilder.generateProjectMemberFullLink(
                    task.getProjectId(), task.getAssignUser()));

            userLink.setAttribute("onmouseover", TooltipHelper.userHoverJsFunction(uid, task.getAssignUser()));
            userLink.setAttribute("onmouseleave", TooltipHelper.itemMouseLeaveJsFunction(uid));
            userLink.appendText(StringUtils.trim(task.getAssignUserFullName(), 30, true));

            String assigneeTxt = AppContext.getMessage(GenericI18Enum.FORM_ASSIGNEE) + ": ";

            div.appendChild(DivLessFormatter.EMPTY_SPACE(), DivLessFormatter.EMPTY_SPACE(), DivLessFormatter.EMPTY_SPACE(),
                    DivLessFormatter.EMPTY_SPACE(), new Text(assigneeTxt), userAvatar, DivLessFormatter.EMPTY_SPACE(),
                    userLink, DivLessFormatter.EMPTY_SPACE(), TooltipHelper.buildDivTooltipEnable(uid));
            return div;
        }
    }
}
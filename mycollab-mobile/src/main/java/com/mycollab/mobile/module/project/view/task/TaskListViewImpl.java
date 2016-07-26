/**
 * This file is part of mycollab-mobile.
 *
 * mycollab-mobile is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * mycollab-mobile is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with mycollab-mobile.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.mycollab.mobile.module.project.view.task;

import com.esofthead.vaadin.navigationbarquickmenu.NavigationBarQuickMenu;
import com.hp.gagawa.java.elements.A;
import com.hp.gagawa.java.elements.Div;
import com.hp.gagawa.java.elements.Img;
import com.mycollab.common.i18n.DayI18nEnum;
import com.mycollab.common.i18n.GenericI18Enum;
import com.mycollab.configuration.StorageFactory;
import com.mycollab.core.utils.StringUtils;
import com.mycollab.db.arguments.NumberSearchField;
import com.mycollab.db.arguments.StringSearchField;
import com.mycollab.eventmanager.EventBusFactory;
import com.mycollab.html.DivLessFormatter;
import com.mycollab.mobile.module.project.events.TaskEvent;
import com.mycollab.mobile.module.project.ui.AbstractListPageView;
import com.mycollab.mobile.ui.AbstractPagedBeanList;
import com.mycollab.mobile.ui.DefaultPagedBeanList;
import com.mycollab.mobile.ui.MobileUIConstants;
import com.mycollab.mobile.ui.SearchInputField;
import com.mycollab.module.project.CurrentProjectVariables;
import com.mycollab.module.project.ProjectLinkBuilder;
import com.mycollab.module.project.ProjectLinkGenerator;
import com.mycollab.module.project.ProjectTypeConstants;
import com.mycollab.module.project.domain.SimpleTask;
import com.mycollab.module.project.domain.criteria.TaskSearchCriteria;
import com.mycollab.module.project.i18n.OptionI18nEnum;
import com.mycollab.module.project.i18n.TaskI18nEnum;
import com.mycollab.module.project.service.ProjectTaskService;
import com.mycollab.module.project.ui.ProjectAssetsManager;
import com.mycollab.spring.AppContextUtil;
import com.mycollab.vaadin.AppContext;
import com.mycollab.vaadin.mvp.ViewComponent;
import com.mycollab.vaadin.ui.ELabel;
import com.mycollab.vaadin.ui.UIConstants;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.CssLayout;
import org.vaadin.viritin.layouts.MHorizontalLayout;
import org.vaadin.viritin.layouts.MVerticalLayout;

/**
 * @author MyCollab Ltd.
 * @since 4.5.0
 */
@ViewComponent
public class TaskListViewImpl extends AbstractListPageView<TaskSearchCriteria, SimpleTask> implements TaskListView {
    private static final long serialVersionUID = -3705209608075399509L;

    public TaskListViewImpl() {
        this.setCaption(AppContext.getMessage(TaskI18nEnum.M_VIEW_LIST_TITLE));
    }

    @Override
    protected AbstractPagedBeanList<TaskSearchCriteria, SimpleTask> createBeanList() {
        return new DefaultPagedBeanList<>(AppContextUtil.getSpringBean(ProjectTaskService.class), new TaskRowDisplayHandler());
    }

    @Override
    protected SearchInputField<TaskSearchCriteria> createSearchField() {
        return new SearchInputField<TaskSearchCriteria>() {
            @Override
            protected TaskSearchCriteria fillUpSearchCriteria(String value) {
                TaskSearchCriteria searchCriteria = new TaskSearchCriteria();
                searchCriteria.setProjectId(NumberSearchField.and(CurrentProjectVariables.getProjectId()));
                searchCriteria.setTaskName(StringSearchField.and(value));
                return searchCriteria;
            }
        };
    }

    @Override
    protected Component buildRightComponent() {
        NavigationBarQuickMenu menu = new NavigationBarQuickMenu();
        menu.setButtonCaption("...");
        MVerticalLayout content = new MVerticalLayout();
        content.with(new Button(AppContext.getMessage(TaskI18nEnum.NEW),
                clickEvent -> EventBusFactory.getInstance().post(new TaskEvent.GotoAdd(this, null))));
        menu.setContent(content);
        return menu;
    }

    private static class TaskRowDisplayHandler implements AbstractPagedBeanList.RowDisplayHandler<SimpleTask> {

        @Override
        public Component generateRow(final SimpleTask task, int rowIndex) {
            MVerticalLayout rowLayout = new MVerticalLayout().withFullWidth();

            A taskLink = new A(ProjectLinkBuilder.generateTaskPreviewFullLink(task.getTaskkey(), task
                    .getProjectShortname())).appendText(String.format("[#%s] - %s", task.getTaskkey(), task.getTaskname()));

            CssLayout taskLbl = new CssLayout(new ELabel(taskLink.write(), ContentMode.HTML).withStyleName(UIConstants.TEXT_ELLIPSIS));
            rowLayout.with(new MHorizontalLayout(new ELabel(ProjectAssetsManager.getAsset(ProjectTypeConstants.TASK)
                    .getHtml(), ContentMode.HTML).withWidthUndefined(), taskLbl).expand(taskLbl).withFullWidth());

            CssLayout metaInfoLayout = new CssLayout();
            rowLayout.with(metaInfoLayout);

            ELabel lastUpdatedTimeLbl = new ELabel(AppContext.getMessage(DayI18nEnum.LAST_UPDATED_ON, AppContext
                    .formatPrettyTime((task.getLastupdatedtime())))).withStyleName(UIConstants.META_INFO);
            metaInfoLayout.addComponent(lastUpdatedTimeLbl);

            A assigneeLink = new A(ProjectLinkGenerator.generateProjectMemberFullLink(AppContext.getSiteUrl(),
                    CurrentProjectVariables.getProjectId(), task.getAssignuser()));
            assigneeLink.appendText(StringUtils.trim(task.getAssignUserFullName(), 30, true));
            Div assigneeDiv = new Div().appendText(AppContext.getMessage(GenericI18Enum.FORM_ASSIGNEE))
                    .appendChild(DivLessFormatter.EMPTY_SPACE(), new Img("", StorageFactory
                                    .getAvatarPath(task.getAssignUserAvatarId(), 16)), DivLessFormatter.EMPTY_SPACE(),
                            assigneeLink);

            ELabel assigneeLbl = new ELabel(assigneeDiv.write(), ContentMode.HTML).withStyleName(UIConstants.META_INFO)
                    .withWidthUndefined();
            metaInfoLayout.addComponent(assigneeLbl);

            ELabel statusLbl = new ELabel(AppContext.getMessage(GenericI18Enum.FORM_STATUS) + ": " + AppContext.getMessage
                    (OptionI18nEnum.BugStatus.class, task.getStatus()), ContentMode.HTML).withStyleName(UIConstants.META_INFO);
            metaInfoLayout.addComponent(statusLbl);

            return rowLayout;
        }
    }
}

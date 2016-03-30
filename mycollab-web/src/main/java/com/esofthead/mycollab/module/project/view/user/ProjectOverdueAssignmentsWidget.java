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

import com.esofthead.mycollab.core.arguments.DateSearchField;
import com.esofthead.mycollab.core.arguments.SearchField;
import com.esofthead.mycollab.core.arguments.SetSearchField;
import com.esofthead.mycollab.core.arguments.StringSearchField;
import com.esofthead.mycollab.core.utils.DateTimeUtils;
import com.esofthead.mycollab.module.project.CurrentProjectVariables;
import com.esofthead.mycollab.module.project.domain.ProjectGenericTask;
import com.esofthead.mycollab.module.project.domain.criteria.ProjectGenericTaskSearchCriteria;
import com.esofthead.mycollab.module.project.i18n.ProjectCommonI18nEnum;
import com.esofthead.mycollab.module.project.service.ProjectGenericTaskService;
import com.esofthead.mycollab.module.project.ui.components.GenericTaskRowDisplayHandler;
import com.esofthead.mycollab.spring.ApplicationContextUtil;
import com.esofthead.mycollab.vaadin.AppContext;
import com.esofthead.mycollab.vaadin.web.ui.DefaultBeanPagedList;
import com.esofthead.mycollab.vaadin.web.ui.Depot;
import com.vaadin.data.Property;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.CssLayout;

/**
 * @author MyCollab Ltd.
 * @since 4.1
 */
public class ProjectOverdueAssignmentsWidget extends Depot {
    private static final long serialVersionUID = 1L;

    private ProjectGenericTaskSearchCriteria searchCriteria;

    private DefaultBeanPagedList<ProjectGenericTaskService, ProjectGenericTaskSearchCriteria, ProjectGenericTask> taskList;

    public ProjectOverdueAssignmentsWidget() {
        super(AppContext.getMessage(ProjectCommonI18nEnum.WIDGET_OVERDUE_ASSIGNMENTS_TITLE, 0), new CssLayout());
        this.setWidth("100%");

        final CheckBox myItemsSelection = new CheckBox("My Items");
        myItemsSelection.addValueChangeListener(new Property.ValueChangeListener() {
            @Override
            public void valueChange(Property.ValueChangeEvent valueChangeEvent) {
                boolean isMyItemsOption = myItemsSelection.getValue();
                if (isMyItemsOption) {
                    searchCriteria.setAssignUser(StringSearchField.and(AppContext.getUsername()));
                } else {
                    searchCriteria.setAssignUser(null);
                }
                updateSearchResult();
            }
        });

        taskList = new DefaultBeanPagedList(ApplicationContextUtil.getSpringBean(ProjectGenericTaskService.class),
                new GenericTaskRowDisplayHandler(), 10) {
            @Override
            protected String stringWhenEmptyList() {
                return "No overdue assignment";
            }
        };
        this.addHeaderElement(myItemsSelection);
        bodyContent.addComponent(taskList);
    }

    public void showOpenAssignments() {
        searchCriteria = new ProjectGenericTaskSearchCriteria();
        searchCriteria.setIsOpenned(new SearchField());
        searchCriteria.setProjectIds(new SetSearchField<>(CurrentProjectVariables.getProjectId()));
        searchCriteria.setDueDate(new DateSearchField(DateTimeUtils.getCurrentDateWithoutMS()));
        updateSearchResult();
    }

    private void updateSearchResult() {
        taskList.setSearchCriteria(searchCriteria);
        this.setTitle(AppContext.getMessage(ProjectCommonI18nEnum.WIDGET_OVERDUE_ASSIGNMENTS_TITLE, taskList.getTotalCount()));
    }
}
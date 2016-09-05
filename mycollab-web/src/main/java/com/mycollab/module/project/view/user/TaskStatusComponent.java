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
package com.mycollab.module.project.view.user;

import com.mycollab.common.i18n.GenericI18Enum;
import com.mycollab.core.utils.DateTimeUtils;
import com.mycollab.db.arguments.DateSearchField;
import com.mycollab.db.arguments.SearchField;
import com.mycollab.db.arguments.SetSearchField;
import com.mycollab.db.arguments.StringSearchField;
import com.mycollab.module.project.domain.ProjectGenericTask;
import com.mycollab.module.project.domain.criteria.ProjectGenericTaskSearchCriteria;
import com.mycollab.module.project.i18n.ProjectCommonI18nEnum;
import com.mycollab.module.project.i18n.ProjectI18nEnum;
import com.mycollab.module.project.service.ProjectGenericTaskService;
import com.mycollab.spring.AppContextUtil;
import com.mycollab.vaadin.AppContext;
import com.mycollab.vaadin.web.ui.DefaultBeanPagedList;
import com.mycollab.vaadin.web.ui.Depot;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.CssLayout;

import java.util.List;

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
public class TaskStatusComponent extends Depot {
    private static final long serialVersionUID = 1L;

    private TaskStatusPagedList taskComponents;
    private ProjectGenericTaskSearchCriteria searchCriteria;

    public TaskStatusComponent() {
        super(AppContext.getMessage(ProjectCommonI18nEnum.OPT_OVERDUE_ASSIGNMENTS_VALUE, 0), new CssLayout());

        final CheckBox myItemsOnly = new CheckBox(AppContext.getMessage(GenericI18Enum.OPT_MY_ITEMS));
        myItemsOnly.addValueChangeListener(valueChangeEvent -> {
            if (searchCriteria != null) {
                boolean selectMyItemsOnly = myItemsOnly.getValue();
                if (selectMyItemsOnly) {
                    searchCriteria.setAssignUser(StringSearchField.and(AppContext.getUsername()));
                } else {
                    searchCriteria.setAssignUser(null);
                }
                taskComponents.setSearchCriteria(searchCriteria);
            }
        });

        this.addHeaderElement(myItemsOnly);

        taskComponents = new TaskStatusPagedList();
        bodyContent.addComponent(taskComponents);
    }

    public void showProjectTasksByStatus(List<Integer> prjKeys) {
        searchCriteria = new ProjectGenericTaskSearchCriteria();
        searchCriteria.setProjectIds(new SetSearchField<>(prjKeys.toArray(new Integer[prjKeys.size()])));
        searchCriteria.setIsOpenned(new SearchField());
        searchCriteria.setDueDate(new DateSearchField(DateTimeUtils.getCurrentDateWithoutMS()));
        updateSearchResult();
    }

    private void updateSearchResult() {
        taskComponents.setSearchCriteria(searchCriteria);
        setTitle(AppContext.getMessage(ProjectCommonI18nEnum.OPT_OVERDUE_ASSIGNMENTS_VALUE, taskComponents.getTotalCount()));
    }

    private static class TaskStatusPagedList extends DefaultBeanPagedList<ProjectGenericTaskService,
            ProjectGenericTaskSearchCriteria, ProjectGenericTask> {

        TaskStatusPagedList() {
            super(AppContextUtil.getSpringBean(ProjectGenericTaskService.class), new GenericTaskRowDisplayHandler(), 10);
        }

        @Override
        protected String stringWhenEmptyList() {
            return AppContext.getMessage(ProjectI18nEnum.OPT_NO_OVERDUE_ASSIGNMENT);
        }
    }
}
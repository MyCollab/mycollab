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
import com.mycollab.db.arguments.DateSearchField;
import com.mycollab.db.arguments.SearchField;
import com.mycollab.db.arguments.SetSearchField;
import com.mycollab.db.arguments.StringSearchField;
import com.mycollab.core.utils.DateTimeUtils;
import com.mycollab.module.project.CurrentProjectVariables;
import com.mycollab.module.project.domain.ProjectTicket;
import com.mycollab.module.project.domain.criteria.ProjectTicketSearchCriteria;
import com.mycollab.module.project.i18n.ProjectCommonI18nEnum;
import com.mycollab.module.project.i18n.ProjectI18nEnum;
import com.mycollab.module.project.service.ProjectTicketService;
import com.mycollab.module.project.ui.components.GenericTaskRowDisplayHandler;
import com.mycollab.spring.AppContextUtil;
import com.mycollab.vaadin.UserUIContext;
import com.mycollab.vaadin.web.ui.DefaultBeanPagedList;
import com.mycollab.vaadin.web.ui.Depot;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.CssLayout;

/**
 * @author MyCollab Ltd.
 * @since 4.1
 */
public class ProjectOverdueTicketsWidget extends Depot {
    private static final long serialVersionUID = 1L;

    private ProjectTicketSearchCriteria searchCriteria;

    private DefaultBeanPagedList<ProjectTicketService, ProjectTicketSearchCriteria, ProjectTicket> taskList;

    public ProjectOverdueTicketsWidget() {
        super(UserUIContext.getMessage(ProjectCommonI18nEnum.OPT_OVERDUE_ASSIGNMENTS_VALUE, 0), new CssLayout());
        this.setWidth("100%");

        final CheckBox myItemsSelection = new CheckBox(UserUIContext.getMessage(GenericI18Enum.OPT_MY_ITEMS));
        myItemsSelection.addValueChangeListener(valueChangeEvent -> {
            boolean isMyItemsOption = myItemsSelection.getValue();
            if (isMyItemsOption) {
                searchCriteria.setAssignUser(StringSearchField.and(UserUIContext.getUsername()));
            } else {
                searchCriteria.setAssignUser(null);
            }
            updateSearchResult();
        });

        taskList = new DefaultBeanPagedList(AppContextUtil.getSpringBean(ProjectTicketService.class),
                new GenericTaskRowDisplayHandler(), 10) {
            @Override
            protected String stringWhenEmptyList() {
                return UserUIContext.getMessage(ProjectI18nEnum.OPT_NO_OVERDUE_TICKET);
            }
        };
        this.addHeaderElement(myItemsSelection);
        bodyContent.addComponent(taskList);
    }

    public void showOpenTickets() {
        searchCriteria = new ProjectTicketSearchCriteria();
        searchCriteria.setIsOpenned(new SearchField());
        searchCriteria.setProjectIds(new SetSearchField<>(CurrentProjectVariables.getProjectId()));
        searchCriteria.setDueDate(new DateSearchField(DateTimeUtils.getCurrentDateWithoutMS()));
        updateSearchResult();
    }

    private void updateSearchResult() {
        taskList.setSearchCriteria(searchCriteria);
        this.setTitle(UserUIContext.getMessage(ProjectCommonI18nEnum.OPT_OVERDUE_ASSIGNMENTS_VALUE, taskList.getTotalCount()));
    }
}
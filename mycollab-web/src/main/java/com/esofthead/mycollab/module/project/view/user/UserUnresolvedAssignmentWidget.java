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

import com.esofthead.mycollab.core.arguments.RangeDateSearchField;
import com.esofthead.mycollab.core.arguments.SearchField;
import com.esofthead.mycollab.core.arguments.SetSearchField;
import com.esofthead.mycollab.core.arguments.StringSearchField;
import com.esofthead.mycollab.core.utils.DateTimeUtils;
import com.esofthead.mycollab.module.project.domain.ProjectGenericTask;
import com.esofthead.mycollab.module.project.domain.criteria.ProjectGenericTaskSearchCriteria;
import com.esofthead.mycollab.module.project.service.ProjectGenericTaskService;
import com.esofthead.mycollab.module.project.view.UserDashboardView;
import com.esofthead.mycollab.spring.ApplicationContextUtil;
import com.esofthead.mycollab.vaadin.AppContext;
import com.esofthead.mycollab.vaadin.web.ui.DefaultBeanPagedList;
import com.esofthead.mycollab.vaadin.web.ui.Depot;
import com.esofthead.mycollab.vaadin.ui.UIUtils;
import com.vaadin.data.Property;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.CssLayout;
import org.joda.time.LocalDate;

import java.util.Date;

/**
 * @author MyCollab Ltd
 * @since 5.2.4
 */
public class UserUnresolvedAssignmentWidget extends Depot {
    private ProjectGenericTaskSearchCriteria searchCriteria;
    private DefaultBeanPagedList<ProjectGenericTaskService, ProjectGenericTaskSearchCriteria, ProjectGenericTask> taskList;
    private String title = "";

    public UserUnresolvedAssignmentWidget(String title) {
        super(title, new CssLayout());
        this.setWidth("100%");
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
        taskList = new DefaultBeanPagedList(ApplicationContextUtil.getSpringBean(ProjectGenericTaskService.class),
                new GenericTaskRowDisplayHandler(), 10) {
            @Override
            protected String stringWhenEmptyList() {
                return "No assignment";
            }
        };
        this.addHeaderElement(myItemsSelection);
        this.bodyContent.addComponent(taskList);
    }

    public void displayUnresolvedAssignmentsThisWeek() {
        title = "Unresolved assignments in this week (%d)";
        searchCriteria = new ProjectGenericTaskSearchCriteria();
        searchCriteria.setIsOpenned(new SearchField());
        UserDashboardView userDashboardView = UIUtils.getRoot(this, UserDashboardView.class);
        searchCriteria.setProjectIds(new SetSearchField<>(userDashboardView.getInvoledProjKeys()));
        LocalDate now = new LocalDate();
        Date[] bounceDateofWeek = DateTimeUtils.getBounceDateofWeek(now.toDate());
        RangeDateSearchField range = new RangeDateSearchField(bounceDateofWeek[0], bounceDateofWeek[1]);
        searchCriteria.setDateInRange(range);
        updateSearchResult();
    }

    public void displayUnresolvedAssignmentsNextWeek() {
        title = "Unresolved assignments in next week (%d)";
        searchCriteria = new ProjectGenericTaskSearchCriteria();
        UserDashboardView userDashboardView = UIUtils.getRoot(this, UserDashboardView.class);
        searchCriteria.setIsOpenned(new SearchField());
        searchCriteria.setProjectIds(new SetSearchField<>(userDashboardView.getInvoledProjKeys()));
        LocalDate now = new LocalDate();
        now = now.plusDays(7);
        Date[] bounceDateofWeek = DateTimeUtils.getBounceDateofWeek(now.toDate());
        RangeDateSearchField range = new RangeDateSearchField(bounceDateofWeek[0], bounceDateofWeek[1]);
        searchCriteria.setDateInRange(range);
        updateSearchResult();
    }

    private void updateSearchResult() {
        taskList.setSearchCriteria(searchCriteria);
        this.setTitle(String.format(title, taskList.getTotalCount()));
    }
}

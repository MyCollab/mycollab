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
package com.mycollab.module.project.view.ticket;

import com.mycollab.common.i18n.OptionI18nEnum.StatusI18nEnum;
import com.mycollab.db.arguments.SearchField;
import com.mycollab.db.query.*;
import com.mycollab.module.project.ProjectTypeConstants;
import com.mycollab.module.project.domain.criteria.ProjectTicketSearchCriteria;
import com.mycollab.module.project.i18n.OptionI18nEnum.BugStatus;
import com.mycollab.module.project.i18n.TicketI18nEnum;
import com.mycollab.module.project.query.CurrentProjectIdInjector;
import com.mycollab.vaadin.UserUIContext;
import com.mycollab.vaadin.web.ui.SavedFilterComboBox;
import org.joda.time.LocalDate;

import java.util.Arrays;
import java.util.Collections;

/**
 * @author MyCollab Ltd
 * @since 5.4.3
 */
public class TicketSavedFilterComboBox extends SavedFilterComboBox {
    public static final String ALL_TICKETS = "ALL_TICKETS";
    public static final String OPEN_TICKETS = "OPEN_TICKETS";
    public static final String OVERDUE_TICKETS = "OVERDUE_TICKETS";
    public static final String CLOSED_TICKETS = "CLOSED_TICKETS";
    public static final String MY_TICKETS = "MY_TICKETS";
    public static final String TICKETS_CREATED_BY_ME = "TICKETS_CREATED_BY_ME";
    public static final String NEW_TICKETS_THIS_WEEK = "NEW_TICKETS_THIS_WEEK";
    public static final String UPDATE_TICKETS_THIS_WEEK = "UPDATE_TICKETS_THIS_WEEK";
    public static final String NEW_TICKETS_LAST_WEEK = "NEW_TICKETS_LAST_WEEK";
    public static final String UPDATE_TICKETS_LAST_WEEK = "UPDATE_TICKETS_LAST_WEEK";

    public TicketSavedFilterComboBox() {
        super(ProjectTypeConstants.TICKET);

        SearchQueryInfo allTasksQuery = new SearchQueryInfo(ALL_TICKETS, UserUIContext.getMessage(TicketI18nEnum.VAL_ALL_TICKETS),
                SearchFieldInfo.inCollection(ProjectTicketSearchCriteria.p_projectIds, new CurrentProjectIdInjector()));

        SearchQueryInfo allOpenTaskQuery = new SearchQueryInfo(OPEN_TICKETS, UserUIContext.getMessage(TicketI18nEnum.VAL_ALL_OPEN_TICKETS),
                SearchFieldInfo.notInCollection(ProjectTicketSearchCriteria.p_status, new VariableInjector() {
                    @Override
                    public Object eval() {
                        return Arrays.asList(StatusI18nEnum.Closed, BugStatus.Verified);
                    }

                    @Override
                    public Class getType() {
                        return String.class;
                    }

                    @Override
                    public boolean isArray() {
                        return false;
                    }

                    @Override
                    public boolean isCollection() {
                        return true;
                    }
                }));

        SearchQueryInfo overdueTaskQuery = new SearchQueryInfo(OVERDUE_TICKETS, UserUIContext.getMessage(TicketI18nEnum.VAL_OVERDUE_TICKETS),
                new SearchFieldInfo(SearchField.AND, ProjectTicketSearchCriteria.p_dueDate, DateParam.BEFORE, new LazyValueInjector() {
                    @Override
                    protected Object doEval() {
                        return new LocalDate().toDate();
                    }
                }), SearchFieldInfo.notInCollection(ProjectTicketSearchCriteria.p_status, new VariableInjector() {
            @Override
            public Object eval() {
                return Arrays.asList(StatusI18nEnum.Closed, BugStatus.Verified);
            }

            @Override
            public Class getType() {
                return String.class;
            }

            @Override
            public boolean isArray() {
                return false;
            }

            @Override
            public boolean isCollection() {
                return true;
            }
        }));

        SearchQueryInfo allClosedTaskQuery = new SearchQueryInfo(CLOSED_TICKETS, UserUIContext.getMessage(TicketI18nEnum.VAL_ALL_CLOSED_TICKETS),
                SearchFieldInfo.inCollection(ProjectTicketSearchCriteria.p_status, new VariableInjector() {
                    @Override
                    public Object eval() {
                        return Arrays.asList(StatusI18nEnum.Closed, BugStatus.Verified);
                    }

                    @Override
                    public Class getType() {
                        return String.class;
                    }

                    @Override
                    public boolean isArray() {
                        return false;
                    }

                    @Override
                    public boolean isCollection() {
                        return true;
                    }
                }));

        SearchQueryInfo myTasksQuery = new SearchQueryInfo(MY_TICKETS, UserUIContext.getMessage(TicketI18nEnum.VAL_MY_TICKETS),
                SearchFieldInfo.inCollection(ProjectTicketSearchCriteria.p_assignee,
                        ConstantValueInjector.valueOf(Collections.singletonList(UserUIContext.getUsername()))));

        SearchQueryInfo tasksCreatedByMeQuery = new SearchQueryInfo(TICKETS_CREATED_BY_ME, UserUIContext.getMessage(TicketI18nEnum.VAL_TICKETS_CREATED_BY_ME),
                SearchFieldInfo.inCollection(ProjectTicketSearchCriteria.p_createdUser, ConstantValueInjector.valueOf(Collections.singletonList(UserUIContext.getUsername()))));

        SearchQueryInfo newTasksThisWeekQuery = new SearchQueryInfo(NEW_TICKETS_THIS_WEEK, UserUIContext.getMessage(TicketI18nEnum.VAL_NEW_THIS_WEEK),
                SearchFieldInfo.inDateRange(ProjectTicketSearchCriteria.p_createtime, VariableInjector.THIS_WEEK));

        SearchQueryInfo updateTasksThisWeekQuery = new SearchQueryInfo(UPDATE_TICKETS_THIS_WEEK, UserUIContext.getMessage(TicketI18nEnum.VAL_UPDATE_THIS_WEEK),
                SearchFieldInfo.inDateRange(ProjectTicketSearchCriteria.p_lastupdatedtime, VariableInjector.THIS_WEEK));

        SearchQueryInfo newTasksLastWeekQuery = new SearchQueryInfo(NEW_TICKETS_LAST_WEEK, UserUIContext.getMessage(TicketI18nEnum.VAL_NEW_LAST_WEEK),
                SearchFieldInfo.inDateRange(ProjectTicketSearchCriteria.p_createtime, VariableInjector.LAST_WEEK));

        SearchQueryInfo updateTasksLastWeekQuery = new SearchQueryInfo(UPDATE_TICKETS_LAST_WEEK, UserUIContext.getMessage(TicketI18nEnum.VAL_UPDATE_LAST_WEEK),
                SearchFieldInfo.inDateRange(ProjectTicketSearchCriteria.p_lastupdatedtime, VariableInjector.LAST_WEEK));

        this.addSharedSearchQueryInfo(allTasksQuery);
        this.addSharedSearchQueryInfo(allOpenTaskQuery);
        this.addSharedSearchQueryInfo(overdueTaskQuery);
        this.addSharedSearchQueryInfo(allClosedTaskQuery);
        this.addSharedSearchQueryInfo(myTasksQuery);
        this.addSharedSearchQueryInfo(tasksCreatedByMeQuery);
        this.addSharedSearchQueryInfo(newTasksThisWeekQuery);
        this.addSharedSearchQueryInfo(updateTasksThisWeekQuery);
        this.addSharedSearchQueryInfo(newTasksLastWeekQuery);
        this.addSharedSearchQueryInfo(updateTasksLastWeekQuery);
    }

    public void setTotalCountNumber(Integer countNumber) {
        componentsText.setReadOnly(false);
        componentsText.setValue(selectedQueryName + " (" + countNumber + ")");
        componentsText.setReadOnly(true);
    }
}

package com.mycollab.module.project.view.ticket;

import com.mycollab.module.project.ProjectTypeConstants;
import com.mycollab.vaadin.web.ui.SavedFilterComboBox;

import static com.mycollab.module.project.query.TicketQueryInfo.*;

/**
 * @author MyCollab Ltd
 * @since 5.4.3
 */
public class TicketSavedFilterComboBox extends SavedFilterComboBox {

    public TicketSavedFilterComboBox() {
        super(ProjectTypeConstants.TICKET);

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
        componentsText.setValue(String.format("%s (%d)", selectedQueryName, countNumber));
        componentsText.setReadOnly(true);
    }
}

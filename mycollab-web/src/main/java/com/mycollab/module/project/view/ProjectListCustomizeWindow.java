package com.mycollab.module.project.view;

import com.mycollab.common.TableViewField;
import com.mycollab.module.project.ProjectTypeConstants;
import com.mycollab.module.project.fielddef.ProjectTableFieldDef;
import com.mycollab.vaadin.web.ui.table.AbstractPagedBeanTable;
import com.mycollab.vaadin.web.ui.table.CustomizedTableWindow;

import java.util.Arrays;
import java.util.Collection;

/**
 * @author MyCollab Ltd
 * @since 5.2.12
 */
public class ProjectListCustomizeWindow extends CustomizedTableWindow {
    private static final long serialVersionUID = 1L;

    public ProjectListCustomizeWindow(AbstractPagedBeanTable table) {
        super(ProjectTypeConstants.PROJECT, table);
    }

    @Override
    protected Collection<TableViewField> getAvailableColumns() {
        return Arrays.asList(ProjectTableFieldDef.projectName, ProjectTableFieldDef.client,
                ProjectTableFieldDef.lead, ProjectTableFieldDef.startDate, ProjectTableFieldDef.status,
                ProjectTableFieldDef.homePage, ProjectTableFieldDef.budget, ProjectTableFieldDef.endDate,
                ProjectTableFieldDef.createdDate, ProjectTableFieldDef.rate, ProjectTableFieldDef.overtimeRate,
                ProjectTableFieldDef.budget, ProjectTableFieldDef.actualBudget);
    }
}

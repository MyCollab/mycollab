package com.mycollab.module.project.view.bug;

import com.mycollab.common.TableViewField;
import com.mycollab.module.project.fielddef.BugTableFieldDef;
import com.mycollab.vaadin.web.ui.table.AbstractPagedBeanTable;
import com.mycollab.vaadin.web.ui.table.CustomizedTableWindow;

import java.util.Arrays;
import java.util.Collection;

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
public class BugListCustomizeWindow extends CustomizedTableWindow {
    private static final long serialVersionUID = 1L;

    public BugListCustomizeWindow(String viewId, AbstractPagedBeanTable table) {
        super(viewId, table);
    }

    @Override
    protected Collection<TableViewField> getAvailableColumns() {
        return Arrays.asList(BugTableFieldDef.assignUser,
                BugTableFieldDef.createdTime, BugTableFieldDef.description,
                BugTableFieldDef.dueDate, BugTableFieldDef.environment,
                BugTableFieldDef.logBy, BugTableFieldDef.priority,
                BugTableFieldDef.resolution, BugTableFieldDef.status,
                BugTableFieldDef.summary);
    }

}

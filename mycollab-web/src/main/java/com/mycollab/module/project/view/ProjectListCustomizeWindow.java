/**
 * Copyright © MyCollab
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.mycollab.module.project.view;

import com.mycollab.common.GridFieldMeta;
import com.mycollab.module.project.ProjectTypeConstants;
import com.mycollab.module.project.fielddef.ProjectTableFieldDef;
import com.mycollab.vaadin.web.ui.table.AbstractPagedGrid;
import com.mycollab.vaadin.web.ui.table.CustomizedGridWindow;

import java.util.Arrays;
import java.util.Collection;

/**
 * @author MyCollab Ltd
 * @since 5.2.12
 */
public class ProjectListCustomizeWindow extends CustomizedGridWindow {
    private static final long serialVersionUID = 1L;

    public ProjectListCustomizeWindow(AbstractPagedGrid table) {
        super(ProjectTypeConstants.PROJECT, table);
    }

    @Override
    protected Collection<GridFieldMeta> getAvailableColumns() {
        return Arrays.asList(ProjectTableFieldDef.projectName, ProjectTableFieldDef.client,
                ProjectTableFieldDef.lead, ProjectTableFieldDef.startDate, ProjectTableFieldDef.status,
                ProjectTableFieldDef.homePage, ProjectTableFieldDef.budget, ProjectTableFieldDef.endDate,
                ProjectTableFieldDef.createdDate, ProjectTableFieldDef.rate, ProjectTableFieldDef.overtimeRate,
                ProjectTableFieldDef.budget, ProjectTableFieldDef.actualBudget);
    }
}

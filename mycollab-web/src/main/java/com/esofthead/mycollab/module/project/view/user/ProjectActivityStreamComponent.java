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

import com.esofthead.mycollab.common.ModuleNameConstants;
import com.esofthead.mycollab.common.domain.criteria.ActivityStreamSearchCriteria;
import com.esofthead.mycollab.core.arguments.NumberSearchField;
import com.esofthead.mycollab.core.arguments.SearchField;
import com.esofthead.mycollab.core.arguments.SetSearchField;
import com.esofthead.mycollab.module.project.CurrentProjectVariables;
import com.esofthead.mycollab.vaadin.AppContext;
import com.vaadin.ui.CssLayout;

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
public class ProjectActivityStreamComponent extends CssLayout {
    private static final long serialVersionUID = 1L;

    public ProjectActivityStreamComponent() {
        this.setStyleName("project-activity-list");
    }

    public void showProjectFeeds() {
        this.removeAllComponents();
        ProjectActivityStreamPagedList activityStreamList = new ProjectActivityStreamPagedList();
        this.addComponent(activityStreamList);
        final ActivityStreamSearchCriteria searchCriteria = new ActivityStreamSearchCriteria();
        searchCriteria.setModuleSet(new SetSearchField<>(SearchField.AND,
                new String[]{ModuleNameConstants.PRJ}));
        searchCriteria.setSaccountid(new NumberSearchField(AppContext
                .getAccountId()));

        searchCriteria.setExtraTypeIds(new SetSearchField<>(
                CurrentProjectVariables.getProjectId()));
        activityStreamList.setSearchCriteria(searchCriteria);
    }
}

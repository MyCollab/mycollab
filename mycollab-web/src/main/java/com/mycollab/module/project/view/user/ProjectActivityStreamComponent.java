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
package com.mycollab.module.project.view.user;

import com.mycollab.common.ModuleNameConstants;
import com.mycollab.common.domain.criteria.ActivityStreamSearchCriteria;
import com.mycollab.db.arguments.NumberSearchField;
import com.mycollab.db.arguments.SetSearchField;
import com.mycollab.module.project.CurrentProjectVariables;
import com.mycollab.vaadin.AppUI;
import fi.jasoft.dragdroplayouts.DDCssLayout;

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
public class ProjectActivityStreamComponent extends DDCssLayout {
    private static final long serialVersionUID = 1L;

    public void showProjectFeeds() {
        this.removeAllComponents();
        ProjectActivityStreamPagedList activityStreamList = new ProjectActivityStreamPagedList();
        this.addComponent(activityStreamList);
        ActivityStreamSearchCriteria searchCriteria = new ActivityStreamSearchCriteria();
        searchCriteria.setModuleSet(new SetSearchField<>(ModuleNameConstants.PRJ));
        searchCriteria.setSaccountid(new NumberSearchField(AppUI.getAccountId()));
        searchCriteria.setExtraTypeIds(new SetSearchField<>(CurrentProjectVariables.getProjectId()));
        activityStreamList.setSearchCriteria(searchCriteria);
    }
}

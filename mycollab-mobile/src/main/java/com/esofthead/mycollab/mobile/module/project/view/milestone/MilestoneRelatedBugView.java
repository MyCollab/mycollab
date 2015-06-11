/**
 * This file is part of mycollab-mobile.
 *
 * mycollab-mobile is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * mycollab-mobile is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with mycollab-mobile.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.esofthead.mycollab.mobile.module.project.view.milestone;

import com.esofthead.mycollab.core.arguments.NumberSearchField;
import com.esofthead.mycollab.core.arguments.SearchField;
import com.esofthead.mycollab.core.arguments.SetSearchField;
import com.esofthead.mycollab.mobile.module.project.CurrentProjectVariables;
import com.esofthead.mycollab.mobile.module.project.view.bug.BugListDisplay;
import com.esofthead.mycollab.mobile.ui.AbstractRelatedListView;
import com.esofthead.mycollab.module.project.domain.SimpleMilestone;
import com.esofthead.mycollab.module.project.i18n.BugI18nEnum;
import com.esofthead.mycollab.module.tracker.domain.SimpleBug;
import com.esofthead.mycollab.module.tracker.domain.criteria.BugSearchCriteria;
import com.esofthead.mycollab.vaadin.AppContext;
import com.vaadin.ui.Component;

/**
 * @author MyCollab Ltd.
 * @since 4.5.2
 */
public class MilestoneRelatedBugView extends AbstractRelatedListView<SimpleBug, BugSearchCriteria> {
    private static final long serialVersionUID = -7918478404907275472L;

    private SimpleMilestone milestone;

    public MilestoneRelatedBugView() {
        this.setCaption(AppContext.getMessage(BugI18nEnum.M_VIEW_RELATED_TITLE));
        itemList = new BugListDisplay();
        this.setContent(itemList);
    }

    @Override
    public void refresh() {
        loadBugs();
    }

    public void displayBugs(final SimpleMilestone targetMilestone) {
        this.milestone = targetMilestone;
        loadBugs();
    }

    @Override
    protected Component createRightComponent() {
        return null;
    }

    private void loadBugs() {
        BugSearchCriteria criteria = new BugSearchCriteria();

        criteria.setProjectId(new NumberSearchField(SearchField.AND,
                CurrentProjectVariables.getProjectId()));
        criteria.setMilestoneIds(new SetSearchField<>(this.milestone
                .getId()));
        setSearchCriteria(criteria);
    }

}

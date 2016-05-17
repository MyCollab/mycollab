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
package com.esofthead.mycollab.module.project.view.milestone;

import com.esofthead.mycollab.core.arguments.BasicSearchRequest;
import com.esofthead.mycollab.core.arguments.SetSearchField;
import com.esofthead.mycollab.module.project.CurrentProjectVariables;
import com.esofthead.mycollab.module.project.domain.SimpleMilestone;
import com.esofthead.mycollab.module.project.domain.criteria.MilestoneSearchCriteria;
import com.esofthead.mycollab.module.project.service.MilestoneService;
import com.esofthead.mycollab.spring.AppContextUtil;
import com.esofthead.mycollab.vaadin.web.ui.IntegerKeyListSelect;
import com.vaadin.ui.ListSelect;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

/**
 * @author MyCollab Ltd
 * @since 5.1.1
 */
public class MilestoneListSelect extends IntegerKeyListSelect {

    public MilestoneListSelect() {
        this.setItemCaptionMode(ItemCaptionMode.EXPLICIT);
        this.setNullSelectionAllowed(false);
        this.setMultiSelect(true);
        this.setRows(4);

        MilestoneService milestoneService = AppContextUtil.getSpringBean(MilestoneService.class);
        MilestoneSearchCriteria criteria = new MilestoneSearchCriteria();
        criteria.setProjectIds(new SetSearchField<>(CurrentProjectVariables.getProjectId()));
        List<SimpleMilestone> milestones = milestoneService.findPagableListByCriteria(new BasicSearchRequest<>(criteria));
        for (SimpleMilestone milestone : milestones) {
            this.addItem(milestone.getId());
            this.setItemCaption(milestone.getId(), milestone.getName());
        }
    }
}

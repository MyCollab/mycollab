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
import com.esofthead.mycollab.core.arguments.SearchRequest;
import com.esofthead.mycollab.mobile.ui.ValueComboBox;
import com.esofthead.mycollab.module.project.CurrentProjectVariables;
import com.esofthead.mycollab.module.project.domain.SimpleMilestone;
import com.esofthead.mycollab.module.project.domain.criteria.MilestoneSearchCriteria;
import com.esofthead.mycollab.module.project.service.MilestoneService;
import com.esofthead.mycollab.spring.ApplicationContextUtil;
import com.vaadin.data.util.BeanContainer;

import java.util.List;

/**
 *
 * @author MyCollab Ltd.
 * @since 1.0
 */
public class MilestoneComboBox extends ValueComboBox {

    private static final long serialVersionUID = 1L;

    @SuppressWarnings("unchecked")
    public MilestoneComboBox() {
        super();
        this.setItemCaptionMode(ItemCaptionMode.PROPERTY);

        MilestoneSearchCriteria criteria = new MilestoneSearchCriteria();
        criteria.setProjectId(new NumberSearchField(SearchField.AND, CurrentProjectVariables.getProjectId()));

        MilestoneService milestoneService = ApplicationContextUtil
                .getSpringBean(MilestoneService.class);
        List<SimpleMilestone> milestoneList = milestoneService.findPagableListByCriteria(new SearchRequest<>(
                criteria, 0, Integer.MAX_VALUE));

        BeanContainer<String, SimpleMilestone> beanItem = new BeanContainer<>(SimpleMilestone.class);
        beanItem.setBeanIdProperty("id");

        for (SimpleMilestone milestone : milestoneList) {
            beanItem.addBean(milestone);
        }

        this.setContainerDataSource(beanItem);
        this.setItemCaptionPropertyId("name");
    }
}

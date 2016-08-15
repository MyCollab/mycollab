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
package com.mycollab.mobile.module.project.view.milestone;

import com.mycollab.db.arguments.BasicSearchRequest;
import com.mycollab.db.arguments.SetSearchField;
import com.mycollab.mobile.ui.ValueComboBox;
import com.mycollab.module.project.CurrentProjectVariables;
import com.mycollab.module.project.domain.SimpleMilestone;
import com.mycollab.module.project.domain.criteria.MilestoneSearchCriteria;
import com.mycollab.module.project.service.MilestoneService;
import com.mycollab.spring.AppContextUtil;
import com.vaadin.data.util.BeanContainer;

import java.util.List;

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
public class MilestoneComboBox extends ValueComboBox {
    private static final long serialVersionUID = 1L;

    public MilestoneComboBox() {
        super();
        this.setItemCaptionMode(ItemCaptionMode.PROPERTY);

        MilestoneSearchCriteria criteria = new MilestoneSearchCriteria();
        criteria.setProjectIds(new SetSearchField<>(CurrentProjectVariables.getProjectId()));

        MilestoneService milestoneService = AppContextUtil.getSpringBean(MilestoneService.class);
        List<SimpleMilestone> milestones = milestoneService.findPageableListByCriteria(new BasicSearchRequest<>(criteria));

        BeanContainer<String, SimpleMilestone> beanItem = new BeanContainer<>(SimpleMilestone.class);
        beanItem.setBeanIdProperty("id");

        for (SimpleMilestone milestone : milestones) {
            beanItem.addBean(milestone);
        }

        this.setContainerDataSource(beanItem);
        this.setItemCaptionPropertyId("name");
    }
}

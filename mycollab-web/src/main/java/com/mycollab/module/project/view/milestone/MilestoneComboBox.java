/**
 * Copyright © MyCollab
 * <p>
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * <p>
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 * <p>
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.mycollab.module.project.view.milestone;

import com.mycollab.core.utils.StringUtils;
import com.mycollab.db.arguments.BasicSearchRequest;
import com.mycollab.db.arguments.SetSearchField;
import com.mycollab.module.project.CurrentProjectVariables;
import com.mycollab.module.project.domain.Milestone;
import com.mycollab.module.project.domain.SimpleMilestone;
import com.mycollab.module.project.domain.SimpleProject;
import com.mycollab.module.project.domain.criteria.MilestoneSearchCriteria;
import com.mycollab.module.project.i18n.OptionI18nEnum.MilestoneStatus;
import com.mycollab.module.project.service.MilestoneService;
import com.mycollab.module.project.ui.ProjectAssetsUtil;
import com.mycollab.spring.AppContextUtil;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.IconGenerator;
import com.vaadin.ui.ItemCaptionGenerator;

import java.io.Serializable;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
public class MilestoneComboBox extends ComboBox {

    public MilestoneComboBox() {
        MilestoneSearchCriteria criteria = new MilestoneSearchCriteria();
        SimpleProject project = CurrentProjectVariables.getProject();
        if (project != null) {
            criteria.setProjectIds(new SetSearchField<>(project.getId()));
            MilestoneService milestoneService = AppContextUtil.getSpringBean(MilestoneService.class);
            List<SimpleMilestone> milestones = (List<SimpleMilestone>) milestoneService.findPageableListByCriteria(new BasicSearchRequest<>(criteria));
            Collections.sort(milestones, new MilestoneComparator());

            this.setItems(milestones);
            this.setItemCaptionGenerator((ItemCaptionGenerator<SimpleMilestone>) milestone -> StringUtils.trim(milestone.getName(), 25, true));
            this.setItemIconGenerator((IconGenerator<SimpleMilestone>) milestone -> ProjectAssetsUtil.getPhaseIcon(milestone.getStatus()));
        }
    }

    private static class MilestoneComparator implements Comparator<Milestone>, Serializable {

        @Override
        public int compare(Milestone milestone1, Milestone milestone2) {
            if (MilestoneStatus.InProgress.toString().equals(milestone1.getStatus())) {
                return -1;
            } else if (MilestoneStatus.Future.toString().equals(milestone1.getStatus())) {
                return MilestoneStatus.InProgress.toString().equals(milestone2.getStatus()) ? 1 : -1;
            } else {
                return 1;
            }
        }
    }
}

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
package com.mycollab.module.project.view.ticket;

import com.mycollab.configuration.SiteConfiguration;
import com.mycollab.module.project.ProjectTypeConstants;
import com.mycollab.module.project.i18n.BugI18nEnum;
import com.mycollab.module.project.i18n.RiskI18nEnum;
import com.mycollab.module.project.i18n.TaskI18nEnum;
import com.mycollab.vaadin.UserUIContext;
import com.vaadin.ui.ItemCaptionGenerator;
import com.vaadin.ui.ListSelect;

/**
 * @author MyCollab Ltd
 * @since 5.4.3
 */
class TicketTypeListSelect extends ListSelect<String> {
    TicketTypeListSelect() {
        this.setRows(3);

        if (!SiteConfiguration.isCommunityEdition()) {
            this.setItems(ProjectTypeConstants.TASK, ProjectTypeConstants.BUG, ProjectTypeConstants.RISK);
        } else {
            this.setItems(ProjectTypeConstants.TASK, ProjectTypeConstants.BUG);
        }

        this.setItemCaptionGenerator((ItemCaptionGenerator<String>) item -> {
            if (ProjectTypeConstants.TASK.equals(item)) {
                return UserUIContext.getMessage(TaskI18nEnum.SINGLE);
            } else if (ProjectTypeConstants.BUG.equals(item)) {
                return UserUIContext.getMessage(BugI18nEnum.SINGLE);
            } else if (ProjectTypeConstants.RISK.equals(item)) {
                return UserUIContext.getMessage(RiskI18nEnum.SINGLE);
            } else {
                return "";
            }
        });
    }
}

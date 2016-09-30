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
package com.mycollab.module.project.view.ticket;

import com.mycollab.module.project.ProjectTypeConstants;
import com.mycollab.module.project.i18n.BugI18nEnum;
import com.mycollab.module.project.i18n.RiskI18nEnum;
import com.mycollab.module.project.i18n.TaskI18nEnum;
import com.mycollab.vaadin.UserUIContext;
import com.vaadin.ui.ListSelect;

/**
 * @author MyCollab Ltd
 * @since 5.4.3
 */
public class TicketTypeListSelect extends ListSelect {
    public TicketTypeListSelect() {
        this.setItemCaptionMode(ItemCaptionMode.EXPLICIT);
        this.setNullSelectionAllowed(false);
        this.setMultiSelect(true);
        this.setRows(4);

        this.addItem(ProjectTypeConstants.TASK);
        this.setItemCaption(ProjectTypeConstants.TASK, UserUIContext.getMessage(TaskI18nEnum.SINGLE));
        this.addItem(ProjectTypeConstants.BUG);
        this.setItemCaption(ProjectTypeConstants.BUG, UserUIContext.getMessage(BugI18nEnum.SINGLE));
        this.addItem(ProjectTypeConstants.RISK);
        this.setItemCaption(ProjectTypeConstants.RISK, UserUIContext.getMessage(RiskI18nEnum.SINGLE));
    }
}

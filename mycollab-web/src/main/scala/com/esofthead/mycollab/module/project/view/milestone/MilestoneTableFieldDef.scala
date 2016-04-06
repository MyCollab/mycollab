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
package com.esofthead.mycollab.module.project.view.milestone

import com.esofthead.mycollab.common.TableViewField
import com.esofthead.mycollab.common.i18n.GenericI18Enum
import com.esofthead.mycollab.module.project.domain.{Milestone, SimpleMilestone}
import com.esofthead.mycollab.module.project.i18n.MilestoneI18nEnum
import com.esofthead.mycollab.vaadin.web.ui.UIConstants

/**
  * @author MyCollab Ltd
  * @since 5.2.11
  */
object MilestoneTableFieldDef {
  val milestonename = new TableViewField(MilestoneI18nEnum.FORM_NAME_FIELD, Milestone.Field.name.name(), UIConstants.TABLE_X_LABEL_WIDTH)

  val status = new TableViewField(MilestoneI18nEnum.FORM_STATUS_FIELD, Milestone.Field.status.name(), UIConstants
    .TABLE_S_LABEL_WIDTH)

  val startdate = new TableViewField(MilestoneI18nEnum.FORM_START_DATE_FIELD, Milestone.Field.startdate.name(), UIConstants
    .TABLE_DATE_WIDTH)

  val enddate = new TableViewField(MilestoneI18nEnum.FORM_END_DATE_FIELD, Milestone.Field.enddate.name(), UIConstants
    .TABLE_DATE_WIDTH)

  val assignee = new TableViewField(GenericI18Enum.FORM_ASSIGNEE, SimpleMilestone.Field.ownerFullName.name(), UIConstants
    .TABLE_M_LABEL_WIDTH)
}

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
package com.esofthead.mycollab.module.project.view;

import com.esofthead.mycollab.common.TableViewField;
import com.esofthead.mycollab.common.i18n.FollowerI18nEnum;
import com.esofthead.mycollab.common.i18n.GenericI18Enum;
import com.esofthead.mycollab.vaadin.web.ui.UIConstants;

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
public class FollowingTicketFieldDef {
    public static final TableViewField summary = new TableViewField(FollowerI18nEnum.FORM_SUMMARY, "summary",
            UIConstants.TABLE_EX_LABEL_WIDTH);

    public static final TableViewField project = new TableViewField(FollowerI18nEnum.FORM_PROJECT_NAME, "projectName",
            (int)(UIConstants.TABLE_EX_LABEL_WIDTH * 1.5));

    public static final TableViewField assignee = new TableViewField(GenericI18Enum.FORM_ASSIGNEE, "assignUser",
            UIConstants.TABLE_X_LABEL_WIDTH);

    public static final TableViewField createdDate = new TableViewField(FollowerI18nEnum.OPT_FOLLOWER_CREATE_DATE,
            "monitorDate", UIConstants.TABLE_DATE_WIDTH);
}

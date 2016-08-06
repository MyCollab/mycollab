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
package com.mycollab.module.crm.view.activity;

import com.mycollab.common.TableViewField;
import com.mycollab.common.i18n.GenericI18Enum;
import com.mycollab.module.crm.domain.SimpleActivity;
import com.mycollab.module.crm.domain.criteria.ActivitySearchCriteria;
import com.mycollab.module.crm.i18n.ActivityI18nEnum;
import com.mycollab.module.crm.ui.components.RelatedListComp;
import com.mycollab.security.RolePermissionCollections;
import com.mycollab.vaadin.AppContext;
import com.mycollab.vaadin.web.ui.WebUIConstants;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.HorizontalLayout;
import org.vaadin.viritin.button.MButton;

import java.util.Arrays;

/**
 * @author MyCollab Ltd.
 * @since 2.0
 */
public class ActivityRelatedItemListComp extends RelatedListComp<SimpleActivity, ActivitySearchCriteria> {
    private static final long serialVersionUID = 1L;

    private final boolean allowCreateNew;

    public ActivityRelatedItemListComp(final boolean allowCreateNew) {
        this.allowCreateNew = allowCreateNew;
        initUI();
    }

    private void initUI() {
        if (allowCreateNew) {
            HorizontalLayout buttonLayout = new HorizontalLayout();
            buttonLayout.setSpacing(true);
            final MButton newTaskBtn = new MButton("New Task", clickEvent -> fireNewRelatedItem("task"))
                    .withIcon(FontAwesome.PLUS).withStyleName(WebUIConstants.BUTTON_ACTION)
                    .withVisible(AppContext.canWrite(RolePermissionCollections.CRM_TASK));
            buttonLayout.addComponent(newTaskBtn);

            final MButton newCallBtn = new MButton("New Call", clickEvent -> fireNewRelatedItem("call"))
                    .withIcon(FontAwesome.PLUS).withStyleName(WebUIConstants.BUTTON_ACTION)
                    .withVisible(AppContext.canWrite(RolePermissionCollections.CRM_CALL));
            buttonLayout.addComponent(newCallBtn);

            final MButton newMeetingBtn = new MButton("New Meeting", clickEvent -> fireNewRelatedItem("meeting"))
                    .withIcon(FontAwesome.PLUS).withStyleName(WebUIConstants.BUTTON_ACTION)
                    .withVisible(AppContext.canWrite(RolePermissionCollections.CRM_MEETING));
            buttonLayout.addComponent(newMeetingBtn);

            this.addComponent(buttonLayout);
            this.addStyleName("activity-realated-content");
        }

        tableItem = new ActivityTableDisplay(Arrays.asList(
                new TableViewField(ActivityI18nEnum.FORM_SUBJECT, "subject", WebUIConstants.TABLE_EX_LABEL_WIDTH),
                new TableViewField(GenericI18Enum.FORM_STATUS, "status", WebUIConstants.TABLE_S_LABEL_WIDTH),
                new TableViewField(GenericI18Enum.FORM_START_DATE, "startDate", WebUIConstants.TABLE_DATE_TIME_WIDTH),
                new TableViewField(GenericI18Enum.FORM_END_DATE, "endDate", WebUIConstants.TABLE_DATE_TIME_WIDTH)));

        this.addComponent(tableItem);
    }

    @Override
    public void refresh() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}

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
package com.esofthead.mycollab.mobile.module.project.view.bug;

import com.esofthead.mycollab.common.i18n.GenericI18Enum;
import com.esofthead.mycollab.mobile.ui.FormSectionBuilder;
import com.esofthead.mycollab.mobile.ui.grid.GridFormLayoutHelper;
import com.esofthead.mycollab.module.project.i18n.BugI18nEnum;
import com.esofthead.mycollab.vaadin.AppContext;
import com.esofthead.mycollab.vaadin.ui.IFormLayoutFactory;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.Field;
import com.vaadin.ui.VerticalLayout;

/**
 * @author MyCollab Ltd.
 * @since 4.5.2
 */
public class BugFormLayoutFactory implements IFormLayoutFactory {
    private static final long serialVersionUID = -9159483523170247666L;

    private GridFormLayoutHelper informationLayout;

    @Override
    public ComponentContainer getLayout() {
        final VerticalLayout layout = new VerticalLayout();
        layout.setMargin(false);
        layout.setWidth("100%");
        layout.addComponent(FormSectionBuilder.build(AppContext.getMessage(BugI18nEnum.M_FORM_READ_TITLE)));

        informationLayout = GridFormLayoutHelper.defaultFormLayoutHelper(1, 14);
        layout.addComponent(informationLayout.getLayout());
        layout.setComponentAlignment(informationLayout.getLayout(), Alignment.BOTTOM_CENTER);
        layout.setExpandRatio(informationLayout.getLayout(), 1.0f);
        return layout;
    }

    @Override
    public void attachField(Object propertyId, Field<?> field) {
        if (propertyId.equals("summary")) {
            informationLayout.addComponent(field, AppContext.getMessage(BugI18nEnum.FORM_SUMMARY), 0, 0);
        } else if (propertyId.equals("milestoneid")) {
            informationLayout.addComponent(field, AppContext.getMessage(BugI18nEnum.FORM_PHASE), 0, 1);
        } else if (propertyId.equals("environment")) {
            informationLayout.addComponent(field, AppContext.getMessage(BugI18nEnum.FORM_ENVIRONMENT), 0, 2);
        } else if (propertyId.equals("status")) {
            informationLayout.addComponent(field, AppContext.getMessage(GenericI18Enum.FORM_STATUS), 0, 3);
        } else if (propertyId.equals("priority")) {
            informationLayout.addComponent(field, AppContext.getMessage(BugI18nEnum.FORM_PRIORITY), 0, 4);
        } else if (propertyId.equals("severity")) {
            informationLayout.addComponent(field, AppContext.getMessage(BugI18nEnum.FORM_SEVERITY), 0, 5);
        } else if (propertyId.equals("resolution")) {
            informationLayout.addComponent(field, AppContext.getMessage(BugI18nEnum.FORM_RESOLUTION), 0, 6);
        } else if (propertyId.equals("startdate")) {
            informationLayout.addComponent(field, AppContext.getMessage(GenericI18Enum.FORM_START_DATE), 0, 7);
        } else if (propertyId.equals("enddate")) {
            informationLayout.addComponent(field, AppContext.getMessage(GenericI18Enum.FORM_END_DATE), 0, 8);
        } else if (propertyId.equals("duedate")) {
            informationLayout.addComponent(field, AppContext.getMessage(GenericI18Enum.FORM_DUE_DATE), 0, 9);
        } else if (propertyId.equals("createdtime")) {
            informationLayout.addComponent(field, AppContext.getMessage(GenericI18Enum.FORM_CREATED_TIME), 0, 10);
        } else if (propertyId.equals("loguserFullName")) {
            informationLayout.addComponent(field, AppContext.getMessage(BugI18nEnum.FORM_LOG_BY), 0, 11);
        } else if (propertyId.equals("assignuserFullName")) {
            informationLayout.addComponent(field, AppContext.getMessage(GenericI18Enum.FORM_ASSIGNEE), 0, 12);
        } else if (propertyId.equals("description")) {
            informationLayout.addComponent(field, AppContext.getMessage(GenericI18Enum.FORM_DESCRIPTION), 0, 13);
        }
    }

}

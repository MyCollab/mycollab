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

import com.esofthead.mycollab.common.i18n.GenericI18Enum;
import com.esofthead.mycollab.mobile.ui.grid.GridFormLayoutHelper;
import com.esofthead.mycollab.module.project.i18n.MilestoneI18nEnum;
import com.esofthead.mycollab.vaadin.AppContext;
import com.esofthead.mycollab.vaadin.ui.IFormLayoutFactory;
import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.Field;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import org.vaadin.viritin.layouts.MCssLayout;

/**
 * @author MyCollab Ltd.
 * @since 4.5.2
 */
public class MilestoneFormLayoutFactory implements IFormLayoutFactory {
    private static final long serialVersionUID = 1L;

    private GridFormLayoutHelper informationLayout;

    @Override
    public void attachField(Object propertyId, final Field<?> field) {
        if (propertyId.equals("name")) {
            this.informationLayout.addComponent(field, AppContext.getMessage(MilestoneI18nEnum.FORM_NAME_FIELD), 0, 0);
        } else if (propertyId.equals("status")) {
            this.informationLayout.addComponent(field, AppContext.getMessage(MilestoneI18nEnum.FORM_STATUS_FIELD), 0, 1);
        } else if (propertyId.equals("owner")) {
            this.informationLayout.addComponent(field, AppContext.getMessage(GenericI18Enum.FORM_ASSIGNEE), 0, 2);
        } else if (propertyId.equals("startdate")) {
            this.informationLayout.addComponent(field, AppContext.getMessage(MilestoneI18nEnum.FORM_START_DATE_FIELD), 0, 3);
        } else if (propertyId.equals("enddate")) {
            this.informationLayout.addComponent(field, AppContext.getMessage(MilestoneI18nEnum.FORM_END_DATE_FIELD), 0, 4);
        } else if (propertyId.equals("description")) {
            this.informationLayout.addComponent(field, AppContext.getMessage(GenericI18Enum.FORM_DESCRIPTION), 0, 7);
        }
    }

    @Override
    public ComponentContainer getLayout() {
        final VerticalLayout layout = new VerticalLayout();
        layout.setWidth("100%");
        layout.setMargin(false);

        Label header = new Label(AppContext.getMessage(MilestoneI18nEnum.M_FORM_READ_TITLE));
        layout.addComponent(new MCssLayout(header).withStyleName("section"));

        informationLayout = GridFormLayoutHelper.defaultFormLayoutHelper(1, 8);
        layout.addComponent(informationLayout.getLayout());
        return layout;
    }
}

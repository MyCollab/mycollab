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
package com.mycollab.mobile.module.project.view.task;

import com.mycollab.common.i18n.GenericI18Enum;
import com.mycollab.mobile.ui.FormSectionBuilder;
import com.mycollab.mobile.ui.grid.GridFormLayoutHelper;
import com.mycollab.module.project.domain.Task;
import com.mycollab.module.project.i18n.TaskI18nEnum;
import com.mycollab.vaadin.UserUIContext;
import com.mycollab.vaadin.ui.AbstractFormLayoutFactory;
import com.vaadin.ui.*;

/**
 * @author MyCollab Ltd.
 * @since 4.5.0
 */
public class TaskFormLayoutFactory extends AbstractFormLayoutFactory {
    private static final long serialVersionUID = 1L;

    private GridFormLayoutHelper informationLayout;

    @Override
    public ComponentContainer getLayout() {
        final VerticalLayout layout = new VerticalLayout();
        layout.setMargin(false);
        layout.addComponent(FormSectionBuilder.build(UserUIContext.getMessage(TaskI18nEnum.M_FORM_READ_TITLE)));

        informationLayout = GridFormLayoutHelper.defaultFormLayoutHelper(1, 9);
        layout.addComponent(informationLayout.getLayout());
        layout.setComponentAlignment(informationLayout.getLayout(), Alignment.BOTTOM_CENTER);
        return layout;
    }

    @Override
    public Component onAttachField(Object propertyId, final Field<?> field) {
        if (propertyId.equals("taskname")) {
            return informationLayout.addComponent(field, UserUIContext.getMessage(GenericI18Enum.FORM_NAME), 0, 0);
        } else if (propertyId.equals("startdate")) {
            return informationLayout.addComponent(field, UserUIContext.getMessage(GenericI18Enum.FORM_START_DATE), 0, 1);
        } else if (propertyId.equals("enddate")) {
            return informationLayout.addComponent(field, UserUIContext.getMessage(GenericI18Enum.FORM_END_DATE), 0, 2);
        } else if (propertyId.equals("deadline")) {
            return informationLayout.addComponent(field, UserUIContext.getMessage(GenericI18Enum.FORM_DUE_DATE), 0, 3);
        } else if (propertyId.equals("priority")) {
            return informationLayout.addComponent(field, UserUIContext.getMessage(TaskI18nEnum.FORM_PRIORITY), 0, 4);
        } else if (propertyId.equals("assignuser")) {
            return informationLayout.addComponent(field, UserUIContext.getMessage(GenericI18Enum.FORM_ASSIGNEE), 0, 5);
        } else if (Task.Field.milestoneid.equalTo(propertyId)) {
            return informationLayout.addComponent(field, UserUIContext.getMessage(TaskI18nEnum.FORM_PHASE), 0, 6);
        } else if (propertyId.equals("percentagecomplete")) {
            return informationLayout.addComponent(field, UserUIContext.getMessage(TaskI18nEnum.FORM_PERCENTAGE_COMPLETE), 0, 7);
        } else if (propertyId.equals("notes")) {
            field.setSizeUndefined();
            return informationLayout.addComponent(field, UserUIContext.getMessage(TaskI18nEnum.FORM_NOTES), 0, 8);
        }
        return null;
    }

    public void setPercent(String labelPercent) {
        ((HorizontalLayout) informationLayout.getComponent(0, 5)).removeAllComponents();
        ((HorizontalLayout) informationLayout.getComponent(0, 5)).addComponent(new Label(labelPercent));
    }
}

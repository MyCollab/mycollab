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
package com.esofthead.mycollab.module.crm.view.activity;

import com.esofthead.mycollab.module.crm.CrmTypeConstants;
import com.esofthead.mycollab.module.crm.ui.CrmAssetsManager;
import com.esofthead.mycollab.vaadin.ui.*;
import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.Field;
import com.vaadin.ui.Layout;
import com.vaadin.ui.VerticalLayout;

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
public abstract class MeetingFormLayoutFactory implements IWrappedFormLayoutFactory {
    private static final long serialVersionUID = 1L;
    private String title;
    private IFormLayoutFactory informationLayout;

    public MeetingFormLayoutFactory(String title) {
        this.title = title;
    }

    @Override
    public ComponentContainer getLayout() {
        AddViewLayout2 meetingLayout = new AddViewLayout2(title, CrmAssetsManager.getAsset(CrmTypeConstants.MEETING));

        Layout topPanel = createTopPanel();
        if (topPanel != null) {
            meetingLayout.addControlButtons(topPanel);
        }
        informationLayout = new DynaFormLayout(CrmTypeConstants.MEETING, MeetingDefaultFormLayoutFactory.getForm());
        VerticalLayout body = new VerticalLayout();
        body.setStyleName(UIConstants.BORDER_BOX_2);
        body.addComponent(informationLayout.getLayout());
        meetingLayout.addBody(body);

        return meetingLayout;
    }

    @Override
    public void attachField(Object propertyId, Field<?> field) {
        informationLayout.attachField(propertyId, field);
    }

    @Override
    public IFormLayoutFactory getWrappedFactory() {
        return informationLayout;
    }

    protected abstract Layout createTopPanel();

    protected abstract Layout createBottomPanel();
}

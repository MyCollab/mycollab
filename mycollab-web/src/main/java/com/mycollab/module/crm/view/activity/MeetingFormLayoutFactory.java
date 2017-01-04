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

import com.mycollab.module.crm.CrmTypeConstants;
import com.mycollab.module.crm.ui.CrmAssetsManager;
import com.mycollab.vaadin.ui.WrappedFormLayoutFactory;
import com.mycollab.vaadin.web.ui.AddViewLayout2;
import com.mycollab.vaadin.web.ui.DefaultDynaFormLayout;
import com.mycollab.vaadin.web.ui.WebThemes;
import com.vaadin.ui.AbstractComponent;
import com.vaadin.ui.Layout;
import com.vaadin.ui.VerticalLayout;

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
public abstract class MeetingFormLayoutFactory extends WrappedFormLayoutFactory {
    private static final long serialVersionUID = 1L;
    private String title;

    public MeetingFormLayoutFactory(String title) {
        this.title = title;
    }

    @Override
    public AbstractComponent getLayout() {
        AddViewLayout2 meetingLayout = new AddViewLayout2(title, CrmAssetsManager.getAsset(CrmTypeConstants.MEETING));

        Layout topPanel = createTopPanel();
        if (topPanel != null) {
            meetingLayout.addControlButtons(topPanel);
        }
        wrappedLayoutFactory = new DefaultDynaFormLayout(CrmTypeConstants.MEETING, MeetingDefaultFormLayoutFactory.getForm());
        VerticalLayout body = new VerticalLayout();
        body.setStyleName(WebThemes.BOX);
        body.addComponent(wrappedLayoutFactory.getLayout());
        meetingLayout.addBody(body);

        return meetingLayout;
    }

    protected abstract Layout createTopPanel();

    protected abstract Layout createBottomPanel();
}

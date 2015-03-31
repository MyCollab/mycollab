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
package com.esofthead.mycollab.module.user.accountsettings.view;

import com.esofthead.mycollab.vaadin.ui.AddViewLayout;
import com.esofthead.mycollab.vaadin.ui.GridFormLayoutHelper;
import com.esofthead.mycollab.vaadin.ui.IFormLayoutFactory;
import com.esofthead.mycollab.vaadin.ui.MyCollabResource;
import com.vaadin.ui.*;

/**
 * @author MyCollab Ltd.
 * @since 2.0
 */
public abstract class RoleFormLayoutFactory implements IFormLayoutFactory {
    private static final long serialVersionUID = 1L;
    private String title;
    private RoleInformationLayout userInformationLayout;

    public RoleFormLayoutFactory(final String title) {
        this.title = title;
    }

    @Override
    public ComponentContainer getLayout() {
        final AddViewLayout userAddLayout = new AddViewLayout(this.title,
                MyCollabResource.newResource("icons/48/user/group.png"));
        Layout topPanel = this.createTopPanel();
        if (topPanel != null) {
            userAddLayout.addHeaderRight(topPanel);
        }

        userInformationLayout = new RoleInformationLayout();
        userInformationLayout.getLayout().setWidth("100%");
        userAddLayout.addBody(this.userInformationLayout.getLayout());

        Layout bottomPanel = this.createBottomPanel();
        if (bottomPanel != null) {
            userAddLayout.addBottomControls(bottomPanel);
        }

        return userAddLayout;
    }

    protected abstract Layout createTopPanel();

    protected abstract Layout createBottomPanel();

    @Override
    public void attachField(final Object propertyId, final Field<?> field) {
        this.userInformationLayout.attachField(propertyId, field);
    }

    public static class RoleInformationLayout implements IFormLayoutFactory {
        private static final long serialVersionUID = 1L;
        private GridFormLayoutHelper informationLayout;

        @Override
        public ComponentContainer getLayout() {
            VerticalLayout layout = new VerticalLayout();
            Label organizationHeader = new Label("Role Information");
            organizationHeader.setStyleName("h2");
            layout.addComponent(organizationHeader);
            informationLayout = GridFormLayoutHelper.defaultFormLayoutHelper(6, 2);
            layout.addComponent(informationLayout.getLayout());
            return layout;
        }

        @Override
        public void attachField(final Object propertyId, final Field<?> field) {
            if (propertyId.equals("rolename")) {
                this.informationLayout.addComponent(field, "Role Name", 0, 0);
            } else if (propertyId.equals("description")) {
                this.informationLayout.addComponent(field, "Description", 0, 1);
            }
        }
    }
}

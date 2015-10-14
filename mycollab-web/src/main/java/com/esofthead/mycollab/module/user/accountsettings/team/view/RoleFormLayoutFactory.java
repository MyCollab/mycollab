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
package com.esofthead.mycollab.module.user.accountsettings.team.view;

import com.esofthead.mycollab.common.i18n.GenericI18Enum;
import com.esofthead.mycollab.module.user.accountsettings.localization.RoleI18nEnum;
import com.esofthead.mycollab.vaadin.AppContext;
import com.esofthead.mycollab.vaadin.ui.*;
import com.esofthead.mycollab.vaadin.ui.grid.GridFormLayoutHelper;
import com.vaadin.ui.*;

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
public abstract class RoleFormLayoutFactory extends AbstractFormLayoutFactory {
    private static final long serialVersionUID = 1L;
    private String title;
    protected RoleInformationLayout userInformationLayout;

    public RoleFormLayoutFactory(String title) {
        this.title = title;
    }

    @Override
    public ComponentContainer getLayout() {
        ReadViewLayout userAddLayout = new DefaultReadViewLayout(this.title);

        this.userInformationLayout = new RoleInformationLayout();
        this.userInformationLayout.getLayout().setWidth("100%");
        userAddLayout.addBody(userInformationLayout.getLayout());

        Layout bottomPanel = this.createBottomPanel();
        if (bottomPanel != null) {
            userAddLayout.addBottomControls(bottomPanel);
        }

        return userAddLayout;
    }

    protected abstract Layout createBottomPanel();

    @Override
    protected void onAttachField(Object propertyId, Field<?> field) {
        this.userInformationLayout.attachField(propertyId, field);
    }

    public static class RoleInformationLayout extends AbstractFormLayoutFactory {
        private static final long serialVersionUID = 1L;
        private GridFormLayoutHelper informationLayout;

        @Override
        public ComponentContainer getLayout() {
            VerticalLayout layout = new VerticalLayout();
            Label organizationHeader = new Label(AppContext.getMessage(RoleI18nEnum.SECTION_INFORMATION));
            organizationHeader.setStyleName(UIConstants.H2_STYLE2);
            layout.addComponent(organizationHeader);

            informationLayout = GridFormLayoutHelper.defaultFormLayoutHelper(6, 2);
            layout.addComponent(informationLayout.getLayout());
            return layout;
        }

        @Override
        protected void onAttachField(final Object propertyId, final Field<?> field) {
            if (propertyId.equals("rolename")) {
                this.informationLayout.addComponent(field,
                        AppContext.getMessage(RoleI18nEnum.FORM_NAME), 0, 0, 2, "100%");
            } else if (propertyId.equals("description")) {
                this.informationLayout.addComponent(field,
                        AppContext.getMessage(GenericI18Enum.FORM_DESCRIPTION), 0, 1, 2, "100%");
            }
        }
    }
}

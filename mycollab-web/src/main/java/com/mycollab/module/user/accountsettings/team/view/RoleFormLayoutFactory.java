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
package com.mycollab.module.user.accountsettings.team.view;

import com.mycollab.common.i18n.GenericI18Enum;
import com.mycollab.module.user.accountsettings.localization.RoleI18nEnum;
import com.mycollab.module.user.domain.Role;
import com.mycollab.vaadin.UserUIContext;
import com.mycollab.vaadin.ui.AbstractFormLayoutFactory;
import com.mycollab.vaadin.ui.FormContainer;
import com.mycollab.vaadin.ui.WrappedFormLayoutFactory;
import com.mycollab.vaadin.web.ui.DefaultReadViewLayout;
import com.mycollab.vaadin.web.ui.ReadViewLayout;
import com.mycollab.vaadin.web.ui.grid.GridFormLayoutHelper;
import com.vaadin.ui.*;

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
public abstract class RoleFormLayoutFactory extends WrappedFormLayoutFactory {
    private static final long serialVersionUID = 1L;

    private String title;

    public RoleFormLayoutFactory(String title) {
        this.title = title;
    }

    @Override
    public AbstractComponent getLayout() {
        ReadViewLayout userAddLayout = new DefaultReadViewLayout(this.title);

        wrappedLayoutFactory = new RoleInformationLayout();
        wrappedLayoutFactory.getLayout().setWidth("100%");
        userAddLayout.addBody(wrappedLayoutFactory.getLayout());

        Layout bottomPanel = this.createBottomPanel();
        if (bottomPanel != null) {
            userAddLayout.addBottomControls(bottomPanel);
        }

        return userAddLayout;
    }

    protected abstract Layout createBottomPanel();


    static class RoleInformationLayout extends AbstractFormLayoutFactory {
        private static final long serialVersionUID = 1L;
        private GridFormLayoutHelper informationLayout;

        @Override
        public AbstractComponent getLayout() {
            FormContainer layout = new FormContainer();

            informationLayout = GridFormLayoutHelper.defaultFormLayoutHelper(1, 3);
            layout.addSection(UserUIContext.getMessage(RoleI18nEnum.SECTION_INFORMATION), informationLayout.getLayout());
            return layout;
        }

        @Override
        protected Component onAttachField(Object propertyId, final Field<?> field) {
            if (propertyId.equals("rolename")) {
                return informationLayout.addComponent(field, UserUIContext.getMessage(GenericI18Enum.FORM_NAME), 0, 0);
            } else if (Role.Field.isdefault.equalTo(propertyId)) {
                return informationLayout.addComponent(field, UserUIContext.getMessage(RoleI18nEnum.FORM_IS_DEFAULT),
                        UserUIContext.getMessage(RoleI18nEnum.FORM_IS_DEFAULT_HELP), 0, 1);
            } else if (propertyId.equals("description")) {
                return informationLayout.addComponent(field, UserUIContext.getMessage(GenericI18Enum.FORM_DESCRIPTION), 0, 2);
            }
            return null;
        }
    }
}

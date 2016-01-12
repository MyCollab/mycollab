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
package com.esofthead.mycollab.module.user.accountsettings.profile.view;

import com.esofthead.mycollab.module.user.accountsettings.localization.UserI18nEnum;
import com.esofthead.mycollab.vaadin.AppContext;
import com.esofthead.mycollab.vaadin.ui.*;
import com.esofthead.mycollab.vaadin.web.ui.DefaultReadViewLayout;
import com.esofthead.mycollab.vaadin.web.ui.ReadViewLayout;
import com.esofthead.mycollab.vaadin.web.ui.WebResourceIds;
import com.esofthead.mycollab.vaadin.web.ui.grid.GridFormLayoutHelper;
import com.vaadin.server.Resource;
import com.vaadin.ui.*;

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
@SuppressWarnings("serial")
public abstract class ProfileFormLayoutFactory implements IFormLayoutFactory {
    private String title;
    private UserInformationLayout userInformationLayout;
    private Resource userAvatarIcon;

    public ProfileFormLayoutFactory(String title) {
        this.title = title;
    }

    @Override
    public ComponentContainer getLayout() {
        if (userAvatarIcon == null) {
            userAvatarIcon = new AssetResource(WebResourceIds._default_user_avatar_24);
        }
        ReadViewLayout userAddLayout = new DefaultReadViewLayout(this.title);

        this.userInformationLayout = new UserInformationLayout();
        this.userInformationLayout.getLayout().setWidth("100%");
        userAddLayout.addBody(this.userInformationLayout.getLayout());

        final Layout bottomPanel = this.createBottomPanel();
        if (bottomPanel != null) {
            userAddLayout.addBottomControls(bottomPanel);
        }

        return userAddLayout;
    }

    protected abstract Layout createBottomPanel();

    @Override
    public void attachField(Object propertyId, final Field<?> field) {
        this.userInformationLayout.attachField(propertyId, field);
    }

    public static class UserInformationLayout implements IFormLayoutFactory {
        private GridFormLayoutHelper basicInformationLayout;
        private GridFormLayoutHelper advancedInformationLayout;
        private GridFormLayoutHelper contactInformationLayout;

        @Override
        public ComponentContainer getLayout() {
            FormContainer layout = new FormContainer();
            basicInformationLayout = GridFormLayoutHelper.defaultFormLayoutHelper(2, 7);
            layout.addSection(AppContext.getMessage(UserI18nEnum.SECTION_BASIC_INFORMATION),
                    basicInformationLayout.getLayout());

            contactInformationLayout = GridFormLayoutHelper.defaultFormLayoutHelper(2, 3);
            layout.addSection(AppContext.getMessage(UserI18nEnum.SECTION_CONTACT_INFORMATION),
                    contactInformationLayout.getLayout());

            advancedInformationLayout = GridFormLayoutHelper.defaultFormLayoutHelper(2, 2);
            layout.addSection(AppContext.getMessage(UserI18nEnum.SECTION_ADVANCED_INFORMATION),
                    advancedInformationLayout.getLayout());
            return layout;
        }

        @Override
        public void attachField(Object propertyId, Field<?> field) {
            if (propertyId.equals("firstname")) {
                basicInformationLayout.addComponent(field, AppContext.getMessage(UserI18nEnum.FORM_FIRST_NAME), 0, 0);
            } else if (propertyId.equals("lastname")) {
                basicInformationLayout.addComponent(field, AppContext.getMessage(UserI18nEnum.FORM_LAST_NAME), 0, 1);
            } else if (propertyId.equals("nickname")) {
                basicInformationLayout.addComponent(field, AppContext.getMessage(UserI18nEnum.FORM_NICK_NAME), 1, 0);
            } else if (propertyId.equals("dateofbirth")) {
                basicInformationLayout.addComponent(field, AppContext.getMessage(UserI18nEnum.FORM_BIRTHDAY), 1, 1);
            } else if (propertyId.equals("email")) {
                basicInformationLayout.addComponent(field, AppContext.getMessage(UserI18nEnum.FORM_EMAIL), 0, 2);
            } else if (propertyId.equals("timezone")) {
                basicInformationLayout.addComponent(field, AppContext.getMessage(UserI18nEnum.FORM_TIMEZONE), 0, 3, 2, "262px");
            } else if (propertyId.equals("roleid")) {
                basicInformationLayout.addComponent(field, AppContext.getMessage(UserI18nEnum.FORM_ROLE), 1, 2);
            } else if (propertyId.equals("company")) {
                advancedInformationLayout.addComponent(field, AppContext.getMessage(UserI18nEnum.FORM_COMPANY), 0, 0);
            } else if (propertyId.equals("country")) {
                advancedInformationLayout.addComponent(field, AppContext.getMessage(UserI18nEnum.FORM_COUNTRY), 0, 1, 2, "262px");
            } else if (propertyId.equals("website")) {
                advancedInformationLayout.addComponent(field, AppContext.getMessage(UserI18nEnum.FORM_WEBSITE), 1, 0);
            } else if (propertyId.equals("workphone")) {
                contactInformationLayout.addComponent(field, AppContext.getMessage(UserI18nEnum.FORM_WORK_PHONE), 0, 0);
            } else if (propertyId.equals("homephone")) {
                contactInformationLayout.addComponent(field, AppContext.getMessage(UserI18nEnum.FORM_HOME_PHONE), 0, 1);
            } else if (propertyId.equals("facebookaccount")) {
                contactInformationLayout.addComponent(field, "Facebook", 1, 0);
            } else if (propertyId.equals("twitteraccount")) {
                contactInformationLayout.addComponent(field, "Twitter", 1, 1);
            } else if (propertyId.equals("skypecontact")) {
                contactInformationLayout.addComponent(field, "Skype", 0, 2, 2, "262px");
            }
        }
    }
}

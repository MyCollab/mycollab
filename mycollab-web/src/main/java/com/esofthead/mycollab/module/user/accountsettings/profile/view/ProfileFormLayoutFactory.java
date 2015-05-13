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
            userAvatarIcon = MyCollabResource
                    .newResource(WebResourceIds._default_user_avatar_24);
        }
        final ReadViewLayout userAddLayout = new DefaultReadViewLayout(this.title);

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
    public void attachField(final Object propertyId, final Field<?> field) {
        this.userInformationLayout.attachField(propertyId, field);
    }

    public static class UserInformationLayout implements IFormLayoutFactory {
        private GridFormLayoutHelper basicInformationLayout;
        private GridFormLayoutHelper advancedInformationLayout;
        private GridFormLayoutHelper contactInformationLayout;

        @Override
        public ComponentContainer getLayout() {
            VerticalLayout layout = new VerticalLayout();
            Label organizationHeader = new Label(AppContext.getMessage(UserI18nEnum.SECTION_BASIC_INFORMATION));
            organizationHeader.setStyleName("h2");
            layout.addComponent(organizationHeader);

            basicInformationLayout = GridFormLayoutHelper.defaultFormLayoutHelper(2, 7);

            layout.addComponent(basicInformationLayout.getLayout());

            Label contactHeader = new Label(AppContext.getMessage(UserI18nEnum.SECTION_CONTACT_INFORMATION));
            contactHeader.setStyleName("h2");
            layout.addComponent(contactHeader);

            contactInformationLayout = GridFormLayoutHelper.defaultFormLayoutHelper(2, 3);

            layout.addComponent(contactInformationLayout.getLayout());

            Label advancedHeader = new Label(AppContext.getMessage(UserI18nEnum.SECTION_ADVANCED_INFORMATION));
            advancedHeader.setStyleName("h2");
            layout.addComponent(advancedHeader);

            advancedInformationLayout = GridFormLayoutHelper.defaultFormLayoutHelper(2, 2);

            layout.addComponent(advancedInformationLayout.getLayout());
            return layout;
        }

        @Override
        public void attachField(final Object propertyId, final Field<?> field) {
            if (propertyId.equals("firstname")) {
                this.basicInformationLayout.addComponent(field,
                        AppContext.getMessage(UserI18nEnum.FORM_FIRST_NAME), 0,
                        0);
            } else if (propertyId.equals("lastname")) {
                this.basicInformationLayout.addComponent(field,
                        AppContext.getMessage(UserI18nEnum.FORM_LAST_NAME), 0,
                        1);
            } else if (propertyId.equals("nickname")) {
                this.basicInformationLayout.addComponent(field,
                        AppContext.getMessage(UserI18nEnum.FORM_NICK_NAME), 1,
                        0);
            } else if (propertyId.equals("dateofbirth")) {
                this.basicInformationLayout
                        .addComponent(field, AppContext
                                .getMessage(UserI18nEnum.FORM_BIRTHDAY), 1, 1);
            } else if (propertyId.equals("email")) {
                this.basicInformationLayout.addComponent(field,
                        AppContext.getMessage(UserI18nEnum.FORM_EMAIL), 0, 2);
            } else if (propertyId.equals("timezone")) {
                this.basicInformationLayout.addComponent(field,
                        AppContext.getMessage(UserI18nEnum.FORM_TIMEZONE), 0,
                        3, 2, "262px", Alignment.MIDDLE_LEFT);
            } else if (propertyId.equals("roleid")) {
                this.basicInformationLayout.addComponent(field,
                        AppContext.getMessage(UserI18nEnum.FORM_ROLE), 1, 2);
            } else if (propertyId.equals("company")) {
                this.advancedInformationLayout.addComponent(field,
                        AppContext.getMessage(UserI18nEnum.FORM_COMPANY), 0, 0);
            } else if (propertyId.equals("country")) {
                this.advancedInformationLayout.addComponent(field,
                        AppContext.getMessage(UserI18nEnum.FORM_COUNTRY), 0, 1,
                        2, "262px", Alignment.MIDDLE_LEFT);
            } else if (propertyId.equals("website")) {
                this.advancedInformationLayout.addComponent(field,
                        AppContext.getMessage(UserI18nEnum.FORM_WEBSITE), 1, 0);
            } else if (propertyId.equals("workphone")) {
                this.contactInformationLayout.addComponent(field,
                        AppContext.getMessage(UserI18nEnum.FORM_WORK_PHONE), 0,
                        0);
            } else if (propertyId.equals("homephone")) {
                this.contactInformationLayout.addComponent(field,
                        AppContext.getMessage(UserI18nEnum.FORM_HOME_PHONE), 0,
                        1);
            } else if (propertyId.equals("facebookaccount")) {
                this.contactInformationLayout.addComponent(field, "Facebook",
                        1, 0);
            } else if (propertyId.equals("twitteraccount")) {
                this.contactInformationLayout.addComponent(field, "Twitter", 1,
                        1);
            } else if (propertyId.equals("skypecontact")) {
                this.contactInformationLayout.addComponent(field, "Skype", 0,
                        2, 2, "262px", Alignment.MIDDLE_LEFT);
            }
        }
    }
}

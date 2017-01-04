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

import com.hp.gagawa.java.Node;
import com.hp.gagawa.java.elements.A;
import com.hp.gagawa.java.elements.Div;
import com.mycollab.common.i18n.GenericI18Enum;
import com.mycollab.common.i18n.ShellI18nEnum;
import com.mycollab.core.utils.TimezoneVal;
import com.mycollab.i18n.LocalizationHelper;
import com.mycollab.module.user.AccountLinkBuilder;
import com.mycollab.module.user.accountsettings.localization.RoleI18nEnum;
import com.mycollab.module.user.accountsettings.localization.UserI18nEnum;
import com.mycollab.module.user.accountsettings.profile.view.PasswordChangeWindow;
import com.mycollab.module.user.domain.SimpleUser;
import com.mycollab.module.user.domain.User;
import com.mycollab.module.user.ui.components.PreviewFormControlsGenerator;
import com.mycollab.security.RolePermissionCollections;
import com.mycollab.vaadin.MyCollabUI;
import com.mycollab.vaadin.UserUIContext;
import com.mycollab.vaadin.events.HasPreviewFormHandlers;
import com.mycollab.vaadin.mvp.AbstractVerticalPageView;
import com.mycollab.vaadin.mvp.ViewComponent;
import com.mycollab.vaadin.ui.*;
import com.mycollab.vaadin.ui.field.DateViewField;
import com.mycollab.vaadin.ui.field.DefaultViewField;
import com.mycollab.vaadin.ui.field.EmailViewField;
import com.mycollab.vaadin.ui.field.UrlLinkViewField;
import com.mycollab.vaadin.web.ui.AdvancedPreviewBeanForm;
import com.mycollab.vaadin.web.ui.WebThemes;
import com.mycollab.vaadin.web.ui.field.LinkViewField;
import com.mycollab.vaadin.web.ui.grid.GridFormLayoutHelper;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.*;
import org.apache.commons.lang3.StringUtils;
import org.vaadin.viritin.button.MButton;
import org.vaadin.viritin.layouts.MHorizontalLayout;
import org.vaadin.viritin.layouts.MVerticalLayout;

import static com.mycollab.module.user.ui.components.PreviewFormControlsGenerator.*;

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
@ViewComponent
public class UserReadViewImpl extends AbstractVerticalPageView implements UserReadView {
    private static final long serialVersionUID = 1L;

    private AdvancedPreviewBeanForm<User> previewForm;
    private MHorizontalLayout header;
    private SimpleUser user;

    public UserReadViewImpl() {
        super();
        this.setMargin(new MarginInfo(false, true, true, true));
        header = new MHorizontalLayout().withMargin(new MarginInfo(true, false, false, false)).withFullWidth();
        addComponent(header);
        previewForm = new PreviewForm();
        addComponent(previewForm);
    }

    private void displayUserAvatar() {
        header.removeAllComponents();
        MHorizontalLayout avatarAndPass = new MHorizontalLayout().withFullWidth();
        Image cropField = UserAvatarControlFactory.createUserAvatarEmbeddedComponent(user.getAvatarid(), 100);
        cropField.addStyleName(UIConstants.CIRCLE_BOX);
        CssLayout userAvatar = new CssLayout();
        userAvatar.addComponent(cropField);
        avatarAndPass.addComponent(userAvatar);

        MVerticalLayout basicLayout = new MVerticalLayout().withMargin(new MarginInfo(false, true, false, true));
        CssLayout userWrapper = new CssLayout();
        String nickName = user.getNickname();
        ELabel userName = ELabel.h2(user.getDisplayName() + (StringUtils.isEmpty(nickName) ? "" : (String.format(" ( %s )", nickName))));
        userWrapper.addComponent(userName);

        basicLayout.addComponent(userWrapper);
        basicLayout.setComponentAlignment(userWrapper, Alignment.MIDDLE_LEFT);

        GridFormLayoutHelper userFormLayout;
        if (UserUIContext.isAdmin()) {
            userFormLayout = GridFormLayoutHelper.defaultFormLayoutHelper(1, 6).withCaptionWidth("140px");
        } else {
            userFormLayout = GridFormLayoutHelper.defaultFormLayoutHelper(1, 5).withCaptionWidth("140px");
        }
        userFormLayout.getLayout().addStyleName(WebThemes.GRIDFORM_BORDERLESS);
        basicLayout.addComponent(userFormLayout.getLayout());

        Node roleDiv;
        if (Boolean.TRUE.equals(user.getIsAccountOwner())) {
            roleDiv = new Div().appendText(UserUIContext.getMessage(RoleI18nEnum.OPT_ACCOUNT_OWNER));
        } else {
            roleDiv = new A(AccountLinkBuilder.generatePreviewFullRoleLink(user.getRoleid())).appendText(user.getRoleName());
        }

        userFormLayout.addComponent(ELabel.html(roleDiv.write()), UserUIContext.getMessage(UserI18nEnum.FORM_ROLE), 0, 0);
        userFormLayout.addComponent(new Label(UserUIContext.formatDate(user.getDateofbirth())),
                UserUIContext.getMessage(UserI18nEnum.FORM_BIRTHDAY), 0, 1);

        if (Boolean.TRUE.equals(MyCollabUI.showEmailPublicly())) {
            userFormLayout.addComponent(ELabel.html(new A("mailto:" + user.getEmail()).appendText(user.getEmail()).write()),
                    UserUIContext.getMessage(GenericI18Enum.FORM_EMAIL), 0, 2);
        } else {
            userFormLayout.addComponent(ELabel.html("******"), UserUIContext.getMessage(GenericI18Enum.FORM_EMAIL), 0, 2);
        }

        userFormLayout.addComponent(new Label(TimezoneVal.getDisplayName(UserUIContext.getUserLocale(), user.getTimezone())),
                UserUIContext.getMessage(UserI18nEnum.FORM_TIMEZONE), 0, 3);
        userFormLayout.addComponent(new Label(LocalizationHelper.getLocaleInstance(user.getLanguage()).getDisplayLanguage(UserUIContext.getUserLocale())),
                UserUIContext.getMessage(UserI18nEnum.FORM_LANGUAGE), 0, 4);

        if (UserUIContext.isAdmin()) {
            MButton btnChangePassword = new MButton(UserUIContext.getMessage(GenericI18Enum.ACTION_CHANGE),
                    clickEvent -> UI.getCurrent().addWindow(new PasswordChangeWindow(user)))
                    .withStyleName(WebThemes.BUTTON_LINK);
            userFormLayout.addComponent(new MHorizontalLayout(new Label("***********"), btnChangePassword),
                    UserUIContext.getMessage(ShellI18nEnum.FORM_PASSWORD), 0, 5);
        }

        avatarAndPass.with(basicLayout).withAlign(basicLayout, Alignment.TOP_LEFT).expand(basicLayout);

        Layout controlButtons = createTopPanel();
        CssLayout avatarAndPassWrapper = new CssLayout();
        avatarAndPass.setWidthUndefined();
        avatarAndPassWrapper.addComponent(avatarAndPass);
        header.with(avatarAndPass, controlButtons).withAlign(avatarAndPass, Alignment.TOP_LEFT)
                .withAlign(controlButtons, Alignment.TOP_RIGHT);
    }

    private Layout createTopPanel() {
        final PreviewFormControlsGenerator<User> controlGenerator = new PreviewFormControlsGenerator<>(previewForm);
        return controlGenerator.createButtonControls(ADD_BTN_PRESENTED | EDIT_BTN_PRESENTED |
                DELETE_BTN_PRESENTED | CLONE_BTN_PRESENTED, RolePermissionCollections.ACCOUNT_USER);
    }

    @Override
    public void previewItem(SimpleUser user) {
        this.user = user;
        previewForm.setBean(user);
        displayUserAvatar();
    }

    @Override
    public HasPreviewFormHandlers<User> getPreviewFormHandlers() {
        return previewForm;
    }

    private class PreviewForm extends AdvancedPreviewBeanForm<User> {
        private static final long serialVersionUID = 1L;

        @Override
        public void setBean(User newDataSource) {
            this.setFormLayoutFactory(new FormLayoutFactory());
            this.setBeanFormFieldFactory(new AbstractBeanFieldGroupViewFieldFactory<User>(PreviewForm.this) {
                private static final long serialVersionUID = 1L;

                @Override
                protected Field<?> onCreateField(Object propertyId) {
                    if (propertyId.equals("email")) {
                        return new EmailViewField(user.getEmail());
                    } else if (propertyId.equals("roleid")) {
                        if (Boolean.TRUE.equals(user.getIsAccountOwner())) {
                            return new DefaultViewField(UserUIContext.getMessage(RoleI18nEnum.OPT_ACCOUNT_OWNER));
                        } else {
                            return new LinkViewField(user.getRoleName(), AccountLinkBuilder.generatePreviewFullRoleLink(user.getRoleid()));
                        }
                    } else if (propertyId.equals("website")) {
                        return new UrlLinkViewField(user.getWebsite());
                    } else if (propertyId.equals("dateofbirth")) {
                        return new DateViewField(user.getDateofbirth());
                    } else if (propertyId.equals("timezone")) {
                        return new DefaultViewField(TimezoneVal.getDisplayName(UserUIContext.getUserLocale(), user.getTimezone()));
                    } else if (propertyId.equals("facebookaccount")) {
                        return new UrlLinkViewField(String.format("https://www.facebook.com/%s", user.getFacebookaccount()),
                                user.getFacebookaccount());
                    } else if (propertyId.equals("twitteraccount")) {
                        return new UrlLinkViewField(String.format("https://www.twitter.com/%s", user.getTwitteraccount()),
                                user.getTwitteraccount());
                    } else if (propertyId.equals("skypecontact")) {
                        return new UrlLinkViewField(String.format("skype:%s?chat", user.getSkypecontact()), user.getSkypecontact());
                    }
                    return null;
                }
            });
            super.setBean(newDataSource);
        }

        private class FormLayoutFactory extends AbstractFormLayoutFactory {
            private static final long serialVersionUID = 1L;

            private GridFormLayoutHelper contactLayout = GridFormLayoutHelper.defaultFormLayoutHelper(1, 5);
            private GridFormLayoutHelper advancedInfoLayout = GridFormLayoutHelper.defaultFormLayoutHelper(1, 3);

            @Override
            public AbstractComponent getLayout() {
                FormContainer layout = new FormContainer();
                layout.addSection(UserUIContext.getMessage(UserI18nEnum.SECTION_CONTACT_INFORMATION), contactLayout.getLayout());
                layout.addSection(UserUIContext.getMessage(UserI18nEnum.SECTION_ADVANCED_INFORMATION), advancedInfoLayout.getLayout());
                return layout;
            }

            @Override
            protected Component onAttachField(Object propertyId, Field<?> field) {
                if (propertyId.equals("website")) {
                    return advancedInfoLayout.addComponent(field, UserUIContext.getMessage(UserI18nEnum.FORM_WEBSITE), 0, 0);
                } else if (propertyId.equals("company")) {
                    return advancedInfoLayout.addComponent(field, UserUIContext.getMessage(UserI18nEnum.FORM_COMPANY), 0, 1);
                } else if (propertyId.equals("country")) {
                    return advancedInfoLayout.addComponent(field, UserUIContext.getMessage(UserI18nEnum.FORM_COUNTRY), 0, 2);
                } else if (propertyId.equals("workphone")) {
                    return contactLayout.addComponent(field, UserUIContext.getMessage(UserI18nEnum.FORM_WORK_PHONE), 0, 0);
                } else if (propertyId.equals("homephone")) {
                    return contactLayout.addComponent(field, UserUIContext.getMessage(UserI18nEnum.FORM_HOME_PHONE), 0, 1);
                } else if (propertyId.equals("facebookaccount")) {
                    return contactLayout.addComponent(field, "Facebook", 0, 2);
                } else if (propertyId.equals("twitteraccount")) {
                    return contactLayout.addComponent(field, "Twitter", 0, 3);
                } else if (propertyId.equals("skypecontact")) {
                    return contactLayout.addComponent(field, "Skype", 0, 4);
                }
                return null;
            }
        }
    }

    @Override
    public SimpleUser getItem() {
        return user;
    }
}

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

import com.esofthead.mycollab.common.i18n.GenericI18Enum;
import com.esofthead.mycollab.common.i18n.ShellI18nEnum;
import com.esofthead.mycollab.core.utils.TimezoneMapper;
import com.esofthead.mycollab.i18n.LocalizationHelper;
import com.esofthead.mycollab.module.file.service.UserAvatarService;
import com.esofthead.mycollab.module.user.accountsettings.localization.UserI18nEnum;
import com.esofthead.mycollab.module.user.domain.User;
import com.esofthead.mycollab.module.user.ui.components.ImagePreviewCropWindow;
import com.esofthead.mycollab.module.user.ui.components.UploadImageField;
import com.esofthead.mycollab.spring.ApplicationContextUtil;
import com.esofthead.mycollab.vaadin.AppContext;
import com.esofthead.mycollab.vaadin.mvp.AbstractPageView;
import com.esofthead.mycollab.vaadin.mvp.ViewComponent;
import com.esofthead.mycollab.vaadin.ui.*;
import com.esofthead.mycollab.vaadin.web.ui.AdvancedPreviewBeanForm;
import com.esofthead.mycollab.vaadin.web.ui.UIConstants;
import com.esofthead.mycollab.vaadin.web.ui.field.UrlLinkViewField;
import com.esofthead.mycollab.vaadin.web.ui.field.UrlSocialNetworkLinkViewField;
import com.esofthead.mycollab.vaadin.web.ui.grid.GridFormLayoutHelper;
import com.vaadin.server.Page;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.*;
import com.vaadin.ui.Button.ClickEvent;
import org.vaadin.viritin.layouts.MHorizontalLayout;
import org.vaadin.viritin.layouts.MVerticalLayout;

import java.awt.image.BufferedImage;

/**
 * @author MyCollab Ltd.
 * @since 2.0
 */
@ViewComponent
public class ProfileReadViewImpl extends AbstractPageView implements ProfileReadView, ImagePreviewCropWindow.ImageSelectionCommand {
    private static final long serialVersionUID = 1L;

    private final PreviewForm formItem;
    private final MHorizontalLayout avatarAndPass;

    public ProfileReadViewImpl() {
        super();
        this.setMargin(new MarginInfo(false, true, true, true));
        this.avatarAndPass = new MHorizontalLayout().withMargin(new MarginInfo(true, true, true, false)).withWidth("100%");

        this.formItem = new PreviewForm();
        this.formItem.setWidth("100%");
        this.addComponent(this.formItem);
    }

    private void displayUserAvatar() {
        avatarAndPass.removeAllComponents();
        Image cropField = UserAvatarControlFactory.createUserAvatarEmbeddedComponent(AppContext.getUserAvatarId(), 100);
        cropField.addStyleName(UIConstants.CIRCLE_BOX);
        CssLayout avatarWrapper = new CssLayout();
        avatarWrapper.addComponent(cropField);
        MVerticalLayout userAvatar = new MVerticalLayout().withMargin(false).with(avatarWrapper);
        userAvatar.setSizeUndefined();

        final UploadImageField avatarUploadField = new UploadImageField(this);
        avatarUploadField.setButtonCaption(AppContext.getMessage(UserI18nEnum.BUTTON_CHANGE_AVATAR));
        userAvatar.addComponent(avatarUploadField);

        avatarAndPass.with(userAvatar);

        User user = formItem.getBean();
        MVerticalLayout basicLayout = new MVerticalLayout().withMargin(false);
        HorizontalLayout userWrapper = new HorizontalLayout();

        ELabel usernameLbl = ELabel.h2(AppContext.getUser().getDisplayName());
        userWrapper.addComponent(usernameLbl);

        Button btnChangeBasicInfo = new Button(AppContext.getMessage(GenericI18Enum.BUTTON_EDIT), new Button.ClickListener() {
            private static final long serialVersionUID = 1L;

            @Override
            public void buttonClick(final ClickEvent event) {
                UI.getCurrent().addWindow(new BasicInfoChangeWindow(formItem.getBean()));
            }
        });
        btnChangeBasicInfo.setStyleName(UIConstants.BUTTON_LINK);

        HorizontalLayout btnChangeBasicInfoWrapper = new HorizontalLayout();
        btnChangeBasicInfoWrapper.addComponent(btnChangeBasicInfo);
        btnChangeBasicInfoWrapper.setComponentAlignment(btnChangeBasicInfo, Alignment.MIDDLE_RIGHT);
        userWrapper.addComponent(btnChangeBasicInfoWrapper);
        basicLayout.addComponent(userWrapper);
        basicLayout.setComponentAlignment(userWrapper, Alignment.MIDDLE_LEFT);

        GridFormLayoutHelper userFormLayout = GridFormLayoutHelper.defaultFormLayoutHelper(1, 5).withCaptionWidth("80px");
        userFormLayout.getLayout().addStyleName(UIConstants.GRIDFORM_BORDERLESS);
        userFormLayout.addComponent(new Label(AppContext.formatDate(user.getDateofbirth())),
                AppContext.getMessage(UserI18nEnum.FORM_BIRTHDAY), 0, 0);
        userFormLayout.addComponent(new Label(user.getEmail()), AppContext.getMessage(UserI18nEnum.FORM_EMAIL), 0, 1);
        userFormLayout.addComponent(new Label(TimezoneMapper.getTimezoneExt(user.getTimezone()).getDisplayName()),
                AppContext.getMessage(UserI18nEnum.FORM_TIMEZONE), 0, 2);
        userFormLayout.addComponent(new Label(LocalizationHelper.getLocaleInstance(user.getLanguage())
                        .getDisplayLanguage(AppContext.getUserLocale())),
                AppContext.getMessage(UserI18nEnum.FORM_LANGUAGE), 0, 3);

        Button btnChangePassword = new Button("Change", new Button.ClickListener() {
            private static final long serialVersionUID = 1L;

            @Override
            public void buttonClick(final ClickEvent event) {
                UI.getCurrent().addWindow(new PasswordChangeWindow(formItem.getBean()));
            }
        });
        btnChangePassword.setStyleName(UIConstants.BUTTON_LINK);
        userFormLayout.addComponent(new MHorizontalLayout(new Label("***********"), btnChangePassword),
                AppContext.getMessage(ShellI18nEnum.FORM_PASSWORD), 0, 4);
        basicLayout.addComponent(userFormLayout.getLayout());

        avatarAndPass.with(basicLayout).expand(basicLayout);
    }

    @Override
    public void process(BufferedImage image) {
        UserAvatarService userAvatarService = ApplicationContextUtil.getSpringBean(UserAvatarService.class);
        userAvatarService.uploadAvatar(image, AppContext.getUsername(), AppContext.getUserAvatarId());
        Page.getCurrent().getJavaScript().execute("window.location.reload();");
    }

    private class PreviewForm extends AdvancedPreviewBeanForm<User> {
        private static final long serialVersionUID = 1L;

        @Override
        public void setBean(final User newDataSource) {
            this.setFormLayoutFactory(new FormLayoutFactory());
            this.setBeanFormFieldFactory(new PreviewFormFieldFactory(PreviewForm.this));
            super.setBean(newDataSource);
        }

        private class FormLayoutFactory implements IFormLayoutFactory {
            private static final long serialVersionUID = 1L;

            private GridFormLayoutHelper contactLayout = GridFormLayoutHelper.defaultFormLayoutHelper(1, 5);
            private GridFormLayoutHelper advancedInfoLayout = GridFormLayoutHelper.defaultFormLayoutHelper(1, 3);

            @Override
            public ComponentContainer getLayout() {
                contactLayout.getLayout().setSpacing(true);
                advancedInfoLayout.getLayout().setSpacing(true);
                FormContainer layout = new FormContainer();
                layout.addComponent(avatarAndPass);

                MHorizontalLayout contactInformationHeader = new MHorizontalLayout();
                contactInformationHeader.setDefaultComponentAlignment(Alignment.MIDDLE_LEFT);
                Label contactInformationHeaderLbl = new Label(AppContext.getMessage(UserI18nEnum.SECTION_CONTACT_INFORMATION));

                Button btnChangeContactInfo = new Button(AppContext.getMessage(GenericI18Enum.BUTTON_EDIT), new Button.ClickListener() {
                    private static final long serialVersionUID = 1L;

                    @Override
                    public void buttonClick(final ClickEvent event) {
                        UI.getCurrent().addWindow(new ContactInfoChangeWindow(formItem.getBean()));
                    }
                });
                btnChangeContactInfo.addStyleName(UIConstants.BUTTON_LINK);
                contactInformationHeader.with(contactInformationHeaderLbl, btnChangeContactInfo).alignAll(Alignment.MIDDLE_LEFT);

                layout.addSection(new CssLayout(contactInformationHeader), contactLayout.getLayout());

                MHorizontalLayout advanceInfoHeader = new MHorizontalLayout();
                Label advanceInfoHeaderLbl = new Label(AppContext.getMessage(UserI18nEnum.SECTION_ADVANCED_INFORMATION));

                Button btnChangeAdvanceInfo = new Button(AppContext.getMessage(GenericI18Enum.BUTTON_EDIT), new Button.ClickListener() {
                    private static final long serialVersionUID = 1L;

                    @Override
                    public void buttonClick(final ClickEvent event) {
                        UI.getCurrent().addWindow(new AdvancedInfoChangeWindow(formItem.getBean()));
                    }
                });
                btnChangeAdvanceInfo.addStyleName(UIConstants.BUTTON_LINK);
                advanceInfoHeader.with(advanceInfoHeaderLbl, btnChangeAdvanceInfo);
                layout.addSection(new CssLayout(advanceInfoHeader), advancedInfoLayout.getLayout());
                return layout;
            }

            @Override
            public void attachField(Object propertyId, Field<?> field) {
                if (propertyId.equals("website")) {
                    advancedInfoLayout.addComponent(field, AppContext.getMessage(UserI18nEnum.FORM_WEBSITE), 0, 0);
                } else if (propertyId.equals("company")) {
                    advancedInfoLayout.addComponent(field, AppContext.getMessage(UserI18nEnum.FORM_COMPANY), 0, 1);
                } else if (propertyId.equals("country")) {
                    advancedInfoLayout.addComponent(field, AppContext.getMessage(UserI18nEnum.FORM_COUNTRY), 0, 2);
                } else if (propertyId.equals("workphone")) {
                    contactLayout.addComponent(field, AppContext.getMessage(UserI18nEnum.FORM_WORK_PHONE), 0, 0);
                } else if (propertyId.equals("homephone")) {
                    contactLayout.addComponent(field, AppContext.getMessage(UserI18nEnum.FORM_HOME_PHONE), 0, 1);
                } else if (propertyId.equals("facebookaccount")) {
                    contactLayout.addComponent(field, "Facebook", 0, 2);
                } else if (propertyId.equals("twitteraccount")) {
                    contactLayout.addComponent(field, "Twitter", 0, 3);
                } else if (propertyId.equals("skypecontact")) {
                    contactLayout.addComponent(field, "Skype", 0, 4);
                }
            }
        }

        private class PreviewFormFieldFactory extends AbstractBeanFieldGroupViewFieldFactory<User> {
            private static final long serialVersionUID = 1L;

            public PreviewFormFieldFactory(GenericBeanForm<User> form) {
                super(form);
            }

            @Override
            protected Field<?> onCreateField(final Object propertyId) {
                User user = formItem.getBean();
                if (propertyId.equals("website")) {
                    return new UrlLinkViewField(user.getWebsite());
                } else if (propertyId.equals("facebookaccount")) {
                    return new UrlSocialNetworkLinkViewField(user.getFacebookaccount(),
                            String.format("https://www.facebook.com/%s", user.getFacebookaccount()));
                } else if (propertyId.equals("twitteraccount")) {
                    return new UrlSocialNetworkLinkViewField(user.getTwitteraccount(),
                            String.format("https://www.twitter.com/%s", user.getTwitteraccount()));
                } else if (propertyId.equals("skypecontact")) {
                    return new UrlSocialNetworkLinkViewField(
                            user.getSkypecontact(), String.format("skype:%s?chat", user.getSkypecontact()));
                }
                return null;
            }
        }
    }

    @Override
    public void previewItem(User user) {
        this.formItem.setBean(user);
        this.displayUserAvatar();
    }

    @Override
    public User getItem() {
        return null;
    }
}

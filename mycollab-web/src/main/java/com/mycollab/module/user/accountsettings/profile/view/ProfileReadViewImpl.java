/**
 * Copyright © MyCollab
 * <p>
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * <p>
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 * <p>
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.mycollab.module.user.accountsettings.profile.view;

import com.hp.gagawa.java.elements.A;
import com.mycollab.common.i18n.GenericI18Enum;
import com.mycollab.common.i18n.ShellI18nEnum;
import com.mycollab.core.utils.TimezoneVal;
import com.mycollab.form.view.LayoutType;
import com.mycollab.i18n.LocalizationHelper;
import com.mycollab.module.file.service.UserAvatarService;
import com.mycollab.module.user.accountsettings.localization.UserI18nEnum;
import com.mycollab.module.user.domain.User;
import com.mycollab.spring.AppContextUtil;
import com.mycollab.vaadin.UserUIContext;
import com.mycollab.vaadin.Utils;
import com.mycollab.vaadin.mvp.AbstractVerticalPageView;
import com.mycollab.vaadin.mvp.ViewComponent;
import com.mycollab.vaadin.ui.*;
import com.mycollab.vaadin.ui.field.CountryViewField;
import com.mycollab.vaadin.ui.field.UrlLinkViewField;
import com.mycollab.vaadin.web.ui.AdvancedPreviewBeanForm;
import com.mycollab.vaadin.web.ui.ImagePreviewCropWindow;
import com.mycollab.vaadin.web.ui.UploadImageField;
import com.mycollab.vaadin.web.ui.WebThemes;
import com.mycollab.vaadin.web.ui.grid.GridFormLayoutHelper;
import com.vaadin.data.HasValue;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;
import org.vaadin.viritin.button.MButton;
import org.vaadin.viritin.layouts.MHorizontalLayout;
import org.vaadin.viritin.layouts.MVerticalLayout;

import java.awt.image.BufferedImage;

/**
 * @author MyCollab Ltd.
 * @since 2.0
 */
@ViewComponent
public class ProfileReadViewImpl extends AbstractVerticalPageView implements ProfileReadView, ImagePreviewCropWindow.ImageSelectionCommand {
    private static final long serialVersionUID = 1L;

    private final PreviewForm formItem;
    private final MHorizontalLayout avatarAndPass;

    public ProfileReadViewImpl() {
        this.setMargin(new MarginInfo(false, true, true, true));
        this.avatarAndPass = new MHorizontalLayout().withMargin(new MarginInfo(true, true, true, false)).withFullWidth();

        this.formItem = new PreviewForm();
        this.formItem.setWidth("100%");
        this.addComponent(this.formItem);
    }

    private void displayUserAvatar() {
        avatarAndPass.removeAllComponents();
        Image cropField = UserAvatarControlFactory.createUserAvatarEmbeddedComponent(UserUIContext.getUserAvatarId(), 100);
        cropField.addStyleName(WebThemes.CIRCLE_BOX);
        CssLayout avatarWrapper = new CssLayout();
        avatarWrapper.addComponent(cropField);
        MVerticalLayout userAvatar = new MVerticalLayout().withMargin(false).with(avatarWrapper);
        userAvatar.setSizeUndefined();

        UploadImageField avatarUploadField = new UploadImageField(this);
        avatarUploadField.setButtonCaption(UserUIContext.getMessage(UserI18nEnum.BUTTON_CHANGE_AVATAR));
        userAvatar.addComponent(avatarUploadField);

        avatarAndPass.with(userAvatar);

        User user = formItem.getBean();
        MVerticalLayout basicLayout = new MVerticalLayout().withMargin(false);

        ELabel usernameLbl = ELabel.h2(UserUIContext.getUser().getDisplayName()).withUndefinedWidth();

        MButton btnChangeBasicInfo = new MButton(UserUIContext.getMessage(GenericI18Enum.BUTTON_EDIT),
                clickEvent -> UI.getCurrent().addWindow(new BasicInfoChangeWindow(formItem.getBean())))
                .withStyleName(WebThemes.BUTTON_LINK);

        MHorizontalLayout userWrapper = new MHorizontalLayout(usernameLbl, btnChangeBasicInfo);
        basicLayout.addComponent(userWrapper);
        basicLayout.setComponentAlignment(userWrapper, Alignment.MIDDLE_LEFT);

        GridFormLayoutHelper userFormLayout = GridFormLayoutHelper.defaultFormLayoutHelper(LayoutType.ONE_COLUMN);
        userFormLayout.getLayout().addStyleName(WebThemes.GRIDFORM_BORDERLESS);
        userFormLayout.addComponent(new Label(UserUIContext.formatDate(user.getBirthday())),
                UserUIContext.getMessage(UserI18nEnum.FORM_BIRTHDAY), 0, 0);
        userFormLayout.addComponent(ELabel.html(new A("mailto:" + user.getEmail()).appendText(user.getEmail()).setTarget("_blank").write()),
                UserUIContext.getMessage(GenericI18Enum.FORM_EMAIL), 0, 1);
        userFormLayout.addComponent(new Label(TimezoneVal.getDisplayName(UserUIContext.getUserLocale(), user.getTimezone())),
                UserUIContext.getMessage(UserI18nEnum.FORM_TIMEZONE), 0, 2);
        userFormLayout.addComponent(new Label(LocalizationHelper.getLocaleInstance(user.getLanguage()).getDisplayLanguage(UserUIContext.getUserLocale())),
                UserUIContext.getMessage(UserI18nEnum.FORM_LANGUAGE), UserUIContext.getMessage(ShellI18nEnum.OPT_SUPPORTED_LANGUAGES_INTRO), 0, 3);

        MButton btnChangePassword = new MButton(UserUIContext.getMessage(GenericI18Enum.ACTION_CHANGE),
                clickEvent -> UI.getCurrent().addWindow(new PasswordChangeWindow(formItem.getBean())))
                .withStyleName(WebThemes.BUTTON_LINK);
        userFormLayout.addComponent(new CssLayout(new MHorizontalLayout(new ELabel("***********"), btnChangePassword).withAlign(btnChangePassword, Alignment.TOP_RIGHT)),
                UserUIContext.getMessage(ShellI18nEnum.FORM_PASSWORD), 0, 4);
        basicLayout.addComponent(userFormLayout.getLayout());

        avatarAndPass.with(basicLayout).expand(basicLayout);
    }

    @Override
    public void process(BufferedImage image) {
        UserAvatarService userAvatarService = AppContextUtil.getSpringBean(UserAvatarService.class);
        userAvatarService.uploadAvatar(image, UserUIContext.getUsername(), UserUIContext.getUserAvatarId());
        Utils.reloadPage();
    }

    private class PreviewForm extends AdvancedPreviewBeanForm<User> {
        private static final long serialVersionUID = 1L;

        @Override
        public void setBean(final User newDataSource) {
            this.setFormLayoutFactory(new FormLayoutFactory());
            this.setBeanFormFieldFactory(new PreviewFormFieldFactory(PreviewForm.this));
            super.setBean(newDataSource);
        }

        private class FormLayoutFactory extends AbstractFormLayoutFactory {

            private GridFormLayoutHelper contactLayout = GridFormLayoutHelper.defaultFormLayoutHelper(LayoutType.ONE_COLUMN);
            private GridFormLayoutHelper advancedInfoLayout = GridFormLayoutHelper.defaultFormLayoutHelper(LayoutType.ONE_COLUMN);

            @Override
            public AbstractComponent getLayout() {
                FormContainer layout = new FormContainer();
                layout.addComponent(avatarAndPass);

                MButton btnChangeContactInfo = new MButton(UserUIContext.getMessage(GenericI18Enum.BUTTON_EDIT),
                        clickEvent -> UI.getCurrent().addWindow(new ContactInfoChangeWindow(formItem.getBean())))
                        .withStyleName(WebThemes.BUTTON_LINK);

                MHorizontalLayout contactInformationHeader = new MHorizontalLayout(new ELabel(UserUIContext.getMessage(UserI18nEnum.SECTION_CONTACT_INFORMATION))
                        .withStyleName(ValoTheme.LABEL_H3, ValoTheme.LABEL_NO_MARGIN), btnChangeContactInfo).alignAll(Alignment.MIDDLE_LEFT);

                layout.addSection(new CssLayout(contactInformationHeader), contactLayout.getLayout());

                MButton btnChangeAdvanceInfo = new MButton(UserUIContext.getMessage(GenericI18Enum.BUTTON_EDIT),
                        clickEvent -> UI.getCurrent().addWindow(new AdvancedInfoChangeWindow(formItem.getBean())))
                        .withStyleName(WebThemes.BUTTON_LINK);

                MHorizontalLayout advanceInfoHeader = new MHorizontalLayout(new ELabel(UserUIContext.getMessage(UserI18nEnum.SECTION_ADVANCED_INFORMATION))
                        .withStyleName(ValoTheme.LABEL_H3, ValoTheme.LABEL_NO_MARGIN), btnChangeAdvanceInfo).alignAll(Alignment.MIDDLE_LEFT);

                layout.addSection(new CssLayout(advanceInfoHeader), advancedInfoLayout.getLayout());
                return layout;
            }

            @Override
            protected HasValue<?> onAttachField(Object propertyId, HasValue<?> field) {
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

        private class PreviewFormFieldFactory extends AbstractBeanFieldGroupViewFieldFactory<User> {
            private static final long serialVersionUID = 1L;

            PreviewFormFieldFactory(GenericBeanForm<User> form) {
                super(form);
            }

            @Override
            protected HasValue<?> onCreateField(final Object propertyId) {
                User user = formItem.getBean();
                if (propertyId.equals("website")) {
                    return new UrlLinkViewField(user.getWebsite());
                } else if (propertyId.equals("facebookaccount")) {
                    return new UrlLinkViewField(String.format("https://www.facebook.com/%s", user.getFacebookaccount()),
                            user.getFacebookaccount());
                } else if (propertyId.equals("twitteraccount")) {
                    return new UrlLinkViewField(String.format("https://www.twitter.com/%s", user.getTwitteraccount()),
                            user.getTwitteraccount());
                } else if (propertyId.equals("skypecontact")) {
                    return new UrlLinkViewField(String.format("skype:%s?chat", user.getSkypecontact()), user.getSkypecontact());
                } else if (User.Field.country.equalTo(propertyId)) {
                    return new CountryViewField();
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

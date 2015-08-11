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
import com.esofthead.mycollab.common.i18n.LangI18Enum;
import com.esofthead.mycollab.common.i18n.ShellI18nEnum;
import com.esofthead.mycollab.core.UserInvalidInputException;
import com.esofthead.mycollab.core.utils.ImageUtil;
import com.esofthead.mycollab.core.utils.TimezoneMapper;
import com.esofthead.mycollab.eventmanager.EventBusFactory;
import com.esofthead.mycollab.module.user.accountsettings.localization.UserI18nEnum;
import com.esofthead.mycollab.module.user.accountsettings.view.events.ProfileEvent;
import com.esofthead.mycollab.module.user.domain.User;
import com.esofthead.mycollab.vaadin.AppContext;
import com.esofthead.mycollab.vaadin.mvp.AbstractPageView;
import com.esofthead.mycollab.vaadin.mvp.ViewComponent;
import com.esofthead.mycollab.vaadin.ui.*;
import com.esofthead.mycollab.vaadin.ui.form.field.UrlLinkViewField;
import com.esofthead.mycollab.vaadin.ui.form.field.UrlSocialNetworkLinkViewField;
import com.esofthead.mycollab.vaadin.ui.grid.GridFormLayoutHelper;
import com.hp.gagawa.java.elements.Div;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.*;
import com.vaadin.ui.Button.ClickEvent;
import org.vaadin.easyuploads.UploadField;
import org.vaadin.easyuploads.UploadField.FieldType;
import org.vaadin.maddon.layouts.MHorizontalLayout;
import org.vaadin.maddon.layouts.MVerticalLayout;

/**
 * @author MyCollab Ltd.
 * @since 2.0
 */
@ViewComponent
public class ProfileReadViewImpl extends AbstractPageView implements ProfileReadView {
    private static final long serialVersionUID = 1L;

    private final PreviewForm formItem;
    private final MHorizontalLayout avatarAndPass;

    public ProfileReadViewImpl() {
        super();
        this.setMargin(new MarginInfo(false, true, true, true));
        this.addStyleName("userInfoContainer");
        this.avatarAndPass = new MHorizontalLayout().withMargin(new MarginInfo(true, true, true, false)).withWidth("100%");

        this.formItem = new PreviewForm();
        this.formItem.setWidth("100%");
        this.addComponent(this.formItem);
    }

    private void displayUserAvatar() {
        avatarAndPass.removeAllComponents();
        Image cropField = UserAvatarControlFactory.createUserAvatarEmbeddedComponent(AppContext.getUserAvatarId(), 100);
        CssLayout avatarWrapper = new CssLayout();
        avatarWrapper.addComponent(cropField);
        MVerticalLayout userAvatar = new MVerticalLayout().withMargin(false).with(avatarWrapper);
        userAvatar.setSizeUndefined();

        final UploadField avatarUploadField = new UploadField() {
            private static final long serialVersionUID = 1L;

            @Override
            protected void updateDisplay() {
                byte[] imageData = (byte[]) this.getValue();
                String mimeType = this.getLastMimeType();
                if (mimeType.equals("image/jpeg")) {
                    imageData = ImageUtil.convertJpgToPngFormat(imageData);
                    if (imageData == null) {
                        throw new UserInvalidInputException("Can not convert image to jpg format");
                    } else {
                        mimeType = "image/png";
                    }
                }

                if (mimeType.equals("image/png")) {
                    EventBusFactory.getInstance().post(new ProfileEvent.GotoUploadPhoto(
                            ProfileReadViewImpl.this, imageData));
                } else {
                    throw new UserInvalidInputException(
                            "Upload file does not have valid image format. The supported formats are jpg/png");
                }
            }
        };
        avatarUploadField.addStyleName("upload-field");
        avatarUploadField.setButtonCaption(AppContext.getMessage(UserI18nEnum.BUTTON_CHANGE_AVATAR));
        avatarUploadField.setSizeUndefined();
        avatarUploadField.setFieldType(FieldType.BYTE_ARRAY);
        userAvatar.addComponent(avatarUploadField);

        avatarAndPass.with(userAvatar);

        User user = formItem.getBean();

        MVerticalLayout basicLayout = new MVerticalLayout().withMargin(false);

        HorizontalLayout userWrapper = new HorizontalLayout();

        Label usernameLbl = new Label(AppContext.getUser().getDisplayName());
        usernameLbl.setStyleName("h1");
        userWrapper.addComponent(usernameLbl);

        Button btnChangeBasicInfo = new Button(AppContext.getMessage(GenericI18Enum.BUTTON_EDIT),
                new Button.ClickListener() {
                    private static final long serialVersionUID = 1L;

                    @Override
                    public void buttonClick(final ClickEvent event) {
                        UI.getCurrent().addWindow(new BasicInfoChangeWindow(formItem.getBean()));
                    }
                });
        btnChangeBasicInfo.setStyleName(UIConstants.THEME_LINK);

        HorizontalLayout btnChangeBasicInfoWrapper = new HorizontalLayout();
        btnChangeBasicInfoWrapper.setWidth("40px");
        btnChangeBasicInfoWrapper.addComponent(btnChangeBasicInfo);
        btnChangeBasicInfoWrapper.setComponentAlignment(btnChangeBasicInfo, Alignment.MIDDLE_RIGHT);
        userWrapper.addComponent(btnChangeBasicInfoWrapper);
        basicLayout.addComponent(userWrapper);
        basicLayout.setComponentAlignment(userWrapper, Alignment.MIDDLE_LEFT);

        basicLayout.addComponent(new Label(String.format("%s: %s", AppContext.getMessage(UserI18nEnum.FORM_BIRTHDAY),
                AppContext.formatDate(user.getDateofbirth()))));
        basicLayout.addComponent(new MHorizontalLayout(new Label(AppContext
                .getMessage(UserI18nEnum.FORM_EMAIL) + ": "), new LabelLink(
                user.getEmail(), String.format("mailto:%s", user.getEmail()))));
        basicLayout.addComponent(new Label(String.format("%s: %s", AppContext.getMessage(UserI18nEnum.FORM_TIMEZONE),
                TimezoneMapper.getTimezoneExt(user.getTimezone()).getDisplayName())));
        basicLayout.addComponent(new Label(String.format("%s: %s", AppContext.getMessage(UserI18nEnum.FORM_LANGUAGE),
                AppContext.getMessage(LangI18Enum.class, user.getLanguage()))));

        MHorizontalLayout passwordWrapper = new MHorizontalLayout();
        passwordWrapper.addComponent(new Label(AppContext
                .getMessage(ShellI18nEnum.FORM_PASSWORD) + ": ***********"));

        Button btnChangePassword = new Button("Change",
                new Button.ClickListener() {
                    private static final long serialVersionUID = 1L;

                    @Override
                    public void buttonClick(final ClickEvent event) {
                        UI.getCurrent().addWindow(new PasswordChangeWindow(formItem.getBean()));
                    }
                });
        btnChangePassword.setStyleName(UIConstants.THEME_LINK);
        passwordWrapper.with(btnChangePassword).withAlign(btnChangePassword, Alignment.MIDDLE_LEFT);
        basicLayout.with(passwordWrapper).withAlign(passwordWrapper, Alignment.MIDDLE_LEFT);

        avatarAndPass.with(basicLayout).expand(basicLayout);
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

            private GridFormLayoutHelper contactLayout = new GridFormLayoutHelper(1, 5, "100%", "120px");
            private GridFormLayoutHelper advancedInfoLayout = new GridFormLayoutHelper(1, 3, "100%", "120px");

            @Override
            public ComponentContainer getLayout() {
                contactLayout.getLayout().setSpacing(true);
                advancedInfoLayout.getLayout().setSpacing(true);
                MVerticalLayout layout = new MVerticalLayout().withMargin(false);
                layout.addComponent(avatarAndPass);

                String separatorStyle = "width: 100%; height: 1px; background-color: #CFCFCF; margin-top: 0px; margin-bottom: 10px";
                MHorizontalLayout contactInformationHeader = new MHorizontalLayout();
                contactInformationHeader.setDefaultComponentAlignment(Alignment.MIDDLE_LEFT);
                Label contactInformationHeaderLbl = new Label(AppContext.getMessage(UserI18nEnum.SECTION_CONTACT_INFORMATION));
                contactInformationHeaderLbl.addStyleName("h1");

                Button btnChangeContactInfo = new Button(AppContext.getMessage(GenericI18Enum.BUTTON_EDIT),
                        new Button.ClickListener() {
                            private static final long serialVersionUID = 1L;

                            @Override
                            public void buttonClick(final ClickEvent event) {
                                UI.getCurrent().addWindow(new ContactInfoChangeWindow(formItem.getBean()));
                            }
                        });
                btnChangeContactInfo.addStyleName(UIConstants.THEME_LINK);
                contactInformationHeader.with(contactInformationHeaderLbl, btnChangeContactInfo);

                layout.addComponent(contactInformationHeader);
                Div contactSeparator = new Div();
                contactSeparator.setAttribute("style", separatorStyle);
                layout.addComponent(new Label(contactSeparator.write(), ContentMode.HTML));

                layout.with(contactLayout.getLayout(), new Label());

                MHorizontalLayout advanceInfoHeader = new MHorizontalLayout();
                Label advanceInfoHeaderLbl = new Label(AppContext.getMessage(UserI18nEnum.SECTION_ADVANCED_INFORMATION));
                advanceInfoHeaderLbl.addStyleName("h1");
                advanceInfoHeader.addComponent(advanceInfoHeaderLbl);
                advanceInfoHeader.setComponentAlignment(advanceInfoHeaderLbl, Alignment.BOTTOM_LEFT);

                layout.addComponent(advanceInfoHeader);
                Div advanceSeparator = new Div();
                advanceSeparator.setAttribute("style", separatorStyle);
                layout.addComponent(new Label(advanceSeparator.write(), ContentMode.HTML));

                Button btnChangeAdvanceInfo = new Button(AppContext.getMessage(GenericI18Enum.BUTTON_EDIT),
                        new Button.ClickListener() {
                            private static final long serialVersionUID = 1L;

                            @Override
                            public void buttonClick(final ClickEvent event) {
                                UI.getCurrent().addWindow(new AdvancedInfoChangeWindow(formItem.getBean()));
                            }
                        });
                btnChangeAdvanceInfo.addStyleName(UIConstants.THEME_LINK);
                advanceInfoHeader.with(btnChangeAdvanceInfo).withAlign(btnChangeAdvanceInfo, Alignment.MIDDLE_LEFT);
                layout.addComponent(advancedInfoLayout.getLayout());
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

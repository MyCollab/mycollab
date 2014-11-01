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

import org.vaadin.easyuploads.UploadField;
import org.vaadin.easyuploads.UploadField.FieldType;
import org.vaadin.maddon.layouts.MHorizontalLayout;

import com.esofthead.mycollab.common.i18n.GenericI18Enum;
import com.esofthead.mycollab.common.i18n.LangI18Enum;
import com.esofthead.mycollab.common.i18n.ShellI18nEnum;
import com.esofthead.mycollab.core.UserInvalidInputException;
import com.esofthead.mycollab.core.utils.ImageUtil;
import com.esofthead.mycollab.core.utils.TimezoneMapper;
import com.esofthead.mycollab.eventmanager.EventBusFactory;
import com.esofthead.mycollab.module.project.LabelLink;
import com.esofthead.mycollab.module.user.accountsettings.localization.UserI18nEnum;
import com.esofthead.mycollab.module.user.accountsettings.view.events.ProfileEvent;
import com.esofthead.mycollab.module.user.domain.User;
import com.esofthead.mycollab.vaadin.AppContext;
import com.esofthead.mycollab.vaadin.mvp.AbstractPageView;
import com.esofthead.mycollab.vaadin.mvp.ViewComponent;
import com.esofthead.mycollab.vaadin.ui.AbstractBeanFieldGroupViewFieldFactory;
import com.esofthead.mycollab.vaadin.ui.AdvancedPreviewBeanForm;
import com.esofthead.mycollab.vaadin.ui.GenericBeanForm;
import com.esofthead.mycollab.vaadin.ui.IFormLayoutFactory;
import com.esofthead.mycollab.vaadin.ui.UserAvatarControlFactory;
import com.esofthead.mycollab.vaadin.ui.form.field.UrlLinkViewField;
import com.esofthead.mycollab.vaadin.ui.form.field.UrlSocialNetworkLinkViewField;
import com.hp.gagawa.java.elements.Div;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.Field;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Image;
import com.vaadin.ui.Label;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

/**
 * 
 * @author MyCollab Ltd.
 * @since 2.0
 * 
 */
@ViewComponent
public class ProfileReadViewImpl extends AbstractPageView implements
		ProfileReadView {
	private static final long serialVersionUID = 1L;

	public static final int MAX_UPLOAD_SIZE = 20 * 1024 * 1024;

	private final PreviewForm formItem;
	private final VerticalLayout userAvatar;
	private final HorizontalLayout avatarAndPass;

	public ProfileReadViewImpl() {
		super();
		this.setMargin(new MarginInfo(false, true, true, true));
		this.addStyleName("userInfoContainer");
		this.userAvatar = new VerticalLayout();
		this.userAvatar.setWidthUndefined();
		this.userAvatar.setDefaultComponentAlignment(Alignment.TOP_LEFT);
		this.avatarAndPass = new HorizontalLayout();

		this.formItem = new PreviewForm();
		this.formItem.setWidth("100%");
		this.addComponent(this.formItem);
	}

	private void displayUserAvatar() {
		this.userAvatar.removeAllComponents();
		final Image cropField = UserAvatarControlFactory
				.createUserAvatarEmbeddedComponent(
						AppContext.getUserAvatarId(), 100);
		userAvatar.addComponent(cropField);

		this.avatarAndPass.removeAllComponents();
		avatarAndPass.setSpacing(true);
		avatarAndPass.setMargin(new MarginInfo(true, true, true, false));
		avatarAndPass.setWidth("100%");
		avatarAndPass.addComponent(userAvatar);

		User user = formItem.getUser();

		final VerticalLayout basicLayout = new VerticalLayout();
		basicLayout.setSpacing(true);
		final HorizontalLayout userWrapper = new HorizontalLayout();

		final Label userName = new Label(AppContext.getSession()
				.getDisplayName());
		userName.setStyleName("h1");
		userWrapper.addComponent(userName);

		final Button btnChangeBasicInfo = new Button(
				AppContext.getMessage(GenericI18Enum.BUTTON_EDIT),
				new Button.ClickListener() {
					private static final long serialVersionUID = 1L;

					@Override
					public void buttonClick(final ClickEvent event) {
						UI.getCurrent().addWindow(
								new BasicInfoChangeWindow(formItem.getUser()));
					}
				});
		btnChangeBasicInfo.setStyleName("link");
		HorizontalLayout btnChangeBasicInfoWrapper = new HorizontalLayout();
		btnChangeBasicInfoWrapper.setWidth("40px");
		btnChangeBasicInfoWrapper.addComponent(btnChangeBasicInfo);
		btnChangeBasicInfoWrapper.setComponentAlignment(btnChangeBasicInfo,
				Alignment.MIDDLE_RIGHT);
		userWrapper.addComponent(btnChangeBasicInfoWrapper);
		basicLayout.addComponent(userWrapper);
		basicLayout.setComponentAlignment(userWrapper, Alignment.MIDDLE_LEFT);

		basicLayout.addComponent(new Label(AppContext
				.getMessage(UserI18nEnum.FORM_BIRTHDAY)
				+ ": "
				+ AppContext.formatDate(user.getDateofbirth())));
		basicLayout.addComponent(new MHorizontalLayout(new Label(AppContext
				.getMessage(UserI18nEnum.FORM_EMAIL) + ": "), new LabelLink(
				user.getEmail(), "mailto:" + user.getEmail())));
		basicLayout.addComponent(new Label(AppContext
				.getMessage(UserI18nEnum.FORM_TIMEZONE)
				+ ": "
				+ TimezoneMapper.getTimezone(user.getTimezone())
						.getDisplayName()));
		basicLayout
				.addComponent(new Label(AppContext
						.getMessage(UserI18nEnum.FORM_LANGUAGE)
						+ ": "
						+ AppContext.getMessage(LangI18Enum.class,
								user.getLanguage())));

		HorizontalLayout passwordWrapper = new HorizontalLayout();
		passwordWrapper.addComponent(new Label(AppContext
				.getMessage(ShellI18nEnum.FORM_PASSWORD) + ": ***********"));

		final Button btnChangePassword = new Button("Change",
				new Button.ClickListener() {
					private static final long serialVersionUID = 1L;

					@Override
					public void buttonClick(final ClickEvent event) {
						UI.getCurrent().addWindow(
								new PasswordChangeWindow(formItem.user));
					}
				});
		btnChangePassword.setStyleName("link");
		HorizontalLayout btnChangePasswordWrapper = new HorizontalLayout();
		btnChangePasswordWrapper.setWidth("50px");
		btnChangePasswordWrapper.addComponent(btnChangePassword);
		btnChangePasswordWrapper.setComponentAlignment(btnChangePassword,
				Alignment.MIDDLE_RIGHT);
		passwordWrapper.addComponent(btnChangePasswordWrapper);
		basicLayout.addComponent(passwordWrapper);
		basicLayout
				.setComponentAlignment(passwordWrapper, Alignment.MIDDLE_LEFT);

		avatarAndPass.addComponent(basicLayout);
		avatarAndPass.setComponentAlignment(basicLayout, Alignment.TOP_LEFT);
		avatarAndPass.setExpandRatio(basicLayout, 1.0f);

		final UploadField avatarUploadField = new UploadField() {
			private static final long serialVersionUID = 1L;

			@Override
			protected void updateDisplay() {
				byte[] imageData = (byte[]) this.getValue();
				String mimeType = this.getLastMimeType();
				if (mimeType.equals("image/jpeg")) {
					imageData = ImageUtil.convertJpgToPngFormat(imageData);
					if (imageData == null) {
						throw new UserInvalidInputException(
								"Do not support image format for avatar");
					} else {
						mimeType = "image/png";
					}
				}

				if (mimeType.equals("image/png")) {
					EventBusFactory.getInstance().post(
							new ProfileEvent.GotoUploadPhoto(
									ProfileReadViewImpl.this, imageData));
				} else {
					throw new UserInvalidInputException(
							"Upload file does not have valid image format. The supported formats are jpg/png");
				}
			}
		};
		avatarUploadField.setButtonCaption(AppContext
				.getMessage(UserI18nEnum.BUTTON_CHANGE_AVATAR));
		avatarUploadField.setSizeUndefined();
		avatarUploadField.setFieldType(FieldType.BYTE_ARRAY);
		this.userAvatar.addComponent(avatarUploadField);
	}

	private class PreviewForm extends AdvancedPreviewBeanForm<User> {

		private static final long serialVersionUID = 1L;

		private User user;

		public void setUser(final User user) {
			this.user = user;
		}

		public User getUser() {
			return user;
		}

		@Override
		public void setBean(final User newDataSource) {
			this.setFormLayoutFactory(new FormLayoutFactory());
			this.setBeanFormFieldFactory(new PreviewFormFieldFactory(
					PreviewForm.this));
			super.setBean(newDataSource);
		}

		private class FormLayoutFactory implements IFormLayoutFactory {
			private static final long serialVersionUID = 1L;

			protected VerticalLayout contactInformation = new VerticalLayout();;
			protected VerticalLayout contactInformationTitle = new VerticalLayout();;

			protected VerticalLayout advanceInformation = new VerticalLayout();;
			protected VerticalLayout advanceInformationTitle = new VerticalLayout();;

			@Override
			public ComponentContainer getLayout() {
				final VerticalLayout layout = new VerticalLayout();
				layout.addComponent(avatarAndPass);

				contactInformationTitle.setWidth("250px");
				advanceInformationTitle.setWidth("250px");
				
				contactInformationTitle.setSpacing(true);
				advanceInformationTitle.setSpacing(true);
				
				contactInformation.setSpacing(true);
				advanceInformation.setSpacing(true);

				final HorizontalLayout contactInformationHeader = new HorizontalLayout();
				final Label contactInformationHeaderLbl = new Label(
						AppContext
								.getMessage(UserI18nEnum.SECTION_CONTACT_INFORMATION));
				contactInformationHeaderLbl.addStyleName("h1");
				contactInformationHeader.setHeight("50px");
				contactInformationHeader
						.addComponent(contactInformationHeaderLbl);
				contactInformationHeader.setComponentAlignment(
						contactInformationHeaderLbl, Alignment.BOTTOM_LEFT);

				final HorizontalLayout advanceInfoHeader = new HorizontalLayout();
				final Label advanceInfoHeaderLbl = new Label(
						AppContext
								.getMessage(UserI18nEnum.SECTION_ADVANCED_INFORMATION));
				advanceInfoHeaderLbl.addStyleName("h1");
				advanceInfoHeader.setHeight("50px");
				advanceInfoHeader.addComponent(advanceInfoHeaderLbl);
				advanceInfoHeader.setComponentAlignment(advanceInfoHeaderLbl,
						Alignment.BOTTOM_LEFT);

				String separatorStyle = "width: 100%; height: 1px; background-color: #CFCFCF; margin-top: 3px; margin-bottom: 10px";

				layout.addComponent(contactInformationHeader);
				Div contactSeparator = new Div();
				contactSeparator.setAttribute("style", separatorStyle);
				layout.addComponent(new Label(contactSeparator.write(),
						ContentMode.HTML));
				HorizontalLayout contactInformationWrapper = new HorizontalLayout();
				contactInformationWrapper.addComponent(contactInformationTitle);
				contactInformationWrapper.addComponent(contactInformation);
				layout.addComponent(contactInformationWrapper);

				final HorizontalLayout btnChangeContactInfoWrapper = new HorizontalLayout();
				btnChangeContactInfoWrapper.setWidth("40px");
				btnChangeContactInfoWrapper.setHeight("100%");
				final Button btnChangeContactInfo = new Button(
						AppContext.getMessage(GenericI18Enum.BUTTON_EDIT),
						new Button.ClickListener() {
							private static final long serialVersionUID = 1L;

							@Override
							public void buttonClick(final ClickEvent event) {
								UI.getCurrent().addWindow(
										new ContactInfoChangeWindow(
												PreviewForm.this.user));
							}
						});
				btnChangeContactInfo.addStyleName("link");
				btnChangeContactInfoWrapper.addComponent(btnChangeContactInfo);
				btnChangeContactInfoWrapper.setComponentAlignment(
						btnChangeContactInfo, Alignment.BOTTOM_RIGHT);
				contactInformationHeader
						.addComponent(btnChangeContactInfoWrapper);

				layout.addComponent(advanceInfoHeader);
				Div advanceSeparator = new Div();
				advanceSeparator.setAttribute("style", separatorStyle);
				layout.addComponent(new Label(advanceSeparator.write(),
						ContentMode.HTML));
				HorizontalLayout advancedInformationWrapper = new HorizontalLayout();
				advancedInformationWrapper
						.addComponent(advanceInformationTitle);
				advancedInformationWrapper.addComponent(advanceInformation);
				layout.addComponent(advancedInformationWrapper);

				final HorizontalLayout btnChangeAdvanceInfoWrapper = new HorizontalLayout();
				btnChangeAdvanceInfoWrapper.setWidth("40px");
				btnChangeAdvanceInfoWrapper.setHeight("100%");
				final Button btnChangeAdvanceInfo = new Button(
						AppContext.getMessage(GenericI18Enum.BUTTON_EDIT),
						new Button.ClickListener() {
							private static final long serialVersionUID = 1L;

							@Override
							public void buttonClick(final ClickEvent event) {
								UI.getCurrent().addWindow(
										new AdvancedInfoChangeWindow(
												PreviewForm.this.user));
							}
						});
				btnChangeAdvanceInfo.addStyleName("link");
				btnChangeAdvanceInfoWrapper.addComponent(btnChangeAdvanceInfo);
				btnChangeAdvanceInfoWrapper.setComponentAlignment(
						btnChangeAdvanceInfo, Alignment.BOTTOM_RIGHT);
				advanceInfoHeader.addComponent(btnChangeAdvanceInfoWrapper);
				return layout;
			}

			@Override
			public void attachField(final Object propertyId,
					final Field<?> field) {
				if (propertyId.equals("website")) {
					this.advanceInformationTitle.addComponent(new Label(
							AppContext.getMessage(UserI18nEnum.FORM_WEBSITE)));
					this.advanceInformation.addComponent(field);
				} else if (propertyId.equals("company")) {
					this.advanceInformationTitle.addComponent(new Label(
							AppContext.getMessage(UserI18nEnum.FORM_COMPANY)));
					this.advanceInformation.addComponent(field);
				} else if (propertyId.equals("country")) {
					this.advanceInformationTitle.addComponent(new Label(
							AppContext.getMessage(UserI18nEnum.FORM_COUNTRY)));
					this.advanceInformation.addComponent(field);
				} else if (propertyId.equals("workphone")) {
					this.contactInformationTitle
							.addComponent(new Label(AppContext
									.getMessage(UserI18nEnum.FORM_WORK_PHONE)));
					this.contactInformation.addComponent(field);
				} else if (propertyId.equals("homephone")) {
					this.contactInformationTitle
							.addComponent(new Label(AppContext
									.getMessage(UserI18nEnum.FORM_HOME_PHONE)));
					this.contactInformation.addComponent(field);
				} else if (propertyId.equals("facebookaccount")) {
					this.contactInformationTitle.addComponent(new Label(
							"Facebook"));
					this.contactInformation.addComponent(field);
				} else if (propertyId.equals("twitteraccount")) {
					this.contactInformationTitle.addComponent(new Label(
							"Twitter"));
					this.contactInformation.addComponent(field);
				} else if (propertyId.equals("skypecontact")) {
					this.contactInformationTitle
							.addComponent(new Label("Skype"));
					this.contactInformation.addComponent(field);
				}
			}
		}

		private class PreviewFormFieldFactory extends
				AbstractBeanFieldGroupViewFieldFactory<User> {
			private static final long serialVersionUID = 1L;

			public PreviewFormFieldFactory(GenericBeanForm<User> form) {
				super(form);
			}

			@Override
			protected Field<?> onCreateField(final Object propertyId) {
				String value = "";

				if (propertyId.equals("website")) {
					value = PreviewForm.this.user.getWebsite();
					return new UrlLinkViewField(value);
				} else if (propertyId.equals("facebookaccount")) {
					return new UrlSocialNetworkLinkViewField(
							PreviewForm.this.user.getFacebookaccount(),
							"https://www.facebook.com/"
									+ PreviewForm.this.user
											.getFacebookaccount());
				} else if (propertyId.equals("twitteraccount")) {
					return new UrlSocialNetworkLinkViewField(
							PreviewForm.this.user.getTwitteraccount(),
							"https://www.twitter.com/"
									+ PreviewForm.this.user.getTwitteraccount());
				} else if (propertyId.equals("skypecontact")) {
					return new UrlSocialNetworkLinkViewField(
							PreviewForm.this.user.getSkypecontact(), "skype:"
									+ PreviewForm.this.user.getSkypecontact()
									+ "?chat");
				}
				return null;
			}
		}
	}

	@Override
	public void previewItem(final User user) {
		this.formItem.setUser(user);
		this.formItem.setBean(user);
		this.displayUserAvatar();
	}

	@Override
	public User getItem() {
		return null;
	}
}

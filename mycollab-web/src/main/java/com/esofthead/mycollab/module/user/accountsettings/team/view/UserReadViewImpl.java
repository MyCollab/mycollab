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

import org.apache.commons.lang3.StringUtils;
import org.vaadin.maddon.layouts.MHorizontalLayout;

import com.esofthead.mycollab.common.i18n.LangI18Enum;
import com.esofthead.mycollab.core.utils.TimezoneMapper;
import com.esofthead.mycollab.module.user.AccountLinkBuilder;
import com.esofthead.mycollab.module.user.accountsettings.localization.UserI18nEnum;
import com.esofthead.mycollab.module.user.domain.SimpleUser;
import com.esofthead.mycollab.module.user.domain.User;
import com.esofthead.mycollab.security.RolePermissionCollections;
import com.esofthead.mycollab.vaadin.AppContext;
import com.esofthead.mycollab.vaadin.events.HasPreviewFormHandlers;
import com.esofthead.mycollab.vaadin.mvp.AbstractPageView;
import com.esofthead.mycollab.vaadin.mvp.ViewComponent;
import com.esofthead.mycollab.vaadin.ui.AbstractBeanFieldGroupViewFieldFactory;
import com.esofthead.mycollab.vaadin.ui.AdvancedPreviewBeanForm;
import com.esofthead.mycollab.vaadin.ui.IFormLayoutFactory;
import com.esofthead.mycollab.vaadin.ui.LabelLink;
import com.esofthead.mycollab.vaadin.ui.PreviewFormControlsGenerator;
import com.esofthead.mycollab.vaadin.ui.UIConstants;
import com.esofthead.mycollab.vaadin.ui.UserAvatarControlFactory;
import com.esofthead.mycollab.vaadin.ui.form.field.DefaultViewField;
import com.esofthead.mycollab.vaadin.ui.form.field.EmailViewField;
import com.esofthead.mycollab.vaadin.ui.form.field.LinkViewField;
import com.esofthead.mycollab.vaadin.ui.form.field.UrlLinkViewField;
import com.esofthead.mycollab.vaadin.ui.form.field.UrlSocialNetworkLinkViewField;
import com.hp.gagawa.java.elements.Div;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Component;
import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.Field;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Image;
import com.vaadin.ui.Label;
import com.vaadin.ui.Layout;
import com.vaadin.ui.VerticalLayout;

/**
 * 
 * @author MyCollab Ltd.
 * @since 1.0
 */
@ViewComponent
public class UserReadViewImpl extends AbstractPageView implements UserReadView {

	private static final long serialVersionUID = 1L;
	protected AdvancedPreviewBeanForm<User> previewForm;
	private final VerticalLayout userAvatar;
	private final HorizontalLayout avatarAndPass;
	protected SimpleUser user;

	public UserReadViewImpl() {
		super();
		this.setMargin(new MarginInfo(false, true, true, true));
		this.addStyleName("userInfoContainer");
		this.userAvatar = new VerticalLayout();
		this.userAvatar.setWidthUndefined();
		this.userAvatar.setDefaultComponentAlignment(Alignment.TOP_LEFT);
		this.avatarAndPass = new HorizontalLayout();

		this.setMargin(new MarginInfo(false, true, false, true));

		HorizontalLayout header = new HorizontalLayout();
		header.addComponent(avatarAndPass);
		header.setWidth("100%");
		header.setStyleName(UIConstants.HEADER_VIEW);
		header.setDefaultComponentAlignment(Alignment.MIDDLE_LEFT);
		header.setSpacing(true);
		header.setMargin(new MarginInfo(true, false, true, false));
		this.addComponent(header);

		previewForm = new PreviewForm();
		this.addComponent(previewForm);

		Layout controlButtons = createTopPanel();
		if (controlButtons != null) {
			header.addComponent(controlButtons);
			header.setComponentAlignment(controlButtons, Alignment.TOP_RIGHT);
		}
	}

	private void displayUserAvatar() {
		this.userAvatar.removeAllComponents();
		final Image cropField = UserAvatarControlFactory
				.createUserAvatarEmbeddedComponent(user.getAvatarid(), 100);
		userAvatar.addComponent(cropField);

		this.avatarAndPass.removeAllComponents();
		avatarAndPass.setSpacing(true);
		avatarAndPass.setMargin(new MarginInfo(true, true, true, false));
		avatarAndPass.setWidth("100%");
		avatarAndPass.addComponent(userAvatar);

		final VerticalLayout basicLayout = new VerticalLayout();
		basicLayout.setSpacing(true);
		final HorizontalLayout userWrapper = new HorizontalLayout();

		String nickName = user.getNickname();

		final Label userName = new Label(user.getDisplayName()
				+ (StringUtils.isEmpty(nickName) ? ""
						: (" ( " + nickName + " )")));
		userName.setStyleName("h1");
		userWrapper.addComponent(userName);

		basicLayout.addComponent(userWrapper);
		basicLayout.setComponentAlignment(userWrapper, Alignment.MIDDLE_LEFT);

		Component role;
		if (user.getIsAccountOwner() != null
				&& user.getIsAccountOwner() == Boolean.TRUE) {
			role = new DefaultViewField("Account Owner");
		} else {
			role = new LinkViewField(user.getRoleName(),
					AccountLinkBuilder.generatePreviewFullRoleLink(user
							.getRoleid()));
		}
		HorizontalLayout roleWrapper = new HorizontalLayout();
		roleWrapper.addComponent(new Label(AppContext
				.getMessage(UserI18nEnum.FORM_ROLE) + ": "));
		roleWrapper.addComponent(role);

		basicLayout.addComponent(roleWrapper);

		basicLayout.addComponent(new Label(AppContext
				.getMessage(UserI18nEnum.FORM_BIRTHDAY)
				+ ": "
				+ AppContext.formatDate(user.getDateofbirth())));
		// basicLayout.addComponent(new Label(AppContext
		// .getMessageClsEnum(UserI18nEnum.FORM_EMAIL) + ": " + user.getEmail()));
		basicLayout.addComponent(new MHorizontalLayout()
				.add(new Label(AppContext.getMessage(UserI18nEnum.FORM_EMAIL)
						+ ": ")).add(
						new LabelLink(user.getEmail(), "mailto:"
								+ user.getEmail())));
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

		avatarAndPass.addComponent(basicLayout);
		avatarAndPass.setComponentAlignment(basicLayout, Alignment.TOP_LEFT);
		avatarAndPass.setExpandRatio(basicLayout, 1.0f);
	}

	protected Layout createTopPanel() {
		PreviewFormControlsGenerator<User> previewForm = new PreviewFormControlsGenerator<User>(
				this.previewForm);
		previewForm
				.createButtonControls(RolePermissionCollections.ACCOUNT_USER);
		previewForm.removeCloneButton();
		return previewForm.getLayout();
	}

	@Override
	public void previewItem(SimpleUser user) {
		this.user = user;
		previewForm.setBean(user);
		this.displayUserAvatar();
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
			this.setBeanFormFieldFactory(new AbstractBeanFieldGroupViewFieldFactory<User>(
					PreviewForm.this) {
				private static final long serialVersionUID = 1L;

				@Override
				protected Field<?> onCreateField(Object propertyId) {

					if (propertyId.equals("email")) {
						return new EmailViewField(user.getEmail());
					} else if (propertyId.equals("roleid")) {
						if (user.getIsAccountOwner() != null
								&& user.getIsAccountOwner() == Boolean.TRUE) {
							return new DefaultViewField("Account Owner");
						} else {
							LinkViewField roleLink = new LinkViewField(user
									.getRoleName(), AccountLinkBuilder
									.generatePreviewFullRoleLink(user
											.getRoleid()));
							return roleLink;
						}
					} else if (propertyId.equals("website")) {
						return new UrlLinkViewField(user.getWebsite());
					} else if (propertyId.equals("dateofbirth")) {
						return new DefaultViewField(AppContext.formatDate(user
								.getDateofbirth()));
					} else if (propertyId.equals("timezone")) {
						return new DefaultViewField(TimezoneMapper.getTimezone(
								user.getTimezone()).getDisplayName());
					} else if (propertyId.equals("facebookaccount")) {
						return new UrlSocialNetworkLinkViewField(user
								.getFacebookaccount(),
								"https://www.facebook.com/"
										+ user.getFacebookaccount());
					} else if (propertyId.equals("twitteraccount")) {
						return new UrlSocialNetworkLinkViewField(user
								.getTwitteraccount(),
								"https://www.twitter.com/"
										+ user.getTwitteraccount());
					} else if (propertyId.equals("skypecontact")) {
						return new UrlSocialNetworkLinkViewField(user
								.getSkypecontact(), "skype:"
								+ user.getSkypecontact() + "?chat");
					}
					return null;
				}
			});
			super.setBean(newDataSource);
		}

		@Override
		public void showHistory() {
			// TODO: show user edit history
		}

		private class FormLayoutFactory implements IFormLayoutFactory {
			private static final long serialVersionUID = 1L;

			protected VerticalLayout contactInformation = new VerticalLayout();
			protected VerticalLayout contactInformationTitle = new VerticalLayout();

			protected VerticalLayout advanceInformation = new VerticalLayout();
			protected VerticalLayout advanceInformationTitle = new VerticalLayout();

			@Override
			public ComponentContainer getLayout() {
				final VerticalLayout layout = new VerticalLayout();

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

				layout.addComponent(advanceInfoHeader);
				Div advancSeparator = new Div();
				advancSeparator.setAttribute("style", separatorStyle);
				layout.addComponent(new Label(advancSeparator.write(),
						ContentMode.HTML));
				HorizontalLayout advancedInformationWrapper = new HorizontalLayout();
				advancedInformationWrapper
						.addComponent(advanceInformationTitle);
				advancedInformationWrapper.addComponent(advanceInformation);
				layout.addComponent(advancedInformationWrapper);

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
	}

	@Override
	public SimpleUser getItem() {
		return user;
	}
}

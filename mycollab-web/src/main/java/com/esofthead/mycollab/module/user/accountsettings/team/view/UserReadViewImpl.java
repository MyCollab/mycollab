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

import com.esofthead.mycollab.core.utils.TimezoneMapper;
import com.esofthead.mycollab.module.user.AccountLinkBuilder;
import com.esofthead.mycollab.module.user.accountsettings.profile.view.ProfileFormLayoutFactory;
import com.esofthead.mycollab.module.user.domain.SimpleUser;
import com.esofthead.mycollab.module.user.domain.User;
import com.esofthead.mycollab.security.RolePermissionCollections;
import com.esofthead.mycollab.vaadin.AppContext;
import com.esofthead.mycollab.vaadin.events.HasPreviewFormHandlers;
import com.esofthead.mycollab.vaadin.mvp.AbstractPageView;
import com.esofthead.mycollab.vaadin.mvp.ViewComponent;
import com.esofthead.mycollab.vaadin.ui.AbstractBeanFieldGroupViewFieldFactory;
import com.esofthead.mycollab.vaadin.ui.AdvancedPreviewBeanForm;
import com.esofthead.mycollab.vaadin.ui.MyCollabResource;
import com.esofthead.mycollab.vaadin.ui.PreviewFormControlsGenerator;
import com.esofthead.mycollab.vaadin.ui.UIConstants;
import com.esofthead.mycollab.vaadin.ui.form.field.EmailViewField;
import com.esofthead.mycollab.vaadin.ui.form.field.LinkViewField;
import com.esofthead.mycollab.vaadin.ui.form.field.UrlLinkViewField;
import com.esofthead.mycollab.vaadin.ui.form.field.UrlSocialNetworkLinkViewField;
import com.esofthead.mycollab.vaadin.ui.form.field.DefaultViewField;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.Alignment;
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
	protected SimpleUser user;

	public UserReadViewImpl() {
		super();

		this.setMargin(new MarginInfo(false, true, false, true));

		HorizontalLayout header = new HorizontalLayout();
		header.setWidth("100%");
		header.setStyleName(UIConstants.HEADER_VIEW);
		header.setDefaultComponentAlignment(Alignment.MIDDLE_LEFT);
		header.setSpacing(true);
		header.setMargin(new MarginInfo(true, false, true, false));
		header.addComponent(new Image(null, MyCollabResource
				.newResource("icons/24/project/user.png")));

		Label headerText = new Label("Detail User");
		headerText.setSizeUndefined();
		headerText.setStyleName(UIConstants.HEADER_TEXT);

		header.addComponent(headerText);
		header.setExpandRatio(headerText, 1.0f);

		this.addComponent(header);

		previewForm = new PreviewForm();
		this.addComponent(previewForm);

		Layout controlButtons = createTopPanel();
		if (controlButtons != null) {
			header.addComponent(controlButtons);
		}
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
							LinkViewField roleLink = new LinkViewField(
									user.getRoleName(), AccountLinkBuilder
											.generatePreviewFullRoleLink(user
													.getRoleid()));
							return roleLink;
						}
					} else if (propertyId.equals("website")) {
						return new UrlLinkViewField(
								user.getWebsite());
					} else if (propertyId.equals("dateofbirth")) {
						return new DefaultViewField(
								AppContext.formatDate(user.getDateofbirth()));
					} else if (propertyId.equals("timezone")) {
						return new DefaultViewField(
								TimezoneMapper.getTimezone(user.getTimezone())
										.getDisplayName());
					} else if (propertyId.equals("facebookaccount")) {
						return new UrlSocialNetworkLinkViewField(
								user.getFacebookaccount(),
								"https://www.facebook.com/"
										+ user.getFacebookaccount());
					} else if (propertyId.equals("twitteraccount")) {
						return new UrlSocialNetworkLinkViewField(
								user.getTwitteraccount(),
								"https://www.twitter.com/"
										+ user.getTwitteraccount());
					} else if (propertyId.equals("skypecontact")) {
						return new UrlSocialNetworkLinkViewField(
								user.getSkypecontact(), "skype:"
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

		class FormLayoutFactory extends ProfileFormLayoutFactory {

			private static final long serialVersionUID = 1L;

			public FormLayoutFactory() {
				super(user.getDisplayName());
				this.setAvatarLink(user.getAvatarid());
			}

			@Override
			protected Layout createBottomPanel() {
				VerticalLayout relatedItemsPanel = new VerticalLayout();

				return relatedItemsPanel;
			}
		}
	}

	@Override
	public SimpleUser getItem() {
		return user;
	}
}

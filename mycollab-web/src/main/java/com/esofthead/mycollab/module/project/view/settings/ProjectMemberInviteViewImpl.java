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

package com.esofthead.mycollab.module.project.view.settings;

import java.util.ArrayList;
import java.util.List;

import org.vaadin.tokenfield.TokenField;

import com.esofthead.mycollab.common.i18n.GenericI18Enum;
import com.esofthead.mycollab.core.MyCollabException;
import com.esofthead.mycollab.core.utils.EmailValidator;
import com.esofthead.mycollab.eventmanager.EventBus;
import com.esofthead.mycollab.module.project.CurrentProjectVariables;
import com.esofthead.mycollab.module.project.events.ProjectMemberEvent;
import com.esofthead.mycollab.module.project.i18n.ProjectMemberI18nEnum;
import com.esofthead.mycollab.module.project.service.ProjectMemberService;
import com.esofthead.mycollab.module.project.view.settings.component.ProjectRoleComboBox;
import com.esofthead.mycollab.module.user.domain.SimpleUser;
import com.esofthead.mycollab.spring.ApplicationContextUtil;
import com.esofthead.mycollab.vaadin.AppContext;
import com.esofthead.mycollab.vaadin.mvp.AbstractPageView;
import com.esofthead.mycollab.vaadin.mvp.HistoryViewManager;
import com.esofthead.mycollab.vaadin.mvp.NullViewState;
import com.esofthead.mycollab.vaadin.mvp.ViewComponent;
import com.esofthead.mycollab.vaadin.mvp.ViewState;
import com.esofthead.mycollab.vaadin.ui.AddViewLayout;
import com.esofthead.mycollab.vaadin.ui.GridFormLayoutHelper;
import com.esofthead.mycollab.vaadin.ui.MyCollabResource;
import com.esofthead.mycollab.vaadin.ui.NotificationUtil;
import com.esofthead.mycollab.vaadin.ui.UIConstants;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.shared.ui.combobox.FilteringMode;
import com.vaadin.ui.AbstractSelect.ItemCaptionMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Layout;
import com.vaadin.ui.TextArea;

/**
 * 
 * @author MyCollab Ltd.
 * @since 1.0
 */
@ViewComponent
public class ProjectMemberInviteViewImpl extends AbstractPageView implements
		ProjectMemberInviteView {

	private static final long serialVersionUID = 1L;

	private static final EmailValidator emailValidate = new EmailValidator();

	private List<String> inviteEmails;
	private Integer roleId = 0;

	private InviteUserTokenField inviteUserTokenField;
	private ProjectRoleComboBox roleComboBox;
	private TextArea messageArea;

	public ProjectMemberInviteViewImpl() {
		super();
	}

	@Override
	public void display() {
		inviteEmails = new ArrayList<String>();
		roleId = 0;

		initContent();
	}

	private void initContent() {
		this.removeAllComponents();

		this.roleComboBox = new ProjectRoleComboBox();

		final AddViewLayout userAddLayout = new AddViewLayout(
				AppContext
						.getMessage(ProjectMemberI18nEnum.FORM_INVITE_MEMBERS),
				MyCollabResource.newResource("icons/24/project/user.png"));

		userAddLayout.addHeaderRight(createButtonControls());

		GridFormLayoutHelper informationLayout = new GridFormLayoutHelper(1, 3,
				"100%", "167px", Alignment.TOP_LEFT);
		informationLayout.getLayout().setWidth("100%");
		informationLayout.getLayout().setMargin(false);
		informationLayout.getLayout().addStyleName("colored-gridlayout");

		HorizontalLayout lo = new HorizontalLayout();
		lo.setSpacing(true);
		inviteUserTokenField = new InviteUserTokenField(lo);
		informationLayout.addComponent(inviteUserTokenField, AppContext
				.getMessage(ProjectMemberI18nEnum.FORM_INVITEES_EMAIL), 0, 0);
		informationLayout.addComponent(roleComboBox,
				AppContext.getMessage(ProjectMemberI18nEnum.FORM_ROLE), 0, 1);

		messageArea = new TextArea();
		messageArea
				.setValue("Please join me to our new tool. This is a place where everyone can manage our business sales, projects, documents. But it is easy to use too!");
		informationLayout
				.addComponent(messageArea, AppContext
						.getMessage(ProjectMemberI18nEnum.FORM_MESSAGE), 0, 2);

		userAddLayout.addBody(informationLayout.getLayout());
		this.addComponent(userAddLayout);
	}

	private Layout createButtonControls() {
		final HorizontalLayout controlButtons = new HorizontalLayout();
		controlButtons.setSpacing(true);

		Button inviteBtn = new Button(
				AppContext.getMessage(ProjectMemberI18nEnum.BUTTON_NEW_INVITEE),
				new Button.ClickListener() {
					private static final long serialVersionUID = 1L;

					@Override
					public void buttonClick(ClickEvent event) {
						roleId = (Integer) roleComboBox.getValue();
						ProjectMemberInviteViewImpl.this
								.fireEvent(new ProjectMemberEvent.InviteProjectMembers(
										ProjectMemberInviteViewImpl.this,
										inviteEmails, roleId, messageArea
												.getValue()));

					}
				});
		inviteBtn.setStyleName(UIConstants.THEME_GREEN_LINK);
		controlButtons.addComponent(inviteBtn);

		Button cancelBtn = new Button(
				AppContext.getMessage(GenericI18Enum.BUTTON_CANCEL_LABEL),
				new Button.ClickListener() {
					private static final long serialVersionUID = 1L;

					@Override
					public void buttonClick(ClickEvent event) {
						ViewState viewState = HistoryViewManager.back();
						if (viewState instanceof NullViewState) {
							EventBus.getInstance()
									.fireEvent(
											new ProjectMemberEvent.GotoList(
													this, null));
						}

					}
				});
		cancelBtn.setStyleName(UIConstants.THEME_GRAY_LINK);
		controlButtons.addComponent(cancelBtn);

		controlButtons.setSizeUndefined();
		return controlButtons;
	}

	private class InviteUserTokenField extends TokenField {
		private static final long serialVersionUID = 1L;

		public InviteUserTokenField(Layout container) {
			super(container);
			this.setInputPrompt(AppContext
					.getMessage(ProjectMemberI18nEnum.USER_TOKEN_INVITE_HINT));
			this.setWidth("100%");
			this.setInputWidth("100%");
			this.setFilteringMode(FilteringMode.CONTAINS);
			this.setRememberNewTokens(true);

			final ProjectMemberService prjMemberService = ApplicationContextUtil
					.getSpringBean(ProjectMemberService.class);
			final List<SimpleUser> users = prjMemberService
					.getUsersNotInProject(
							CurrentProjectVariables.getProjectId(),
							AppContext.getAccountId());

			BeanItemContainer<SimpleUser> dsContainer = new BeanItemContainer<SimpleUser>(
					SimpleUser.class, users);
			this.setContainerDataSource(dsContainer);

			this.setTokenCaptionMode(ItemCaptionMode.PROPERTY);
			this.setTokenCaptionPropertyId("displayName");
		}

		@Override
		protected void onTokenInput(Object tokenId) {
			String invitedEmail;

			if (tokenId instanceof SimpleUser) {
				invitedEmail = ((SimpleUser) tokenId).getEmail();
			} else if (tokenId instanceof String) {
				invitedEmail = (String) tokenId;
			} else {
				throw new MyCollabException("Do not support token field "
						+ tokenId);
			}

			if (emailValidate.validate(invitedEmail)) {
				if (!inviteEmails.contains(invitedEmail)) {
					inviteEmails.add(invitedEmail);
					super.onTokenInput(tokenId);
					this.setInputPrompt(null);
				}
			} else {
				NotificationUtil.showErrorNotification(AppContext
						.getMessage(GenericI18Enum.WARNING_NOT_VALID_EMAIL));
			}

		}

		@Override
		protected void onTokenClick(final Object tokenId) {
			onTokenDelete(tokenId);
		}

		@Override
		protected void onTokenDelete(Object tokenId) {
			String invitedEmail;

			if (tokenId instanceof SimpleUser) {
				invitedEmail = ((SimpleUser) tokenId).getEmail();
			} else if (tokenId instanceof String) {
				invitedEmail = (String) tokenId;
			} else {
				throw new MyCollabException("Do not support token field "
						+ tokenId);
			}

			inviteEmails.remove(invitedEmail);
			if (inviteEmails.size() == 0) {
				this.setInputPrompt(AppContext
						.getMessage(ProjectMemberI18nEnum.EMPTY_EMAILS_OF_USERS_TO_INVITE_ERRPR_MESSAGE));
			}
			super.onTokenClick(tokenId);
		}
	}
}

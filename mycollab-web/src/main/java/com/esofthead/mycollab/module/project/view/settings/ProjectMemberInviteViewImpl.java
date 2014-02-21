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

import com.esofthead.mycollab.common.localization.GenericI18Enum;
import com.esofthead.mycollab.core.MyCollabException;
import com.esofthead.mycollab.core.utils.EmailValidator;
import com.esofthead.mycollab.core.utils.LocalizationHelper;
import com.esofthead.mycollab.eventmanager.EventBus;
import com.esofthead.mycollab.module.project.CurrentProjectVariables;
import com.esofthead.mycollab.module.project.events.ProjectMemberEvent;
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
import com.esofthead.mycollab.vaadin.ui.UserAvatarControlFactory;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.shared.ui.combobox.FilteringMode;
import com.vaadin.ui.AbstractSelect.ItemCaptionMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Layout;

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

	public ProjectMemberInviteViewImpl() {
		super();
		this.setMargin(new MarginInfo(true, false, false, false));
	}

	@Override
	public void display() {
		inviteEmails = new ArrayList<String>();
		roleId = 0;

		initContent();
	}

	private void initContent() {
		this.removeAllComponents();

		// init invite token field
		inviteUserTokenField = new InviteUserTokenField();
		inviteUserTokenField.setFilteringMode(FilteringMode.CONTAINS);

		final ProjectMemberService prjMemberService = ApplicationContextUtil
				.getSpringBean(ProjectMemberService.class);
		final List<SimpleUser> users = prjMemberService.getUsersNotInProject(
				CurrentProjectVariables.getProjectId(),
				AppContext.getAccountId());

		BeanItemContainer<SimpleUser> dsContainer = new BeanItemContainer<SimpleUser>(
				SimpleUser.class, users);
		inviteUserTokenField.setContainerDataSource(dsContainer);

		inviteUserTokenField.setTokenCaptionMode(ItemCaptionMode.PROPERTY);
		inviteUserTokenField.setTokenCaptionPropertyId("displayName");
		for (SimpleUser user : users) {
			inviteUserTokenField.setTokenIcon(
					user,
					UserAvatarControlFactory.createAvatarResource(
							user.getAvatarid(), 16));
		}
		this.addComponent(inviteUserTokenField);

		this.roleComboBox = new ProjectRoleComboBox();

		final AddViewLayout userAddLayout = new AddViewLayout("Invite Members",
				MyCollabResource.newResource("icons/24/project/group.png"));

		userAddLayout.addTopControls(createButtonControls());

		GridFormLayoutHelper informationLayout = new GridFormLayoutHelper(1, 2,
				"100%", "167px", Alignment.MIDDLE_LEFT);
		informationLayout.getLayout().setWidth("100%");
		informationLayout.getLayout().setMargin(false);
		informationLayout.getLayout().addStyleName("colored-gridlayout");

		informationLayout.addComponent(inviteUserTokenField,
				"Invitee's emails", 0, 0);
		informationLayout.addComponent(roleComboBox, "Role", 0, 1);

		userAddLayout.addBody(informationLayout.getLayout());
		this.addComponent(userAddLayout);
	}

	private Layout createButtonControls() {
		final HorizontalLayout controlPanel = new HorizontalLayout();
		controlPanel.setMargin(new MarginInfo(true, false, true, false));

		final HorizontalLayout controlButtons = new HorizontalLayout();
		controlButtons.setSpacing(true);

		Button inviteBtn = new Button("Invite Members",
				new Button.ClickListener() {
					private static final long serialVersionUID = 1L;

					@Override
					public void buttonClick(ClickEvent event) {
						roleId = (Integer) roleComboBox.getValue();
						ProjectMemberInviteViewImpl.this
								.fireEvent(new ProjectMemberEvent.InviteProjectMembers(
										ProjectMemberInviteViewImpl.this,
										inviteEmails, roleId));

					}
				});
		inviteBtn.setStyleName(UIConstants.THEME_BLUE_LINK);
		controlButtons.addComponent(inviteBtn);

		Button cancelBtn = new Button("Cancel", new Button.ClickListener() {
			private static final long serialVersionUID = 1L;

			@Override
			public void buttonClick(ClickEvent event) {
				ViewState viewState = HistoryViewManager.back();
				if (viewState instanceof NullViewState) {
					EventBus.getInstance().fireEvent(
							new ProjectMemberEvent.GotoList(this, null));
				}

			}
		});
		cancelBtn.setStyleName(UIConstants.THEME_BLUE_LINK);
		controlButtons.addComponent(cancelBtn);

		controlButtons.setSizeUndefined();
		controlPanel.addComponent(controlButtons);
		controlPanel.setWidth("100%");
		controlPanel.setComponentAlignment(controlButtons,
				Alignment.MIDDLE_CENTER);
		return controlPanel;
	}

	private class InviteUserTokenField extends TokenField {
		private static final long serialVersionUID = 1L;

		@Override
		public void addToken(Object tokenId) {
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
					super.addToken(tokenId);
				}
			} else {
				NotificationUtil.showErrorNotification(LocalizationHelper
						.getMessage(GenericI18Enum.WARNING_NOT_VALID_EMAIL));
			}

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
			super.onTokenDelete(tokenId);
		}
	}
}

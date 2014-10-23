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
import com.esofthead.mycollab.common.i18n.SecurityI18nEnum;
import com.esofthead.mycollab.core.MyCollabException;
import com.esofthead.mycollab.core.utils.StringUtils;
import com.esofthead.mycollab.eventmanager.EventBusFactory;
import com.esofthead.mycollab.module.project.CurrentProjectVariables;
import com.esofthead.mycollab.module.project.ProjectRolePermissionCollections;
import com.esofthead.mycollab.module.project.domain.SimpleProjectRole;
import com.esofthead.mycollab.module.project.events.ProjectMemberEvent;
import com.esofthead.mycollab.module.project.events.ProjectMemberEvent.InviteProjectMembers;
import com.esofthead.mycollab.module.project.i18n.ProjectMemberI18nEnum;
import com.esofthead.mycollab.module.project.i18n.ProjectRoleI18nEnum;
import com.esofthead.mycollab.module.project.i18n.RolePermissionI18nEnum;
import com.esofthead.mycollab.module.project.service.ProjectMemberService;
import com.esofthead.mycollab.module.project.service.ProjectRoleService;
import com.esofthead.mycollab.module.project.view.settings.component.ProjectRoleComboBox;
import com.esofthead.mycollab.module.user.domain.SimpleUser;
import com.esofthead.mycollab.security.PermissionMap;
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
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.shared.ui.combobox.FilteringMode;
import com.vaadin.ui.AbstractSelect.ItemCaptionMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Layout;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.VerticalLayout;

/**
 * 
 * @author MyCollab Ltd.
 * @since 1.0
 */
@ViewComponent
public class ProjectMemberInviteViewImpl extends AbstractPageView implements
		ProjectMemberInviteView {

	private static final long serialVersionUID = 1L;

	private List<String> inviteEmails;
	private Integer roleId = 0;

	private InviteUserTokenField inviteUserTokenField;
	private ProjectRoleComboBox roleComboBox;
	private TextArea messageArea;
	private VerticalLayout permissionsPanel;
	private GridFormLayoutHelper projectFormHelper;

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
		this.roleComboBox.addValueChangeListener(new ValueChangeListener() {
			private static final long serialVersionUID = 1L;

			@Override
			public void valueChange(ValueChangeEvent event) {
				Integer roleId = (Integer) roleComboBox.getValue();
				displayRolePermission(roleId);
			}
		});

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

		final HorizontalLayout lo = new HorizontalLayout();
		lo.setSpacing(true);
		inviteUserTokenField = new InviteUserTokenField(lo);
		informationLayout.addComponent(inviteUserTokenField, AppContext
				.getMessage(ProjectMemberI18nEnum.FORM_INVITEES_EMAIL), 0, 0);
		informationLayout.addComponent(roleComboBox,
				AppContext.getMessage(ProjectMemberI18nEnum.FORM_ROLE), 0, 1);

		messageArea = new TextArea();
		messageArea
				.setValue(AppContext
						.getMessage(ProjectMemberI18nEnum.MSG_DEFAULT_INVITATION_COMMENT));
		informationLayout
				.addComponent(messageArea, AppContext
						.getMessage(ProjectMemberI18nEnum.FORM_MESSAGE), 0, 2);

		userAddLayout.addBody(informationLayout.getLayout());
		userAddLayout.addBottomControls(createBottomPanel());
		this.addComponent(userAddLayout);
	}

	private Layout createButtonControls() {
		final HorizontalLayout controlButtons = new HorizontalLayout();
		controlButtons.setSpacing(true);

		Button cancelBtn = new Button(
				AppContext.getMessage(GenericI18Enum.BUTTON_CANCEL),
				new Button.ClickListener() {
					private static final long serialVersionUID = 1L;

					@Override
					public void buttonClick(ClickEvent event) {
						ViewState viewState = HistoryViewManager.back();
						if (viewState instanceof NullViewState) {
							EventBusFactory.getInstance()
									.post(new ProjectMemberEvent.GotoList(this,
											null));
						}

					}
				});
		cancelBtn.setStyleName(UIConstants.THEME_GRAY_LINK);
		controlButtons.addComponent(cancelBtn);

		Button inviteBtn = new Button(
				AppContext.getMessage(ProjectMemberI18nEnum.BUTTON_NEW_INVITEE),
				new Button.ClickListener() {
					private static final long serialVersionUID = 1L;

					@Override
					public void buttonClick(ClickEvent event) {
						roleId = (Integer) roleComboBox.getValue();
						ProjectMemberInviteViewImpl.this.fireEvent(new ViewEvent<InviteProjectMembers>(
								ProjectMemberInviteViewImpl.this,
								new InviteProjectMembers(inviteEmails, roleId,
										messageArea.getValue())));

					}
				});
		inviteBtn.setStyleName(UIConstants.THEME_GREEN_LINK);
		controlButtons.addComponent(inviteBtn);

		controlButtons.setSizeUndefined();
		return controlButtons;
	}

	private Layout createBottomPanel() {
		permissionsPanel = new VerticalLayout();
		final Label organizationHeader = new Label(
				AppContext.getMessage(ProjectRoleI18nEnum.SECTION_PERMISSIONS));
		organizationHeader.setStyleName("h2");
		permissionsPanel.addComponent(organizationHeader);

		projectFormHelper = new GridFormLayoutHelper(2,
				ProjectRolePermissionCollections.PROJECT_PERMISSIONS.length,
				"100%", "167px", Alignment.TOP_LEFT);
		projectFormHelper.getLayout().setWidth("100%");
		projectFormHelper.getLayout().setMargin(false);
		projectFormHelper.getLayout().addStyleName("colored-gridlayout");

		permissionsPanel.addComponent(projectFormHelper.getLayout());

		roleId = (Integer) roleComboBox.getValue();
		displayRolePermission(roleId);

		return permissionsPanel;
	}

	private void displayRolePermission(Integer roleId) {
		projectFormHelper.getLayout().removeAllComponents();
		if (roleId != null && roleId > 0) {
			ProjectRoleService roleService = ApplicationContextUtil
					.getSpringBean(ProjectRoleService.class);
			SimpleProjectRole role = roleService.findById(roleId,
					AppContext.getAccountId());
			if (role != null) {
				final PermissionMap permissionMap = role.getPermissionMap();
				for (int i = 0; i < ProjectRolePermissionCollections.PROJECT_PERMISSIONS.length; i++) {
					final String permissionPath = ProjectRolePermissionCollections.PROJECT_PERMISSIONS[i];
					projectFormHelper.addComponent(
							new Label(AppContext.getPermissionCaptionValue(
									permissionMap, permissionPath)), AppContext
									.getMessage(RolePermissionI18nEnum
											.valueOf(permissionPath)), 0, i);
				}
			}
		} else {
			for (int i = 0; i < ProjectRolePermissionCollections.PROJECT_PERMISSIONS.length; i++) {
				final String permissionPath = ProjectRolePermissionCollections.PROJECT_PERMISSIONS[i];
				projectFormHelper.addComponent(
						new Label(AppContext
								.getMessage(SecurityI18nEnum.ACCESS)),
						permissionPath, 0, i);
			}
		}

	}

	private class InviteUserTokenField extends TokenField {
		private static final long serialVersionUID = 1L;

		private Button newButton;

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

			this.setTokenCaptionMode(ItemCaptionMode.EXPLICIT);
			for (SimpleUser user : users) {
				this.cb.addItem(user);
				this.cb.setItemCaption(user, user.getDisplayName());
				this.cb.setItemIcon(
						user,
						UserAvatarControlFactory.createAvatarResource(
								user.getAvatarid(), 16));
			}
		}

		@Override
		protected void configureTokenButton(Object tokenId, Button button) {
			super.configureTokenButton(tokenId, button);
			this.newButton = button;
		}

		@Override
		protected void setInternalValue(Object newValue) {
			super.setInternalValue(newValue);
			((HorizontalLayout) layout).setExpandRatio(newButton, 0);
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

			if (StringUtils.isValidEmail(invitedEmail)) {
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
						.getMessage(ProjectMemberI18nEnum.ERROR_EMPTY_EMAILS_OF_USERS_TO_INVITE_MESSAGE));
			}
			super.onTokenClick(tokenId);
		}
	}
}

/**
 * This file is part of mycollab-mobile.
 *
 * mycollab-mobile is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * mycollab-mobile is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with mycollab-mobile.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.esofthead.mycollab.mobile.module.project.view.settings;

import java.util.Arrays;

import com.esofthead.mycollab.common.i18n.SecurityI18nEnum;
import com.esofthead.mycollab.mobile.module.project.events.ProjectMemberEvent;
import com.esofthead.mycollab.mobile.ui.AbstractMobilePageView;
import com.esofthead.mycollab.module.project.ProjectRolePermissionCollections;
import com.esofthead.mycollab.module.project.domain.SimpleProjectRole;
import com.esofthead.mycollab.module.project.i18n.ProjectMemberI18nEnum;
import com.esofthead.mycollab.module.project.i18n.ProjectRoleI18nEnum;
import com.esofthead.mycollab.module.project.i18n.RolePermissionI18nEnum;
import com.esofthead.mycollab.module.project.service.ProjectRoleService;
import com.esofthead.mycollab.security.PermissionMap;
import com.esofthead.mycollab.spring.ApplicationContextUtil;
import com.esofthead.mycollab.vaadin.AppContext;
import com.esofthead.mycollab.vaadin.mvp.ViewComponent;
import com.vaadin.addon.touchkit.ui.EmailField;
import com.vaadin.addon.touchkit.ui.VerticalComponentGroup;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Label;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.VerticalLayout;

/**
 * @author MyCollab Ltd.
 *
 * @since 4.5.2
 */

@ViewComponent
public class ProjectMemberInviteViewImpl extends AbstractMobilePageView
		implements ProjectMemberInviteView {

	private static final long serialVersionUID = 6319585054784302576L;

	// private List<String> inviteEmails;
	private Integer roleId = 0;

	private EmailField inviteEmailField;
	private ProjectRoleComboBox roleComboBox;
	private TextArea messageArea;
	private VerticalComponentGroup permissionsPanel;
	private VerticalComponentGroup inviteFormLayout;

	public ProjectMemberInviteViewImpl() {
		super();
		this.addStyleName("member-invite-view");
		this.setCaption(AppContext
				.getMessage(ProjectMemberI18nEnum.FORM_NEW_TITLE));

		constructUI();
	}

	private void constructUI() {
		this.roleComboBox = new ProjectRoleComboBox();
		this.roleComboBox.addValueChangeListener(new ValueChangeListener() {
			private static final long serialVersionUID = 1L;

			@Override
			public void valueChange(ValueChangeEvent event) {
				Integer roleId = (Integer) roleComboBox.getValue();
				displayRolePermission(roleId);
			}
		});
		roleComboBox.setCaption(AppContext
				.getMessage(ProjectMemberI18nEnum.FORM_ROLE));

		final VerticalLayout mainLayout = new VerticalLayout();
		mainLayout.setStyleName("main-layout");
		mainLayout.addStyleName("editview-layout");
		mainLayout.setWidth("100%");

		inviteFormLayout = new VerticalComponentGroup();
		inviteFormLayout.setWidth("100%");

		inviteEmailField = new EmailField();
		inviteEmailField.setCaption(AppContext
				.getMessage(ProjectMemberI18nEnum.M_FORM_EMAIL));
		inviteFormLayout.addComponent(inviteEmailField);

		messageArea = new TextArea();
		messageArea
				.setValue(AppContext
						.getMessage(ProjectMemberI18nEnum.MSG_DEFAULT_INVITATION_COMMENT));
		messageArea.setCaption(AppContext
				.getMessage(ProjectMemberI18nEnum.FORM_MESSAGE));
		inviteFormLayout.addComponent(messageArea);

		inviteFormLayout.addComponent(roleComboBox);

		mainLayout.addComponent(inviteFormLayout);

		Label permissionSectionHdr = new Label(
				AppContext.getMessage(ProjectRoleI18nEnum.SECTION_PERMISSIONS));
		permissionSectionHdr.setStyleName("h2");
		mainLayout.addComponent(permissionSectionHdr);

		permissionsPanel = new VerticalComponentGroup();
		mainLayout.addComponent(permissionsPanel);

		Button inviteBtn = new Button(
				AppContext.getMessage(ProjectMemberI18nEnum.BUTTON_NEW_INVITEE),
				new Button.ClickListener() {
					private static final long serialVersionUID = 1L;

					@Override
					public void buttonClick(ClickEvent event) {
						if ("".equals(inviteEmailField.getValue())) {
							return;
						}
						ProjectMemberInviteViewImpl.this.roleId = (Integer) roleComboBox
								.getValue();
						ProjectMemberInviteViewImpl.this
								.fireEvent(new ViewEvent<>(ProjectMemberInviteViewImpl.this,
										new ProjectMemberEvent.InviteProjectMembers(
												Arrays.asList(inviteEmailField.getValue()),
												ProjectMemberInviteViewImpl.this.roleId,
												messageArea.getValue())));

					}
				});
		inviteBtn.addStyleName("save-btn");
		this.setRightComponent(inviteBtn);
		this.setContent(mainLayout);
	}

	private void displayRolePermission(Integer roleId) {
		permissionsPanel.removeAllComponents();
		if (roleId != null && roleId > 0) {
			ProjectRoleService roleService = ApplicationContextUtil
					.getSpringBean(ProjectRoleService.class);
			SimpleProjectRole role = roleService.findById(roleId,
					AppContext.getAccountId());
			if (role != null) {
				final PermissionMap permissionMap = role.getPermissionMap();
				for (int i = 0; i < ProjectRolePermissionCollections.PROJECT_PERMISSIONS.length; i++) {
					final String permissionPath = ProjectRolePermissionCollections.PROJECT_PERMISSIONS[i];
					Label permissionLbl = new Label(
							AppContext.getPermissionCaptionValue(permissionMap,
									permissionPath));
					permissionLbl.setCaption(AppContext
							.getMessage(RolePermissionI18nEnum
									.valueOf(permissionPath)));
					permissionsPanel.addComponent(permissionLbl);
				}
			}
		} else {
			for (int i = 0; i < ProjectRolePermissionCollections.PROJECT_PERMISSIONS.length; i++) {
				final String permissionPath = ProjectRolePermissionCollections.PROJECT_PERMISSIONS[i];
				Label permissionLbl = new Label(
						AppContext.getMessage(SecurityI18nEnum.ACCESS));
				permissionLbl.setCaption(permissionPath);
				permissionsPanel.addComponent(permissionLbl);
			}
		}

	}

	@Override
	public void display() {
		roleId = 0;

		displayRolePermission(roleId);
		inviteEmailField.setValue("");
		messageArea
				.setValue(AppContext
						.getMessage(ProjectMemberI18nEnum.MSG_DEFAULT_INVITATION_COMMENT));
	}

}

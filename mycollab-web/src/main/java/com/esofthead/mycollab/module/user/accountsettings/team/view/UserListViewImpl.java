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

import java.util.Arrays;
import java.util.List;

import org.vaadin.dialogs.ConfirmDialog;

import com.esofthead.mycollab.common.i18n.GenericI18Enum;
import com.esofthead.mycollab.configuration.SiteConfiguration;
import com.esofthead.mycollab.core.arguments.SearchRequest;
import com.esofthead.mycollab.core.utils.DateTimeUtils;
import com.esofthead.mycollab.eventmanager.EventBus;
import com.esofthead.mycollab.module.billing.RegisterStatusConstants;
import com.esofthead.mycollab.module.user.AccountLinkUtils;
import com.esofthead.mycollab.module.user.domain.SimpleRole;
import com.esofthead.mycollab.module.user.domain.SimpleUser;
import com.esofthead.mycollab.module.user.domain.criteria.UserSearchCriteria;
import com.esofthead.mycollab.module.user.events.UserEvent;
import com.esofthead.mycollab.module.user.service.UserService;
import com.esofthead.mycollab.security.RolePermissionCollections;
import com.esofthead.mycollab.spring.ApplicationContextUtil;
import com.esofthead.mycollab.vaadin.AppContext;
import com.esofthead.mycollab.vaadin.mvp.AbstractPageView;
import com.esofthead.mycollab.vaadin.mvp.ViewComponent;
import com.esofthead.mycollab.vaadin.ui.ButtonLink;
import com.esofthead.mycollab.vaadin.ui.ConfirmDialogExt;
import com.esofthead.mycollab.vaadin.ui.MyCollabResource;
import com.esofthead.mycollab.vaadin.ui.UIConstants;
import com.esofthead.mycollab.vaadin.ui.UserAvatarControlFactory;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Component;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Image;
import com.vaadin.ui.Label;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

/**
 * 
 * @author MyCollab Ltd.
 * @since 1.0
 */
@ViewComponent
public class UserListViewImpl extends AbstractPageView implements UserListView {

	private static final long serialVersionUID = 1L;

	public UserListViewImpl() {
		super();
		this.setMargin(new MarginInfo(false, true, false, true));
	}

	@Override
	public void setSearchCriteria(UserSearchCriteria searchCriteria) {
		UserService userService = ApplicationContextUtil
				.getSpringBean(UserService.class);
		List<SimpleUser> userAccountList = userService
				.findPagableListByCriteria(new SearchRequest<UserSearchCriteria>(
						searchCriteria, 0, Integer.MAX_VALUE));

		this.removeAllComponents();
		this.setSpacing(true);
		HorizontalLayout header = new HorizontalLayout();
		header.setStyleName(UIConstants.HEADER_VIEW);
		header.setWidth("100%");
		header.setMargin(new MarginInfo(true, false, true, false));
		Button createBtn = new Button("Invite user",
				new Button.ClickListener() {
					private static final long serialVersionUID = 1L;

					@Override
					public void buttonClick(Button.ClickEvent event) {
						EventBus.getInstance().fireEvent(
								new UserEvent.GotoAdd(this, null));
					}
				});
		createBtn.setEnabled(AppContext
				.canWrite(RolePermissionCollections.ACCOUNT_USER));
		createBtn.setStyleName(UIConstants.THEME_GREEN_LINK);
		createBtn.setIcon(MyCollabResource
				.newResource("icons/16/addRecord.png"));

		header.addComponent(createBtn);
		header.setComponentAlignment(createBtn, Alignment.MIDDLE_RIGHT);
		this.addComponent(header);

		CssLayout contentLayout = new CssLayout();
		contentLayout.setWidth("100%");
		for (SimpleUser userAccount : userAccountList) {
			contentLayout.addComponent(generateMemberBlock(userAccount));
		}
		this.addComponent(contentLayout);
	}

	private Component generateMemberBlock(final SimpleUser member) {
		CssLayout memberBlock = new CssLayout();
		memberBlock.addStyleName("member-block");

		VerticalLayout blockContent = new VerticalLayout();
		HorizontalLayout blockTop = new HorizontalLayout();
		blockTop.setSpacing(true);
		Image memberAvatar = UserAvatarControlFactory
				.createUserAvatarEmbeddedComponent(member.getAvatarid(), 100);
		blockTop.addComponent(memberAvatar);

		VerticalLayout memberInfo = new VerticalLayout();

		HorizontalLayout layoutButtonDelete = new HorizontalLayout();
		layoutButtonDelete.setVisible(AppContext
				.canWrite(RolePermissionCollections.ACCOUNT_USER));
		layoutButtonDelete.setWidth("100%");

		Label emptylb = new Label("");
		layoutButtonDelete.addComponent(emptylb);
		layoutButtonDelete.setExpandRatio(emptylb, 1.0f);

		Button btnDelete = new Button();
		btnDelete.addClickListener(new Button.ClickListener() {
			private static final long serialVersionUID = 1L;

			@Override
			public void buttonClick(ClickEvent event) {
				ConfirmDialogExt.show(
						UI.getCurrent(),
						AppContext.getMessage(
								GenericI18Enum.DIALOG_DELETE_TITLE,
								SiteConfiguration.getSiteName()),
						AppContext
								.getMessage(GenericI18Enum.DIALOG_CONFIRM_DELETE_RECORD_MESSAGE),
						AppContext
								.getMessage(GenericI18Enum.BUTTON_YES_LABEL),
						AppContext
								.getMessage(GenericI18Enum.BUTTON_NO_LABEL),
						new ConfirmDialog.Listener() {
							private static final long serialVersionUID = 1L;

							@Override
							public void onClose(ConfirmDialog dialog) {
								if (dialog.isConfirmed()) {
									UserService userService = ApplicationContextUtil
											.getSpringBean(UserService.class);
									userService.pendingUserAccounts(Arrays
											.asList(new String[] { member
													.getUsername() }),
											AppContext.getAccountId());
									EventBus.getInstance()
											.fireEvent(
													new UserEvent.GotoList(
															UserListViewImpl.this,
															null));
								}
							}
						});
			}
		});
		btnDelete.setIcon(MyCollabResource
				.newResource("icons/12/project/icon_x.png"));
		btnDelete.setStyleName("link");
		layoutButtonDelete.addComponent(btnDelete);

		memberInfo.addComponent(layoutButtonDelete);

		ButtonLink userAccountLink = new ButtonLink(member.getDisplayName());
		userAccountLink.addClickListener(new ClickListener() {

			private static final long serialVersionUID = 1L;

			@Override
			public void buttonClick(ClickEvent event) {
				EventBus.getInstance().fireEvent(
						new UserEvent.GotoRead(UserListViewImpl.this, member
								.getUsername()));
			}
		});
		userAccountLink.setWidth("100%");
		userAccountLink.setHeight("100%");

		memberInfo.addComponent(userAccountLink);

		Label memberEmailLabel = new Label("<a href='mailto:"
				+ member.getUsername() + "'>" + member.getUsername() + "</a>",
				ContentMode.HTML);
		memberEmailLabel.addStyleName("member-email");
		memberEmailLabel.setWidth("100%");
		memberInfo.addComponent(memberEmailLabel);

		if (RegisterStatusConstants.SENT_VERIFICATION_EMAIL.equals(member
				.getRegisterstatus())) {
			final VerticalLayout waitingNotLayout = new VerticalLayout();
			Label infoStatus = new Label("Waiting for accept invitation");
			infoStatus.addStyleName("member-email");
			waitingNotLayout.addComponent(infoStatus);

			ButtonLink resendInvitationLink = new ButtonLink(
					"Resend Invitation", new Button.ClickListener() {
						private static final long serialVersionUID = 1L;

						@Override
						public void buttonClick(ClickEvent event) {
							UserService userService = ApplicationContextUtil
									.getSpringBean(UserService.class);
							userService.updateUserAccountStatus(
									member.getUsername(),
									member.getAccountId(),
									RegisterStatusConstants.VERIFICATING);
							waitingNotLayout.removeAllComponents();
							Label statusEmail = new Label(
									"Sending invitation email");
							statusEmail.addStyleName("member-email");
							waitingNotLayout.addComponent(statusEmail);
						}
					});
			resendInvitationLink.setStyleName("link");
			resendInvitationLink.addStyleName("member-email");
			waitingNotLayout.addComponent(resendInvitationLink);
			memberInfo.addComponent(waitingNotLayout);
		} else if (RegisterStatusConstants.ACTIVE.equals(member
				.getRegisterstatus())) {
			Label lastAccessTimeLbl = new Label("Logged in "
					+ DateTimeUtils.getStringDateFromNow(member
							.getLastaccessedtime()));
			lastAccessTimeLbl.addStyleName("member-email");
			memberInfo.addComponent(lastAccessTimeLbl);
		} else if (RegisterStatusConstants.VERIFICATING.equals(member
				.getRegisterstatus())) {
			Label infoStatus = new Label("Sending invitation email");
			infoStatus.addStyleName("member-email");
			memberInfo.addComponent(infoStatus);
		}

		blockTop.addComponent(memberInfo);
		blockTop.setExpandRatio(memberInfo, 1.0f);
		blockTop.setWidth("100%");
		blockContent.addComponent(blockTop);

		if (member.getRoleid() != null) {
			String memberRoleLinkPrefix = "<a href=\""
					+ AppContext.getSiteUrl()
					+ AccountLinkUtils.generateUserRoleLink(member.getRoleid())
					+ "\"";
			Label memberRole = new Label();
			memberRole.setContentMode(ContentMode.HTML);
			if (member.getRoleName().equals(SimpleRole.ADMIN)
					|| member.getIsAccountOwner() != null
					&& member.getIsAccountOwner()) {
				memberRole.setValue(memberRoleLinkPrefix
						+ "style=\"color: #B00000;\">" + "Administrator"
						+ "</a>");
			} else {
				memberRole.setValue(memberRoleLinkPrefix
						+ "style=\"color:gray;font-size:12px;\">"
						+ member.getRoleName() + "</a>");
			}
			memberRole.setSizeUndefined();
			blockContent.addComponent(memberRole);
			blockContent.setComponentAlignment(memberRole,
					Alignment.MIDDLE_RIGHT);
		} else if (member.getIsAccountOwner() != null
				&& member.getIsAccountOwner() == Boolean.TRUE) {
			Label memberRole = new Label();
			memberRole.setContentMode(ContentMode.HTML);
			memberRole.setValue("<a style=\"color: #B00000;\">"
					+ "Administrator" + "</a>");
			memberRole.setSizeUndefined();
			blockContent.addComponent(memberRole);
			blockContent.setComponentAlignment(memberRole,
					Alignment.MIDDLE_RIGHT);
		} else {
			Label lbl = new Label();
			lbl.setHeight("10px");
			blockContent.addComponent(lbl);
		}
		blockContent.setWidth("100%");

		memberBlock.addComponent(blockContent);

		return memberBlock;
	}
}

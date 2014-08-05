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

package com.esofthead.mycollab.common.ui.components;

import java.util.List;

import com.esofthead.mycollab.common.domain.SimpleComment;
import com.esofthead.mycollab.common.i18n.GenericI18Enum;
import com.esofthead.mycollab.common.service.CommentService;
import com.esofthead.mycollab.core.utils.DateTimeUtils;
import com.esofthead.mycollab.eventmanager.EventBusFactory;
import com.esofthead.mycollab.module.ecm.domain.Content;
import com.esofthead.mycollab.module.project.events.ProjectMemberEvent;
import com.esofthead.mycollab.spring.ApplicationContextUtil;
import com.esofthead.mycollab.vaadin.AppContext;
import com.esofthead.mycollab.vaadin.ui.AttachmentDisplayComponent;
import com.esofthead.mycollab.vaadin.ui.BeanList;
import com.esofthead.mycollab.vaadin.ui.MyCollabResource;
import com.esofthead.mycollab.vaadin.ui.UIConstants;
import com.esofthead.mycollab.vaadin.ui.UrlDetectableLabel;
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
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;

/**
 * 
 * @author MyCollab Ltd.
 * @since 1.0
 */
public class CommentRowDisplayHandler extends
		BeanList.RowDisplayHandler<SimpleComment> {
	private static final long serialVersionUID = 1L;

	@Override
	public Component generateRow(final SimpleComment comment, int rowIndex) {
		HorizontalLayout layout = new HorizontalLayout();
		layout.setStyleName("message");
		layout.setWidth("100%");
		layout.setSpacing(true);
		VerticalLayout userBlock = new VerticalLayout();
		userBlock.setDefaultComponentAlignment(Alignment.TOP_CENTER);
		userBlock.setWidth("80px");
		userBlock.setSpacing(true);
		ClickListener gotoUser = new ClickListener() {
			private static final long serialVersionUID = 1L;

			@Override
			public void buttonClick(ClickEvent event) {
				EventBusFactory.getInstance().post(
						new ProjectMemberEvent.GotoRead(this, comment
								.getCreateduser()));
			}
		};
		Button userAvatarBtn = UserAvatarControlFactory
				.createUserAvatarButtonLink(comment.getOwnerAvatarId(),
						comment.getOwnerFullName());
		userAvatarBtn.addClickListener(gotoUser);
		userBlock.addComponent(userAvatarBtn);

		Button userName = new Button(comment.getOwnerFullName());
		userName.setStyleName("user-name");
		userName.addStyleName("link");
		userName.addStyleName(UIConstants.WORD_WRAP);
		userName.addClickListener(gotoUser);
		userBlock.addComponent(userName);
		layout.addComponent(userBlock);

		CssLayout rowLayout = new CssLayout();
		rowLayout.setStyleName("message-container");
		rowLayout.setWidth("100%");

		HorizontalLayout messageHeader = new HorizontalLayout();
		messageHeader.setStyleName("message-header");
		messageHeader.setDefaultComponentAlignment(Alignment.MIDDLE_LEFT);
		messageHeader.setSpacing(true);

		Label timePostLbl = new Label(AppContext.getMessage(
				GenericI18Enum.EXT_ADDED_COMMENT, comment.getOwnerFullName(),
				DateTimeUtils.getStringDateFromNow(comment.getCreatedtime(),
						AppContext.getUserLocale())), ContentMode.HTML);

		timePostLbl.setSizeUndefined();
		timePostLbl.setStyleName("time-post");
		messageHeader.addComponent(timePostLbl);
		messageHeader.setExpandRatio(timePostLbl, 1.0f);
		messageHeader.setWidth("100%");
		messageHeader.setMargin(new MarginInfo(true, true, false, true));

		// Message delete button
		Button msgDeleteBtn = new Button();
		msgDeleteBtn.setIcon(MyCollabResource
				.newResource("icons/12/project/icon_x.png"));
		msgDeleteBtn.setStyleName("delete-btn");
		messageHeader.addComponent(msgDeleteBtn);

		if (hasDeletePermission(comment)) {
			msgDeleteBtn.setVisible(true);
			msgDeleteBtn.addClickListener(new Button.ClickListener() {
				private static final long serialVersionUID = 1L;

				@Override
				public void buttonClick(ClickEvent event) {
					CommentService commentService = ApplicationContextUtil
							.getSpringBean(CommentService.class);
					commentService.removeWithSession(comment.getId(),
							AppContext.getUsername(), AppContext.getAccountId());
				}
			});
		} else {
			msgDeleteBtn.setVisible(false);
		}

		rowLayout.addComponent(messageHeader);

		Label messageContent = new UrlDetectableLabel(comment.getComment());
		messageContent.setStyleName("message-body");
		rowLayout.addComponent(messageContent);

		List<Content> attachments = comment.getAttachments();
		if (attachments != null && !attachments.isEmpty()) {
			VerticalLayout messageFooter = new VerticalLayout();
			messageFooter.setWidth("100%");
			messageFooter.setStyleName("message-footer");
			AttachmentDisplayComponent attachmentDisplay = new AttachmentDisplayComponent(
					attachments);
			messageFooter.addComponent(attachmentDisplay);
			messageFooter.setMargin(true);
			messageFooter.setComponentAlignment(attachmentDisplay,
					Alignment.MIDDLE_RIGHT);
			rowLayout.addComponent(messageFooter);
		}

		layout.addComponent(rowLayout);
		layout.setExpandRatio(rowLayout, 1.0f);
		return layout;
	}

	private boolean hasDeletePermission(SimpleComment comment) {
		return false;
	}
}

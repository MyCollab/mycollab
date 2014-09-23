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
package com.esofthead.mycollab.mobile.module.project.view.message;

import com.esofthead.mycollab.common.CommentType;
import com.esofthead.mycollab.core.utils.DateTimeUtils;
import com.esofthead.mycollab.core.utils.StringUtils;
import com.esofthead.mycollab.mobile.module.project.CurrentProjectVariables;
import com.esofthead.mycollab.mobile.module.project.ui.ProjectCommentListDisplay;
import com.esofthead.mycollab.mobile.ui.AbstractMobilePageView;
import com.esofthead.mycollab.module.project.domain.SimpleMessage;
import com.esofthead.mycollab.module.project.i18n.MessageI18nEnum;
import com.esofthead.mycollab.schedule.email.project.MessageRelayEmailNotificationAction;
import com.esofthead.mycollab.vaadin.AppContext;
import com.esofthead.mycollab.vaadin.mvp.ViewComponent;
import com.esofthead.mycollab.vaadin.ui.UserAvatarControlFactory;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Image;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;

/**
 * @author MyCollab Ltd.
 *
 * @since 4.4.0
 *
 */
@ViewComponent
public class MessageReadViewImpl extends AbstractMobilePageView implements
		MessageReadView {

	private static final long serialVersionUID = -2234834060460314620L;

	private SimpleMessage bean;
	private final VerticalLayout mainLayout;

	public MessageReadViewImpl() {
		super();
		this.addStyleName("message-read-view");
		this.setCaption(AppContext
				.getMessage(MessageI18nEnum.M_VIEW_READ_TITLE));
		mainLayout = new VerticalLayout();
		mainLayout.setWidth("100%");
		this.setContent(mainLayout);
	}

	@Override
	public void previewItem(SimpleMessage message) {
		this.bean = message;
		mainLayout.removeAllComponents();

		HorizontalLayout messageBlock = new HorizontalLayout();
		messageBlock.setStyleName("message-block");
		Image userAvatarImg = UserAvatarControlFactory
				.createUserAvatarEmbeddedComponent(
						message.getPostedUserAvatarId(), 32);
		userAvatarImg.setStyleName("user-avatar");
		messageBlock.addComponent(userAvatarImg);

		CssLayout rightCol = new CssLayout();
		rightCol.setWidth("100%");

		HorizontalLayout metadataRow = new HorizontalLayout();
		metadataRow.setWidth("100%");
		metadataRow.setStyleName("metadata-row");
		Label userNameLbl = new Label(message.getFullPostedUserName());
		userNameLbl.setStyleName("user-name");
		metadataRow.addComponent(userNameLbl);
		metadataRow.setExpandRatio(userNameLbl, 1.0f);

		Label messageTimePost = new Label(DateTimeUtils.getPrettyDateValue(
				message.getPosteddate(), AppContext.getUserLocale()));
		messageTimePost.setStyleName("time-post");
		messageTimePost.setWidthUndefined();
		metadataRow.addComponent(messageTimePost);
		rightCol.addComponent(metadataRow);

		HorizontalLayout titleRow = new HorizontalLayout();
		titleRow.setWidth("100%");
		titleRow.setStyleName("title-row");
		Label messageTitle = new Label(message.getTitle());
		messageTitle.setStyleName("message-title");
		titleRow.addComponent(messageTitle);
		titleRow.setExpandRatio(messageTitle, 1.0f);

		if (message.getCommentsCount() > 0) {
			Label msgCommentCount = new Label(String.valueOf(message
					.getCommentsCount()));
			msgCommentCount.setStyleName("comment-count");
			msgCommentCount.setWidthUndefined();
			titleRow.addComponent(msgCommentCount);
			titleRow.addStyleName("has-comment");
			titleRow.setComponentAlignment(messageTitle, Alignment.MIDDLE_LEFT);
		}
		rightCol.addComponent(titleRow);

		Label messageContent = new Label(StringUtils.trim(
				StringUtils.trimHtmlTags(message.getMessage()), 150, true));
		messageContent.setStyleName("message-content");
		rightCol.addComponent(messageContent);

		messageBlock.addComponent(rightCol);
		messageBlock.setExpandRatio(rightCol, 1.0f);
		messageBlock.setWidth("100%");

		mainLayout.addComponent(messageBlock);

		ProjectCommentListDisplay commentDisplay = new ProjectCommentListDisplay(
				CommentType.PRJ_MESSAGE,
				CurrentProjectVariables.getProjectId(), true, true,
				MessageRelayEmailNotificationAction.class);
		commentDisplay.loadComments("" + message.getId());

		this.setToolbar(commentDisplay.getCommentBox());
		mainLayout.addComponent(commentDisplay);
	}

	@Override
	public SimpleMessage getItem() {

		return this.bean;
	}

}

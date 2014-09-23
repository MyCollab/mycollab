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

import com.esofthead.mycollab.core.utils.DateTimeUtils;
import com.esofthead.mycollab.core.utils.StringUtils;
import com.esofthead.mycollab.eventmanager.EventBusFactory;
import com.esofthead.mycollab.mobile.module.project.events.MessageEvent;
import com.esofthead.mycollab.mobile.ui.DefaultPagedBeanList;
import com.esofthead.mycollab.module.project.domain.SimpleMessage;
import com.esofthead.mycollab.module.project.domain.criteria.MessageSearchCriteria;
import com.esofthead.mycollab.module.project.service.MessageService;
import com.esofthead.mycollab.spring.ApplicationContextUtil;
import com.esofthead.mycollab.vaadin.AppContext;
import com.esofthead.mycollab.vaadin.ui.UserAvatarControlFactory;
import com.vaadin.event.LayoutEvents;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Component;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Image;
import com.vaadin.ui.Label;

/**
 * @author MyCollab Ltd.
 *
 * @since 4.4.0
 *
 */
public class MessageListDisplay
		extends
		DefaultPagedBeanList<MessageService, MessageSearchCriteria, SimpleMessage> {

	private static final long serialVersionUID = 7625380843753142287L;

	public MessageListDisplay() {
		super(ApplicationContextUtil.getSpringBean(MessageService.class),
				new MessageRowDisplayHandler());
	}

	public static class MessageRowDisplayHandler implements
			RowDisplayHandler<SimpleMessage> {

		@Override
		public Component generateRow(final SimpleMessage message, int rowIndex) {
			HorizontalLayout mainLayout = new HorizontalLayout();
			mainLayout.setStyleName("message-block");
			Image userAvatarImg = UserAvatarControlFactory
					.createUserAvatarEmbeddedComponent(
							message.getPostedUserAvatarId(), 32);
			userAvatarImg.setStyleName("user-avatar");
			mainLayout.addComponent(userAvatarImg);

			CssLayout rightCol = new CssLayout();
			rightCol.setWidth("100%");

			HorizontalLayout metadataRow = new HorizontalLayout();
			metadataRow.setWidth("100%");
			metadataRow.setStyleName("metadata-row");
			Label userNameLbl = new Label(message.getFullPostedUserName());
			userNameLbl.setStyleName("user-name");
			metadataRow.addComponent(userNameLbl);
			metadataRow.setExpandRatio(userNameLbl, 1.0f);

			Label messageTimePost = new Label(
					DateTimeUtils.getPrettyDateValue(message.getPosteddate(),
							AppContext.getUserLocale()));
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
				titleRow.setComponentAlignment(messageTitle,
						Alignment.MIDDLE_LEFT);
			}
			rightCol.addComponent(titleRow);

			Label messageContent = new Label(StringUtils.trim(
					StringUtils.trimHtmlTags(message.getMessage()), 150, true));
			messageContent.setStyleName("message-content");
			rightCol.addComponent(messageContent);

			mainLayout.addComponent(rightCol);
			mainLayout.setExpandRatio(rightCol, 1.0f);
			mainLayout
					.addLayoutClickListener(new LayoutEvents.LayoutClickListener() {

						private static final long serialVersionUID = 4964058820993577962L;

						@Override
						public void layoutClick(
								LayoutEvents.LayoutClickEvent arg0) {
							EventBusFactory.getInstance().post(
									new MessageEvent.GotoRead(
											MessageRowDisplayHandler.this,
											message.getId()));
						}
					});
			mainLayout.setWidth("100%");
			mainLayout.addStyleName("list-item");
			return mainLayout;
		}
	}

}

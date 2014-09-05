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
package com.esofthead.mycollab.mobile.module.project.ui;

import java.util.GregorianCalendar;

import com.esofthead.mycollab.common.CommentType;
import com.esofthead.mycollab.common.domain.Comment;
import com.esofthead.mycollab.common.i18n.GenericI18Enum;
import com.esofthead.mycollab.common.service.CommentService;
import com.esofthead.mycollab.schedule.email.SendingRelayEmailNotificationAction;
import com.esofthead.mycollab.spring.ApplicationContextUtil;
import com.esofthead.mycollab.vaadin.AppContext;
import com.esofthead.mycollab.vaadin.ui.ReloadableComponent;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.TextArea;

/**
 * @author MyCollab Ltd.
 *
 * @since 4.4.0
 *
 */
public class ProjectCommentInput extends HorizontalLayout {

	private static final long serialVersionUID = 8118887310759503892L;

	private TextArea commentInput;

	private CommentType type;
	private String typeid;
	private Integer extraTypeId;

	ProjectCommentInput(
			final ReloadableComponent component,
			final CommentType typeVal,
			final Integer extraTypeIdVal,
			final boolean cancelButtonEnable,
			final boolean isSendingEmailRelay,
			final Class<? extends SendingRelayEmailNotificationAction> emailHandler) {
		this.setSizeFull();
		this.setStyleName("comment-box");
		this.setSpacing(true);
		this.setDefaultComponentAlignment(Alignment.MIDDLE_LEFT);

		type = typeVal;
		extraTypeId = extraTypeIdVal;

		commentInput = new TextArea();
		commentInput.setInputPrompt(AppContext
				.getMessage(GenericI18Enum.M_NOTE_INPUT_PROMPT));
		commentInput.setSizeFull();
		this.addComponent(commentInput);
		this.setExpandRatio(commentInput, 1.0f);

		Button postBtn = new Button(
				AppContext.getMessage(GenericI18Enum.M_BUTTON_SEND));
		postBtn.setStyleName("submit-btn");
		postBtn.setWidthUndefined();
		postBtn.addClickListener(new Button.ClickListener() {

			private static final long serialVersionUID = 6687918902751556313L;

			@Override
			public void buttonClick(Button.ClickEvent arg0) {
				final Comment comment = new Comment();
				comment.setComment(commentInput.getValue());
				comment.setCreatedtime(new GregorianCalendar().getTime());
				comment.setCreateduser(AppContext.getUsername());
				comment.setSaccountid(AppContext.getAccountId());
				comment.setType(type.toString());
				comment.setTypeid("" + typeid);
				comment.setExtratypeid(extraTypeId);

				final CommentService commentService = ApplicationContextUtil
						.getSpringBean(CommentService.class);
				int commentId = commentService.saveWithSession(comment,
						AppContext.getUsername(), isSendingEmailRelay,
						emailHandler);
				//
				// String attachmentPath = AttachmentUtils
				// .getProjectEntityCommentAttachmentPath(typeVal,
				// AppContext.getAccountId(),
				// CurrentProjectVariables.getProjectId(),
				// typeid, commentId);
				//
				// if (!"".equals(attachmentPath)) {
				// attachments.saveContentsToRepo(attachmentPath);
				// }

				// save success, clear comment area and load list
				// comments again
				commentInput.setValue("");
				// attachments.removeAllAttachmentsDisplay();
				component.reload();
			}

		});
		this.addComponent(postBtn);
	}

	public void setTypeAndId(final String typeid) {
		this.typeid = typeid;
	}

}

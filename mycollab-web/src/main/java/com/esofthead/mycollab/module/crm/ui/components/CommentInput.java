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

package com.esofthead.mycollab.module.crm.ui.components;

import java.util.GregorianCalendar;

import org.vaadin.easyuploads.MultiFileUploadExt;

import com.esofthead.mycollab.common.CommentType;
import com.esofthead.mycollab.common.domain.Comment;
import com.esofthead.mycollab.common.i18n.GenericI18Enum;
import com.esofthead.mycollab.common.service.CommentService;
import com.esofthead.mycollab.module.file.AttachmentUtils;
import com.esofthead.mycollab.module.user.domain.SimpleUser;
import com.esofthead.mycollab.spring.ApplicationContextUtil;
import com.esofthead.mycollab.vaadin.AppContext;
import com.esofthead.mycollab.vaadin.ui.AttachmentPanel;
import com.esofthead.mycollab.vaadin.ui.MyCollabResource;
import com.esofthead.mycollab.vaadin.ui.ReloadableComponent;
import com.esofthead.mycollab.vaadin.ui.UIConstants;
import com.esofthead.mycollab.vaadin.ui.UserAvatarControlFactory;
import com.vaadin.addon.touchkit.settings.ApplicationCacheSettings;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.RichTextArea;
import com.vaadin.ui.VerticalLayout;

/**
 * 
 * @author MyCollab Ltd.
 * @since 1.0
 */
public class CommentInput extends VerticalLayout {
	private static final long serialVersionUID = 1L;
	private final RichTextArea commentArea;
	private CommentType type;
	private String typeid;
	private Integer extraTypeId;
	
	com.vaadin.shared.VBrowserDetails a;
	
	ApplicationCacheSettings b;

	public CommentInput(final ReloadableComponent component,
			final CommentType typeVal, final String typeidVal,
			final Integer extraTypeIdVal, final boolean cancelButtonEnable,
			final boolean isSendingEmailRelay) {
		this(component, typeVal, typeidVal, extraTypeIdVal, cancelButtonEnable,
				isSendingEmailRelay, null);
	}

	CommentInput(final ReloadableComponent component,
			final CommentType typeVal, final String typeidVal,
			final Integer extraTypeIdVal, final boolean cancelButtonEnable,
			final boolean isSendingEmailRelay, final Class emailHandler) {
		this.setWidth("600px");
		setSpacing(true);

		type = typeVal;
		typeid = typeidVal;
		extraTypeId = extraTypeIdVal;

		commentArea = new RichTextArea();
		commentArea.setWidth("100%");
		commentArea.setHeight("200px");

		final AttachmentPanel attachments = new AttachmentPanel();

		final HorizontalLayout controlsLayout = new HorizontalLayout();
		controlsLayout.setWidth("100%");
		controlsLayout.setSpacing(true);

		final MultiFileUploadExt uploadExt = new MultiFileUploadExt(attachments);
		uploadExt.addComponent(attachments);
		controlsLayout.addComponent(uploadExt);
		controlsLayout.setComponentAlignment(uploadExt, Alignment.MIDDLE_LEFT);

		final Label emptySpace = new Label();
		controlsLayout.addComponent(emptySpace);
		controlsLayout.setExpandRatio(emptySpace, 1.0f);

		if (cancelButtonEnable) {
			final Button cancelBtn = new Button(
					AppContext.getMessage(GenericI18Enum.BUTTON_CANCEL),
					new Button.ClickListener() {
						private static final long serialVersionUID = 1L;

						@Override
						public void buttonClick(final ClickEvent event) {
							component.cancel();
						}
					});
			cancelBtn.setStyleName(UIConstants.THEME_BLANK_LINK);
			controlsLayout.addComponent(cancelBtn);
			controlsLayout.setComponentAlignment(cancelBtn,
					Alignment.MIDDLE_RIGHT);
		}

		final Button saveBtn = new Button(
				AppContext.getMessage(GenericI18Enum.BUTTON_POST),
				new Button.ClickListener() {
					private static final long serialVersionUID = 1L;

					@Override
					public void buttonClick(final Button.ClickEvent event) {
						final Comment comment = new Comment();
						comment.setComment(commentArea.getValue());
						comment.setCreatedtime(new GregorianCalendar()
								.getTime());
						comment.setCreateduser(AppContext.getUsername());
						comment.setSaccountid(AppContext.getAccountId());
						comment.setType(type.toString());
						comment.setTypeid(typeid);
						comment.setExtratypeid(extraTypeId);

						final CommentService commentService = ApplicationContextUtil
								.getSpringBean(CommentService.class);
						int commentId = 0;
						if (isSendingEmailRelay) {
							commentId = commentService.saveWithSession(comment,
									AppContext.getUsername(),
									isSendingEmailRelay, emailHandler);
						} else {
							commentId = commentService.saveWithSession(comment,
									AppContext.getUsername(), false,
									emailHandler);
						}

						String attachmentPath = "";
						if (CommentType.CRM_NOTE.equals(type)) {
							attachmentPath = AttachmentUtils
									.getCrmNoteCommentAttachmentPath(
											AppContext.getAccountId(),
											Integer.parseInt(typeid), commentId);
						} else {
							// do nothing
						}

						if (!"".equals(attachmentPath)) {
							attachments.saveContentsToRepo(attachmentPath);
						}

						// save success, clear comment area and load list
						// comments again
						commentArea.setValue("");
						attachments.removeAllAttachmentsDisplay();
						component.reload();
					}
				});
		saveBtn.setStyleName(UIConstants.THEME_GREEN_LINK);
		saveBtn.setIcon(MyCollabResource.newResource("icons/16/post.png"));
		controlsLayout.addComponent(saveBtn);

		VerticalLayout editBox = new VerticalLayout();
		editBox.setMargin(true);
		editBox.setSpacing(true);

		HorizontalLayout commentWrap = new HorizontalLayout();
		commentWrap.setSpacing(true);
		commentWrap.addStyleName("message");
		commentWrap.setWidth("100%");

		SimpleUser currentUser = AppContext.getSession();
		VerticalLayout userBlock = new VerticalLayout();
		userBlock.setDefaultComponentAlignment(Alignment.TOP_CENTER);
		userBlock.setWidth("80px");
		userBlock.setSpacing(true);
		userBlock.addComponent(UserAvatarControlFactory
				.createUserAvatarButtonLink(currentUser.getAvatarid(),
						currentUser.getDisplayName()));
		Label userName = new Label(currentUser.getDisplayName());
		userName.setStyleName("user-name");
		userBlock.addComponent(userName);

		commentWrap.addComponent(userBlock);
		VerticalLayout textAreaWrap = new VerticalLayout();
		textAreaWrap.setStyleName("message-container");
		textAreaWrap.setWidth("100%");
		textAreaWrap.addComponent(editBox);

		commentWrap.addComponent(textAreaWrap);
		commentWrap.setExpandRatio(textAreaWrap, 1.0f);

		editBox.addComponent(commentArea);
		editBox.addComponent(controlsLayout);
		this.addComponent(commentWrap);
	}

	void setTypeAndId(final CommentType type, final String typeid) {
		this.type = type;
		this.typeid = typeid;
	}
}

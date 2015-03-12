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

import com.esofthead.mycollab.common.CommentType;
import com.esofthead.mycollab.common.domain.CommentWithBLOBs;
import com.esofthead.mycollab.common.i18n.GenericI18Enum;
import com.esofthead.mycollab.common.service.CommentService;
import com.esofthead.mycollab.module.file.AttachmentUtils;
import com.esofthead.mycollab.module.user.domain.SimpleUser;
import com.esofthead.mycollab.spring.ApplicationContextUtil;
import com.esofthead.mycollab.vaadin.AppContext;
import com.esofthead.mycollab.vaadin.ui.AttachmentPanel;
import com.esofthead.mycollab.vaadin.ui.ReloadableComponent;
import com.esofthead.mycollab.vaadin.ui.UIConstants;
import com.esofthead.mycollab.vaadin.ui.UserAvatarControlFactory;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.*;
import com.vaadin.ui.Button.ClickEvent;
import org.jsoup.Jsoup;
import org.jsoup.safety.Whitelist;
import org.vaadin.easyuploads.MultiFileUploadExt;
import org.vaadin.maddon.layouts.MHorizontalLayout;
import org.vaadin.maddon.layouts.MVerticalLayout;

import java.util.GregorianCalendar;

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

	public CommentInput(final ReloadableComponent component,
			final CommentType typeVal, final String typeidVal,
			final Integer extraTypeIdVal, final boolean cancelButtonEnable,
			final boolean isSendingEmailRelay) {
		this(component, typeVal, typeidVal, extraTypeIdVal, cancelButtonEnable,
				isSendingEmailRelay, null);
	}

	@SuppressWarnings("rawtypes")
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

		final MHorizontalLayout controlsLayout = new MHorizontalLayout().withWidth("100%");

		final MultiFileUploadExt uploadExt = new MultiFileUploadExt(attachments);
		uploadExt.addComponent(attachments);
		controlsLayout.with(uploadExt).withAlign(uploadExt, Alignment.TOP_LEFT).expand(uploadExt);

		final Button saveBtn = new Button(
				AppContext.getMessage(GenericI18Enum.BUTTON_POST),
				new Button.ClickListener() {
					private static final long serialVersionUID = 1L;

					@Override
					public void buttonClick(final Button.ClickEvent event) {
						final CommentWithBLOBs comment = new CommentWithBLOBs();
						comment.setComment(Jsoup.clean(commentArea.getValue(),
								Whitelist.relaxed()));
						comment.setCreatedtime(new GregorianCalendar()
								.getTime());
						comment.setCreateduser(AppContext.getUsername());
						comment.setSaccountid(AppContext.getAccountId());
						comment.setType(type.toString());
						comment.setTypeid(typeid);
						comment.setExtratypeid(extraTypeId);

						final CommentService commentService = ApplicationContextUtil
								.getSpringBean(CommentService.class);
						int commentId;
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
		saveBtn.setIcon(FontAwesome.SEND);
		controlsLayout.with(saveBtn).withAlign(saveBtn, Alignment.TOP_RIGHT);

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
            cancelBtn.setStyleName(UIConstants.THEME_GRAY_LINK);
            controlsLayout.addComponent(cancelBtn);
            controlsLayout.setComponentAlignment(cancelBtn,
                    Alignment.TOP_RIGHT);
        }

		MVerticalLayout editBox = new MVerticalLayout();

		MHorizontalLayout commentWrap = new MHorizontalLayout().withWidth("100%");
		commentWrap.addStyleName("message");

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
}

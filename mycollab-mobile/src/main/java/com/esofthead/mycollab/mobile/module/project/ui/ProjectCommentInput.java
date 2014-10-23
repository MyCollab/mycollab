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

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.GregorianCalendar;

import javax.imageio.ImageIO;

import org.apache.log4j.Logger;

import com.esofthead.mycollab.common.CommentType;
import com.esofthead.mycollab.common.domain.Comment;
import com.esofthead.mycollab.common.i18n.GenericI18Enum;
import com.esofthead.mycollab.common.service.CommentService;
import com.esofthead.mycollab.core.utils.ImageUtil;
import com.esofthead.mycollab.mobile.module.project.CurrentProjectVariables;
import com.esofthead.mycollab.mobile.ui.IconConstants;
import com.esofthead.mycollab.mobile.ui.MobileAttachmentUtils;
import com.esofthead.mycollab.module.ecm.service.ResourceService;
import com.esofthead.mycollab.module.file.AttachmentUtils;
import com.esofthead.mycollab.schedule.email.SendingRelayEmailNotificationAction;
import com.esofthead.mycollab.spring.ApplicationContextUtil;
import com.esofthead.mycollab.vaadin.AppContext;
import com.esofthead.mycollab.vaadin.ui.ReloadableComponent;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.ProgressBar;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.UI;
import com.vaadin.ui.Upload;
import com.vaadin.ui.Upload.FailedEvent;
import com.vaadin.ui.Upload.FinishedEvent;
import com.vaadin.ui.Upload.StartedEvent;
import com.vaadin.ui.Upload.SucceededEvent;
import com.vaadin.ui.VerticalLayout;

/**
 * @author MyCollab Ltd.
 *
 * @since 4.4.0
 *
 */
public class ProjectCommentInput extends VerticalLayout implements
		Upload.StartedListener, Upload.ProgressListener,
		Upload.SucceededListener, Upload.FailedListener,
		Upload.FinishedListener {

	private static final long serialVersionUID = 8118887310759503892L;

	private TextArea commentInput;

	private CommentType type;
	private String typeid;
	private Integer extraTypeId;

	private final ProjectAttachmentReceiver receiver;
	private ResourceService resourceService;

	private ProgressBar uploadProgress;
	private Label uploadResult;

	private int currentPollInterval;

	Logger log = Logger.getLogger(ProjectCommentInput.class.getName());

	private HorizontalLayout inputWrapper;

	private final CssLayout statusWrapper;

	public ProjectCommentInput(
			final ReloadableComponent component,
			final CommentType typeVal,
			final Integer extraTypeIdVal,
			final boolean cancelButtonEnable,
			final boolean isSendingEmailRelay,
			final Class<? extends SendingRelayEmailNotificationAction> emailHandler) {
		this.setWidth("100%");

		statusWrapper = new CssLayout();
		statusWrapper.setWidth("100%");
		statusWrapper.setStyleName("upload-status-wrap");
		this.addComponent(statusWrapper);

		inputWrapper = new HorizontalLayout();
		inputWrapper.setWidth("100%");
		inputWrapper.setStyleName("comment-box");
		inputWrapper.setSpacing(true);
		inputWrapper.setDefaultComponentAlignment(Alignment.MIDDLE_LEFT);

		resourceService = ApplicationContextUtil
				.getSpringBean(ResourceService.class);

		type = typeVal;
		extraTypeId = extraTypeIdVal;

		currentPollInterval = UI.getCurrent().getPollInterval();

		receiver = new ProjectAttachmentReceiver();

		Upload attachmentBtn = new Upload(null, receiver);
		attachmentBtn.setButtonCaption("");
		attachmentBtn.setImmediate(true);
		attachmentBtn.addStartedListener(this);
		attachmentBtn.addProgressListener(this);
		attachmentBtn.addFailedListener(this);
		attachmentBtn.addSucceededListener(this);

		inputWrapper.addComponent(attachmentBtn);

		commentInput = new TextArea();
		commentInput.setInputPrompt(AppContext
				.getMessage(GenericI18Enum.M_NOTE_INPUT_PROMPT));
		commentInput.setSizeFull();
		inputWrapper.addComponent(commentInput);
		inputWrapper.setExpandRatio(commentInput, 1.0f);

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

				if (receiver.getFile() != null) {
					String attachmentPath = AttachmentUtils
							.getProjectEntityCommentAttachmentPath(typeVal,
									AppContext.getAccountId(),
									CurrentProjectVariables.getProjectId(),
									typeid, commentId);
					if (!"".equals(attachmentPath)) {
						saveContentToRepo(attachmentPath);
					}
				}

				// save success, clear comment area and load list
				// comments again
				commentInput.setValue("");
				statusWrapper.removeAllComponents();
				component.reload();
			}

		});
		inputWrapper.addComponent(postBtn);
		this.addComponent(inputWrapper);
	}

	private void saveContentToRepo(String attachmentPath) {
		try {
			String fileName = receiver.getFileName();
			String fileExt = "";
			int index = fileName.lastIndexOf(".");
			if (index > 0) {
				fileExt = fileName.substring(index + 1, fileName.length());
			}

			if ("jpg".equalsIgnoreCase(fileExt)
					|| "png".equalsIgnoreCase(fileExt)) {
				try {
					BufferedImage bufferedImage = ImageIO.read(receiver
							.getFile());

					int imgHeight = bufferedImage.getHeight();
					int imgWidth = bufferedImage.getWidth();

					BufferedImage scaledImage = null;

					float scale;
					float destWidth = 974;
					float destHeight = 718;

					float scaleX = Math.min(destHeight / imgHeight, 1);
					float scaleY = Math.min(destWidth / imgWidth, 1);
					scale = Math.min(scaleX, scaleY);
					scaledImage = ImageUtil.scaleImage(bufferedImage, scale);

					ByteArrayOutputStream outStream = new ByteArrayOutputStream();
					ImageIO.write(scaledImage, fileExt, outStream);

					resourceService.saveContent(MobileAttachmentUtils
							.constructContent(fileName, attachmentPath),
							AppContext.getUsername(), new ByteArrayInputStream(
									outStream.toByteArray()), AppContext
									.getAccountId());
				} catch (IOException e) {
					log.error("Error in upload file", e);
					resourceService.saveContent(MobileAttachmentUtils
							.constructContent(fileName, attachmentPath),
							AppContext.getUsername(), new FileInputStream(
									receiver.getFile()), AppContext
									.getAccountId());
				}
			} else {
				resourceService.saveContent(MobileAttachmentUtils
						.constructContent(fileName, attachmentPath), AppContext
						.getUsername(),
						new FileInputStream(receiver.getFile()), AppContext
								.getAccountId());
			}

		} catch (FileNotFoundException e) {
			log.error("Error when attach content in UI", e);
		}
	}

	public void setTypeAndId(final String typeid) {
		this.typeid = typeid;
	}

	@Override
	public void updateProgress(long readBytes, long contentLength) {
		uploadProgress.setValue(new Float(readBytes / (float) contentLength));
	}

	@Override
	public void uploadStarted(StartedEvent event) {
		statusWrapper.removeAllComponents();
		uploadProgress = new ProgressBar(0f);
		uploadProgress.setStyleName("upload-progress");
		uploadProgress.setWidth("100%");
		statusWrapper.addComponent(uploadProgress);
		UI.getCurrent().setPollInterval(500);
	}

	@Override
	public void uploadFailed(FailedEvent event) {
		statusWrapper.removeAllComponents();
		uploadResult = new Label("Error! Upload failed!");
		uploadResult.setStyleName("upload-status");
		statusWrapper.addComponent(uploadResult);
	}

	@Override
	public void uploadSucceeded(SucceededEvent event) {
		statusWrapper.removeAllComponents();
		HorizontalLayout uploadSucceedLayout = new HorizontalLayout();
		uploadSucceedLayout.setWidth("100%");
		uploadResult = new Label(event.getFilename());
		uploadResult.setWidth("100%");
		uploadSucceedLayout.addComponent(uploadResult);
		uploadSucceedLayout.setExpandRatio(uploadResult, 1.0f);

		Button removeAttachment = new Button(
				"<span aria-hidden=\"true\" data-icon=\""
						+ IconConstants.DELETE + "\"></span>",
				new Button.ClickListener() {

					private static final long serialVersionUID = 1L;

					@Override
					public void buttonClick(ClickEvent event) {
						receiver.clearData();
						statusWrapper.removeAllComponents();
					}

				});
		removeAttachment.setHtmlContentAllowed(true);
		removeAttachment.setStyleName("link");
		uploadSucceedLayout.addComponent(removeAttachment);
		uploadSucceedLayout.setStyleName("upload-succeed-layout");
		uploadSucceedLayout.setSpacing(true);
		statusWrapper.addComponent(uploadSucceedLayout);
	}

	@Override
	public void uploadFinished(FinishedEvent arg0) {
		UI.getCurrent().setPollInterval(currentPollInterval);
	}

}

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
package com.esofthead.mycollab.mobile.ui;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Map;

import javax.imageio.ImageIO;

import org.apache.commons.collections.MapUtils;
import org.apache.log4j.Logger;

import com.esofthead.mycollab.common.i18n.GenericI18Enum;
import com.esofthead.mycollab.core.utils.ImageUtil;
import com.esofthead.mycollab.module.ecm.domain.Content;
import com.esofthead.mycollab.module.ecm.service.ResourceService;
import com.esofthead.mycollab.spring.ApplicationContextUtil;
import com.esofthead.mycollab.vaadin.AppContext;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Component;
import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.UI;

/**
 * @author MyCollab Ltd.
 *
 * @since 4.5.2
 */
public class MobileAttachmentUtils {

	private static final Logger LOG = Logger.getLogger(MobileAttachmentUtils.class
			.getName());

	public static Component renderAttachmentRow(final Content attachment) {
		// HorizontalLayout attachmentRow = new HorizontalLayout();
		// attachmentRow.setStyleName("attachment-row");
		// attachmentRow.setWidth("100%");
		// attachmentRow.setHeightUndefined();
		// attachmentRow.setSpacing(true);

		String docName = attachment.getPath();
		int lastIndex = docName.lastIndexOf("/");
		if (lastIndex != -1) {
			docName = docName.substring(lastIndex + 1, docName.length());
		}

		Label attachmentName = new Label(docName);
		attachmentName.setStyleName("attachment-name");
		attachmentName.setWidth("100%");
		// attachmentRow.addComponent(attachmentName);

		// Button downloadBtn = new Button(
		// "<span class=\"nav-btn-icon\" aria-hidden=\"true\" data-icon=\""
		// + IconConstants.DOWNLOAD + "\"></span>");
		// downloadBtn.setStyleName("link");
		// downloadBtn.setHtmlContentAllowed(true);
		// FileDownloader fileDownloader = new FileDownloader(
		// VaadinResourceManager.getResourceManager().getStreamResource(
		// attachment.getPath()));
		// fileDownloader.extend(downloadBtn);
		// attachmentRow.addComponent(downloadBtn);
		// attachmentRow.setExpandRatio(downloadBtn, 1.0f);
		// attachmentRow.setComponentAlignment(downloadBtn,
		// Alignment.MIDDLE_LEFT);

		return attachmentName;
	}

	public static Component renderAttachmentFieldRow(final Content attachment,
			Button.ClickListener additionalListener) {
		String docName = attachment.getPath();
		int lastIndex = docName.lastIndexOf("/");
		if (lastIndex != -1) {
			docName = docName.substring(lastIndex + 1, docName.length());
		}

		final HorizontalLayout attachmentLayout = new HorizontalLayout();
		attachmentLayout.setSpacing(true);
		attachmentLayout.setStyleName("attachment-row");
		attachmentLayout.setWidth("100%");
		attachmentLayout.setDefaultComponentAlignment(Alignment.MIDDLE_LEFT);

		Label attachmentLink = new Label(docName);
		attachmentLayout.addComponent(attachmentLink);
		attachmentLayout.setExpandRatio(attachmentLink, 1.0f);

		Button removeAttachment = new Button(
				"<span aria-hidden=\"true\" data-icon=\""
						+ IconConstants.DELETE + "\"></span>",
				new Button.ClickListener() {
					private static final long serialVersionUID = 1L;

					@Override
					public void buttonClick(ClickEvent event) {

						ConfirmDialog.show(
								UI.getCurrent(),
								AppContext
										.getMessage(GenericI18Enum.CONFIRM_DELETE_ATTACHMENT),
								AppContext
										.getMessage(GenericI18Enum.BUTTON_YES),
								AppContext.getMessage(GenericI18Enum.BUTTON_NO),
								new ConfirmDialog.CloseListener() {
									private static final long serialVersionUID = 1L;

									@Override
									public void onClose(ConfirmDialog dialog) {
										if (dialog.isConfirmed()) {
											ResourceService attachmentService = ApplicationContextUtil
													.getSpringBean(ResourceService.class);
											attachmentService.removeResource(
													attachment.getPath(),
													AppContext.getUsername(),
													AppContext.getAccountId());
											((ComponentContainer) attachmentLayout
													.getParent())
													.removeComponent(attachmentLayout);
										}
									}
								});

					}
				});
		if (additionalListener != null)
			removeAttachment.addClickListener(additionalListener);
		removeAttachment.setHtmlContentAllowed(true);
		removeAttachment.setStyleName("link");
		attachmentLayout.addComponent(removeAttachment);

		return attachmentLayout;
	}

	public static Component renderAttachmentFieldRow(final Content attachment) {
		return renderAttachmentFieldRow(attachment, null);
	}

	public static void saveContentsToRepo(String attachmentPath,
			Map<String, File> fileStores) {
		if (MapUtils.isNotEmpty(fileStores)) {
			ResourceService resourceService = ApplicationContextUtil
					.getSpringBean(ResourceService.class);
			for (String fileName : fileStores.keySet()) {
				try {
					String fileExt = "";
					int index = fileName.lastIndexOf(".");
					if (index > 0) {
						fileExt = fileName.substring(index + 1,
								fileName.length());
					}

					if ("jpg".equalsIgnoreCase(fileExt)
							|| "png".equalsIgnoreCase(fileExt)) {
						try {
							BufferedImage bufferedImage = ImageIO
									.read(fileStores.get(fileName));

							int imgHeight = bufferedImage.getHeight();
							int imgWidth = bufferedImage.getWidth();

							BufferedImage scaledImage = null;

							float scale;
							float destWidth = 974;
							float destHeight = 718;

							float scaleX = Math.min(destHeight / imgHeight, 1);
							float scaleY = Math.min(destWidth / imgWidth, 1);
							scale = Math.min(scaleX, scaleY);
							scaledImage = ImageUtil.scaleImage(bufferedImage,
									scale);

							ByteArrayOutputStream outStream = new ByteArrayOutputStream();
							ImageIO.write(scaledImage, fileExt, outStream);

							resourceService.saveContent(
									constructContent(fileName, attachmentPath),
									AppContext.getUsername(),
									new ByteArrayInputStream(outStream
											.toByteArray()), AppContext
											.getAccountId());
						} catch (IOException e) {
							LOG.error("Error in upload file", e);
							resourceService.saveContent(
									constructContent(fileName, attachmentPath),
									AppContext.getUsername(),
									new FileInputStream(fileStores
											.get(fileName)), AppContext
											.getAccountId());
						}
					} else {
						resourceService.saveContent(
								constructContent(fileName, attachmentPath),
								AppContext.getUsername(), new FileInputStream(
										fileStores.get(fileName)), AppContext
										.getAccountId());
					}

				} catch (FileNotFoundException e) {
					LOG.error("Error when attach content in UI", e);
				}
			}
		}
	}

	public static Content constructContent(String fileName, String path) {
		Content content = new Content(path + "/" + fileName);
		content.setTitle(fileName);
		content.setDescription("");
		return content;
	}
}

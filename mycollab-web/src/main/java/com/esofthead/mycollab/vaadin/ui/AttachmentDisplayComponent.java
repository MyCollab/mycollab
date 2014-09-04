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

package com.esofthead.mycollab.vaadin.ui;

import java.util.List;

import org.vaadin.dialogs.ConfirmDialog;

import com.esofthead.mycollab.common.i18n.GenericI18Enum;
import com.esofthead.mycollab.configuration.SiteConfiguration;
import com.esofthead.mycollab.core.utils.MimeTypesUtil;
import com.esofthead.mycollab.core.utils.StringUtils;
import com.esofthead.mycollab.module.ecm.domain.Content;
import com.esofthead.mycollab.module.ecm.service.ResourceService;
import com.esofthead.mycollab.module.file.resource.StreamDownloadResourceUtil;
import com.esofthead.mycollab.spring.ApplicationContextUtil;
import com.esofthead.mycollab.vaadin.AppContext;
import com.vaadin.server.FileDownloader;
import com.vaadin.server.Resource;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Component;
import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.Embedded;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

/**
 * 
 * @author MyCollab Ltd.
 * @since 2.0
 * 
 */
public class AttachmentDisplayComponent extends VerticalLayout {
	private static final long serialVersionUID = 1L;

	public AttachmentDisplayComponent(List<Content> attachments) {
		for (Content attachment : attachments) {
			this.addComponent(constructAttachmentRow(attachment));
		}
	}

	public static Component constructAttachmentRow(final Content attachment) {
		String docName = attachment.getPath();
		int lastIndex = docName.lastIndexOf("/");
		if (lastIndex != -1) {
			docName = docName.substring(lastIndex + 1, docName.length());
		}

		final HorizontalLayout attachmentLayout = new HorizontalLayout();
		attachmentLayout.setSpacing(true);
		attachmentLayout.setMargin(new MarginInfo(false, false, false, true));

		Embedded fileTypeIcon = new Embedded(null,
				UiUtils.getFileIconResource(docName));
		attachmentLayout.addComponent(fileTypeIcon);

		Label attachmentLink = new Label(StringUtils.trim(docName, 60, true));
		attachmentLayout.addComponent(attachmentLink);
		attachmentLayout.setComponentAlignment(attachmentLink,
				Alignment.MIDDLE_CENTER);

		if (MimeTypesUtil.isImage(docName)) {

			Button previewBtn = new Button(null, new Button.ClickListener() {

				private static final long serialVersionUID = 1L;

				@Override
				public void buttonClick(ClickEvent event) {
					Resource previewResource = StreamDownloadResourceUtil
							.getImagePreviewResource(attachment.getPath());
					UI.getCurrent().addWindow(
							new AttachmentPreviewWindow(previewResource));
				}
			});
			previewBtn.setIcon(MyCollabResource
					.newResource("icons/16/preview.png"));
			previewBtn.setStyleName("link");
			attachmentLayout.addComponent(previewBtn);
		}

		Button trashBtn = new Button(null, new Button.ClickListener() {
			private static final long serialVersionUID = 1L;

			@Override
			public void buttonClick(ClickEvent event) {

				ConfirmDialogExt.show(UI.getCurrent(), AppContext.getMessage(
						GenericI18Enum.DIALOG_DELETE_TITLE,
						SiteConfiguration.getSiteName()), AppContext
						.getMessage(GenericI18Enum.CONFIRM_DELETE_ATTACHMENT),
						AppContext.getMessage(GenericI18Enum.BUTTON_YES_LABEL),
						AppContext.getMessage(GenericI18Enum.BUTTON_NO_LABEL),
						new ConfirmDialog.Listener() {
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
		trashBtn.setIcon(MyCollabResource.newResource("icons/16/trash.png"));
		trashBtn.setStyleName("link");
		attachmentLayout.addComponent(trashBtn);

		Button downloadBtn = new Button();
		FileDownloader fileDownloader = new FileDownloader(
				StreamDownloadResourceUtil.getStreamResource(attachment
						.getPath()));
		fileDownloader.extend(downloadBtn);

		downloadBtn.setIcon(MyCollabResource
				.newResource("icons/16/download.png"));
		downloadBtn.setStyleName("link");
		attachmentLayout.addComponent(downloadBtn);
		return attachmentLayout;
	}
}

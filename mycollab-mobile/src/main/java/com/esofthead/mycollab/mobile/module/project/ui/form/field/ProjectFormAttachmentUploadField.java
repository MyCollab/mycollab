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
package com.esofthead.mycollab.mobile.module.project.ui.form.field;

import java.io.File;
import java.io.OutputStream;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.vaadin.easyuploads.FileBuffer;
import org.vaadin.easyuploads.FileFactory;
import org.vaadin.easyuploads.MultiUpload;
import org.vaadin.easyuploads.MultiUploadHandler;
import org.vaadin.easyuploads.UploadField;

import com.esofthead.mycollab.mobile.ui.MobileAttachmentUtils;
import com.esofthead.mycollab.mobile.ui.TempFileFactory;
import com.esofthead.mycollab.module.ecm.domain.Content;
import com.esofthead.mycollab.module.ecm.service.ResourceService;
import com.esofthead.mycollab.module.file.AttachmentType;
import com.esofthead.mycollab.module.file.AttachmentUtils;
import com.esofthead.mycollab.spring.ApplicationContextUtil;
import com.esofthead.mycollab.vaadin.AppContext;
import com.esofthead.mycollab.vaadin.ui.NotificationUtil;
import com.vaadin.server.StreamVariable;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.CustomField;
import com.vaadin.ui.Label;
import com.vaadin.ui.ProgressBar;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

/**
 * 
 * @author MyCollab Ltd.
 * @since 4.5.3
 *
 */
@SuppressWarnings({ "rawtypes" })
public class ProjectFormAttachmentUploadField extends CustomField {
	private static final long serialVersionUID = 1L;
	private MultiUpload attachmentBtn;
	private Map<String, File> fileStores;
	private FileBuffer receiver;

	private VerticalLayout content;
	private ResourceService resourceService;
	private int currentPollInterval;
	private String attachmentPath;

	public ProjectFormAttachmentUploadField() {
		content = new VerticalLayout();
		content.setStyleName("attachment-field");

		resourceService = ApplicationContextUtil
				.getSpringBean(ResourceService.class);
		currentPollInterval = UI.getCurrent().getPollInterval();

		receiver = createReceiver();

		attachmentBtn = new MultiUpload();
		attachmentBtn.setButtonCaption("Select File...");
		attachmentBtn.setImmediate(true);

		MultiUploadHandler handler = new MultiUploadHandler() {
			private LinkedList<ProgressBar> indicators;

			@Override
			public void streamingStarted(
					StreamVariable.StreamingStartEvent event) {
			}

			@Override
			public void streamingFinished(StreamVariable.StreamingEndEvent event) {
				String tempName = event.getFileName();
				final String fileName;
				int index = tempName.lastIndexOf(".");
				if (index > 0) {
					String fileExt = tempName.substring(index + 1,
							tempName.length());
					fileName = MobileAttachmentUtils.ATTACHMENT_NAME_PREFIX
							+ System.currentTimeMillis() + "." + fileExt;
				} else {
					fileName = MobileAttachmentUtils.ATTACHMENT_NAME_PREFIX
							+ System.currentTimeMillis();
				}
				if (!indicators.isEmpty()) {
					content.replaceComponent(indicators.remove(0),
							MobileAttachmentUtils.renderAttachmentFieldRow(
									MobileAttachmentUtils.constructContent(
											fileName, attachmentPath),
									new Button.ClickListener() {

										private static final long serialVersionUID = 581451358291203810L;

										@Override
										public void buttonClick(
												Button.ClickEvent event) {
											fileStores.remove(fileName);
										}
									}));
				}

				if (indicators.size() == 0) {
					UI.getCurrent().setPollInterval(currentPollInterval);
				}

				File file = receiver.getFile();

				receiveFile(file, fileName, event.getMimeType(),
						event.getBytesReceived());
				receiver.setValue(null);

			}

			@Override
			public void streamingFailed(StreamVariable.StreamingErrorEvent event) {
				if (!indicators.isEmpty()) {
					Label uploadResult = new Label("Upload failed! File: "
							+ event.getFileName());
					uploadResult.setStyleName("upload-status");
					content.replaceComponent(indicators.remove(0), uploadResult);
				}
			}

			@Override
			public void onProgress(StreamVariable.StreamingProgressEvent event) {
				long readBytes = event.getBytesReceived();
				long contentLength = event.getContentLength();
				float f = (float) readBytes / (float) contentLength;
				indicators.get(0).setValue(f);
			}

			@Override
			public OutputStream getOutputStream() {
				MultiUpload.FileDetail next = attachmentBtn
						.getPendingFileNames().iterator().next();
				return receiver.receiveUpload(next.getFileName(),
						next.getMimeType());
			}

			@Override
			public void filesQueued(
					Collection<MultiUpload.FileDetail> pendingFileNames) {
				UI.getCurrent().setPollInterval(500);
				if (indicators == null) {
					indicators = new LinkedList<ProgressBar>();
				}
				for (MultiUpload.FileDetail f : pendingFileNames) {
					ProgressBar pi = new ProgressBar();
					pi.setValue(0f);
					pi.setStyleName("upload-progress");
					pi.setWidth("100%");
					content.addComponent(pi);
					pi.setEnabled(true);
					pi.setVisible(true);
					indicators.add(pi);
				}
			}

			@Override
			public boolean isInterrupted() {
				return false;
			}
		};
		attachmentBtn.setHandler(handler);

		content.addComponent(attachmentBtn);

		fileStores = new HashMap<String, File>();
	}

	public void getAttachments(int projectId, AttachmentType type, int typeid) {
		attachmentPath = AttachmentUtils.getProjectEntityAttachmentPath(
				AppContext.getAccountId(), projectId, type, typeid);
		List<Content> attachments = resourceService.getContents(attachmentPath);
		if (CollectionUtils.isNotEmpty(attachments)) {
			for (final Content attachment : attachments) {
				content.addComponent(MobileAttachmentUtils
						.renderAttachmentFieldRow(attachment));
			}
		}
	}

	@Override
	public Class<?> getType() {
		return Object.class;
	}

	public void saveContentsToRepo() {
		MobileAttachmentUtils.saveContentsToRepo(attachmentPath, fileStores);
	}

	public void saveContentsToRepo(int projectId, AttachmentType type,
			int typeid) {
		attachmentPath = AttachmentUtils.getProjectEntityAttachmentPath(
				AppContext.getAccountId(), projectId, type, typeid);
		MobileAttachmentUtils.saveContentsToRepo(attachmentPath, fileStores);
	}

	@Override
	protected Component initContent() {
		return content;
	}

	protected FileBuffer createReceiver() {
		FileBuffer receiver = new FileBuffer(UploadField.FieldType.FILE) {
			private static final long serialVersionUID = 1L;

			@Override
			public FileFactory getFileFactory() {
				return new TempFileFactory();
			}
		};
		receiver.setDeleteFiles(false);
		return receiver;
	}

	public void receiveFile(File file, String fileName, String mimeType,
			long length) {
		if (fileStores == null) {
			fileStores = new HashMap<String, File>();
		}
		if (fileStores.containsKey(fileName)) {
			NotificationUtil.showWarningNotification("File " + fileName
					+ " is already existed.");
		} else {
			fileStores.put(fileName, file);
		}
	}
}

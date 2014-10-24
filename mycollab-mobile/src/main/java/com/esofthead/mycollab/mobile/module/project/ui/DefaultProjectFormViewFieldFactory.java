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

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;

import com.esofthead.mycollab.mobile.ui.MobileAttachmentUtils;
import com.esofthead.mycollab.module.ecm.domain.Content;
import com.esofthead.mycollab.module.ecm.service.ResourceService;
import com.esofthead.mycollab.module.file.AttachmentType;
import com.esofthead.mycollab.module.file.AttachmentUtils;
import com.esofthead.mycollab.spring.ApplicationContextUtil;
import com.esofthead.mycollab.vaadin.AppContext;
import com.esofthead.mycollab.vaadin.ui.NotificationUtil;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.CustomField;
import com.vaadin.ui.Label;
import com.vaadin.ui.ProgressBar;
import com.vaadin.ui.UI;
import com.vaadin.ui.Upload;
import com.vaadin.ui.Upload.FailedEvent;
import com.vaadin.ui.Upload.FinishedEvent;
import com.vaadin.ui.Upload.StartedEvent;
import com.vaadin.ui.Upload.SucceededEvent;
import com.vaadin.ui.VerticalLayout;

/**
 * 
 * @author MyCollab Ltd.
 * @since 4.5.2
 * 
 */
@SuppressWarnings("rawtypes")
public class DefaultProjectFormViewFieldFactory {

	public static class ProjectFormAttachmentDisplayField extends CustomField {
		private static final long serialVersionUID = 1L;

		private int projectid;
		private AttachmentType type;
		private int typeid;

		public ProjectFormAttachmentDisplayField(final int projectid,
				final AttachmentType type, final int typeid) {
			this.projectid = projectid;
			this.type = type;
			this.typeid = typeid;
		}

		@Override
		public Class<?> getType() {
			return Object.class;
		}

		@Override
		protected Component initContent() {
			ResourceService resourceService = ApplicationContextUtil
					.getSpringBean(ResourceService.class);
			List<Content> attachments = resourceService
					.getContents(AttachmentUtils
							.getProjectEntityAttachmentPath(
									AppContext.getAccountId(), projectid, type,
									typeid));
			if (CollectionUtils.isNotEmpty(attachments)) {
				VerticalLayout comp = new VerticalLayout();
				comp.setStyleName("attachment-view-panel");

				for (Content attachment : attachments) {
					Label l = new Label(attachment.getTitle());
					l.setWidth("100%");
					comp.addComponent(l);
				}

				return comp;
			}
			final Label l = new Label("&nbsp;", ContentMode.HTML);
			return l;
		}
	}

	public static class ProjectFormAttachmentUploadField extends CustomField
			implements Upload.StartedListener, Upload.ProgressListener,
			Upload.SucceededListener, Upload.FailedListener,
			Upload.FinishedListener {
		private static final long serialVersionUID = 1L;
		private Upload attachmentBtn;
		private Map<String, File> fileStores;
		private ProjectAttachmentReceiver receiver;

		private VerticalLayout content;
		private ResourceService resourceService;
		private ProgressBar uploadProgress;
		private Label uploadResult;
		private int currentPollInterval;
		private String attachmentPath;

		public ProjectFormAttachmentUploadField() {
			content = new VerticalLayout();
			content.setStyleName("attachment-field");

			resourceService = ApplicationContextUtil
					.getSpringBean(ResourceService.class);
			currentPollInterval = UI.getCurrent().getPollInterval();

			receiver = new ProjectAttachmentReceiver();

			attachmentBtn = new Upload(null, receiver);
			attachmentBtn.setButtonCaption("Select File...");
			attachmentBtn.setImmediate(true);
			attachmentBtn.addStartedListener(this);
			attachmentBtn.addProgressListener(this);
			attachmentBtn.addFailedListener(this);
			attachmentBtn.addSucceededListener(this);

			content.addComponent(attachmentBtn);

			fileStores = new HashMap<String, File>();
		}

		public void getAttachments(int projectId, AttachmentType type,
				int typeid) {
			attachmentPath = AttachmentUtils.getProjectEntityAttachmentPath(
					AppContext.getAccountId(), projectId, type, typeid);
			List<Content> attachments = resourceService
					.getContents(attachmentPath);
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
			MobileAttachmentUtils
					.saveContentsToRepo(attachmentPath, fileStores);
		}

		public void saveContentsToRepo(int projectId, AttachmentType type,
				int typeid) {
			attachmentPath = AttachmentUtils.getProjectEntityAttachmentPath(
					AppContext.getAccountId(), projectId, type, typeid);
			MobileAttachmentUtils
					.saveContentsToRepo(attachmentPath, fileStores);
		}

		@Override
		protected Component initContent() {
			return content;
		}

		@Override
		public void updateProgress(long readBytes, long contentLength) {
			uploadProgress
					.setValue(new Float(readBytes / (float) contentLength));
		}

		@Override
		public void uploadStarted(StartedEvent event) {
			uploadProgress = new ProgressBar(0f);
			uploadProgress.setStyleName("upload-progress");
			uploadProgress.setWidth("100%");
			content.addComponent(uploadProgress);
			UI.getCurrent().setPollInterval(500);
		}

		@Override
		public void uploadFailed(FailedEvent event) {
			uploadResult = new Label("Error! Upload failed!");
			uploadResult.setStyleName("upload-status");
			content.replaceComponent(uploadProgress, uploadResult);
		}

		@Override
		public void uploadSucceeded(SucceededEvent event) {
			final String fileName = event.getFilename();
			if (fileStores.containsKey(fileName)) {
				NotificationUtil.showWarningNotification("File " + fileName
						+ " is already existed.");
				content.removeComponent(uploadProgress);
				receiver.clearData();
				return;
			}
			fileStores.put(fileName, receiver.getFile());
			content.replaceComponent(uploadProgress, MobileAttachmentUtils
					.renderAttachmentFieldRow(
							MobileAttachmentUtils.constructContent(
									event.getFilename(), attachmentPath),
							new Button.ClickListener() {

								private static final long serialVersionUID = 3644051988159897490L;

								@Override
								public void buttonClick(Button.ClickEvent event) {
									fileStores.remove(fileName);
								}
							}));
		}

		@Override
		public void uploadFinished(FinishedEvent event) {
			UI.getCurrent().setPollInterval(currentPollInterval);
		}
	}
}

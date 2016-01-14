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

import com.esofthead.mycollab.common.i18n.GenericI18Enum;
import com.esofthead.mycollab.mobile.ui.MobileAttachmentUtils;
import com.esofthead.mycollab.mobile.ui.TempFileFactory;
import com.esofthead.mycollab.module.ecm.domain.Content;
import com.esofthead.mycollab.module.ecm.service.ResourceService;
import com.esofthead.mycollab.module.file.AttachmentUtils;
import com.esofthead.mycollab.spring.ApplicationContextUtil;
import com.esofthead.mycollab.vaadin.AppContext;
import com.esofthead.mycollab.vaadin.ui.NotificationUtil;
import com.vaadin.server.StreamVariable;
import com.vaadin.ui.*;
import org.apache.commons.collections.CollectionUtils;
import org.vaadin.easyuploads.*;

import java.io.File;
import java.io.OutputStream;
import java.util.*;

/**
 * @author MyCollab Ltd.
 * @since 4.5.3
 */
public class ProjectFormAttachmentUploadField extends CustomField {
    private static final long serialVersionUID = 1L;
    protected MultiUpload attachmentBtn;
    private Map<String, File> fileStores;
    private FileBuffer receiver;

    protected VerticalLayout content;
    protected VerticalLayout rowWrap;
    private ResourceService resourceService;
    private int currentPollInterval;
    private String attachmentPath;

    public ProjectFormAttachmentUploadField() {
        resourceService = ApplicationContextUtil.getSpringBean(ResourceService.class);
        currentPollInterval = UI.getCurrent().getPollInterval();

        receiver = createReceiver();

        attachmentBtn = new MultiUpload();
        attachmentBtn.setButtonCaption("Select File(s)");
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
                    String fileExt = tempName.substring(index + 1, tempName.length());
                    fileName = String.format("%s%d.%s", MobileAttachmentUtils.ATTACHMENT_NAME_PREFIX, System.currentTimeMillis(), fileExt);
                } else {
                    fileName = String.format("%s%d", MobileAttachmentUtils.ATTACHMENT_NAME_PREFIX, System.currentTimeMillis());
                }
                if (!indicators.isEmpty()) {
                    rowWrap.replaceComponent(indicators.remove(0),
                            MobileAttachmentUtils.renderAttachmentFieldRow(MobileAttachmentUtils.constructContent(fileName, attachmentPath),
                                    new Button.ClickListener() {
                                        private static final long serialVersionUID = 581451358291203810L;

                                        @Override
                                        public void buttonClick(Button.ClickEvent event) {
                                            fileStores.remove(fileName);
                                        }
                                    }));
                }

                if (indicators.size() == 0) {
                    UI.getCurrent().setPollInterval(currentPollInterval);
                }

                File file = receiver.getFile();

                receiveFile(file, fileName, event.getMimeType(), event.getBytesReceived());
                receiver.setValue(null);

            }

            @Override
            public void streamingFailed(StreamVariable.StreamingErrorEvent event) {
                if (!indicators.isEmpty()) {
                    Label uploadResult = new Label("Upload failed! File: " + event.getFileName());
                    uploadResult.setStyleName("upload-status");
                    rowWrap.replaceComponent(indicators.remove(0), uploadResult);
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
                MultiUpload.FileDetail next = attachmentBtn.getPendingFileNames().iterator().next();
                return receiver.receiveUpload(next.getFileName(), next.getMimeType());
            }

            @Override
            public void filesQueued(
                    Collection<MultiUpload.FileDetail> pendingFileNames) {
                UI.getCurrent().setPollInterval(500);
                if (indicators == null) {
                    indicators = new LinkedList<>();
                }
                for (MultiUpload.FileDetail f : pendingFileNames) {
                    ProgressBar pi = new ProgressBar();
                    pi.setValue(0f);
                    pi.setStyleName("upload-progress");
                    pi.setWidth("100%");
                    rowWrap.addComponentAsFirst(pi);
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
        fileStores = new HashMap<>();
        constructUI();
    }

    protected void constructUI() {
        content = new VerticalLayout();
        content.setStyleName("attachment-field");

        rowWrap = new VerticalLayout();
        rowWrap.setWidth("100%");
        rowWrap.setStyleName("attachment-row-wrap");

        Label compHeader = new Label(AppContext.getMessage(GenericI18Enum.M_FORM_ATTACHMENT));
        compHeader.setStyleName("h2");

        content.addComponent(compHeader);

        CssLayout btnWrap = new CssLayout();
        btnWrap.setWidth("100%");
        btnWrap.setStyleName("attachment-row");
        btnWrap.addComponent(attachmentBtn);

        content.addComponent(btnWrap);

        content.addComponent(rowWrap);
    }

    public void getAttachments(int projectId, String type, int typeId) {
        attachmentPath = AttachmentUtils.getProjectEntityAttachmentPath(AppContext.getAccountId(), projectId, type, "" + typeId);
        List<Content> attachments = resourceService.getContents(attachmentPath);
        rowWrap.removeAllComponents();
        if (CollectionUtils.isNotEmpty(attachments)) {
            for (final Content attachment : attachments) {
                rowWrap.addComponent(MobileAttachmentUtils.renderAttachmentFieldRow(attachment));
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

    public void saveContentsToRepo(Integer projectId, String type, Integer typeId) {
        attachmentPath = AttachmentUtils.getProjectEntityAttachmentPath(AppContext.getAccountId(), projectId, type, "" + typeId);
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

    public void receiveFile(File file, String fileName, String mimeType, long length) {
        if (fileStores == null) {
            fileStores = new HashMap<>();
        }
        if (fileStores.containsKey(fileName)) {
            NotificationUtil.showWarningNotification("File " + fileName + " is already existed.");
        } else {
            fileStores.put(fileName, file);
        }
    }
}

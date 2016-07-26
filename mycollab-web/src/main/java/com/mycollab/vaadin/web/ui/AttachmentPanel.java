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
package com.mycollab.vaadin.web.ui;

import com.mycollab.core.utils.FileUtils;
import com.mycollab.module.ecm.domain.Content;
import com.mycollab.module.ecm.service.ResourceService;
import com.mycollab.spring.AppContextUtil;
import com.mycollab.vaadin.AppContext;
import com.mycollab.vaadin.resources.file.FileAssetsUtil;
import com.mycollab.vaadin.ui.ELabel;
import com.mycollab.vaadin.ui.NotificationUtil;
import com.mycollab.vaadin.ui.UIConstants;
import com.vaadin.server.FontAwesome;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.VerticalLayout;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.vaadin.easyuploads.MultiFileUploadExt;
import org.vaadin.viritin.button.MButton;
import org.vaadin.viritin.layouts.MHorizontalLayout;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author MyCollab Ltd.
 * @since 2.0
 */
public class AttachmentPanel extends VerticalLayout implements AttachmentUploadComponent {
    private static final long serialVersionUID = 1L;
    private static final Logger LOG = LoggerFactory.getLogger(AttachmentPanel.class);
    private Map<String, File> fileStores;

    private AttachmentDisplayComponent attachmentDisplayComponent;
    private MultiFileUploadExt multiFileUpload;
    private ResourceService resourceService;

    public AttachmentPanel() {
        resourceService = AppContextUtil.getSpringBean(ResourceService.class);
        this.setSpacing(true);
        attachmentDisplayComponent = new AttachmentDisplayComponent();
        this.addComponent(attachmentDisplayComponent);
    }

    @Override
    public void registerMultiUpload(MultiFileUploadExt fileUpload) {
        multiFileUpload = fileUpload;
    }

    private void displayFileName(File file, final String fileName) {
        final MHorizontalLayout fileAttachmentLayout = new MHorizontalLayout().withFullWidth();
        MButton removeBtn = new MButton("", clickEvent -> {
            File tmpFile = fileStores.get(fileName);
            if (tmpFile != null) {
                tmpFile.delete();
            }
            fileStores.remove(fileName);
            AttachmentPanel.this.removeComponent(fileAttachmentLayout);
            if (multiFileUpload != null) {
                multiFileUpload.removeAndReInitMultiUpload();
            }
        }).withIcon(FontAwesome.TRASH_O).withStyleName(WebUIConstants.BUTTON_ICON_ONLY);

        ELabel fileLbl = new ELabel(fileName, ContentMode.HTML).withDescription(fileName).withStyleName(UIConstants.TEXT_ELLIPSIS);
        fileAttachmentLayout.with(new ELabel(FileAssetsUtil.getFileIconResource(fileName).getHtml(), ContentMode.HTML).withWidthUndefined(),
                fileLbl, new ELabel(" - " + FileUtils.getVolumeDisplay(file.length()))
                        .withStyleName(UIConstants.META_INFO).withWidthUndefined(), removeBtn).expand(fileLbl);
        this.addComponent(fileAttachmentLayout);
    }

    public void removeAllAttachmentsDisplay() {
        this.removeAllComponents();
        if (fileStores != null) {
            fileStores.clear();
        }
    }

    public void getAttachments(String attachmentPath) {
        List<Content> attachments = resourceService.getContents(attachmentPath);
        if (CollectionUtils.isNotEmpty(attachments)) {
            for (Content attachment : attachments) {
                attachmentDisplayComponent.addAttachmentRow(attachment);
            }
        }
    }

    public void saveContentsToRepo(String attachmentPath) {
        if (MapUtils.isNotEmpty(fileStores)) {
            for (Map.Entry<String, File> entry : fileStores.entrySet()) {
                try {
                    String fileName = entry.getKey();
                    File file = entry.getValue();
                    resourceService.saveContent(constructContent(fileName, attachmentPath),
                            AppContext.getUsername(), new FileInputStream(file), AppContext.getAccountId());

                } catch (FileNotFoundException e) {
                    LOG.error("Error when attach content in UI", e);
                }
            }
        }
    }

    private Content constructContent(String fileName, String path) {
        Content content = new Content(path + "/" + fileName);
        content.setTitle(fileName);
        content.setDescription("");
        return content;
    }

    public List<File> files() {
        List<File> listFile = null;
        if (MapUtils.isNotEmpty(fileStores)) {
            listFile = new ArrayList<>();
            for (Map.Entry<String, File> entry : fileStores.entrySet()) {
                File oldFile = entry.getValue();
                File parentFile = oldFile.getParentFile();
                File newFile = new File(parentFile, entry.getKey());
                if (newFile.exists())
                    newFile.delete();
                if (oldFile.renameTo(newFile)) {
                    listFile.add(newFile);
                }

                if (listFile.size() <= 0)
                    return null;
            }
        }
        return listFile;
    }

    @Override
    public void receiveFile(File file, String fileName, String mimeType, long length) {
        if (fileStores == null) {
            fileStores = new HashMap<>();
        }
        if (fileStores.containsKey(fileName)) {
            NotificationUtil.showWarningNotification("File " + fileName + " is existed.");
        } else {
            LOG.debug("Store file " + fileName + " in path " + file.getAbsolutePath() + " is existed: " + file.exists());
            fileStores.put(fileName, file);
            displayFileName(file, fileName);
        }
    }
}

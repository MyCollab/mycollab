/**
 * Copyright © MyCollab
 * <p>
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * <p>
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 * <p>
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.mycollab.vaadin.web.ui;

import com.mycollab.common.i18n.FileI18nEnum;
import com.mycollab.core.utils.FileUtils;
import com.mycollab.module.ecm.domain.Content;
import com.mycollab.module.ecm.service.ResourceService;
import com.mycollab.spring.AppContextUtil;
import com.mycollab.vaadin.AppUI;
import com.mycollab.vaadin.UserUIContext;
import com.mycollab.vaadin.resources.file.FileAssetsUtil;
import com.mycollab.vaadin.ui.ELabel;
import com.mycollab.vaadin.ui.NotificationUtil;
import com.mycollab.vaadin.ui.UIConstants;
import com.mycollab.vaadin.web.ui.utils.MultiFileUpload;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import org.apache.commons.collections4.MapUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.vaadin.viritin.button.MButton;
import org.vaadin.viritin.layouts.MCssLayout;
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
public class AttachmentPanel extends MCssLayout {
    private static final long serialVersionUID = 1L;
    private static final Logger LOG = LoggerFactory.getLogger(AttachmentPanel.class);
    private Map<String, File> fileStores;

    private MultiFileUpload multiFileUpload;
    private ResourceService resourceService;

    public AttachmentPanel() {
        resourceService = AppContextUtil.getSpringBean(ResourceService.class);
        multiFileUpload = new MultiFileUploadExt();
        multiFileUpload.setWidth("100%");
        withFullWidth().withComponent(multiFileUpload);
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
        }).withIcon(VaadinIcons.TRASH).withStyleName(WebThemes.BUTTON_ICON_ONLY);

        ELabel fileLbl = ELabel.html(fileName).withDescription(fileName).withStyleName(UIConstants.TEXT_ELLIPSIS);
        fileAttachmentLayout.with(ELabel.fontIcon(FileAssetsUtil.getFileIconResource(fileName)).withUndefinedWidth(),
                fileLbl, new ELabel(" - " + FileUtils.getVolumeDisplay(file.length()))
                        .withStyleName(UIConstants.META_INFO).withUndefinedWidth(), removeBtn).expand(fileLbl);
        this.addComponent(fileAttachmentLayout, 0);
    }

    private void removeAllAttachmentsDisplay() {
        if (fileStores != null) {
            fileStores.clear();
        }
        for (int i = getComponentCount() - 1; i >= 0; i--) {
            Component comp = getComponent(i);
            if (comp instanceof HorizontalLayout) {
                removeComponent(comp);
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
                            UserUIContext.getUsername(), new FileInputStream(file), AppUI.getAccountId());
                    if (file.exists()) {
                        file.delete();
                    }
                } catch (FileNotFoundException e) {
                    LOG.error("Error when attach content in UI", e);
                }
            }
            removeAllAttachmentsDisplay();
        }
    }

    private Content constructContent(String fileName, String path) {
        Content content = new Content(path + "/" + fileName);
        content.setTitle(fileName);
        content.setDescription("");
        return content;
    }

    public List<File> files() {
        List<File> files = null;
        if (MapUtils.isNotEmpty(fileStores)) {
            files = new ArrayList<>();
            for (Map.Entry<String, File> entry : fileStores.entrySet()) {
                File oldFile = entry.getValue();
                File parentFile = oldFile.getParentFile();
                File newFile = new File(parentFile, entry.getKey());
                if (newFile.exists()) {
                    newFile.delete();
                }
                if (oldFile.renameTo(newFile)) {
                    files.add(newFile);
                }

                if (files.size() <= 0) {
                    return null;
                }
            }
        }
        return files;
    }

    private class MultiFileUploadExt extends MultiFileUpload {
        private static final long serialVersionUID = 1L;

        @Override
        protected String getAreaText() {
            return UserUIContext.getMessage(FileI18nEnum.OPT_DRAG_OR_CLICK_TO_UPLOAD);
        }

        @Override
        protected void handleFile(File file, String fileName, String mimeType, long length) {
            if (fileStores == null) {
                fileStores = new HashMap<>();
            }
            if (fileStores.containsKey(fileName)) {
                NotificationUtil.showWarningNotification(UserUIContext.getMessage(FileI18nEnum.ERROR_FILE_IS_EXISTED, fileName));
            } else {
                fileStores.put(fileName, file);
                displayFileName(file, fileName);
            }
        }
    }
}

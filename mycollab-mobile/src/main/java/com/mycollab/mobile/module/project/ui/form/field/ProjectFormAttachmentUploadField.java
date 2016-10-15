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
package com.mycollab.mobile.module.project.ui.form.field;

import com.mycollab.common.i18n.FileI18nEnum;
import com.mycollab.common.i18n.GenericI18Enum;
import com.mycollab.core.utils.FileUtils;
import com.mycollab.mobile.ui.FormSectionBuilder;
import com.mycollab.mobile.ui.MobileAttachmentUtils;
import com.mycollab.mobile.ui.MobileUIConstants;
import com.mycollab.module.ecm.domain.Content;
import com.mycollab.module.ecm.service.ResourceService;
import com.mycollab.module.file.AttachmentUtils;
import com.mycollab.spring.AppContextUtil;
import com.mycollab.vaadin.MyCollabUI;
import com.mycollab.vaadin.UserUIContext;
import com.mycollab.vaadin.resources.file.FileAssetsUtil;
import com.mycollab.vaadin.ui.ELabel;
import com.mycollab.vaadin.ui.NotificationUtil;
import com.mycollab.vaadin.ui.UIConstants;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.VerticalLayout;
import org.apache.commons.collections.CollectionUtils;
import org.vaadin.easyuploads.FileBuffer;
import org.vaadin.easyuploads.MultiFileUpload;
import org.vaadin.viritin.button.MButton;
import org.vaadin.viritin.layouts.MCssLayout;
import org.vaadin.viritin.layouts.MHorizontalLayout;
import org.vaadin.viritin.layouts.MVerticalLayout;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author MyCollab Ltd.
 * @since 4.5.3
 */
public class ProjectFormAttachmentUploadField extends MVerticalLayout {
    private static final long serialVersionUID = 1L;

    private Map<String, File> fileStores;
    private MultiFileUpload multiFileUpload;

    private VerticalLayout rowWrap;
    private ResourceService resourceService;
    private String attachmentPath;

    public ProjectFormAttachmentUploadField() {
        withMargin(false);
        resourceService = AppContextUtil.getSpringBean(ResourceService.class);
        fileStores = new HashMap<>();

        multiFileUpload = new MultiFileUploadExt();
        multiFileUpload.setWidth("100%");
        this.setStyleName("attachment-field");

        rowWrap = new VerticalLayout();
        rowWrap.setWidth("100%");
        rowWrap.setStyleName("attachment-row-wrap");

        this.addComponent(FormSectionBuilder.build(FontAwesome.FILE, GenericI18Enum.FORM_ATTACHMENTS));
        MCssLayout btnWrap = new MCssLayout(multiFileUpload).withFullWidth();
        this.addComponent(btnWrap);
        this.addComponent(rowWrap);
    }

    public void getAttachments(Integer projectId, String type, Integer typeId) {
        attachmentPath = AttachmentUtils.getProjectEntityAttachmentPath(MyCollabUI.getAccountId(), projectId, type, "" + typeId);
        List<Content> attachments = resourceService.getContents(attachmentPath);
        rowWrap.removeAllComponents();
        if (CollectionUtils.isNotEmpty(attachments)) {
            for (Content attachment : attachments) {
                rowWrap.addComponent(MobileAttachmentUtils.renderAttachmentFieldRow(attachment));
            }
        }
    }

    public void saveContentsToRepo() {
        MobileAttachmentUtils.saveContentsToRepo(attachmentPath, fileStores);
    }

    public void saveContentsToRepo(Integer projectId, String type, Integer typeId) {
        attachmentPath = AttachmentUtils.getProjectEntityAttachmentPath(MyCollabUI.getAccountId(), projectId, type, "" + typeId);
        MobileAttachmentUtils.saveContentsToRepo(attachmentPath, fileStores);
    }

    private void displayFileName(File file, final String fileName) {
        final MHorizontalLayout fileAttachmentLayout = new MHorizontalLayout().withFullWidth();
        MButton removeBtn = new MButton("", clickEvent -> {
            File tmpFile = fileStores.get(fileName);
            if (tmpFile != null) {
                tmpFile.delete();
            }
            fileStores.remove(fileName);
            rowWrap.removeComponent(fileAttachmentLayout);
        }).withIcon(FontAwesome.TRASH_O).withStyleName(MobileUIConstants.BUTTON_LINK);

        ELabel fileLbl = ELabel.html(fileName).withDescription(fileName).withStyleName(UIConstants.TEXT_ELLIPSIS);
        fileAttachmentLayout.with(ELabel.fontIcon(FileAssetsUtil.getFileIconResource(fileName)).withWidthUndefined(),
                fileLbl, new ELabel(" - " + FileUtils.getVolumeDisplay(file.length()))
                        .withStyleName(UIConstants.META_INFO).withWidthUndefined(), removeBtn).expand(fileLbl);
        rowWrap.addComponent(fileAttachmentLayout);
    }

    private void receiveFile(File file, String fileName, String mimeType, long length) {
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

    private class MultiFileUploadExt extends MultiFileUpload {
        private static final long serialVersionUID = 1L;

        protected FileBuffer createReceiver() {
            FileBuffer receiver = super.createReceiver();
            receiver.setDeleteFiles(false);
            return receiver;
        }

        @Override
        protected String getAreaText() {
            return "Select File(s)";
        }

        @Override
        protected void handleFile(File file, String fileName, String mimeType, long length) {
            receiveFile(file, fileName, mimeType, length);
        }
    }
}

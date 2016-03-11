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
package com.esofthead.mycollab.module.project.ui.form;

import com.esofthead.mycollab.module.file.AttachmentUtils;
import com.esofthead.mycollab.vaadin.AppContext;
import com.esofthead.mycollab.vaadin.web.ui.AttachmentPanel;
import com.vaadin.ui.Component;
import com.vaadin.ui.CustomField;
import com.vaadin.ui.VerticalLayout;
import org.vaadin.easyuploads.MultiFileUploadExt;

/**
 * @author MyCollab Ltd.
 * @since 4.5.3
 */
public class ProjectFormAttachmentUploadField extends CustomField {
    private static final long serialVersionUID = 1L;
    private MultiFileUploadExt uploadExt;
    private AttachmentPanel attachmentPanel;

    public ProjectFormAttachmentUploadField() {
        attachmentPanel = new AttachmentPanel();
    }

    public void getAttachments(int projectId, String type, int typeId) {
        String attachmentPath = AttachmentUtils.getProjectEntityAttachmentPath(AppContext.getAccountId(),
                projectId, type, "" + typeId);
        attachmentPanel.getAttachments(attachmentPath);
    }

    @Override
    public Class<?> getType() {
        return Object.class;
    }

    public void saveContentsToRepo(int projectId, String type, int typeId) {
        String attachmentPath = AttachmentUtils.getProjectEntityAttachmentPath(AppContext.getAccountId(),
                projectId, type, "" + typeId);
        attachmentPanel.saveContentsToRepo(attachmentPath);
    }

    @Override
    protected Component initContent() {
        final VerticalLayout layout = new VerticalLayout();
        uploadExt = new MultiFileUploadExt(attachmentPanel);
        uploadExt.addComponent(attachmentPanel);
        layout.addComponent(uploadExt);
        return layout;
    }
}

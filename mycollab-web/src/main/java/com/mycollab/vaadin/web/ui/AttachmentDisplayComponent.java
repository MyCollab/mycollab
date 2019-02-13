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

import com.hp.gagawa.java.elements.Div;
import com.hp.gagawa.java.elements.Li;
import com.hp.gagawa.java.elements.Span;
import com.hp.gagawa.java.elements.Ul;
import com.mycollab.common.i18n.FileI18nEnum;
import com.mycollab.common.i18n.GenericI18Enum;
import com.mycollab.core.utils.DateTimeUtils;
import com.mycollab.core.utils.FileUtils;
import com.mycollab.core.utils.MimeTypesUtil;
import com.mycollab.core.utils.StringUtils;
import com.mycollab.module.ecm.domain.Content;
import com.mycollab.module.ecm.service.ResourceService;
import com.mycollab.spring.AppContextUtil;
import com.mycollab.vaadin.AppUI;
import com.mycollab.vaadin.UserUIContext;
import com.mycollab.vaadin.resources.VaadinResourceFactory;
import com.mycollab.vaadin.resources.file.FileAssetsUtil;
import com.mycollab.vaadin.ui.ELabel;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.server.FileDownloader;
import com.vaadin.shared.ui.ContentMode;
import com.vaadin.ui.*;
import org.vaadin.viritin.button.MButton;
import org.vaadin.viritin.layouts.MCssLayout;

import java.util.List;

/**
 * @author MyCollab Ltd.
 * @since 2.0
 */
public class AttachmentDisplayComponent extends CssLayout {
    private static final long serialVersionUID = 1L;

    private ResourceService resourceService = AppContextUtil.getSpringBean(ResourceService.class);

    public AttachmentDisplayComponent() {

    }

    public AttachmentDisplayComponent(List<Content> attachments) {
        attachments.forEach(this::addAttachmentRow);
    }

    public void loadAttachments(String attachmentPath) {

    }

    private void addAttachmentRow(Content attachment) {
        String docName = attachment.getPath();
        int lastIndex = docName.lastIndexOf("/");
        if (lastIndex != -1) {
            docName = docName.substring(lastIndex + 1);
        }

        final AbsoluteLayout attachmentLayout = new AbsoluteLayout();
        attachmentLayout.setWidth(WebUIConstants.DEFAULT_ATTACHMENT_THUMBNAIL_WIDTH);
        attachmentLayout.setHeight(WebUIConstants.DEFAULT_ATTACHMENT_THUMBNAIL_HEIGHT);
        attachmentLayout.setStyleName("attachment-block");

        MCssLayout thumbnailWrap = new MCssLayout().withFullSize().withStyleName("thumbnail-wrap");

        Link thumbnail = new Link();
        thumbnail.setTargetName("_blank");
        if (StringUtils.isBlank(attachment.getThumbnail())) {
            thumbnail.setIcon(FileAssetsUtil.getFileIconResource(attachment.getName()));
        } else {
            thumbnail.setIcon(VaadinResourceFactory.getResource(attachment.getThumbnail()));
        }

        if (MimeTypesUtil.isImageType(docName)) {
            thumbnail.setResource(VaadinResourceFactory.getResource(attachment.getPath()));
        }

        Div contentTooltip = new Div().appendChild(new Span().appendText(docName).setStyle("font-weight:bold"));
        Ul ul = new Ul().appendChild(new Li().appendText(UserUIContext.getMessage(FileI18nEnum.OPT_SIZE_VALUE,
                FileUtils.getVolumeDisplay(attachment.getSize())))).setStyle("line-height:1.5em");
        ul.appendChild(new Li().appendText(UserUIContext.getMessage(GenericI18Enum.OPT_LAST_MODIFIED,
                UserUIContext.formatPrettyTime(DateTimeUtils.toLocalDateTime(attachment.getLastModified())))));
        contentTooltip.appendChild(ul);
        thumbnail.setDescription(contentTooltip.write(), ContentMode.HTML);
        thumbnail.setWidth(WebUIConstants.DEFAULT_ATTACHMENT_THUMBNAIL_WIDTH);
        thumbnailWrap.addComponent(thumbnail);

        attachmentLayout.addComponent(thumbnailWrap, "top: 0px; left: 0px; bottom: 0px; right: 0px; z-index: 0;");

        MCssLayout attachmentNameWrap = new MCssLayout().withWidth(WebUIConstants.DEFAULT_ATTACHMENT_THUMBNAIL_WIDTH)
                .withStyleName("attachment-name-wrap");

        Label attachmentName = new ELabel(docName).withStyleName(WebThemes.TEXT_ELLIPSIS);
        attachmentNameWrap.addComponent(attachmentName);
        attachmentLayout.addComponent(attachmentNameWrap, "bottom: 0px; left: 0px; right: 0px; z-index: 1;");

        MButton trashBtn = new MButton("", clickEvent -> {
            ConfirmDialogExt.show(UI.getCurrent(), UserUIContext.getMessage(GenericI18Enum.DIALOG_DELETE_TITLE,
                    AppUI.getSiteName()), UserUIContext.getMessage(GenericI18Enum.CONFIRM_DELETE_ATTACHMENT),
                    UserUIContext.getMessage(GenericI18Enum.ACTION_YES),
                    UserUIContext.getMessage(GenericI18Enum.ACTION_NO),
                    confirmDialog -> {
                        if (confirmDialog.isConfirmed()) {
                            resourceService.removeResource(attachment.getPath(), UserUIContext.getUsername(), true, AppUI.getAccountId());
                            ((ComponentContainer) attachmentLayout.getParent()).removeComponent(attachmentLayout);
                        }
                    });
        }).withIcon(VaadinIcons.TRASH).withStyleName("attachment-control");
        attachmentLayout.addComponent(trashBtn, "top: 9px; left: 9px; z-index: 1;");

        MButton downloadBtn = new MButton().withIcon(VaadinIcons.DOWNLOAD).withStyleName("attachment-control");
        FileDownloader fileDownloader = new FileDownloader(VaadinResourceFactory.getInstance().getStreamResource(attachment.getPath()));
        fileDownloader.extend(downloadBtn);
        attachmentLayout.addComponent(downloadBtn, "right: 9px; top: 9px; z-index: 1;");
        this.addComponent(attachmentLayout);
    }
}

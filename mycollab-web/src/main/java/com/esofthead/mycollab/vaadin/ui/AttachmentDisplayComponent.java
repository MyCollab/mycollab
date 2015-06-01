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

import com.esofthead.mycollab.common.i18n.GenericI18Enum;
import com.esofthead.mycollab.configuration.SiteConfiguration;
import com.esofthead.mycollab.core.utils.FileUtils;
import com.esofthead.mycollab.core.utils.MimeTypesUtil;
import com.esofthead.mycollab.core.utils.StringUtils;
import com.esofthead.mycollab.module.ecm.domain.Content;
import com.esofthead.mycollab.module.ecm.service.ResourceService;
import com.esofthead.mycollab.spring.ApplicationContextUtil;
import com.esofthead.mycollab.vaadin.AppContext;
import com.esofthead.mycollab.vaadin.resources.VaadinResourceManager;
import com.hp.gagawa.java.elements.Div;
import com.hp.gagawa.java.elements.Li;
import com.hp.gagawa.java.elements.Span;
import com.hp.gagawa.java.elements.Ul;
import com.vaadin.event.MouseEvents;
import com.vaadin.server.FileDownloader;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Resource;
import com.vaadin.ui.*;
import com.vaadin.ui.Button.ClickEvent;
import org.vaadin.dialogs.ConfirmDialog;

import java.util.List;

/**
 *
 * @author MyCollab Ltd.
 * @since 2.0
 *
 */
public class AttachmentDisplayComponent extends CssLayout {
    private static final long serialVersionUID = 1L;

    private static final Resource DEFAULT_SOURCE = MyCollabResource.newResource(WebResourceIds._docs_256);

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

        final AbsoluteLayout attachmentLayout = new AbsoluteLayout();
        attachmentLayout.setWidth(UIConstants.DEFAULT_ATTACHMENT_THUMBNAIL_WIDTH);
        attachmentLayout.setHeight(UIConstants.DEFAULT_ATTACHMENT_THUMBNAIL_HEIGHT);
        attachmentLayout.setStyleName("attachment-block");

        CssLayout thumbnailWrap = new CssLayout();
        thumbnailWrap.setSizeFull();
        thumbnailWrap.setStyleName("thumbnail-wrap");

        Image thumbnail = new Image(null);
        if (org.apache.commons.lang3.StringUtils.isBlank(attachment.getThumbnail())) {
            thumbnail.setSource(DEFAULT_SOURCE);
        } else {
            thumbnail.setSource(VaadinResourceManager.getResourceManager()
                    .getImagePreviewResource(attachment.getThumbnail()));
        }

        Div contentTooltip = new Div().appendChild(new Span().appendText(docName).setStyle("font-weight:bold"));
        Ul ul = new Ul().appendChild(new Li().appendText("Size: " + FileUtils.getVolumeDisplay(attachment.getSize()))).setStyle("line-height:1.5em");
        ul.appendChild(new Li().appendText("Last modified: " + AppContext.formatPrettyTime(attachment.getLastModified().getTime())));
        contentTooltip.appendChild(ul);
        thumbnail.setDescription(contentTooltip.write());
        thumbnail.setWidth(UIConstants.DEFAULT_ATTACHMENT_THUMBNAIL_WIDTH);
        thumbnailWrap.addComponent(thumbnail);

        attachmentLayout.addComponent(thumbnailWrap, "top: 0px; left: 0px; bottom: 0px; right: 0px; z-index: 0;");

        if (MimeTypesUtil.isImageType(docName)) {
            thumbnail.addClickListener(new MouseEvents.ClickListener() {
                private static final long serialVersionUID = -2853211588120500523L;

                @Override
                public void click(MouseEvents.ClickEvent event) {
                    Resource previewResource = VaadinResourceManager
                            .getResourceManager().getImagePreviewResource(
                                    attachment.getPath());
                    UI.getCurrent().addWindow(new AttachmentPreviewWindow(previewResource));
                }
            });
        }

        CssLayout attachmentNameWrap = new CssLayout();
        attachmentNameWrap.setWidth(UIConstants.DEFAULT_ATTACHMENT_THUMBNAIL_WIDTH);
        attachmentNameWrap.setStyleName("attachment-name-wrap");

        Label attachmentName = new Label(StringUtils.trim(docName, 60, true));
        attachmentName.setStyleName("attachment-name");
        attachmentNameWrap.addComponent(attachmentName);
        attachmentLayout.addComponent(attachmentNameWrap, "bottom: 0px; left: 0px; right: 0px; z-index: 1;");

        Button trashBtn = new Button(null, new Button.ClickListener() {
            private static final long serialVersionUID = 1L;

            @Override
            public void buttonClick(ClickEvent event) {
                ConfirmDialogExt.show(UI.getCurrent(), AppContext.getMessage(
                                GenericI18Enum.DIALOG_DELETE_TITLE,
                                SiteConfiguration.getSiteName()), AppContext
                                .getMessage(GenericI18Enum.CONFIRM_DELETE_ATTACHMENT),
                        AppContext.getMessage(GenericI18Enum.BUTTON_YES),
                        AppContext.getMessage(GenericI18Enum.BUTTON_NO),
                        new ConfirmDialog.Listener() {
                            private static final long serialVersionUID = 1L;

                            @Override
                            public void onClose(ConfirmDialog dialog) {
                                if (dialog.isConfirmed()) {
                                    ResourceService attachmentService = ApplicationContextUtil
                                            .getSpringBean(ResourceService.class);
                                    attachmentService.removeResource(attachment.getPath(),
                                            AppContext.getUsername(), AppContext.getAccountId());
                                    ((ComponentContainer) attachmentLayout.getParent())
                                            .removeComponent(attachmentLayout);
                                }
                            }
                        });

            }
        });
        trashBtn.setIcon(FontAwesome.TRASH_O);
        trashBtn.setStyleName("attachment-control");
        attachmentLayout.addComponent(trashBtn, "top: 9px; left: 9px; z-index: 1;");

        Button downloadBtn = new Button();
        FileDownloader fileDownloader = new FileDownloader(VaadinResourceManager.getResourceManager()
                .getStreamResource(attachment.getPath()));
        fileDownloader.extend(downloadBtn);

        downloadBtn.setIcon(FontAwesome.DOWNLOAD);
        downloadBtn.setStyleName("attachment-control");
        attachmentLayout.addComponent(downloadBtn, "right: 9px; top: 9px; z-index: 1;");
        return attachmentLayout;
    }
}

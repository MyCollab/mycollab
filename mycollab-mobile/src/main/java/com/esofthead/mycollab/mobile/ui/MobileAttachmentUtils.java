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
package com.esofthead.mycollab.mobile.ui;

import com.esofthead.mycollab.common.i18n.GenericI18Enum;
import com.esofthead.mycollab.core.utils.ImageUtil;
import com.esofthead.mycollab.core.utils.MimeTypesUtil;
import com.esofthead.mycollab.core.utils.StringUtils;
import com.esofthead.mycollab.eventmanager.EventBusFactory;
import com.esofthead.mycollab.mobile.shell.events.ShellEvent;
import com.esofthead.mycollab.module.ecm.domain.Content;
import com.esofthead.mycollab.module.ecm.service.ResourceService;
import com.esofthead.mycollab.spring.AppContextUtil;
import com.esofthead.mycollab.vaadin.AppContext;
import com.esofthead.mycollab.vaadin.resources.VaadinResourceFactory;
import com.esofthead.mycollab.vaadin.resources.file.FileAssetsUtil;
import com.esofthead.mycollab.vaadin.ui.ELabel;
import com.vaadin.server.FontAwesome;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.*;
import com.vaadin.ui.Button.ClickEvent;
import org.apache.commons.collections.MapUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.vaadin.viritin.layouts.MHorizontalLayout;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.Map;

/**
 * @author MyCollab Ltd.
 * @since 4.5.2
 */
public class MobileAttachmentUtils {
    private static final Logger LOG = LoggerFactory.getLogger(MobileAttachmentUtils.class.getName());

    public static final String ATTACHMENT_NAME_PREFIX = "attachment_";

    public static Component renderAttachmentRow(final Content attachment) {
        String docName = attachment.getPath();
        int lastIndex = docName.lastIndexOf("/");
        MHorizontalLayout attachmentRow = new MHorizontalLayout().withSpacing(false).withFullWidth().withStyleName("attachment-row");
        attachmentRow.setDefaultComponentAlignment(Alignment.MIDDLE_LEFT);

        CssLayout thumbnailWrap = new CssLayout();
        thumbnailWrap.setWidth("25px");
        thumbnailWrap.setStyleName("thumbnail-wrap");

        Component thumbnail;

        if (StringUtils.isNotBlank(attachment.getThumbnail())) {
            thumbnail = new Image(null, VaadinResourceFactory.getInstance().getResource(attachment.getThumbnail()));
        } else {
            thumbnail = new ELabel(FileAssetsUtil.getFileIconResource(attachment.getName()).getHtml(), ContentMode.HTML);
        }
        thumbnail.setWidth("100%");
        thumbnailWrap.addComponent(thumbnail);
        attachmentRow.addComponent(thumbnailWrap);

        if (lastIndex != -1) {
            docName = docName.substring(lastIndex + 1, docName.length());
        }

        if (MimeTypesUtil.isImageType(docName)) {
            Button b = new Button(attachment.getTitle(), new Button.ClickListener() {
                private static final long serialVersionUID = -1713187920922886934L;

                @Override
                public void buttonClick(Button.ClickEvent event) {
                    AttachmentPreviewView previewView = new AttachmentPreviewView(VaadinResourceFactory.getInstance()
                            .getResource(attachment.getPath()));
                    EventBusFactory.getInstance().post(new ShellEvent.PushView(this, previewView));
                }
            });
            b.setWidth("100%");
            b.addStyleName(UIConstants.TRUNCATE);
            attachmentRow.with(b).expand(b);
        } else {
            Label l = new Label(attachment.getTitle());
            l.setWidth("100%");
            attachmentRow.addComponent(l);
            attachmentRow.setExpandRatio(l, 1.0f);
        }
        return attachmentRow;
    }

    public static Component renderAttachmentFieldRow(final Content attachment, Button.ClickListener additionalListener) {
        String docName = attachment.getPath();
        int lastIndex = docName.lastIndexOf("/");
        if (lastIndex != -1) {
            docName = docName.substring(lastIndex + 1, docName.length());
        }

        final MHorizontalLayout attachmentLayout = new MHorizontalLayout().withStyleName("attachment-row").withFullWidth();
        attachmentLayout.setDefaultComponentAlignment(Alignment.MIDDLE_LEFT);

        CssLayout thumbnailWrap = new CssLayout();
        thumbnailWrap.setWidth("25px");
        thumbnailWrap.setStyleName("thumbnail-wrap");

        Component thumbnail;
        if (StringUtils.isNotBlank(attachment.getThumbnail())) {
            thumbnail = new Image(null, VaadinResourceFactory.getInstance().getResource(attachment.getThumbnail()));
        } else {
            thumbnail = new ELabel(FileAssetsUtil.getFileIconResource(attachment.getName()).getHtml(), ContentMode.HTML);
        }
        thumbnail.setWidth("100%");
        thumbnailWrap.addComponent(thumbnail);
        attachmentLayout.addComponent(thumbnailWrap);

        ELabel attachmentLink = new ELabel(docName).withStyleName(UIConstants.META_INFO);
        attachmentLink.addStyleName(UIConstants.TRUNCATE);
        attachmentLayout.with(attachmentLink).expand(attachmentLink);

        Button removeAttachment = new Button("", new Button.ClickListener() {
                    private static final long serialVersionUID = 1L;

                    @Override
                    public void buttonClick(ClickEvent event) {
                        ConfirmDialog.show(UI.getCurrent(),
                                AppContext.getMessage(GenericI18Enum.CONFIRM_DELETE_ATTACHMENT),
                                AppContext.getMessage(GenericI18Enum.BUTTON_YES),
                                AppContext.getMessage(GenericI18Enum.BUTTON_NO),
                                new ConfirmDialog.CloseListener() {
                                    private static final long serialVersionUID = 1L;

                                    @Override
                                    public void onClose(ConfirmDialog dialog) {
                                        if (dialog.isConfirmed()) {
                                            ResourceService attachmentService = AppContextUtil.getSpringBean(ResourceService.class);
                                            attachmentService.removeResource(attachment.getPath(), AppContext.getUsername(), AppContext.getAccountId());
                                            ((ComponentContainer) attachmentLayout.getParent()).removeComponent(attachmentLayout);
                                        }
                                    }
                                });

                    }
                });
        removeAttachment.setIcon(FontAwesome.TRASH_O);
        if (additionalListener != null) {
            removeAttachment.addClickListener(additionalListener);
        }
        removeAttachment.setHtmlContentAllowed(true);
        removeAttachment.setStyleName("link");
        attachmentLayout.addComponent(removeAttachment);

        return attachmentLayout;
    }

    public static Component renderAttachmentFieldRow(Content attachment) {
        return renderAttachmentFieldRow(attachment, null);
    }

    public static void saveContentsToRepo(String attachmentPath, Map<String, File> fileStores) {
        if (MapUtils.isNotEmpty(fileStores)) {
            ResourceService resourceService = AppContextUtil.getSpringBean(ResourceService.class);
            for (Map.Entry<String, File> entry : fileStores.entrySet()) {
                try {
                    String fileExt = "";
                    String fileName = entry.getKey();
                    File file = entry.getValue();
                    int index = fileName.lastIndexOf(".");
                    if (index > 0) {
                        fileExt = fileName.substring(index + 1, fileName.length());
                    }

                    if ("jpg".equalsIgnoreCase(fileExt) || "png".equalsIgnoreCase(fileExt)) {
                        try {
                            BufferedImage bufferedImage = ImageIO.read(file);

                            int imgHeight = bufferedImage.getHeight();
                            int imgWidth = bufferedImage.getWidth();

                            BufferedImage scaledImage;

                            float scale;
                            float destWidth = 974;
                            float destHeight = 718;

                            float scaleX = Math.min(destHeight / imgHeight, 1);
                            float scaleY = Math.min(destWidth / imgWidth, 1);
                            scale = Math.min(scaleX, scaleY);
                            scaledImage = ImageUtil.scaleImage(bufferedImage, scale);

                            ByteArrayOutputStream outStream = new ByteArrayOutputStream();
                            ImageIO.write(scaledImage, fileExt, outStream);

                            resourceService.saveContent(constructContent(fileName, attachmentPath), AppContext.getUsername(),
                                    new ByteArrayInputStream(outStream.toByteArray()), AppContext.getAccountId());
                        } catch (IOException e) {
                            LOG.error("Error in upload file", e);
                            resourceService.saveContent(constructContent(fileName, attachmentPath), AppContext.getUsername(),
                                    new FileInputStream(fileStores.get(fileName)), AppContext.getAccountId());
                        }
                    } else {
                        resourceService.saveContent(constructContent(fileName, attachmentPath),
                                AppContext.getUsername(), new FileInputStream(file), AppContext.getAccountId());
                    }

                } catch (FileNotFoundException e) {
                    LOG.error("Error when attach content in UI", e);
                }
            }
        }
    }

    public static Content constructContent(String fileName, String path) {
        Content content = new Content(path + "/" + fileName);
        content.setTitle(fileName);
        content.setDescription("");
        return content;
    }
}

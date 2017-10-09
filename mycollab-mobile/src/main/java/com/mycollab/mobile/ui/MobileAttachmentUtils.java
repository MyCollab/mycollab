package com.mycollab.mobile.ui;

import com.mycollab.common.i18n.GenericI18Enum;
import com.mycollab.core.utils.ImageUtil;
import com.mycollab.core.utils.MimeTypesUtil;
import com.mycollab.core.utils.StringUtils;
import com.mycollab.vaadin.EventBusFactory;
import com.mycollab.mobile.shell.event.ShellEvent;
import com.mycollab.module.ecm.domain.Content;
import com.mycollab.module.ecm.service.ResourceService;
import com.mycollab.spring.AppContextUtil;
import com.mycollab.vaadin.AppUI;
import com.mycollab.vaadin.UserUIContext;
import com.mycollab.vaadin.resources.VaadinResourceFactory;
import com.mycollab.vaadin.resources.file.FileAssetsUtil;
import com.mycollab.vaadin.ui.ELabel;
import com.mycollab.vaadin.ui.UIConstants;
import com.vaadin.server.FontAwesome;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.*;
import org.apache.commons.collections.MapUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.vaadin.viritin.button.MButton;
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
            thumbnail = new Image(null, VaadinResourceFactory.getResource(attachment.getThumbnail()));
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
            MButton b = new MButton(attachment.getTitle(), clickEvent -> {
                AttachmentPreviewView previewView = new AttachmentPreviewView(VaadinResourceFactory.getResource(attachment.getPath()));
                EventBusFactory.getInstance().post(new ShellEvent.PushView(attachment, previewView));
            }).withStyleName(UIConstants.TEXT_ELLIPSIS);
            b.setWidth("100%");
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
            thumbnail = new Image(null, VaadinResourceFactory.getResource(attachment.getThumbnail()));
        } else {
            thumbnail = ELabel.fontIcon(FileAssetsUtil.getFileIconResource(attachment.getName()));
        }
        thumbnail.setWidth("100%");
        thumbnailWrap.addComponent(thumbnail);
        attachmentLayout.addComponent(thumbnailWrap);

        ELabel attachmentLink = new ELabel(docName).withStyleName(UIConstants.META_INFO, UIConstants.TEXT_ELLIPSIS);
        attachmentLayout.with(attachmentLink).expand(attachmentLink);

        MButton removeAttachment = new MButton("", clickEvent -> {
            ConfirmDialog.show(UI.getCurrent(),
                    UserUIContext.getMessage(GenericI18Enum.CONFIRM_DELETE_ATTACHMENT),
                    UserUIContext.getMessage(GenericI18Enum.BUTTON_YES),
                    UserUIContext.getMessage(GenericI18Enum.BUTTON_NO),
                    dialog -> {
                        if (dialog.isConfirmed()) {
                            ResourceService attachmentService = AppContextUtil.getSpringBean(ResourceService.class);
                            attachmentService.removeResource(attachment.getPath(), UserUIContext.getUsername(), true,
                                    AppUI.getAccountId());
                            ((ComponentContainer) attachmentLayout.getParent()).removeComponent(attachmentLayout);
                        }
                    });
        }).withIcon(FontAwesome.TRASH_O).withStyleName(MobileUIConstants.BUTTON_LINK);
        if (additionalListener != null) {
            removeAttachment.addClickListener(additionalListener);
        }
        removeAttachment.setHtmlContentAllowed(true);
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

                            resourceService.saveContent(constructContent(fileName, attachmentPath), UserUIContext.getUsername(),
                                    new ByteArrayInputStream(outStream.toByteArray()), AppUI.getAccountId());
                        } catch (IOException e) {
                            LOG.error("Error in upload file", e);
                            resourceService.saveContent(constructContent(fileName, attachmentPath), UserUIContext.getUsername(),
                                    new FileInputStream(fileStores.get(fileName)), AppUI.getAccountId());
                        }
                    } else {
                        resourceService.saveContent(constructContent(fileName, attachmentPath),
                                UserUIContext.getUsername(), new FileInputStream(file), AppUI.getAccountId());
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

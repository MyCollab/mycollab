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
package com.mycollab.module.user.ui.components;

import com.mycollab.common.i18n.GenericI18Enum;
import com.mycollab.core.MyCollabException;
import com.mycollab.core.UserInvalidInputException;
import com.mycollab.core.utils.ImageUtil;
import com.mycollab.vaadin.AppContext;
import com.mycollab.vaadin.ui.ELabel;
import com.mycollab.vaadin.web.ui.ByteArrayImageResource;
import com.mycollab.vaadin.web.ui.UIConstants;
import com.esofthead.vaadin.cropField.CropField;
import com.esofthead.vaadin.cropField.client.VCropSelection;
import com.vaadin.data.Property;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Resource;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.vaadin.viritin.layouts.MHorizontalLayout;
import org.vaadin.viritin.layouts.MVerticalLayout;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * @author MyCollab Ltd
 * @since 5.2.12
 */
public class ImagePreviewCropWindow extends Window {
    private static final Logger LOG = LoggerFactory.getLogger(ImagePreviewCropWindow.class);

    private VerticalLayout previewPhoto;
    private BufferedImage originalImage;
    private byte[] scaleImageData;

    public ImagePreviewCropWindow(final ImageSelectionCommand imageSelectionCommand, final byte[] imageData) {
        super("Preview and modify image");
        setModal(true);
        setResizable(false);
        setWidth("700px");
        center();

        MVerticalLayout content = new MVerticalLayout();
        setContent(content);

        try {
            originalImage = ImageIO.read(new ByteArrayInputStream(imageData));
        } catch (IOException e) {
            throw new UserInvalidInputException("Invalid image type");
        }
        originalImage = ImageUtil.scaleImage(originalImage, 650, 650);

        MHorizontalLayout previewBox = new MHorizontalLayout().withSpacing(true).withMargin(new MarginInfo(false,
                true, true, false)).withFullWidth();

        previewPhoto = new VerticalLayout();
        previewPhoto.setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);
        previewPhoto.setWidth("100px");

        previewBox.with(previewPhoto).withAlign(previewPhoto, Alignment.TOP_LEFT);

        VerticalLayout previewBoxTitle = new VerticalLayout();
        previewBoxTitle.setMargin(new MarginInfo(false, true, false, true));
        Label lbPreview = new Label("<p style='margin: 0px;'><strong>To the bottom is what your profile photo will " +
                "look like.</strong></p>" +
                "<p style='margin-top: 0px;'>To make adjustment, you can drag around and resize the selection square below. " +
                "When you are happy with your photo, click the &ldquo;Accept&ldquo; button.</p>",
                ContentMode.HTML);
        previewBoxTitle.addComponent(lbPreview);

        MHorizontalLayout controlBtns = new MHorizontalLayout();
        controlBtns.setSizeUndefined();

        Button cancelBtn = new Button(AppContext.getMessage(GenericI18Enum.BUTTON_CANCEL), new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                close();
            }
        });
        cancelBtn.setStyleName(UIConstants.BUTTON_OPTION);

        Button acceptBtn = new Button(AppContext.getMessage(GenericI18Enum.BUTTON_ACCEPT), new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                if (scaleImageData != null && scaleImageData.length > 0) {
                    try {
                        BufferedImage image = ImageIO.read(new ByteArrayInputStream(scaleImageData));
                        imageSelectionCommand.process(image);
                        close();
                    } catch (IOException e) {
                        throw new MyCollabException("Error when saving user avatar", e);
                    }
                }
            }
        });
        acceptBtn.setStyleName(UIConstants.BUTTON_ACTION);
        acceptBtn.setIcon(FontAwesome.CHECK);

        controlBtns.with(acceptBtn, cancelBtn).alignAll(Alignment.MIDDLE_LEFT);

        previewBoxTitle.addComponent(controlBtns);
        previewBoxTitle.setComponentAlignment(controlBtns, Alignment.TOP_LEFT);
        previewBox.with(previewBoxTitle).expand(previewBoxTitle);

        CssLayout cropBox = new CssLayout();
        cropBox.setWidth("100%");
        VerticalLayout currentPhotoBox = new VerticalLayout();
        Resource resource = new ByteArrayImageResource(ImageUtil.convertImageToByteArray(originalImage), "image/png");
        CropField cropField = new CropField(resource);
        cropField.setImmediate(true);
        cropField.setSelectionAspectRatio(1.0f);
        cropField.addValueChangeListener(new Property.ValueChangeListener() {
            @Override
            public void valueChange(Property.ValueChangeEvent event) {
                VCropSelection newSelection = (VCropSelection) event.getProperty().getValue();
                int x1 = newSelection.getXTopLeft();
                int y1 = newSelection.getYTopLeft();
                int x2 = newSelection.getXBottomRight();
                int y2 = newSelection.getYBottomRight();
                if (x2 > x1 && y2 > y1) {
                    BufferedImage subImage = originalImage.getSubimage(x1, y1, (x2 - x1), (y2 - y1));
                    ByteArrayOutputStream outStream = new ByteArrayOutputStream();
                    try {
                        ImageIO.write(subImage, "png", outStream);
                        scaleImageData = outStream.toByteArray();
                        displayPreviewImage();
                    } catch (IOException e) {
                        LOG.error("Error while scale image: ", e);
                    }
                }
            }
        });
        currentPhotoBox.setWidth("520px");
        currentPhotoBox.setHeight("470px");
        currentPhotoBox.addComponent(cropField);
        cropBox.addComponent(currentPhotoBox);

        content.with(previewBox, ELabel.hr(), cropBox);
        displayPreviewImage();
    }

    private void displayPreviewImage() {
        previewPhoto.removeAllComponents();
        if (scaleImageData != null && scaleImageData.length > 0) {
            Embedded previewImage = new Embedded(null, new ByteArrayImageResource(scaleImageData, "image/png"));
            previewImage.setWidth("100px");
            previewPhoto.addComponent(previewImage);
        } else {
            previewPhoto.addComponent(ELabel.fontIcon(FontAwesome.QUESTION_CIRCLE).withStyleName("icon-48px").withWidthUndefined());
        }
    }

    public interface ImageSelectionCommand {
        void process(BufferedImage image);
    }
}

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

import com.mycollab.common.i18n.GenericI18Enum;
import com.mycollab.common.i18n.ShellI18nEnum;
import com.mycollab.core.MyCollabException;
import com.mycollab.core.UserInvalidInputException;
import com.mycollab.core.utils.ImageUtil;
import com.mycollab.vaadin.UserUIContext;
import com.mycollab.vaadin.ui.ELabel;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.server.Resource;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.Embedded;
import com.vaadin.ui.VerticalLayout;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.vaadin.cropper.Cropper;
import org.vaadin.cropper.client.CropSelection;
import org.vaadin.viritin.button.MButton;
import org.vaadin.viritin.layouts.MHorizontalLayout;
import org.vaadin.viritin.layouts.MVerticalLayout;
import org.vaadin.viritin.layouts.MWindow;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * @author MyCollab Ltd
 * @since 5.2.12
 */
public class ImagePreviewCropWindow extends MWindow {
    private static final Logger LOG = LoggerFactory.getLogger(ImagePreviewCropWindow.class);

    private VerticalLayout previewPhoto;
    private BufferedImage originalImage;
    private byte[] scaleImageData;

    public ImagePreviewCropWindow(final ImageSelectionCommand imageSelectionCommand, final byte[] imageData) {
        super(UserUIContext.getMessage(ShellI18nEnum.OPT_PREVIEW_EDIT_IMAGE));
        MVerticalLayout content = new MVerticalLayout();
        withModal(true).withResizable(false).withWidth("700px").withCenter().withContent(content);

        try {
            originalImage = ImageIO.read(new ByteArrayInputStream(imageData));
        } catch (IOException e) {
            throw new UserInvalidInputException("Invalid image type");
        }
        originalImage = ImageUtil.scaleImage(originalImage, 650, 650);

        MHorizontalLayout previewBox = new MHorizontalLayout().withMargin(new MarginInfo(false, true, true, false)).withFullWidth();

        previewPhoto = new VerticalLayout();
        previewPhoto.setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);
        previewPhoto.setWidth("100px");

        previewBox.with(previewPhoto).withAlign(previewPhoto, Alignment.TOP_LEFT);

        VerticalLayout previewBoxTitle = new VerticalLayout();
        previewBoxTitle.setMargin(new MarginInfo(false, true, false, true));
        previewBoxTitle.addComponent(ELabel.html(UserUIContext.getMessage(ShellI18nEnum.OPT_IMAGE_EDIT_INSTRUCTION)));

        MButton cancelBtn = new MButton(UserUIContext.getMessage(GenericI18Enum.BUTTON_CANCEL), clickEvent -> close())
                .withStyleName(WebThemes.BUTTON_OPTION);

        MButton acceptBtn = new MButton(UserUIContext.getMessage(GenericI18Enum.BUTTON_ACCEPT), clickEvent -> {
            if (scaleImageData != null && scaleImageData.length > 0) {
                try {
                    BufferedImage image = ImageIO.read(new ByteArrayInputStream(scaleImageData));
                    imageSelectionCommand.process(image);
                    close();
                } catch (IOException e) {
                    throw new MyCollabException("Error when saving user avatar", e);
                }
            }
        }).withIcon(VaadinIcons.CHECK).withStyleName(WebThemes.BUTTON_ACTION);

        MHorizontalLayout controlBtns = new MHorizontalLayout(acceptBtn, cancelBtn);

        previewBoxTitle.addComponent(controlBtns);
        previewBoxTitle.setComponentAlignment(controlBtns, Alignment.TOP_LEFT);
        previewBox.with(previewBoxTitle).expand(previewBoxTitle);

        CssLayout cropBox = new CssLayout();
        cropBox.setSizeFull();
        Resource resource = new ByteArrayImageResource(ImageUtil.convertImageToByteArray(originalImage), "image/png");
        Cropper cropField = new Cropper(resource);
        cropField.setAspectRatio(1.0d);
        cropField.setWidth("470px");
        cropField.setHeight("470px");
        cropField.addCropSelectionChangedListener(valueChangeEvent -> {
            CropSelection newSelection = valueChangeEvent.getSelection();
            int x1 = newSelection.getX();
            int y1 = newSelection.getY();
            int x2 = newSelection.getWidth();
            int y2 = newSelection.getHeight();
            if (x2 > x1 && y2 > y1) {
                BufferedImage subImage = originalImage.getSubimage(x1, y1, x2, y2);
                ByteArrayOutputStream outStream = new ByteArrayOutputStream();
                try {
                    ImageIO.write(subImage, "png", outStream);
                    scaleImageData = outStream.toByteArray();
                    displayPreviewImage();
                } catch (IOException e) {
                    LOG.error("Error while scale image: ", e);
                }
            }
        });
        cropBox.setWidth("470px");
        cropBox.setHeight("470px");
        cropBox.addComponent(cropField);

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
            previewPhoto.addComponent(ELabel.fontIcon(VaadinIcons.QUESTION_CIRCLE).withStyleName("icon-48px").withUndefinedWidth());
        }
    }

    public interface ImageSelectionCommand {
        void process(BufferedImage image);
    }
}

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
package com.esofthead.mycollab.module.user.accountsettings.profile.view;

import com.esofthead.mycollab.common.i18n.GenericI18Enum;
import com.esofthead.mycollab.core.MyCollabException;
import com.esofthead.mycollab.core.UserInvalidInputException;
import com.esofthead.mycollab.core.utils.ImageUtil;
import com.esofthead.mycollab.eventmanager.EventBusFactory;
import com.esofthead.mycollab.module.file.service.UserAvatarService;
import com.esofthead.mycollab.module.user.accountsettings.view.events.ProfileEvent;
import com.esofthead.mycollab.spring.ApplicationContextUtil;
import com.esofthead.mycollab.vaadin.AppContext;
import com.esofthead.mycollab.vaadin.mvp.AbstractPageView;
import com.esofthead.mycollab.vaadin.mvp.ViewComponent;
import com.esofthead.mycollab.vaadin.ui.ByteArrayImageResource;
import com.esofthead.mycollab.vaadin.ui.UIConstants;
import com.esofthead.mycollab.vaadin.ui.UserAvatarControlFactory;
import com.esofthead.vaadin.cropField.CropField;
import com.esofthead.vaadin.cropField.client.VCropSelection;
import com.vaadin.data.Property;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Page;
import com.vaadin.server.Resource;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.*;
import com.vaadin.ui.Button.ClickEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.vaadin.viritin.layouts.MHorizontalLayout;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
@ViewComponent
public class ProfilePhotoUploadViewImpl extends AbstractPageView implements ProfilePhotoUploadView {
    private static final long serialVersionUID = 1L;

    private static final Logger LOG = LoggerFactory.getLogger(ProfilePhotoUploadViewImpl.class);

    private BufferedImage originalImage;
    private Embedded previewImage;
    private byte[] scaleImageData;

    public ProfilePhotoUploadViewImpl() {
        this.setMargin(true);
    }

    @SuppressWarnings("serial")
    @Override
    public void editPhoto(final byte[] imageData) {
        this.removeAllComponents();
        LOG.debug("Receive avatar upload with size: " + imageData.length);
        try {
            originalImage = ImageIO.read(new ByteArrayInputStream(imageData));
        } catch (IOException e) {
            throw new UserInvalidInputException("Invalid image type");
        }
        originalImage = ImageUtil.scaleImage(originalImage, 650, 650);

        MHorizontalLayout previewBox = new MHorizontalLayout().withSpacing(true).withMargin(new MarginInfo(false,
                true, true, false)).withWidth("100%");

        Resource defaultPhoto = UserAvatarControlFactory.createAvatarResource(AppContext.getUserAvatarId(), 100);
        previewImage = new Embedded(null, defaultPhoto);
        previewImage.setWidth("100px");
        previewBox.with(previewImage).withAlign(previewImage, Alignment.TOP_LEFT);

        VerticalLayout previewBoxRight = new VerticalLayout();
        previewBoxRight.setMargin(new MarginInfo(false, true, false, true));
        Label lbPreview = new Label("<p style='margin: 0px;'><strong>To the left is what your profile photo will look like.</strong></p>" +
                "<p style='margin-top: 0px;'>To make adjustment, you can drag around and resize the selection square below. " +
                "When you are happy with your photo, click the &ldquo;Accept&ldquo; button.</p>",
                ContentMode.HTML);
        previewBoxRight.addComponent(lbPreview);

        MHorizontalLayout controlBtns = new MHorizontalLayout();
        controlBtns.setSizeUndefined();

        Button cancelBtn = new Button(AppContext.getMessage(GenericI18Enum.BUTTON_CANCEL), new Button.ClickListener() {
            @Override
            public void buttonClick(ClickEvent event) {
                EventBusFactory.getInstance().post(
                        new ProfileEvent.GotoProfileView(ProfilePhotoUploadViewImpl.this, null));
            }
        });
        cancelBtn.setStyleName(UIConstants.THEME_GRAY_LINK);

        Button acceptBtn = new Button(AppContext.getMessage(GenericI18Enum.BUTTON_ACCEPT), new Button.ClickListener() {
            @Override
            public void buttonClick(ClickEvent event) {
                if (scaleImageData != null && scaleImageData.length > 0) {
                    try {
                        BufferedImage image = ImageIO.read(new ByteArrayInputStream(scaleImageData));
                        UserAvatarService userAvatarService = ApplicationContextUtil.getSpringBean(UserAvatarService.class);
                        userAvatarService.uploadAvatar(image, AppContext.getUsername(), AppContext.getUserAvatarId());
                        Page.getCurrent().getJavaScript().execute("window.location.reload();");
                    } catch (IOException e) {
                        throw new MyCollabException("Error when saving user avatar", e);
                    }

                }

            }
        });
        acceptBtn.setStyleName(UIConstants.THEME_GREEN_LINK);
        acceptBtn.setIcon(FontAwesome.CHECK);

        controlBtns.with(acceptBtn, cancelBtn).alignAll(Alignment.MIDDLE_LEFT);

        previewBoxRight.addComponent(controlBtns);
        previewBoxRight.setComponentAlignment(controlBtns, Alignment.TOP_LEFT);

        previewBox.addComponent(previewBoxRight);
        previewBox.setExpandRatio(previewBoxRight, 1.0f);

        this.addComponent(previewBox);

        CssLayout cropBox = new CssLayout();
        cropBox.addStyleName(UIConstants.PHOTO_CROPBOX);
        cropBox.setWidth("100%");
        VerticalLayout currentPhotoBox = new VerticalLayout();
        Resource resource = new ByteArrayImageResource(
                ImageUtil.convertImageToByteArray(originalImage), "image/png");
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
        currentPhotoBox.setWidth("650px");
        currentPhotoBox.setHeight("650px");

        currentPhotoBox.addComponent(cropField);

        cropBox.addComponent(currentPhotoBox);

        this.addComponent(previewBox);
        this.addComponent(cropBox);
        this.setExpandRatio(cropBox, 1.0f);
    }

    private void displayPreviewImage() {
        if (scaleImageData != null && scaleImageData.length > 0) {
            ByteArrayImageResource previewResource = new ByteArrayImageResource(
                    scaleImageData, "image/png");
            previewImage.setSource(previewResource);
        }
    }

}

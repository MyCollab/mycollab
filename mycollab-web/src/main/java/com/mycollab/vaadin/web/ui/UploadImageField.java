package com.mycollab.vaadin.web.ui;

import com.mycollab.core.UserInvalidInputException;
import com.mycollab.core.utils.ImageUtil;
import com.mycollab.module.project.ui.ProjectAssetsUtil;
import com.vaadin.ui.UI;
import org.vaadin.easyuploads.UploadField;

/**
 * @author MyCollab Ltd
 * @since 5.2.12
 */
public class UploadImageField extends UploadField {
    private ImagePreviewCropWindow.ImageSelectionCommand imageSelectionCommand;

    public UploadImageField(ImagePreviewCropWindow.ImageSelectionCommand imageSelectionCommand) {
        this.imageSelectionCommand = imageSelectionCommand;
        this.addStyleName("upload-field");
        this.setFieldType(FieldType.BYTE_ARRAY);
        this.setSizeUndefined();
    }

    @Override
    protected void updateDisplay() {
        byte[] imageData = (byte[]) this.getValue();
        String mimeType = this.getLastMimeType();
        if (mimeType.equals("image/jpeg") || mimeType.equals("image/jpg")) {
            imageData = ImageUtil.convertJpgToPngFormat(imageData);
            if (imageData == null) {
                throw new UserInvalidInputException("Can not convert image to jpg format");
            } else {
                mimeType = "image/png";
            }
        }

        if (mimeType.equals("image/png")) {
            UI.getCurrent().addWindow(new ImagePreviewCropWindow(imageSelectionCommand, imageData));
        } else {
            throw new UserInvalidInputException("Upload file does not have valid image format. The supported formats are jpg/png");
        }
    }
}

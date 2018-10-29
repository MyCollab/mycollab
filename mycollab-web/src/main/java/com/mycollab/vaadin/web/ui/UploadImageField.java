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

import com.vaadin.ui.CssLayout;

/**
 * @author MyCollab Ltd
 * @since 5.2.12
 */
// TODO
public class UploadImageField extends CssLayout {
    private ImagePreviewCropWindow.ImageSelectionCommand imageSelectionCommand;

    public UploadImageField(ImagePreviewCropWindow.ImageSelectionCommand imageSelectionCommand) {
        this.imageSelectionCommand = imageSelectionCommand;
//        this.addStyleName("upload-field");
//        this.setFieldType(FieldType.BYTE_ARRAY);
//        this.setSizeUndefined();
    }

//    @Override
    protected void updateDisplay() {
//        byte[] imageData = (byte[]) this.getValue();
//        String mimeType = this.getLastMimeType();
//        if (mimeType.equals("image/jpeg") || mimeType.equals("image/jpg")) {
//            imageData = ImageUtil.convertJpgToPngFormat(imageData);
//            if (imageData == null) {
//                throw new UserInvalidInputException("Can not convert image to jpg format");
//            } else {
//                mimeType = "image/png";
//            }
//        }
//
//        if (mimeType.equals("image/png")) {
//            UI.getCurrent().addWindow(new ImagePreviewCropWindow(imageSelectionCommand, imageData));
//        } else {
//            throw new UserInvalidInputException("Upload file does not have valid image format. The supported formats are jpg/png");
//        }
    }
}

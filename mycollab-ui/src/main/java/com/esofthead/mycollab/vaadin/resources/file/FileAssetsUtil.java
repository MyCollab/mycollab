/**
 * This file is part of mycollab-ui.
 *
 * mycollab-ui is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * mycollab-ui is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with mycollab-ui.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.esofthead.mycollab.vaadin.resources.file;

import com.esofthead.mycollab.core.utils.MimeTypesUtil;
import com.vaadin.server.FontAwesome;

import java.util.HashMap;
import java.util.Map;

/**
 * @author MyCollab Ltd.
 * @since 5.0.3
 */
public class FileAssetsUtil {
    private static final Map<String, FontAwesome> extraIconMap;

    static {
        extraIconMap = new HashMap<>();
        extraIconMap.put("application/pdf", FontAwesome.FILE_PDF_O);
        extraIconMap.put("application/vnd.ms-excel", FontAwesome.FILE_EXCEL_O);
        extraIconMap.put("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet", FontAwesome.FILE_EXCEL_O);
        extraIconMap.put("application/msword", FontAwesome.FILE_WORD_O);
        extraIconMap.put("application/vnd.openxmlformats-officedocument.wordprocessingml.document", FontAwesome.FILE_WORD_O);
        extraIconMap.put("application/vnd.ms-powerpoint", FontAwesome.FILE_POWERPOINT_O);
        extraIconMap.put("application/vnd.openxmlformats-officedocument.presentationml.presentation", FontAwesome.FILE_POWERPOINT_O);
        extraIconMap.put("application/zip", FontAwesome.FILE_ZIP_O);
        extraIconMap.put("application/x-rar-compressed", FontAwesome.FILE_ARCHIVE_O);
        extraIconMap.put("application/x-7z-compressed", FontAwesome.FILE_ARCHIVE_O);
        extraIconMap.put("application/java-archive", FontAwesome.FILE_ARCHIVE_O);
        extraIconMap.put("application/x-tar", FontAwesome.FILE_ARCHIVE_O);
    }

    public static FontAwesome getFileIconResource(String fileName) {
        String mimeType = MimeTypesUtil.detectMimeType(fileName);
        if (MimeTypesUtil.isImage(mimeType)) {
            return FontAwesome.FILE_IMAGE_O;
        } else if (MimeTypesUtil.isAudio(mimeType)) {
            return FontAwesome.FILE_AUDIO_O;
        } else if (MimeTypesUtil.isVideo(mimeType)) {
            return FontAwesome.FILE_VIDEO_O;
        } else if (MimeTypesUtil.isText(mimeType)) {
            return FontAwesome.FILE_TEXT_O;
        }
        FontAwesome icon = extraIconMap.get(mimeType);
        return (icon == null) ? FontAwesome.FILE_O : icon;
    }
}

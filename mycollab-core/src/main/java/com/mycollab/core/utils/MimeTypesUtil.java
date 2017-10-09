package com.mycollab.core.utils;

import org.apache.tika.Tika;

import java.io.IOException;
import java.io.InputStream;

/**
 * Utility class mainly used to detect mimetype of file upload to MyCollab
 *
 * @author MyCollab Ltd.
 * @since 2.0
 */
public class MimeTypesUtil {
    public static final String BINARY_MIME_TYPE = "application/octet-stream";

    private static Tika tika = new Tika();

    /**
     * Detect mimetype of <code>inStream</code>
     *
     * @param inStream
     * @return mimetype of <code>inStream</code>. Return BINARY mimetype if it
     * can not detect
     */
    public static String detectMimeType(InputStream inStream) {
        try {
            return tika.detect(inStream);
        } catch (IOException e) {
            return BINARY_MIME_TYPE;
        }
    }

    /**
     * Detect mimetype of content <code>contentName</code>
     *
     * @param contentName
     * @return
     */
    public static String detectMimeType(String contentName) {
        return tika.detect(contentName.trim());
    }

    public static boolean isImageType(String contentName) {
        return tika.detect(contentName).startsWith("image/");
    }

    public static boolean isImage(String mimeType) {
        return mimeType.startsWith("image/");
    }

    public static boolean isText(String mimeType) {
        return mimeType.startsWith("text/");
    }

    public static boolean isAudio(String mimeType) {
        return mimeType.startsWith("audio/");
    }

    public static boolean isVideo(String mimeType) {
        return mimeType.startsWith("video/");
    }
}

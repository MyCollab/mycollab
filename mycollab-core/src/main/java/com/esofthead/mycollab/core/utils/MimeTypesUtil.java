/**
 * This file is part of mycollab-core.
 *
 * mycollab-core is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * mycollab-core is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with mycollab-core.  If not, see <http://www.gnu.org/licenses/>.
 */
/**
 * This file is part of mycollab-services.
 *
 * mycollab-services is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * mycollab-services is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with mycollab-services.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.esofthead.mycollab.core.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;

import org.apache.tika.Tika;

/**
 * Utility class mainly used to detect mimetype of file upload to MyCollab
 * 
 * @author MyCollab Ltd.
 * @since 2.0
 * 
 */
public class MimeTypesUtil {
	public static String BINARY_MIME_TYPE = "application/octet-stream";

	public static String BINARY_TYPE = "application";

	public static String TEXT_TYPE = "text";

	public static String IMAGE_TYPE = "image";

	public static String AUDIO_TYPE = "audio";

	public static String VIDEO_TYPE = "video";

	private static Tika tika = new Tika();

	/**
	 * Detect mimetype of <code>inStream</code>
	 * 
	 * @param inStream
	 * @return mimetype of <code>inStream</code>. Return BINARY mimetype if it
	 *         can not detect
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

	public static boolean isImage(String contentName) {
		return tika.detect(contentName).startsWith("image/");
	}

	private static final List<String> SUPPORTED_IMAGES = Arrays
			.asList("image/jpg", "image/jpeg", "image/png",
					"image/gif", "image/bmp");

	public static boolean isImageMimetype(String mimeType) {
		return SUPPORTED_IMAGES.contains(mimeType);
	}
}

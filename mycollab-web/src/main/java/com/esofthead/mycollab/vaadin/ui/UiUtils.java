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
package com.esofthead.mycollab.vaadin.ui;

import com.vaadin.server.Resource;

/**
 * 
 * @author MyCollab Ltd.
 * @since 1.0
 * 
 */
public class UiUtils {

	public static Resource getFileIconResource(String fileName) {
		String themeLink = "";

		int index = fileName.lastIndexOf(".");
		if (index > 0) {
			String suffix = fileName.substring(index + 1, fileName.length());

			if ("mp3".equalsIgnoreCase(suffix)
					|| "wav".equalsIgnoreCase(suffix)) {
				themeLink = "icons/16/filetypes/audio.png";
			} else if ("bmp".equalsIgnoreCase(suffix)) {
				themeLink = "icons/16/filetypes/bmp.png";
			} else if ("xls".equalsIgnoreCase(suffix)
					|| "xlsx".equalsIgnoreCase(suffix)) {
				themeLink = WebResourceIds._16_filetypes_excel;
			} else if ("gz".equalsIgnoreCase(suffix)) {
				themeLink = "icons/16/filetypes/gz.png";
			} else if ("html".equalsIgnoreCase(suffix)
					|| "xhtml".equalsIgnoreCase(suffix)) {
				themeLink = "icons/16/filetypes/html.png";
			} else if ("jpg".equalsIgnoreCase(suffix)) {
				themeLink = "icons/16/filetypes/jpg.png";
			} else if ("png".equalsIgnoreCase(suffix)) {
				themeLink = "icons/16/filetypes/png.png";
			} else if ("pdf".equalsIgnoreCase(suffix)) {
				themeLink = WebResourceIds._16_filetypes_pdf;
			} else if ("ppt".equalsIgnoreCase(suffix)
					|| "pptx".equalsIgnoreCase(suffix)
					|| "pps".equalsIgnoreCase(suffix)
					|| "pot".equalsIgnoreCase(suffix)
					|| "pptm".equalsIgnoreCase(suffix)
					|| "potx".equalsIgnoreCase(suffix)
					|| "potm".equalsIgnoreCase(suffix)) {
				themeLink = "icons/16/filetypes/ppt.png";
			} else if ("psd".equalsIgnoreCase(suffix)) {
				themeLink = "icons/16/filetypes/psd.png";
			} else if ("rar".equalsIgnoreCase(suffix)) {
				themeLink = "icons/16/filetypes/rar.png";
			} else if ("svg".equalsIgnoreCase(suffix)) {
				themeLink = "icons/16/filetypes/svg.png";
			} else if ("tar".equalsIgnoreCase(suffix)) {
				themeLink = "icons/16/filetypes/tar.png";
			} else if ("tiff".equalsIgnoreCase(suffix)) {
				themeLink = "icons/16/filetypes/tiff.png";
			} else if ("txt".equalsIgnoreCase(suffix)
					|| "rtf".equalsIgnoreCase(suffix)) {
				themeLink = "icons/16/filetypes/txt.png";
			} else if ("doc".equalsIgnoreCase(suffix)
					|| "docx".equalsIgnoreCase(suffix)) {
				themeLink = "icons/16/filetypes/word.png";
			} else if ("zip".equalsIgnoreCase(suffix)) {
				themeLink = "icons/16/filetypes/zip.png";
			} else if ("odt".equalsIgnoreCase(suffix)
					|| "odg".equalsIgnoreCase(suffix)
					|| "otp".equalsIgnoreCase(suffix)) {
				themeLink = "icons/16/filetypes/document.png";
			} else {
				themeLink = "icons/16/filetypes/blank.png";
			}
		} else {
			themeLink = "icons/16/filetypes/blank.png";
		}

		return MyCollabResource.newResource(themeLink);
	}
}

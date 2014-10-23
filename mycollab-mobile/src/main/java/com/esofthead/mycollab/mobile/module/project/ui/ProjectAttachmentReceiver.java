/**
 * This file is part of mycollab-mobile.
 *
 * mycollab-mobile is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * mycollab-mobile is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with mycollab-mobile.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.esofthead.mycollab.mobile.module.project.ui;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import com.vaadin.ui.Upload.Receiver;

/**
 * @author MyCollab Ltd.
 *
 * @since 4.5.2
 */
public class ProjectAttachmentReceiver implements Receiver {

	private static final long serialVersionUID = -2215467984054667160L;
	private String fileName;
	private String mimeType;
	private File file;

	@Override
	public OutputStream receiveUpload(String filename, String MIMEType) {
		fileName = filename;
		mimeType = MIMEType;
		try {
			if (file == null) {
				file = createFile(filename, mimeType);
			}
			return new FileOutputStream(file);
		} catch (final FileNotFoundException e) {
			throw new RuntimeException(e);
		}
	}

	protected File createFile(String fileName, String mimeType) {
		final String tempFileName = "upload_tmpfile_"
				+ System.currentTimeMillis();
		try {
			return File.createTempFile(tempFileName, null);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	public String getFileName() {
		return fileName;
	}

	public File getFile() {
		return file;
	}

	public String getMimeType() {
		return mimeType;
	}

	public void clearData() {
		if (this.file != null)
			file.delete();

		this.file = null;
		this.fileName = null;
		this.mimeType = null;
	}

}

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

package com.esofthead.mycollab.module.file.resource;

import java.io.InputStream;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.esofthead.mycollab.configuration.S3StorageConfiguration;
import com.esofthead.mycollab.configuration.SiteConfiguration;
import com.esofthead.mycollab.core.MyCollabException;
import com.vaadin.server.DownloadStream;
import com.vaadin.server.StreamResource;
import com.vaadin.util.FileTypeResolver;

/**
 * 
 * @author MyCollab Ltd.
 * @since 1.0
 */
class S3StreamDownloadResource extends StreamResource {

	private static final long serialVersionUID = 1L;

	private String documentPath;

	S3StreamDownloadResource(String documentPath) {
		super(new S3StreamSource(documentPath), getFilename(documentPath));
		this.documentPath = documentPath;
		this.setMIMEType(FileTypeResolver
				.getMIMEType(getFilename(documentPath)));
	}

	@Override
	public DownloadStream getStream() {
		final StreamSource ss = getStreamSource();
		if (ss == null) {
			return null;
		}
		final DownloadStream ds = new DownloadStream(ss.getStream(),
				getMIMEType(), getFilename(documentPath));
		ds.setBufferSize(getBufferSize());
		ds.setParameter("Content-Disposition", "attachment; filename="
				+ getFilename(documentPath));
		ds.setCacheTime(0);
		return ds;
	}

	private static String getFilename(String documentPath) {
		int index = documentPath.lastIndexOf("/");
		if (index > -1) {
			return documentPath.substring(index + 1);
		} else {
			return documentPath;
		}
	}

	private static class S3StreamSource implements StreamSource {
		private static final long serialVersionUID = 1L;
		private String documentPath;

		public S3StreamSource(String documentPath) {
			this.documentPath = documentPath;
		}

		@Override
		public InputStream getStream() {
			String fileName = getFilename(documentPath);
			S3StorageConfiguration storageConfiguration = (S3StorageConfiguration) SiteConfiguration
					.getStorageConfiguration();
			fileName = fileName.replaceAll(" ", "_").replaceAll("-", "_");
			AmazonS3 s3Client = storageConfiguration.newS3Client();
			try {
				S3Object obj = s3Client.getObject(new GetObjectRequest(
						storageConfiguration.getBucket(), documentPath));

				return obj.getObjectContent();
			} catch (Exception e) {
				throw new MyCollabException(
						"Error when get input stream from s3 with path "
								+ documentPath, e);
			}
		}

	}
}

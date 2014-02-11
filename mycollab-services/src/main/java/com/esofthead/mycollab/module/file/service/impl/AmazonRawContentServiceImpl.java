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
package com.esofthead.mycollab.module.file.service.impl;

import java.io.InputStream;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.CopyObjectRequest;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.ObjectListing;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import com.esofthead.mycollab.configuration.S3StorageConfiguration;
import com.esofthead.mycollab.configuration.SiteConfiguration;
import com.esofthead.mycollab.core.MyCollabException;
import com.esofthead.mycollab.module.file.service.RawContentService;

/**
 * 
 * @author MyCollab Ltd.
 * @since 1.0
 * 
 */
public class AmazonRawContentServiceImpl implements RawContentService {

	private S3StorageConfiguration storageConfiguration;

	public AmazonRawContentServiceImpl() {
		storageConfiguration = (S3StorageConfiguration) SiteConfiguration
				.getStorageConfiguration();
	}

	@Override
	public void saveContent(String objectPath, InputStream stream) {

		AmazonS3 s3client = storageConfiguration.newS3Client();
		try {
			ObjectMetadata metaData = new ObjectMetadata();
			metaData.setCacheControl("max-age=8640000");
			metaData.setContentLength(stream.available());
			PutObjectRequest request = new PutObjectRequest(
					storageConfiguration.getBucket(), objectPath, stream,
					metaData);

			s3client.putObject(request
					.withCannedAcl(CannedAccessControlList.PublicRead));
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public InputStream getContentStream(String objectPath) {
		AmazonS3 s3client = storageConfiguration.newS3Client();

		try {
			S3Object obj = s3client.getObject(new GetObjectRequest(
					storageConfiguration.getBucket(), objectPath));

			return obj.getObjectContent();
		} catch (Exception e) {
			throw new MyCollabException(e);
		}
	}

	@Override
	public void removePath(String object) {
		AmazonS3 s3client = storageConfiguration.newS3Client();

		try {
			ObjectListing listObjects = s3client.listObjects(
					storageConfiguration.getBucket(), object);
			for (S3ObjectSummary objectSummary : listObjects
					.getObjectSummaries()) {
				s3client.deleteObject(storageConfiguration.getBucket(),
						objectSummary.getKey());
			}

		} catch (Exception e) {
			throw new MyCollabException(e);
		}
	}

	@Override
	public void renamePath(String oldPath, String newPath) {
		AmazonS3 s3client = storageConfiguration.newS3Client();

		try {
			ObjectListing listObjects = s3client.listObjects(
					storageConfiguration.getBucket(), oldPath);
			for (S3ObjectSummary objectSummary : listObjects
					.getObjectSummaries()) {
				String key = objectSummary.getKey();
				String appendPath = key.substring(oldPath.length());
				String newAppPath = newPath + appendPath;

				CopyObjectRequest copyRequest = new CopyObjectRequest(
						storageConfiguration.getBucket(), key,
						storageConfiguration.getBucket(), newAppPath);
				copyRequest
						.withCannedAccessControlList(CannedAccessControlList.PublicRead);
				s3client.copyObject(copyRequest);

				DeleteObjectRequest deleteRequest = new DeleteObjectRequest(
						storageConfiguration.getBucket(), key);
				s3client.deleteObject(deleteRequest);
			}

		} catch (Exception e) {
			throw new MyCollabException(e);
		}

	}

	@Override
	public void movePath(String oldPath, String destinationPath) {
		removePath(destinationPath);
		renamePath(oldPath, destinationPath);
	}

	@Override
	public long getSize(String path) {
		AmazonS3 s3client = storageConfiguration.newS3Client();

		try {
			ObjectListing listObjects = s3client.listObjects(
					storageConfiguration.getBucket(), path);
			long size = 0;
			for (S3ObjectSummary objectSummary : listObjects
					.getObjectSummaries()) {
				size += objectSummary.getSize();
			}

			return size;

		} catch (Exception e) {
			throw new MyCollabException(e);
		}
	}
}

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
package com.esofthead.mycollab.tools;

import java.io.File;
import java.io.FileInputStream;
import java.util.Properties;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.esofthead.mycollab.configuration.S3StorageConfiguration;
import com.esofthead.mycollab.module.ecm.MimeTypesUtil;

public class SyncWebResourcesToS3Command {

	private static S3StorageConfiguration buildS3Configuration() {
		Properties props = new Properties();
		props.setProperty(S3StorageConfiguration.AWS_KEY,
				"AKIAJKX7POSGEJ4VRIDQ");
		props.setProperty(S3StorageConfiguration.AWS_SECRET_KEY,
				"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");
		props.setProperty(S3StorageConfiguration.BUCKET, "mycollab_assets");
		return S3StorageConfiguration.build(props);
	}

	public static void syncLocalResourcesToS3(String localPath, String s3Path) {
		S3StorageConfiguration conf = buildS3Configuration();
		AmazonS3 s3client = conf.newS3Client();

		File localFolder = new File(localPath);
		syncFoldersToS3(conf, s3client, localFolder,
				localFolder.getAbsolutePath(), s3Path);
	}

	private static void syncFoldersToS3(S3StorageConfiguration s3Conf,
			AmazonS3 s3client, File file, String baseFolderPath, String s3Path) {
		if (file.isDirectory()) {
			File[] files = file.listFiles();
			for (File subFile : files) {
				try {
					ObjectMetadata metaData = new ObjectMetadata();
					metaData.setCacheControl("max-age=8640000");
					metaData.setContentType(MimeTypesUtil
							.detectMimeType(subFile.getAbsolutePath()));
					metaData.setContentLength(subFile.length());
					String objectPath = s3Path
							+ subFile.getAbsolutePath().substring(
									baseFolderPath.length() + 1);
					PutObjectRequest request = new PutObjectRequest(
							s3Conf.getBucket(), objectPath,
							new FileInputStream(subFile), metaData);

					s3client.putObject(request
							.withCannedAcl(CannedAccessControlList.PublicRead));
				} catch (Exception e) {
					throw new RuntimeException(e);
				}
			}
		} else {

		}
	}

	public static void main(String[] args) {
		syncLocalResourcesToS3(
				"/Users/haiphucnguyen/Documents/workspace/mycollab/mycollab-web/src/main/webapp/assets/images/email",
				"assets/images/email/");
	}
}

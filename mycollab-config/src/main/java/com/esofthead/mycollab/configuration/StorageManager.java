/**
 * This file is part of mycollab-config.
 *
 * mycollab-config is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * mycollab-config is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with mycollab-config.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.esofthead.mycollab.configuration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.esofthead.mycollab.core.MyCollabException;

/**
 * 
 * @author MyCollab Ltd.
 * @since 4.5.1
 *
 */
public class StorageManager {
	private static final Logger LOG = LoggerFactory.getLogger(StorageManager.class);

	private static final String S3_CONF_CLS = "com.esofthead.mycollab.ondemand.configuration.S3StorageConfiguration";

	private static StorageManager instance = new StorageManager();

	private StorageConfiguration storageConf;

	private String storageSystem;

	private StorageManager() {
	}

	@SuppressWarnings("unchecked")
	static void loadStorageConfig() {
		// Load storage configuration
		String storageSystem = ApplicationProperties.getString(
				ApplicationProperties.STORAGE_SYSTEM,
				StorageConfiguration.FILE_STORAGE_SYSTEM);
		instance.storageSystem = storageSystem;
		if (StorageConfiguration.FILE_STORAGE_SYSTEM.equals(storageSystem)) {
			LOG.debug("MyCollab uses file storage system");
			instance.storageConf = new FileStorageConfiguration();
		} else if (StorageConfiguration.S3_STORAGE_SYSTEM.equals(storageSystem)) {
			LOG.debug("MyCollab uses amazon s3 system");
			try {
				Class<StorageConfiguration> s3Conf = (Class<StorageConfiguration>) Class
						.forName(S3_CONF_CLS);
				StorageConfiguration newInstance = s3Conf.newInstance();
				instance.storageConf = newInstance;
			} catch (Exception e) {
				LOG.error("Can not load s3 file system with class "
						+ S3_CONF_CLS, e);
				System.exit(-1);
			}
		} else {
			throw new MyCollabException("Can not load storage  "
					+ storageSystem);
		}
	}

	public static String getAvatarLink(String userAvatarId, int size) {
		return instance.storageConf.getAvatarPath(userAvatarId, size);
	}

	public static StorageConfiguration getConfiguration() {
		return instance.storageConf;
	}

	public static boolean isFileStorage() {
		return StorageConfiguration.FILE_STORAGE_SYSTEM.equals(instance.storageSystem);
	}

	public static boolean isS3Storage() {
		return StorageConfiguration.S3_STORAGE_SYSTEM.equals(instance.storageSystem);
	}
}

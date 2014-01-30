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

/**
 * 
 * @author MyCollab Ltd.
 * @since 1.0
 * 
 */
class StorageSetting {
	public static final String FILE_STORAGE_SYSTEM = "file";

	public static final String S3_STORAGE_SYSTEM = "s3";

	public static boolean isFileStorage() {
		String storageSystem = ApplicationProperties.getString(
				ApplicationProperties.STORAGE_SYSTEM, "file");
		return StorageSetting.FILE_STORAGE_SYSTEM.equals(storageSystem.trim());
	}

	public static boolean isS3Storage() {
		String storageSystem = ApplicationProperties.getString(
				ApplicationProperties.STORAGE_SYSTEM, "file");
		return StorageSetting.S3_STORAGE_SYSTEM.equals(storageSystem.trim());
	}
}

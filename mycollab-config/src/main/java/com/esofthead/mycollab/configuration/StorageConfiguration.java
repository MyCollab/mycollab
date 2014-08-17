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
 * File configuration for storage file in MyCollab. We support two kinds of file
 * system:
 * <ul>
 * <li>S3 mode: Files are stored in Amazon S3. This is used for MyCollab cloud
 * service.</li>
 * <li>File system: Files are stored in OS file storage. This is used for
 * installation mode of MyCollab</li>
 * </ul>
 * 
 * @author MyCollab Ltd.
 * @since 1.0
 * 
 */
public interface StorageConfiguration {
	public static final String FILE_STORAGE_SYSTEM = "file";

	public static final String S3_STORAGE_SYSTEM = "s3";

	public String getAvatarPath(String userAvatarId, int size);

	public String getLogoPath(String accountLogoId, int size);

	public String getResourcePath(String documentPath);
}

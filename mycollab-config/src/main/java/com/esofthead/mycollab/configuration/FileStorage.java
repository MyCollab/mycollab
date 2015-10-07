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

import com.esofthead.mycollab.core.utils.FileUtils;

import java.io.File;

/**
 * Configuration of file system storage mode
 *
 * @author MyCollab Ltd.
 * @since 1.0
 */
public final class FileStorage extends Storage {
    private static final FileStorage _instance = new FileStorage();

    private File baseContentFolder;

    private FileStorage() {
        String userFolder = System.getProperty("user.home");
        baseContentFolder = new File(userFolder + "/.mycollab");
        FileUtils.mkdirs(baseContentFolder);
        File avatarFolder = new File(baseContentFolder, "avatar");
        File logoFolder = new File(baseContentFolder, "logo");
        FileUtils.mkdirs(avatarFolder);
        FileUtils.mkdirs(logoFolder);
    }

    public static final FileStorage getInstance() {
        return _instance;
    }

    /**
     * @param username
     * @param size
     * @return null if user avatar is not existed
     */
    public File getAvatarFile(String username, int size) {
        File userAvatarFile = new File(baseContentFolder, String.format("/avatar/%s_%d.png", username, size));
        if (userAvatarFile.exists()) {
            return userAvatarFile;
        } else {
            return null;
        }
    }

    public File getBaseContentFolder() {
        return baseContentFolder;
    }
}

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
package com.esofthead.mycollab.core.utils;

import com.esofthead.mycollab.core.MyCollabException;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * @author MyCollab Ltd.
 * @since 5.0.4
 */
public class FileUtils {
    private static long KB_SIZE = 1024;
    private static long MB_SIZE = 1024 * 1024;
    private static long GB_SIZE = 1024 * 1024 * 1024;

    public static String getVolumeDisplay(Long volume) {
        if (volume == null) {
            return "0 Kb";
        } else if (volume < KB_SIZE) {
            return volume + " Bytes";
        } else if (volume < MB_SIZE) {
            return Math.floor(((float) volume / KB_SIZE) * 100) / 100 + " Kb";
        } else if (volume < GB_SIZE) {
            return Math.floor(((float) volume / MB_SIZE) * 100) / 100 + " Mb";
        } else {
            return Math.floor((volume / GB_SIZE) * 100) / 100 + " Gb";
        }
    }

    /**
     *
     * @param baseFolder
     * @param relativePaths
     * @return null if can not get the folder
     */
    public static File getDesireFile(String baseFolder, String... relativePaths) {
        File file;
        for (String relativePath:relativePaths) {
            file = new File(baseFolder, relativePath);
            if (file.exists()) {
                return file;
            }
        }
        return null;
    }

    public static Reader getReader(String templateFile) {
        try {
            return new InputStreamReader(FileUtils.class.getClassLoader()
                    .getResourceAsStream(templateFile), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            return new InputStreamReader(FileUtils.class.getClassLoader()
                    .getResourceAsStream(templateFile));
        }
    }

    public static void mkdirs(File file) {
        if (file.exists()) {
            return;
        }
        try {
            Files.createDirectories(file.toPath());
        } catch (IOException e) {
            throw new MyCollabException(e);
        }
    }
}

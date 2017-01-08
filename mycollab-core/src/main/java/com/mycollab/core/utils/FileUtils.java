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
package com.mycollab.core.utils;

import com.google.common.base.MoreObjects;
import com.mycollab.core.MyCollabException;
import com.mycollab.core.UserInvalidInputException;
import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;

/**
 * @author MyCollab Ltd.
 * @since 5.0.4
 */
public class FileUtils {

    private static File homeFolder;

    static {
        String userFolder = System.getProperty("user.dir");
        File homeDir = new File(userFolder + "/.mycollab");
        File userHomeDir = new File(System.getProperty("user.home") + "/.mycollab");
        if (userHomeDir.exists()) {
            try {
                Files.move(userHomeDir.toPath(), homeDir.toPath(), REPLACE_EXISTING);
                homeFolder = homeDir;
            } catch (Exception e) {
                homeFolder = userHomeDir;
            }
        } else {
            if (homeDir.exists()) {
                homeFolder = homeDir;
            } else {
                FileUtils.mkdirs(homeDir);
                homeFolder = homeDir;
            }
        }
    }

    public static File getHomeFolder() {
        return homeFolder;
    }

    public static File getUserFolder() {
        String userDir = MoreObjects.firstNonNull(System.getProperty("MYCOLLAB_APP_HOME"), System.getProperty("user.dir"));
        return new File(userDir);
    }

    public static String readFileAsPlainString(String fileName) {
        try {
            File pricingFile = FileUtils.getDesireFile(FileUtils.getUserFolder(), fileName, "src/main/conf/" + fileName);
            InputStream pricingStream;
            if (pricingFile != null) {
                pricingStream = new FileInputStream(pricingFile);
            } else {
                pricingStream = FileUtils.class.getClassLoader().getResourceAsStream(fileName);
            }

            return IOUtils.toString(pricingStream, "UTF-8");
        } catch (IOException e) {
            throw new MyCollabException(e);
        }
    }

    public static String getVolumeDisplay(Long volume) {
        long GB_SIZE = 1024 * 1024 * 1024;
        long MB_SIZE = 1024 * 1024;
        long KB_SIZE = 1024;
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
     * @param baseFolder
     * @param relativePaths
     * @return null if can not get the folder
     */
    public static File getDesireFile(File baseFolder, String... relativePaths) {
        File file;
        for (String relativePath : relativePaths) {
            file = new File(baseFolder, relativePath);
            if (file.exists()) {
                return file;
            }
        }
        return null;
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

    private static final String[] INVALID_RESOURCE_BASENAMES = new String[]{"aux", "com1", "com2", "com3", "com4", //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$
            "com5", "com6", "com7", "com8", "com9", "con", "lpt1", "lpt2", //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$ //$NON-NLS-7$ //$NON-NLS-8$
            "lpt3", "lpt4", "lpt5", "lpt6", "lpt7", "lpt8", "lpt9", "nul", "prn"}; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$ //$NON-NLS-7$ //$NON-NLS-8$ //$NON-NLS-9$

    private static final String[] INVALID_RESOURCE_FULLNAMES = new String[]{"clock$"}; //$NON-NLS-1$;

    public static boolean isValidFileName(String name) {
        if (name.equals(".") || name.equals("..")) //$NON-NLS-1$ //$NON-NLS-2$
            return false;
        //empty names are not valid
        final int length = name.length();
        if (length == 0)
            return false;
        final char lastChar = name.charAt(length - 1);
        // filenames ending in dot are not valid
        if (lastChar == '.')
            return false;
        // file names ending with whitespace are truncated (bug 118997)
        if (Character.isWhitespace(lastChar))
            return false;
        int dot = name.indexOf('.');
        //on windows, filename suffixes are not relevant to name validity
        String basename = dot == -1 ? name : name.substring(0, dot);
        if (Arrays.binarySearch(INVALID_RESOURCE_BASENAMES, basename.toLowerCase()) >= 0)
            return false;
        return Arrays.binarySearch(INVALID_RESOURCE_FULLNAMES, name.toLowerCase()) < 0;
    }

    private static final Pattern ILLEGAL_FOLDER_PATTERN = Pattern.compile("[.<>:&/\\|?*&%()\\+\\-\\[\\]]");

    public static void assertValidFolderName(String name) {
        Matcher matcher = ILLEGAL_FOLDER_PATTERN.matcher(name);
        if (matcher.find()) {
            throw new UserInvalidInputException("Please enter valid folder name except any follow characters: " + ILLEGAL_FOLDER_PATTERN.pattern());
        }
    }

    public static String escape(String fileName) {
        return fileName.replaceAll("[<>:&/\\|?*&%()\\+\\-\\[\\]]", "");
    }
}

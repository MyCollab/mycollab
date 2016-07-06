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
package com.mycollab.module.file.service.impl;

import com.mycollab.configuration.FileStorage;
import com.mycollab.core.MyCollabException;
import com.mycollab.module.file.service.RawContentService;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
public class FileRawContentServiceImpl implements RawContentService {
    private static final int BUFFER_SIZE = 1024;

    private static final Logger LOG = LoggerFactory.getLogger(FileRawContentServiceImpl.class);

    private File baseFolder;

    public FileRawContentServiceImpl() {
        baseFolder = FileStorage.getInstance().getBaseContentFolder();
    }

    @Override
    public void saveContent(String objectPath, InputStream stream) {
        int startFileNameIndex = objectPath.lastIndexOf("/");
        if (startFileNameIndex > 0) {
            /*
             * make sure the directory exist
			 */
            String folderPath = objectPath.substring(0, startFileNameIndex);
            File file = new File(baseFolder, folderPath);
            if (!file.exists() && !file.mkdirs()) {
                throw new MyCollabException("Create directory failed");
            }
        }

        try (BufferedOutputStream outStream = new BufferedOutputStream(
                new FileOutputStream(new File(baseFolder, objectPath)))) {
            byte[] buffer = new byte[BUFFER_SIZE];
            int byteRead;

            while ((byteRead = stream.read(buffer)) >= 0) {
                outStream.write(buffer, 0, byteRead);
            }
        } catch (IOException e) {
            throw new MyCollabException(e);
        }

    }

    @Override
    public InputStream getContentStream(String objectPath) {
        try {
            File file = new File(baseFolder, objectPath);
            return new FileInputStream(file);
        } catch (FileNotFoundException e) {
            throw new MyCollabException(e);
        }
    }

    @Override
    public void removePath(String object) {
        try {
            File file = new File(baseFolder, object);
            if (file.exists()) {
                if (file.isDirectory()) {
                    FileUtils.deleteDirectory(file);
                } else {
                    file.delete();
                }
            }
        } catch (Exception e) {
            throw new MyCollabException(e);
        }
    }

    @Override
    public void renamePath(String oldPath, String newPath) {
        File file = new File(baseFolder, oldPath);
        if (file.exists()) {
            boolean result = file.renameTo(new File(baseFolder + "/" + newPath));
            if (!result) {
                LOG.error("Can not rename old path {} to new path {}", oldPath, newPath);
            }
        } else {
            LOG.error("Can not rename old path {} to new path {} because file is not existed", oldPath, newPath);
        }
    }

    @Override
    public void movePath(String oldPath, String destinationPath) {
        try {
            File src = new File(baseFolder + "/" + oldPath);
            File dest = new File(baseFolder + "/" + destinationPath);

            if (!src.exists()) {
                LOG.debug("Source: {} is not existed", src.getPath());
                return;
            }

            if (dest.exists()) {
                FileUtils.deleteQuietly(dest);
            }

            if (src.isFile()) {
                FileUtils.moveFile(src, dest);
            } else {
                FileUtils.moveDirectory(src, dest);
            }
        } catch (IOException e) {
            throw new MyCollabException(e);
        }
    }

    @Override
    public long getSize(String path) {
        File file = new File(baseFolder + "/" + path);
        if (file.exists()) {
            if (file.isFile()) {
                return FileUtils.sizeOf(file);
            } else if (file.isDirectory()) {
                return FileUtils.sizeOfDirectory(file);
            } else {
                return 0;
            }
        }

        return 0;
    }
}

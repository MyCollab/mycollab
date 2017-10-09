package com.mycollab.mobile.ui;

import com.mycollab.core.MyCollabException;
import org.vaadin.easyuploads.FileFactory;

import java.io.File;
import java.io.IOException;

/**
 * @author MyCollab Ltd.
 * @since 4.5.2
 */
public class TempFileFactory implements FileFactory {

    public File createFile(String fileName, String mimeType) {
        final String tempFileName = "upload_tmpfile_" + System.currentTimeMillis();
        try {
            return File.createTempFile(tempFileName, null);
        } catch (IOException e) {
            throw new MyCollabException(e);
        }
    }

}

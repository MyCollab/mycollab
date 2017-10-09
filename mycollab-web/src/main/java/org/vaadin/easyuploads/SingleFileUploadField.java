package org.vaadin.easyuploads;

import java.io.File;

/**
 * @author MyCollab Ltd.
 * @since 2.0
 */
public class SingleFileUploadField extends UploadField {
    private static final long serialVersionUID = 1L;

    public SingleFileUploadField() {
        super(StorageMode.FILE);
        this.setFileFactory(new TempFileFactory());
    }

    public File getContentAsFile() {
        return (File) getValue();
    }

    public String getFileName() {
        return getLastFileName();
    }

    public long getFileSize() {
        return getLastFileSize();
    }
}

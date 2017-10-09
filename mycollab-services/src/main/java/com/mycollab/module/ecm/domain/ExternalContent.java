package com.mycollab.module.ecm.domain;

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
public class ExternalContent extends Content {
    private String storageName;

    private ExternalDrive externalDrive;

    private byte[] thumbnailBytes;

    public ExternalContent() {
        super();
    }

    public ExternalContent(String path) {
        super(path);
    }

    public String getStorageName() {
        return storageName;
    }

    public void setStorageName(String storageName) {
        this.storageName = storageName;
    }

    public ExternalDrive getExternalDrive() {
        return externalDrive;
    }

    public void setExternalDrive(ExternalDrive externalDrive) {
        this.externalDrive = externalDrive;
    }

    public byte[] getThumbnailBytes() {
        return thumbnailBytes;
    }

    public void setThumbnailBytes(byte[] thumbnailBytes) {
        this.thumbnailBytes = thumbnailBytes;
    }
}

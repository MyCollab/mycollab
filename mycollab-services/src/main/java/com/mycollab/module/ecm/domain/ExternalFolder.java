package com.mycollab.module.ecm.domain;

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
public class ExternalFolder extends Folder {
    private String storageName;

    private ExternalDrive externalDrive;

    public ExternalFolder() {
        super();
    }

    public ExternalFolder(String path) {
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
}

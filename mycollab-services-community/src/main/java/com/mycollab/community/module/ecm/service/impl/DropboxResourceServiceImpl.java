package com.mycollab.community.module.ecm.service.impl;

import com.mycollab.core.UnsupportedFeatureException;
import com.mycollab.module.ecm.service.DropboxResourceService;
import com.mycollab.module.ecm.domain.*;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.util.List;

/**
 * @author MyCollab Ltd.
 * @since 4.5.2
 */
@Service
public class DropboxResourceServiceImpl implements DropboxResourceService {

    @Override
    public List<Resource> getResources(ExternalDrive drive, String path) {
        throw new UnsupportedFeatureException(
                "This feature is not supported except onsite mode");
    }

    @Override
    public List<ExternalFolder> getSubFolders(ExternalDrive drive, String path) {
        throw new UnsupportedFeatureException(
                "This feature is not supported except onsite mode");
    }

    @Override
    public Resource getCurrentResourceByPath(ExternalDrive drive, String path) {
        throw new UnsupportedFeatureException(
                "This feature is not supported except onsite mode");
    }

    @Override
    public Folder getParentResourceFolder(ExternalDrive drive, String childPath) {
        throw new UnsupportedFeatureException(
                "This feature is not supported except onsite mode");
    }

    @Override
    public Folder createNewFolder(ExternalDrive drive, String path) {
        throw new UnsupportedFeatureException(
                "This feature is not supported except onsite mode");
    }

    @Override
    public void saveContent(ExternalDrive drive, Content content, InputStream in) {
        throw new UnsupportedFeatureException(
                "This feature is not supported except onsite mode");

    }

    @Override
    public void rename(ExternalDrive drive, String oldPath, String newPath) {
        throw new UnsupportedFeatureException(
                "This feature is not supported except onsite mode");

    }

    @Override
    public void deleteResource(ExternalDrive drive, String path) {
        throw new UnsupportedFeatureException(
                "This feature is not supported except onsite mode");

    }

    @Override
    public InputStream download(ExternalDrive drive, String path) {
        throw new UnsupportedFeatureException(
                "This feature is not supported except onsite mode");
    }

    @Override
    public void move(ExternalDrive drive, String fromPath, String toPath) {
        throw new UnsupportedFeatureException(
                "This feature is not supported except onsite mode");

    }
}

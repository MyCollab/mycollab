package com.mycollab.vaadin.resources;

import com.mycollab.common.i18n.FileI18nEnum;
import com.mycollab.core.UserInvalidInputException;
import com.mycollab.module.ecm.domain.Folder;
import com.mycollab.module.ecm.domain.Resource;
import com.mycollab.vaadin.UserUIContext;
import com.vaadin.server.StreamResource;
import com.vaadin.server.StreamResource.StreamSource;
import org.apache.commons.collections.CollectionUtils;

import java.util.Collection;
import java.util.List;

/**
 * @author MyCollab Ltd.
 * @since 3.0
 */
public class StreamDownloadResourceUtil {

    public static String getDownloadFileName(Collection<Resource> lstRes) {
        if (CollectionUtils.isEmpty(lstRes)) {
            throw new UserInvalidInputException(UserUIContext.getMessage(FileI18nEnum.ERROR_NO_SELECTED_FILE_TO_DOWNLOAD));
        } else if (lstRes.size() == 1) {
            Resource resource = lstRes.iterator().next();
            return (resource instanceof Folder) ? "out.zip" : resource.getName();
        } else {
            return "out.zip";
        }

    }

    public static StreamResource getStreamResourceSupportExtDrive(List<Resource> lstRes) {
        String filename = getDownloadFileName(lstRes);
        StreamSource streamSource = getStreamSourceSupportExtDrive(lstRes);
        return new StreamResource(streamSource, filename);
    }

    public static StreamSource getStreamSourceSupportExtDrive(Collection<Resource> lstRes) {
        if (CollectionUtils.isEmpty(lstRes)) {
            throw new UserInvalidInputException(UserUIContext.getMessage(FileI18nEnum.ERROR_NO_SELECTED_FILE_TO_DOWNLOAD));
        } else {
            return new StreamDownloadResourceSupportExtDrive(lstRes);
        }
    }
}

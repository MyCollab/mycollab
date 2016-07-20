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

import com.mycollab.core.MyCollabException;
import com.mycollab.core.utils.ImageUtil;
import com.mycollab.core.utils.StringUtils;
import com.mycollab.module.ecm.domain.Content;
import com.mycollab.module.ecm.service.ResourceService;
import com.mycollab.module.file.service.EntityUploaderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.GregorianCalendar;
import java.util.UUID;

/**
 * @author MyCollab Ltd
 * @since 5.2.12
 */
@Service
public class EntityUploaderServiceImpl implements EntityUploaderService {
    private static final Logger LOG = LoggerFactory.getLogger(EntityUploaderServiceImpl.class);

    @Autowired
    private ResourceService resourceService;

    @Override
    public String upload(BufferedImage image, String basePath, String oldId, String uploadedUser, Integer sAccountId, int[] preferSizes) {
        // Construct new logoid
        String newLogoId = new GregorianCalendar().getTimeInMillis() + UUID.randomUUID().toString();

        for (int preferSize : preferSizes) {
            uploadLogoToStorage(uploadedUser, image, basePath, newLogoId, preferSize);
        }

        if (StringUtils.isNotBlank(oldId)) {
            for (int preferSize : preferSizes) {
                try {
                    resourceService.removeResource(String.format("%s/%s_%d.png", basePath, oldId, preferSize),
                            uploadedUser, sAccountId);
                } catch (Exception e) {
                    LOG.error("Error while delete old logo", e);
                }
            }
        }

        return newLogoId;
    }

    private void uploadLogoToStorage(String uploadedUser, BufferedImage image, String basePath, String logoId, int width) {
        BufferedImage scaleImage = ImageUtil.scaleImage(image, (float) width / image.getWidth());
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        try {
            ImageIO.write(scaleImage, "png", outStream);
        } catch (IOException e) {
            throw new MyCollabException("Error while write image to stream", e);
        }
        Content logoContent = new Content();
        logoContent.setPath(String.format("%s/%s_%d.png", basePath, logoId, width));
        logoContent.setName(logoId + "_" + width);
        resourceService.saveContent(logoContent, uploadedUser, new ByteArrayInputStream(outStream.toByteArray()), null);
    }
}

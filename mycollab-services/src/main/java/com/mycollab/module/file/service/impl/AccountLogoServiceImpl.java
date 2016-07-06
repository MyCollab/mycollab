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
import com.mycollab.module.ecm.domain.Content;
import com.mycollab.module.ecm.service.ResourceService;
import com.mycollab.module.file.PathUtils;
import com.mycollab.module.file.service.AccountLogoService;
import com.mycollab.module.user.domain.BillingAccount;
import com.mycollab.module.user.service.BillingAccountService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.UUID;

/**
 * @author MyCollab Ltd.
 * @since 4.1
 */
@Service
public class AccountLogoServiceImpl implements AccountLogoService {
    private static final Logger LOG = LoggerFactory.getLogger(AccountLogoServiceImpl.class);
    private static final int[] SUPPORT_SIZES = {150, 100, 64};

    @Autowired
    private ResourceService resourceService;

    @Autowired
    private BillingAccountService billingAccountService;

    @Override
    public String upload(String uploadedUser, BufferedImage image, Integer sAccountId) {
        BillingAccount account = billingAccountService.getAccountById(sAccountId);
        if (account == null) {
            throw new MyCollabException("There's no account associated with provided id " + sAccountId);
        }

        // Construct new logoid
        String newLogoId = UUID.randomUUID().toString();

        for (int i = 0; i < SUPPORT_SIZES.length; i++) {
            uploadLogoToStorage(uploadedUser, image, newLogoId, SUPPORT_SIZES[i], sAccountId);
        }

        // account old logo
        if (account.getLogopath() != null) {
            for (int i = 0; i < SUPPORT_SIZES.length; i++) {
                try {
                    resourceService.removeResource(PathUtils.buildLogoPath(sAccountId, account.getLogopath(),
                            SUPPORT_SIZES[i]), uploadedUser, sAccountId);
                } catch (Exception e) {
                    LOG.error("Error while delete old logo", e);
                }
            }
        }

        // save logo id
        account.setLogopath(newLogoId);
        billingAccountService.updateSelectiveWithSession(account, uploadedUser);

        return newLogoId;
    }

    private void uploadLogoToStorage(String uploadedUser, BufferedImage image, String logoId, int width, Integer sAccountId) {
        BufferedImage scaleImage = ImageUtil.scaleImage(image, (float) width / image.getWidth());
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        try {
            ImageIO.write(scaleImage, "png", outStream);
        } catch (IOException e) {
            throw new MyCollabException("Error while write image to stream", e);
        }
        Content logoContent = new Content();
        logoContent.setPath(PathUtils.buildLogoPath(sAccountId, logoId, width));
        logoContent.setName(logoId + "_" + width);
        resourceService.saveContent(logoContent, uploadedUser, new ByteArrayInputStream(outStream.toByteArray()), null);
    }
}

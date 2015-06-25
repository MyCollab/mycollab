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
package com.esofthead.mycollab.module.file.service.impl;

import com.esofthead.mycollab.core.MyCollabException;
import com.esofthead.mycollab.core.UserInvalidInputException;
import com.esofthead.mycollab.core.utils.ImageUtil;
import com.esofthead.mycollab.module.ecm.domain.Content;
import com.esofthead.mycollab.module.ecm.service.ResourceService;
import com.esofthead.mycollab.module.file.PathUtils;
import com.esofthead.mycollab.module.file.service.AccountFavIconService;
import com.esofthead.mycollab.module.user.domain.BillingAccount;
import com.esofthead.mycollab.module.user.service.BillingAccountService;
import net.sf.image4j.codec.ico.ICOEncoder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.UUID;

/**
 * @author MyCollab Ltd
 * @since 5.0.10
 */
@Service
public class AccountFavIconServiceImpl implements AccountFavIconService {

    @Autowired
    private ResourceService resourceService;

    @Autowired
    private BillingAccountService billingAccountService;

    @Override
    public String upload(String uploadedUser, BufferedImage logo, Integer sAccountId) {
        if (logo.getWidth() != logo.getHeight()) {
            int min = Math.min(logo.getWidth(), logo.getHeight());
            logo = logo.getSubimage(0, 0, min, min);
        }
        BillingAccount account = billingAccountService.getAccountById(sAccountId);
        if (account == null) {
            throw new MyCollabException(
                    "There's no account associated with provided id " + sAccountId);
        }

        logo = ImageUtil.scaleImage(logo, 32, 32);
        // Construct new logoid
        String newLogoId = UUID.randomUUID().toString();
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        try {
            ICOEncoder.write(logo, outStream);
        } catch (IOException e) {
            throw new UserInvalidInputException("Can not convert file to ico format", e);
        }
        Content logoContent = new Content();
        logoContent.setPath(PathUtils.buildFavIconPath(sAccountId, newLogoId));
        logoContent.setName(newLogoId);
        resourceService.saveContent(logoContent, uploadedUser, new ByteArrayInputStream(outStream.toByteArray()), null);

        //remove the old favicon
        resourceService.removeResource(PathUtils.buildFavIconPath(sAccountId, account.getFaviconpath()),
                uploadedUser, sAccountId);

        account.setFaviconpath(newLogoId);
        billingAccountService.updateSelectiveWithSession(account, uploadedUser);

        return newLogoId;
    }
}

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

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

import javax.imageio.ImageIO;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.esofthead.mycollab.core.MyCollabException;
import com.esofthead.mycollab.core.utils.ImageUtil;
import com.esofthead.mycollab.module.ecm.domain.Content;
import com.esofthead.mycollab.module.ecm.service.ResourceService;
import com.esofthead.mycollab.module.file.service.AccountLogoService;
import com.esofthead.mycollab.module.user.dao.UserAccountMapper;
import com.esofthead.mycollab.module.user.domain.UserAccount;
import com.esofthead.mycollab.module.user.domain.UserAccountExample;
import com.esofthead.mycollab.module.user.service.AccountThemeService;

/**
 * 
 * @author MyCollab Ltd.
 * @since 4.1
 * 
 */

@Service
public class AccountLogoServiceImpl implements AccountLogoService {
	private static final Logger LOG = LoggerFactory
			.getLogger(AccountLogoServiceImpl.class);

	@Autowired
	private ResourceService resourceService;

	@Autowired
	private UserAccountMapper userAccountMapper;

	@Autowired
	private AccountThemeService themeService;

	private static final int PIXELS_150 = 150;
	private static final int PIXELS_100 = 100;
	private static final int PIXELS_64 = 64;

	private static final int[] SUPPORT_SIZES = { PIXELS_150, PIXELS_100,
			PIXELS_64 };

	@Override
	public String uploadLogo(String uploadedUser, BufferedImage logo,
			String logoId, Integer saccountid) {
		UserAccountExample ex = new UserAccountExample();
		ex.createCriteria().andAccountidEqualTo(saccountid)
				.andIsaccountownerEqualTo(true);
		List<UserAccount> userAccounts = userAccountMapper.selectByExample(ex);
		if (userAccounts == null || userAccounts.size() == 0) {
			throw new MyCollabException(
					"There's no account associated with provided id");
		}
		String username = userAccounts.get(0).getUsername();

		// Construct new logoid
		String randomString = UUID.randomUUID().toString();
		String newLogoId = username + "_" + randomString;

		for (int i = 0; i < SUPPORT_SIZES.length; i++) {
			uploadLogoToStorage(uploadedUser, logo, newLogoId, SUPPORT_SIZES[i]);
		}

		// save logo id
		// AccountTheme accountTheme = themeService.getAccountTheme(saccountid);
		// accountTheme.setLogopath(newLogoId);
		// themeService.saveAccountTheme(accountTheme, saccountid);

		// Delete old logo
		if (logoId != null) {
			for (int i = 0; i < SUPPORT_SIZES.length; i++) {
				try {
					resourceService.removeResource("logo/" + logoId + "_"
							+ SUPPORT_SIZES[i] + ".png", uploadedUser,
							saccountid);
				} catch (Exception e) {
					LOG.error("Error while delete old logo", e);
				}
			}
		}

		return newLogoId;
	}

	private void uploadLogoToStorage(String uploadedUser, BufferedImage image,
			String logoId, int width) {
		BufferedImage scaleImage = ImageUtil.scaleImage(image, (float) width
				/ image.getWidth());
		ByteArrayOutputStream outStream = new ByteArrayOutputStream();
		try {
			ImageIO.write(scaleImage, "png", outStream);
		} catch (IOException e) {
			throw new MyCollabException("Error while write image to stream", e);
		}
		resourceService.saveContent(
				Content.buildContentInstance(null, "logo/" + logoId + "_"
						+ width + ".png"), uploadedUser,
				new ByteArrayInputStream(outStream.toByteArray()), null);
	}
}

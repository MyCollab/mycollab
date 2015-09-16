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
import com.esofthead.mycollab.core.utils.ImageUtil;
import com.esofthead.mycollab.eventmanager.EventBusFactory;
import com.esofthead.mycollab.events.SessionEvent;
import com.esofthead.mycollab.module.ecm.domain.Content;
import com.esofthead.mycollab.module.ecm.service.ResourceService;
import com.esofthead.mycollab.module.file.service.UserAvatarService;
import com.esofthead.mycollab.module.user.dao.UserMapper;
import com.esofthead.mycollab.module.user.domain.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
@Service(value = "userAvatarService")
public class UserAvatarServiceImpl implements UserAvatarService {
    private static final Logger LOG = LoggerFactory.getLogger(UserAvatarServiceImpl.class);

    @Autowired
    private ResourceService resourceService;

    @Autowired
    private UserMapper userMapper;

    private static final int[] SUPPORT_SIZES = {100, 64, 48, 32, 24, 16};

    @Override
    public String uploadDefaultAvatar(String username) {
        // Save default user avatar
        InputStream imageResourceStream = this.getClass().getClassLoader().getResourceAsStream("assets/icons/default_user_avatar_100.png");
        BufferedImage imageBuff;
        try {
            imageBuff = ImageIO.read(imageResourceStream);
            return uploadAvatar(imageBuff, username, null);
        } catch (IOException e) {
            throw new MyCollabException("Error while set default avatar to user", e);
        }
    }

    @Override
    public String uploadAvatar(BufferedImage image, String username, String avatarId) {
        // Construct new avatarid
        String randomString = UUID.randomUUID().toString();
        String newAvatarId = username + "_" + randomString;

        for (int i = 0; i < SUPPORT_SIZES.length; i++) {
            uploadAvatarToStorage(username, image, newAvatarId, SUPPORT_SIZES[i]);
        }

        // save avatar id
        User user = new User();
        user.setUsername(username);
        user.setAvatarid(newAvatarId);
        userMapper.updateByPrimaryKeySelective(user);

        // Delete old avatar
        if (avatarId != null) {
            for (int i = 0; i < SUPPORT_SIZES.length; i++) {
                try {
                    resourceService.removeResource(String.format("avatar/%s_%d.png", avatarId, SUPPORT_SIZES[i]), username, null);
                } catch (Exception e) {
                    LOG.error("Error while delete old avatar", e);
                }
            }
        }

        LOG.debug("Notify user avatar change");
        EventBusFactory.getInstance().post(new SessionEvent.UserProfileChangeEvent(UserAvatarServiceImpl.this, "avatarid", newAvatarId));

        return newAvatarId;
    }

    private void uploadAvatarToStorage(String username, BufferedImage image, String avatarId, int width) {
        BufferedImage scaleImage = ImageUtil.scaleImage(image, (float) width / image.getWidth());
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        try {
            ImageIO.write(scaleImage, "png", outStream);
        } catch (IOException e) {
            throw new MyCollabException("Error while write image to stream", e);
        }
        resourceService.saveContent(Content.buildContentInstance(null, String.format("avatar/%s_%d.png", avatarId, width)),
                username, new ByteArrayInputStream(outStream.toByteArray()), null);
    }
}

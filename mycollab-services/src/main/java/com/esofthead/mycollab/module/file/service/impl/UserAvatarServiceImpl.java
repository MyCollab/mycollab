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
import com.esofthead.mycollab.eventmanager.EventBusFactory;
import com.esofthead.mycollab.events.SessionEvent;
import com.esofthead.mycollab.module.ecm.service.ResourceService;
import com.esofthead.mycollab.module.file.service.EntityUploaderService;
import com.esofthead.mycollab.module.file.service.UserAvatarService;
import com.esofthead.mycollab.module.user.dao.UserMapper;
import com.esofthead.mycollab.module.user.domain.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

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
    private EntityUploaderService entityUploaderService;

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
        String newAvatarId = entityUploaderService.upload(image, "avatar", avatarId, username, null, SUPPORT_SIZES);

        // save avatar id
        User user = new User();
        user.setUsername(username);
        user.setAvatarid(newAvatarId);
        userMapper.updateByPrimaryKeySelective(user);
        EventBusFactory.getInstance().post(new SessionEvent.UserProfileChangeEvent(UserAvatarServiceImpl.this, "avatarid", newAvatarId));
        return newAvatarId;
    }
}

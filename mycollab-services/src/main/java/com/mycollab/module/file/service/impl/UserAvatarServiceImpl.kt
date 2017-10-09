package com.mycollab.module.file.service.impl

import com.mycollab.core.MyCollabException
import com.mycollab.core.utils.StringUtils
import com.mycollab.module.ecm.service.ResourceService
import com.mycollab.module.file.service.EntityUploaderService
import com.mycollab.module.file.service.UserAvatarService
import com.mycollab.module.user.dao.UserMapper
import com.mycollab.module.user.domain.User
import org.springframework.stereotype.Service

import javax.imageio.ImageIO
import java.awt.image.BufferedImage
import java.io.IOException

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
@Service(value = "userAvatarService")
class UserAvatarServiceImpl(private val resourceService: ResourceService,
                            private val entityUploaderService: EntityUploaderService,
                            private val userMapper: UserMapper) : UserAvatarService {

    override fun uploadDefaultAvatar(username: String): String {
        // Save default user avatar
        val imageResourceStream = this.javaClass.classLoader.getResourceAsStream("assets/icons/default_user_avatar_100.png")
        val imageBuff: BufferedImage
        try {
            imageBuff = ImageIO.read(imageResourceStream)
            return uploadAvatar(imageBuff, username, null)
        } catch (e: IOException) {
            throw MyCollabException("Error while set default avatar to user", e)
        }

    }

    override fun uploadAvatar(image: BufferedImage, username: String, avatarId: String?): String {
        val newAvatarId = entityUploaderService.upload(image, "avatar", avatarId, username, null, SUPPORT_SIZES)

        // save avatar id
        val user = User()
        user.username = username
        user.avatarid = newAvatarId
        userMapper.updateByPrimaryKeySelective(user)
        return newAvatarId
    }

    override fun removeAvatar(avatarId: String?) {
        if (StringUtils.isNotBlank(avatarId)) {
            SUPPORT_SIZES.forEach { resourceService.removeResource(String.format("%s/%s_%d.png", "avatar", avatarId, it)) }
        }
    }

    companion object {

        private val SUPPORT_SIZES = arrayOf(100, 64, 48, 32, 24, 16)
    }
}

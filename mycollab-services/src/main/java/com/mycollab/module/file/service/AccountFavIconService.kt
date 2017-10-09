package com.mycollab.module.file.service

import com.mycollab.db.persistence.service.IService

import java.awt.image.BufferedImage

/**
 * @author MyCollab Ltd
 * @since 5.0.10
 */
interface AccountFavIconService : IService {
    fun upload(uploadedUser: String, logo: BufferedImage, sAccountId: Int): String
}

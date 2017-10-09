package com.mycollab.html

import com.hp.gagawa.java.elements.Img
import com.mycollab.module.file.service.AbstractStorageService
import com.mycollab.spring.AppContextUtil

/**
 * @author MyCollab Ltd
 * @since 6.0.0
 */
object LinkUtils {
    @JvmStatic
    fun storageService() = AppContextUtil.getSpringBean(AbstractStorageService::class.java)

    @JvmStatic
    fun newAvatar(avatarId: String) = Img("", storageService().getAvatarPath(avatarId, 16)).setWidth("16").
            setHeight("16").setStyle("display: inline-block; vertical-align: top;").setCSSClass("circle-box")

    @JvmStatic
    fun accountLogoPath(accountId: Int, logoId: String?) = if (logoId == null) storageService().generateAssetRelativeLink("icons/logo.png") else
        storageService().getLogoPath(accountId, logoId, 150)
}
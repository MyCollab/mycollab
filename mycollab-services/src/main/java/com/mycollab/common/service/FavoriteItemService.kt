package com.mycollab.common.service

import com.mycollab.common.domain.FavoriteItem
import com.mycollab.db.persistence.service.ICrudService

/**
 * @author MyCollab Ltd.
 * @since 5.0.1
 */
interface FavoriteItemService : ICrudService<Int, FavoriteItem> {
    fun saveOrDelete(item: FavoriteItem)

    fun isUserFavorite(username: String, type: String, typeId: String): Boolean
}

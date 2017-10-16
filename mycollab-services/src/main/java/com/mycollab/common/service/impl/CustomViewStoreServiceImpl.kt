/**
 * Copyright © MyCollab
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http:></http:>//www.gnu.org/licenses/>.
 */
package com.mycollab.common.service.impl

import com.mycollab.common.dao.CustomViewStoreMapper
import com.mycollab.common.domain.CustomViewStore
import com.mycollab.common.domain.CustomViewStoreExample
import com.mycollab.common.domain.NullCustomViewStore
import com.mycollab.common.service.CustomViewStoreService
import com.mycollab.db.persistence.ICrudGenericDAO
import com.mycollab.db.persistence.service.DefaultCrudService
import org.apache.commons.collections.CollectionUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

import java.util.GregorianCalendar

/**
 * @author MyCollab Ltd.
 * @since 2.0
 */
@Service
class CustomViewStoreServiceImpl(private val customViewStoreMapper: CustomViewStoreMapper) : DefaultCrudService<Int, CustomViewStore>(), CustomViewStoreService {

    override val crudMapper: ICrudGenericDAO<Int, CustomViewStore>
        get() = customViewStoreMapper as ICrudGenericDAO<Int, CustomViewStore>

    override fun getViewLayoutDef(accountId: Int?, username: String, viewId: String): CustomViewStore {
        val ex = CustomViewStoreExample()
        ex.createCriteria().andCreateduserEqualTo(username).andViewidEqualTo(viewId).andSaccountidEqualTo(accountId)
        val views = customViewStoreMapper.selectByExampleWithBLOBs(ex)
        return if (views.isNotEmpty()) {
            views[0]
        } else NullCustomViewStore()
    }

    override fun saveOrUpdateViewLayoutDef(viewStore: CustomViewStore) {
        val viewLayoutDef = getViewLayoutDef(viewStore.saccountid, viewStore.createduser,
                viewStore.viewid)
        viewStore.createdtime = GregorianCalendar().time
        when (viewLayoutDef) {
            !is NullCustomViewStore -> {
                viewStore.id = viewLayoutDef.id
                updateWithSession(viewStore, viewStore.createduser)
            }
            else -> saveWithSession(viewStore, viewStore.createduser)
        }
    }
}

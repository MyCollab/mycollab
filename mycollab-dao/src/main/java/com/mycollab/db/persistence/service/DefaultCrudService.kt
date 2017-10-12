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
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.mycollab.db.persistence.service

import com.mycollab.core.MyCollabException
import com.mycollab.core.cache.CacheKey
import com.mycollab.core.utils.StringUtils
import com.mycollab.db.persistence.ICrudGenericDAO
import org.apache.commons.beanutils.PropertyUtils
import java.io.Serializable
import java.lang.reflect.Method
import java.util.*

/**
 * The generic class that serves the basic operations in data access layer:
 * Create, Retrieve, Update and Delete.
 *
 * @param <T>
 * @param <K>
 * @author MyCollab Ltd.
 * @since 1.0
</K></T> */
abstract class DefaultCrudService<K : Serializable, T> : ICrudService<K, T> {

    abstract val crudMapper: ICrudGenericDAO<K, T>

    private var cacheUpdateMethod: Method? = null

    override fun findByPrimaryKey(primaryKey: K, accountId: Int): T {
        return crudMapper.selectByPrimaryKey(primaryKey)
    }

    override fun saveWithSession(record: T, username: String?): Int {
        if (!StringUtils.isBlank(username)) {
            try {
                PropertyUtils.setProperty(record, "createduser", username)
            } catch (e: Exception) {
            }

        }

        try {
            PropertyUtils.setProperty(record, "createdtime", GregorianCalendar().time)
            PropertyUtils.setProperty(record, "lastupdatedtime", GregorianCalendar().time)
        } catch (e: Exception) {
        }

        crudMapper.insertAndReturnKey(record)
        return try {
            PropertyUtils.getProperty(record, "id") as Int
        } catch (e: Exception) {
            0
        }

    }

    override fun updateWithSession(record: T, username: String?): Int {
        try {
            PropertyUtils.setProperty(record, "lastupdatedtime", GregorianCalendar().time)
        } catch (e: Exception) {
        }

        if (cacheUpdateMethod == null) {
            findCacheUpdateMethod()
        }
        try {
            cacheUpdateMethod!!.invoke(crudMapper, record)
            return 1
        } catch (e: Exception) {
            throw MyCollabException(e)
        }
    }

    private fun findCacheUpdateMethod() {
        val crudMapper = crudMapper
        val crudMapperCls = crudMapper.javaClass
        for (method in crudMapperCls.methods) {
            if ("updateByPrimaryKeyWithBLOBs" == method.name) {
                cacheUpdateMethod = method
                return
            } else if ("updateByPrimaryKey" == method.name) {
                cacheUpdateMethod = method
            }
        }
    }

    override fun updateSelectiveWithSession(@CacheKey record: T, username: String?): Int? {
        try {
            PropertyUtils.setProperty(record, "lastupdatedtime", GregorianCalendar().time)
        } catch (e: Exception) {
        }

        return crudMapper.updateByPrimaryKeySelective(record)
    }


    override fun removeWithSession(item: T, username: String?, accountId: Int) {
        massRemoveWithSession(listOf(item), username, accountId)
    }

    override fun massRemoveWithSession(items: List<T>, username: String?, accountId: Int) {
        val primaryKeys = ArrayList<T>(items.size)
        for (item in items) {
            try {
                val primaryKey = PropertyUtils.getProperty(item, "id") as T
                primaryKeys.add(primaryKey)
            } catch (e: Exception) {
                throw MyCollabException(e)
            }

        }
        crudMapper.removeKeysWithSession(primaryKeys)
    }

    override fun massUpdateWithSession(record: T, primaryKeys: List<K>, accountId: Int?) {
        crudMapper.massUpdateWithSession(record, primaryKeys)
    }
}

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
package com.mycollab.db.persistence

import org.apache.ibatis.annotations.Param

import java.io.Serializable

/**
 * @param <K>
 * @param <T>
 * @author MyCollab Ltd.
 * @since 1.0
</T></K> */
interface ICrudGenericDAO<K : Serializable, T> {

    /**
     * @param record
     */
    fun insert(record: T)

    /**
     * @param record
     * @return
     */
    fun updateByPrimaryKey(record: T): Int

    /**
     * @param record
     * @return
     */
    fun updateByPrimaryKeySelective(record: T): Int

    /**
     * @param record
     * @param primaryKeys
     */
    fun massUpdateWithSession(@Param("record") record: T, @Param("primaryKeys") primaryKeys: List<K>)

    /**
     * @param primaryKey
     * @return
     */
    fun selectByPrimaryKey(primaryKey: K): T?

    /**
     * @param primaryKey
     * @return
     */
    fun deleteByPrimaryKey(primaryKey: K): Int

    /**
     * @param value
     * @return
     */
    fun insertAndReturnKey(value: T): Int

    /**
     * @param keys
     */
    fun removeKeysWithSession(keys: List<*>)

}

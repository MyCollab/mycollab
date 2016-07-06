/**
 * This file is part of mycollab-dao.
 *
 * mycollab-dao is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * mycollab-dao is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with mycollab-dao.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.mycollab.db.persistence;

import org.apache.ibatis.annotations.Param;

import java.io.Serializable;
import java.util.List;

/**
 * @param <K>
 * @param <T>
 * @author MyCollab Ltd.
 * @since 1.0
 */
public interface ICrudGenericDAO<K extends Serializable, T> {

    /**
     * @param record
     */
    void insert(T record);

    /**
     * @param record
     * @return
     */
    int updateByPrimaryKey(T record);

    /**
     * @param record
     * @return
     */
    int updateByPrimaryKeySelective(T record);

    /**
     * @param record
     * @param primaryKeys
     */
    void massUpdateWithSession(@Param("record") T record, @Param("primaryKeys") List<K> primaryKeys);

    /**
     * @param primaryKey
     * @return
     */
    T selectByPrimaryKey(K primaryKey);

    /**
     * @param primaryKey
     * @return
     */
    int deleteByPrimaryKey(K primaryKey);

    /**
     * @param value
     * @return
     */
    int insertAndReturnKey(T value);

    /**
     * @param keys
     */
    void removeKeysWithSession(List keys);

}

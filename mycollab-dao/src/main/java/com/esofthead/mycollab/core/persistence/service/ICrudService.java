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
package com.esofthead.mycollab.core.persistence.service;

import com.esofthead.mycollab.core.cache.CacheEvict;
import com.esofthead.mycollab.core.cache.CacheKey;
import com.esofthead.mycollab.core.cache.Cacheable;

import java.io.Serializable;
import java.util.List;

/**
 * @param <K>
 * @param <T>
 * @author MyCollab Ltd.
 * @since 1.0
 */
public interface ICrudService<K extends Serializable, T> extends IService {

    /**
     * @param record
     * @param username
     * @return
     */
    @CacheEvict
    Integer saveWithSession(@CacheKey T record, String username);

    /**
     * @param record
     * @param username
     * @return
     */
    @CacheEvict
    Integer updateWithSession(@CacheKey T record, String username);

    /**
     * @param record
     * @param username
     * @return
     */
    @CacheEvict
    Integer updateSelectiveWithSession(@CacheKey T record, String username);

    /**
     * @param record
     * @param primaryKeys
     * @param accountId
     */
    @CacheEvict
    void massUpdateWithSession(T record, List<K> primaryKeys, @CacheKey Integer accountId);

    /**
     * @param primaryKey
     * @param sAccountId
     * @return
     */
    @Cacheable
    T findByPrimaryKey(K primaryKey, @CacheKey Integer sAccountId);

    /**
     *
     * @param item
     * @param username
     * @param sAccountId
     */
    @CacheEvict
    void removeWithSession(T item, String username, @CacheKey Integer sAccountId);

    /**
     *
     * @param items
     * @param username
     * @param sAccountId
     */
    @CacheEvict
    void massRemoveWithSession(List<T> items, String username, @CacheKey Integer sAccountId);
}

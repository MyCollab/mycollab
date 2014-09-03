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

import java.io.Serializable;
import java.util.List;

import com.esofthead.mycollab.core.cache.CacheEvict;
import com.esofthead.mycollab.core.cache.CacheKey;
import com.esofthead.mycollab.core.cache.Cacheable;

/**
 * 
 * @author MyCollab Ltd.
 * @since 1.0
 * 
 * @param <K>
 * @param <T>
 */
public interface ICrudService<K extends Serializable, T> extends IService {

	/**
	 * 
	 * @param record
	 * @param username
	 * @return
	 */
	@CacheEvict
	int saveWithSession(@CacheKey T record, String username);

	/**
	 * 
	 * @param record
	 * @param username
	 * @return
	 */
	@CacheEvict
	int updateWithSession(@CacheKey T record, String username);

	/**
	 * 
	 * @param record
	 * @param username
	 * @return
	 */
	@CacheEvict
	int updateSelectiveWithSession(@CacheKey T record, String username);

	/**
	 * 
	 * @param record
	 * @param primaryKeys
	 * @param accountId
	 */
	@CacheEvict
	void massUpdateWithSession(T record, List<K> primaryKeys,
			@CacheKey int accountId);

	/**
	 * 
	 * @param primaryKey
	 * @param sAccountId
	 * @return
	 */
	@Cacheable
	T findByPrimaryKey(K primaryKey, @CacheKey int sAccountId);

	/**
	 * 
	 * @param primaryKey
	 * @param username
	 * @param sAccountId
	 * @return
	 */
	@CacheEvict
	int removeWithSession(K primaryKey, String username,
			@CacheKey int sAccountId);

	/**
	 * 
	 * @param primaryKeys
	 * @param username
	 * @param sAccountId
	 */
	@CacheEvict
	void massRemoveWithSession(List<K> primaryKeys, String username,
			@CacheKey int sAccountId);
}

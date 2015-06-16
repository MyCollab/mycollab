/**
 * This file is part of mycollab-services.
 *
 * mycollab-services is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * mycollab-services is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with mycollab-services.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.esofthead.mycollab.common.service;

import com.esofthead.mycollab.common.domain.CustomViewStore;
import com.esofthead.mycollab.core.cache.CacheEvict;
import com.esofthead.mycollab.core.cache.CacheKey;
import com.esofthead.mycollab.core.cache.Cacheable;
import com.esofthead.mycollab.core.persistence.service.ICrudService;

/**
 * 
 * @author MyCollab Ltd.
 * @since 2.0
 * 
 */
public interface CustomViewStoreService extends
		ICrudService<Integer, CustomViewStore> {
	@Cacheable
	CustomViewStore getViewLayoutDef(@CacheKey Integer accountId, String username,
			String viewId);

	@CacheEvict
	void saveOrUpdateViewLayoutDef(@CacheKey CustomViewStore viewStore);
}

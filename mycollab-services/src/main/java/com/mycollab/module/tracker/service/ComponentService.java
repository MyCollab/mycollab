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
package com.mycollab.module.tracker.service;

import com.mycollab.core.cache.CacheKey;
import com.mycollab.core.cache.Cacheable;
import com.mycollab.db.persistence.service.IDefaultService;
import com.mycollab.module.tracker.domain.Component;
import com.mycollab.module.tracker.domain.SimpleComponent;
import com.mycollab.module.tracker.domain.criteria.ComponentSearchCriteria;

/**
 * @author MyCollab Ltd
 * @since 1.0.0
 */
public interface ComponentService extends IDefaultService<Integer, Component, ComponentSearchCriteria> {

    @Cacheable
    SimpleComponent findById(Integer componentId, @CacheKey Integer sAccountId);
}

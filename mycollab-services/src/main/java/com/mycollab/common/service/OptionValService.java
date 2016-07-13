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
package com.mycollab.common.service;

import com.mycollab.common.domain.OptionVal;
import com.mycollab.core.cache.CacheEvict;
import com.mycollab.core.cache.CacheKey;
import com.mycollab.core.cache.Cacheable;
import com.mycollab.db.persistence.service.ICrudService;

import java.util.List;
import java.util.Map;

/**
 * @author MyCollab Ltd
 * @since 5.1.1
 */
public interface OptionValService extends ICrudService<Integer, OptionVal> {

    boolean isExistedOptionVal(String type, String typeVal, String fieldGroup, Integer projectId, Integer sAccountId);

    void createDefaultOptions(Integer sAccountId);

    @Cacheable
    List<OptionVal> findOptionVals(String type, Integer projectId, @CacheKey Integer sAccountId);

    @Cacheable
    List<OptionVal> findOptionValsExcludeClosed(String type, Integer projectId, @CacheKey Integer sAccountId);

    @CacheEvict
    void massUpdateOptionIndexes(List<Map<String, Integer>> mapIndexes, @CacheKey Integer sAccountId);
}

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

import com.mycollab.common.domain.AggregateTag;
import com.mycollab.common.domain.Tag;
import com.mycollab.common.domain.TagExample;
import com.mycollab.core.cache.CacheKey;
import com.mycollab.core.cache.Cacheable;
import com.mycollab.db.persistence.service.ICrudService;

import java.util.List;

/**
 * @author MyCollab Ltd.
 * @since 5.0.1
 */
public interface TagService extends ICrudService<Integer, Tag> {
    @Cacheable
    List<Tag> findTags(String type, String typeId, @CacheKey Integer accountId);

    @Cacheable
    List<Tag> findTagsInAccount(String name, String[] types, @CacheKey Integer accountId);

    @Cacheable
    List<AggregateTag> findTagsInProject(Integer projectId, @CacheKey Integer accountId);

    Integer deleteByExample(TagExample example);
}

/**
 * This file is part of mycollab-services-community.
 *
 * mycollab-services-community is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * mycollab-services-community is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with mycollab-services-community.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.esofthead.mycollab.community.module.common.service.ibatis;

import com.esofthead.mycollab.common.domain.AggregateTag;
import com.esofthead.mycollab.common.domain.Tag;
import com.esofthead.mycollab.common.domain.TagExample;
import com.esofthead.mycollab.common.service.TagService;
import com.esofthead.mycollab.core.cache.CacheKey;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author MyCollab Ltd
 * @since 5.3.4
 */
@Service
public class TagServiceImpl implements TagService {
    @Override
    public List<Tag> findTags(String type, String typeId, @CacheKey Integer accountId) {
        return null;
    }

    @Override
    public List<Tag> findTagsInAccount(String name, String[] types, @CacheKey Integer accountId) {
        return null;
    }

    @Override
    public List<AggregateTag> findTagsInProject(Integer projectId, @CacheKey Integer accountId) {
        return null;
    }

    @Override
    public int deleteByExample(TagExample example) {
        return 0;
    }

    @Override
    public Integer saveWithSession(@CacheKey Tag record, String username) {
        return null;
    }

    @Override
    public Integer updateWithSession(@CacheKey Tag record, String username) {
        return null;
    }

    @Override
    public Integer updateSelectiveWithSession(@CacheKey Tag record, String username) {
        return null;
    }

    @Override
    public void massUpdateWithSession(Tag record, List<Integer> primaryKeys, @CacheKey Integer accountId) {

    }

    @Override
    public Tag findByPrimaryKey(Integer primaryKey, @CacheKey Integer sAccountId) {
        return null;
    }

    @Override
    public void removeWithSession(Tag item, String username, @CacheKey Integer sAccountId) {

    }

    @Override
    public void massRemoveWithSession(List<Tag> items, String username, @CacheKey Integer sAccountId) {

    }
}

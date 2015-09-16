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
package com.esofthead.mycollab.common.service.ibatis;

import com.esofthead.mycollab.cache.CacheUtils;
import com.esofthead.mycollab.common.dao.TagMapper;
import com.esofthead.mycollab.common.domain.Tag;
import com.esofthead.mycollab.common.domain.TagExample;
import com.esofthead.mycollab.common.service.TagService;
import com.esofthead.mycollab.core.cache.CacheKey;
import com.esofthead.mycollab.core.persistence.ICrudGenericDAO;
import com.esofthead.mycollab.core.persistence.service.DefaultCrudService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

/**
 * @author MyCollab Ltd.
 * @since 5.0.1
 */
@Service
public class TagServiceImpl extends DefaultCrudService<Integer, Tag> implements TagService {
    @Autowired
    private TagMapper tagMapper;

    @Override
    public ICrudGenericDAO<Integer, Tag> getCrudMapper() {
        return tagMapper;
    }

    @Override
    public Integer saveWithSession(Tag record, String username) {
        TagExample ex = new TagExample();
        ex.createCriteria().andTypeEqualTo(record.getType()).andTypeidEqualTo(record.getTypeid()).andNameEqualTo
                (record.getName());
        int count = tagMapper.countByExample(ex);
        if (count > 0) {
            return 0;
        }
        tagMapper.insertAndReturnKey(record);
        return record.getId();
    }

    @Override
    public List<Tag> findTags(String type, String typeId, Integer accountId) {
        TagExample ex = new TagExample();
        ex.createCriteria().andTypeEqualTo(type).andTypeidEqualTo(typeId);
        return tagMapper.selectByExample(ex);
    }

    @Override
    public List<Tag> findTagsInAccount(String name, String[] types, @CacheKey Integer accountId) {
        TagExample ex = new TagExample();
        ex.createCriteria().andNameLike(name).andTypeIn(Arrays.asList(types)).andSaccountidEqualTo(accountId);
        return tagMapper.selectByExample(ex);
    }

    @Override
    public List<Tag> findTagsInProject(Integer projectId, @CacheKey Integer accountId) {
        TagExample ex = new TagExample();
        ex.createCriteria().andExtratypeidEqualTo(projectId).andSaccountidEqualTo(accountId);
        return tagMapper.selectByExample(ex);
    }
}

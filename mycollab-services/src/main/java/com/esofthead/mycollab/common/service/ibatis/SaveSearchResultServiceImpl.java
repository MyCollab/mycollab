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

import com.esofthead.mycollab.common.dao.SaveSearchResultMapper;
import com.esofthead.mycollab.common.dao.SaveSearchResultMapperExt;
import com.esofthead.mycollab.common.domain.SaveSearchResult;
import com.esofthead.mycollab.common.domain.SaveSearchResultExample;
import com.esofthead.mycollab.common.domain.criteria.SaveSearchResultCriteria;
import com.esofthead.mycollab.common.service.SaveSearchResultService;
import com.esofthead.mycollab.core.UserInvalidInputException;
import com.esofthead.mycollab.core.persistence.ICrudGenericDAO;
import com.esofthead.mycollab.core.persistence.ISearchableDAO;
import com.esofthead.mycollab.core.persistence.service.DefaultService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
@Service
public class SaveSearchResultServiceImpl extends DefaultService<Integer, SaveSearchResult, SaveSearchResultCriteria>
        implements SaveSearchResultService {

    @Autowired
    private SaveSearchResultMapper saveSearchResultMapper;

    @Autowired
    private SaveSearchResultMapperExt saveSearchResultMapperExt;

    @Override
    public ICrudGenericDAO<Integer, SaveSearchResult> getCrudMapper() {
        return saveSearchResultMapper;
    }

    @Override
    public ISearchableDAO<SaveSearchResultCriteria> getSearchMapper() {
        return saveSearchResultMapperExt;
    }

    @Override
    public Integer saveWithSession(SaveSearchResult record, String username) {
        checkDuplicateEntryName(record);
        return super.saveWithSession(record, username);
    }

    @Override
    public Integer updateWithSession(SaveSearchResult record, String username) {
        return super.updateWithSession(record, username);
    }

    private void checkDuplicateEntryName(SaveSearchResult record) {
        SaveSearchResultExample ex = new SaveSearchResultExample();
        ex.createCriteria().andSaccountidEqualTo(record.getSaccountid()).andTypeEqualTo(record.getType())
                .andQuerynameEqualTo(record.getQueryname());
        if (saveSearchResultMapper.countByExample(ex) > 0) {
            throw new UserInvalidInputException("There is the query name existed");
        }
    }
}

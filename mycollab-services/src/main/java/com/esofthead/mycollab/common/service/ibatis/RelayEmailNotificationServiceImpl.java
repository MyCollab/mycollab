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

import com.esofthead.mycollab.common.dao.RelayEmailNotificationMapper;
import com.esofthead.mycollab.common.dao.RelayEmailNotificationMapperExt;
import com.esofthead.mycollab.common.domain.RelayEmailNotificationWithBLOBs;
import com.esofthead.mycollab.common.domain.criteria.RelayEmailNotificationSearchCriteria;
import com.esofthead.mycollab.common.service.RelayEmailNotificationService;
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
public class RelayEmailNotificationServiceImpl extends
        DefaultService<Integer, RelayEmailNotificationWithBLOBs, RelayEmailNotificationSearchCriteria>
        implements RelayEmailNotificationService {
    @Autowired
    private RelayEmailNotificationMapper relayEmailNotificationMapper;

    @Autowired
    private RelayEmailNotificationMapperExt relayEmailNotificationMapperExt;

    @Override
    public ICrudGenericDAO getCrudMapper() {
        return relayEmailNotificationMapper;
    }

    @Override
    public ISearchableDAO<RelayEmailNotificationSearchCriteria> getSearchMapper() {
        return relayEmailNotificationMapperExt;
    }
}

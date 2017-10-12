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
package com.mycollab.common.service.impl

import com.mycollab.common.dao.RelayEmailNotificationMapper
import com.mycollab.common.dao.RelayEmailNotificationMapperExt
import com.mycollab.common.domain.RelayEmailNotificationWithBLOBs
import com.mycollab.common.domain.criteria.RelayEmailNotificationSearchCriteria
import com.mycollab.common.service.RelayEmailNotificationService
import com.mycollab.db.persistence.ICrudGenericDAO
import com.mycollab.db.persistence.ISearchableDAO
import com.mycollab.db.persistence.service.DefaultService
import org.springframework.stereotype.Service

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
@Service
class RelayEmailNotificationServiceImpl(private val relayEmailNotificationMapper: RelayEmailNotificationMapper,
                                        private val relayEmailNotificationMapperExt: RelayEmailNotificationMapperExt) : DefaultService<Int, RelayEmailNotificationWithBLOBs, RelayEmailNotificationSearchCriteria>(), RelayEmailNotificationService {

    override val crudMapper: ICrudGenericDAO<Int, RelayEmailNotificationWithBLOBs>
        get() = relayEmailNotificationMapper as ICrudGenericDAO<Int, RelayEmailNotificationWithBLOBs>

    override val searchMapper: ISearchableDAO<RelayEmailNotificationSearchCriteria>
        get() = relayEmailNotificationMapperExt
}

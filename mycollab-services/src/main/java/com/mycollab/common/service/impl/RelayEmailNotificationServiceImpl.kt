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

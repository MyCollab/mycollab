package com.mycollab.common.service.impl;

import com.mycollab.common.dao.RelayEmailNotificationMapper;
import com.mycollab.common.dao.RelayEmailNotificationMapperExt;
import com.mycollab.common.domain.RelayEmailNotificationWithBLOBs;
import com.mycollab.common.domain.criteria.RelayEmailNotificationSearchCriteria;
import com.mycollab.common.service.RelayEmailNotificationService;
import com.mycollab.db.persistence.ICrudGenericDAO;
import com.mycollab.db.persistence.ISearchableDAO;
import com.mycollab.db.persistence.service.DefaultService;
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

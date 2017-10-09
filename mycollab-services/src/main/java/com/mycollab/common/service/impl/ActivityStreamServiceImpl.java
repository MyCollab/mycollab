package com.mycollab.common.service.impl;

import com.mycollab.common.dao.ActivityStreamMapper;
import com.mycollab.common.dao.ActivityStreamMapperExt;
import com.mycollab.common.domain.ActivityStreamWithBLOBs;
import com.mycollab.common.domain.criteria.ActivityStreamSearchCriteria;
import com.mycollab.common.service.ActivityStreamService;
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
public class ActivityStreamServiceImpl extends DefaultService<Integer, ActivityStreamWithBLOBs, ActivityStreamSearchCriteria>
        implements ActivityStreamService {

    @Autowired
    private ActivityStreamMapper activityStreamMapper;

    @Autowired
    private ActivityStreamMapperExt activityStreamMapperExt;

    @Override
    public ICrudGenericDAO<Integer, ActivityStreamWithBLOBs> getCrudMapper() {
        return activityStreamMapper;
    }

    @Override
    public ISearchableDAO<ActivityStreamSearchCriteria> getSearchMapper() {
        return activityStreamMapperExt;
    }

    @Override
    public Integer save(ActivityStreamWithBLOBs activityStream) {
        activityStreamMapper.insertAndReturnKey(activityStream);
        return activityStream.getId();
    }
}
package com.mycollab.module.tracker.dao;

import com.mycollab.db.persistence.ISearchableDAO;
import com.mycollab.module.tracker.domain.SimpleComponent;
import com.mycollab.module.tracker.domain.criteria.ComponentSearchCriteria;
import org.apache.ibatis.annotations.Param;

/**
 * @author MyCollab Ltd.
 * @since 1.0.0
 */
public interface ComponentMapperExt extends ISearchableDAO<ComponentSearchCriteria> {

    SimpleComponent findComponentById(int componentId);

    Double getTotalBillableHours(@Param("componentid") int componentId);

    Double getTotalNonBillableHours(@Param("componentid") int componentId);

    Double getRemainHours(@Param("componentid") int componentId);
}

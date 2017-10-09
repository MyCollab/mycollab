package com.mycollab.module.tracker.dao;

import com.mycollab.db.persistence.ISearchableDAO;
import com.mycollab.module.tracker.domain.SimpleVersion;
import com.mycollab.module.tracker.domain.criteria.VersionSearchCriteria;
import org.apache.ibatis.annotations.Param;

/**
 * @author MyCollab Ltd.
 * @since 1.0.0
 */
public interface VersionMapperExt extends ISearchableDAO<VersionSearchCriteria> {

    SimpleVersion findVersionById(int versionId);

    Double getTotalBillableHours(@Param("versionid") int versionId);

    Double getTotalNonBillableHours(@Param("versionid") int versionId);

    Double getRemainHours(@Param("versionid") int versionId);
}

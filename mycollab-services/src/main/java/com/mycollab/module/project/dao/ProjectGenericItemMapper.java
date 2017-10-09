package com.mycollab.module.project.dao;

import com.mycollab.db.persistence.ISearchableDAO;
import com.mycollab.module.project.domain.criteria.ProjectGenericItemSearchCriteria;
import org.apache.ibatis.annotations.Param;

/**
 * @author MyCollab Ltd.
 * @since 5.0.3
 */
public interface ProjectGenericItemMapper extends ISearchableDAO<ProjectGenericItemSearchCriteria> {

    int getTotalCountFromRisk(@Param("searchCriteria") ProjectGenericItemSearchCriteria criteria);

    int getTotalCountFromBug(@Param("searchCriteria") ProjectGenericItemSearchCriteria criteria);

    int getTotalCountFromVersion(@Param("searchCriteria") ProjectGenericItemSearchCriteria criteria);

    int getTotalCountFromComponent(@Param("searchCriteria") ProjectGenericItemSearchCriteria criteria);

    int getTotalCountFromTask(@Param("searchCriteria") ProjectGenericItemSearchCriteria criteria);

    int getTotalCountFromMessage(@Param("searchCriteria") ProjectGenericItemSearchCriteria criteria);

    int getTotalCountFromMilestone(@Param("searchCriteria") ProjectGenericItemSearchCriteria criteria);
}
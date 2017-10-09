package com.mycollab.common.dao;

import com.mycollab.common.domain.MonitorItem;
import com.mycollab.common.domain.criteria.MonitorSearchCriteria;
import com.mycollab.db.persistence.ISearchableDAO;
import com.mycollab.module.user.domain.SimpleUser;
import org.apache.ibatis.annotations.Param;

import java.util.Collection;
import java.util.List;

/**
 * @author MyCollab Ltd
 * @since 1.0.0
 */
public interface MonitorItemMapperExt extends ISearchableDAO<MonitorSearchCriteria> {

    void saveMonitorItems(@Param("monitors") Collection<MonitorItem> monitorItems);

    List<SimpleUser> getWatchers(@Param("type") String type, @Param("typeId") Integer typeId);
}

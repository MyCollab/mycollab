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
package com.mycollab.module.project.dao;

import com.mycollab.common.domain.GroupItem;
import com.mycollab.db.persistence.ISearchableDAO;
import com.mycollab.module.project.domain.SimpleTask;
import com.mycollab.module.project.domain.criteria.TaskSearchCriteria;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
public interface TaskMapperExt extends ISearchableDAO<TaskSearchCriteria> {

    SimpleTask findTaskById(int taskId);

    Integer getMaxKey(int projectId);

    List<GroupItem> getPrioritySummary(@Param("searchCriteria") TaskSearchCriteria criteria);

    List<GroupItem> getStatusSummary(@Param("searchCriteria") TaskSearchCriteria criteria);

    List<GroupItem> getAssignedDefectsSummary(@Param("searchCriteria") TaskSearchCriteria criteria);

    SimpleTask findByProjectAndTaskKey(@Param("taskkey") int taskkey, @Param("prjShortName") String projectShortName,
                                       @Param("sAccountId") int sAccountId);

}

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
package com.mycollab.module.tracker.dao;

import com.mycollab.common.domain.GroupItem;
import com.mycollab.db.persistence.ISearchableDAO;
import com.mycollab.module.tracker.domain.BugStatusGroupItem;
import com.mycollab.module.tracker.domain.SimpleBug;
import com.mycollab.module.tracker.domain.criteria.BugSearchCriteria;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
public interface BugMapperExt extends ISearchableDAO<BugSearchCriteria> {

    SimpleBug getBugById(int bugid);

    SimpleBug findByProjectAndBugKey(@Param("bugkey") int bugKey, @Param("prjShortName") String projectShortName,
                                     @Param("sAccountId") int sAccountId);

    List<GroupItem> getStatusSummary(@Param("searchCriteria") BugSearchCriteria criteria);

    List<GroupItem> getPrioritySummary(@Param("searchCriteria") BugSearchCriteria criteria);

    List<GroupItem> getAssignedDefectsSummary(@Param("searchCriteria") BugSearchCriteria criteria);

    List<GroupItem> getResolutionDefectsSummary(@Param("searchCriteria") BugSearchCriteria criteria);

    List<GroupItem> getReporterDefectsSummary(@Param("searchCriteria") BugSearchCriteria criteria);

    List<GroupItem> getVersionDefectsSummary(@Param("searchCriteria") BugSearchCriteria criteria);

    List<GroupItem> getComponentDefectsSummary(@Param("searchCriteria") BugSearchCriteria criteria);

    List<BugStatusGroupItem> getBugStatusGroupItemBaseComponent(@Param("searchCriteria") BugSearchCriteria criteria);

    Integer getMaxKey(int projectId);
}

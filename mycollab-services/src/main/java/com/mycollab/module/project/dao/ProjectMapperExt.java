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

import com.mycollab.common.domain.criteria.ActivityStreamSearchCriteria;
import com.mycollab.common.domain.criteria.MonitorSearchCriteria;
import com.mycollab.db.persistence.ISearchableDAO;
import com.mycollab.module.project.domain.FollowingTicket;
import com.mycollab.module.project.domain.ProjectActivityStream;
import com.mycollab.module.project.domain.ProjectRelayEmailNotification;
import com.mycollab.module.project.domain.SimpleProject;
import com.mycollab.module.project.domain.criteria.ProjectSearchCriteria;
import com.mycollab.module.user.domain.BillingAccount;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

import java.util.List;

/**
 * @author MyCollab Ltd
 * @since 1.0.0
 */
public interface ProjectMapperExt extends ISearchableDAO<ProjectSearchCriteria> {

    int getTotalActivityStream(@Param("searchCriteria") ActivityStreamSearchCriteria criteria);

    List<ProjectActivityStream> getProjectActivityStreams(@Param("searchCriteria") ActivityStreamSearchCriteria criteria, RowBounds rowBounds);

    List<Integer> getUserProjectKeys(@Param("searchCriteria") ProjectSearchCriteria criteria);

    List<SimpleProject> getProjectsUserInvolved(@Param("username") String username, @Param("sAccountId") Integer sAccountId);

    SimpleProject findProjectById(int projectId);

    BillingAccount getAccountInfoOfProject(int projectId);

    int getTotalFollowingTickets(@Param("searchCriteria") MonitorSearchCriteria searchRequest);

    List<FollowingTicket> getProjectFollowingTickets(@Param("searchCriteria") MonitorSearchCriteria searchRequest, RowBounds rowBounds);

    List<ProjectRelayEmailNotification> findProjectRelayEmailNotifications();
}

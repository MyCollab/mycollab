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
package com.esofthead.mycollab.module.project.dao;

import com.esofthead.mycollab.core.persistence.ISearchableDAO;
import com.esofthead.mycollab.module.project.domain.SimpleStandupReport;
import com.esofthead.mycollab.module.project.domain.StandupReportStatistic;
import com.esofthead.mycollab.module.project.domain.criteria.StandupReportSearchCriteria;
import com.esofthead.mycollab.module.user.domain.SimpleUser;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

import java.util.Date;
import java.util.List;

/**
 * @author MyCollab Ltd
 * @since 1.0.0
 */
public interface StandupReportMapperExt extends ISearchableDAO<StandupReportSearchCriteria> {

    SimpleStandupReport findReportById(Integer standupId);

    List<StandupReportStatistic> getProjectReportsStatistic(@Param("projectIds") List<Integer> projectIds, @Param
            ("onDate") Date onDate, RowBounds rowBounds);

    List<SimpleUser> findUsersNotDoReportYet(@Param("projectId") Integer projectId, @Param("onDate") Date onDate);
}

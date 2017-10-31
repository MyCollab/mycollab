/**
 * Copyright © MyCollab
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.mycollab.module.crm.dao

import com.mycollab.common.domain.GroupItem
import com.mycollab.db.persistence.IMassUpdateDAO
import com.mycollab.db.persistence.ISearchableDAO
import com.mycollab.module.crm.domain.Opportunity
import com.mycollab.module.crm.domain.SimpleOpportunity
import com.mycollab.module.crm.domain.criteria.OpportunitySearchCriteria
import org.apache.ibatis.annotations.Param

/**
 * @author MyCollab Ltd
 * @since 1.0.0
 */
interface OpportunityMapperExt : ISearchableDAO<OpportunitySearchCriteria>, IMassUpdateDAO<Opportunity, OpportunitySearchCriteria> {

    fun findById(opportunityId: Int): SimpleOpportunity?

    fun getSalesStageSummary(@Param("searchCriteria") criteria: OpportunitySearchCriteria): List<GroupItem>

    fun getLeadSourcesSummary(@Param("searchCriteria") criteria: OpportunitySearchCriteria): List<GroupItem>

    fun getPipeline(@Param("searchCriteria") criteria: OpportunitySearchCriteria): List<GroupItem>

    fun findOpportunityAssoWithConvertedLead(@Param("leadId") leadId: Int): SimpleOpportunity?
}

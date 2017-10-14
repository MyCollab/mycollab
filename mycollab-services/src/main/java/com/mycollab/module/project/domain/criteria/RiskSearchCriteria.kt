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
 * along with this program.  If not, see <http:></http:>//www.gnu.org/licenses/>.
 */
package com.mycollab.module.project.domain.criteria

import com.mycollab.common.i18n.GenericI18Enum
import com.mycollab.common.i18n.OptionI18nEnum.StatusI18nEnum
import com.mycollab.db.arguments.NumberSearchField
import com.mycollab.db.arguments.SearchCriteria
import com.mycollab.db.arguments.StringSearchField
import com.mycollab.db.query.*
import com.mycollab.module.project.ProjectTypeConstants
import com.mycollab.module.project.i18n.MilestoneI18nEnum
import com.mycollab.module.project.i18n.OptionI18nEnum.RiskConsequence
import com.mycollab.module.project.i18n.OptionI18nEnum.RiskProbability
import com.mycollab.module.project.i18n.RiskI18nEnum

import java.util.Arrays

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
class RiskSearchCriteria : SearchCriteria() {

    var name: StringSearchField? = null

    var raisedByUser: StringSearchField? = null

    var assignUser: StringSearchField? = null

    var projectId: NumberSearchField? = null

    var id: NumberSearchField? = null

    companion object {

        @JvmField
        val p_assignee = CacheParamMapper.register<PropertyListParam<*>>(ProjectTypeConstants.RISK, GenericI18Enum.FORM_ASSIGNEE,
                PropertyListParam<String>("assignuser", "m_prj_risk", "assignUser"))

        @JvmField
        val p_raisedUser = CacheParamMapper.register<PropertyListParam<*>>(ProjectTypeConstants.RISK, RiskI18nEnum.FORM_RAISED_BY,
                PropertyListParam<String>("createdUser", "m_prj_risk", "createdUser"))

        @JvmField
        val p_duedate = CacheParamMapper.register(ProjectTypeConstants.RISK, GenericI18Enum.FORM_DUE_DATE,
                DateParam("dueDate", "m_prj_risk", "dueDate"))

        @JvmField
        val p_raiseddate = CacheParamMapper.register(ProjectTypeConstants.RISK, GenericI18Enum.FORM_CREATED_TIME,
                DateParam("raiseddate", "m_prj_risk", "dateraised"))

        @JvmField
        val p_status = CacheParamMapper.register(ProjectTypeConstants.RISK, GenericI18Enum.FORM_STATUS,
                I18nStringListParam("status", "m_prj_risk", "status", Arrays.asList(
                        StatusI18nEnum.Open, StatusI18nEnum.Closed)))

        @JvmField
        val p_milestones = CacheParamMapper.register(ProjectTypeConstants.RISK,
                MilestoneI18nEnum.SINGLE, PropertyListParam<Int>("milestones", "m_prj_risk", "milestoneId"))

        @JvmField
        val p_probalitity: Param = CacheParamMapper.register(ProjectTypeConstants.RISK, RiskI18nEnum.FORM_PROBABILITY,
                I18nStringListParam("probalitity", "m_prj_risk", "probalitity",
                        Arrays.asList(RiskProbability.Certain, RiskProbability.Likely, RiskProbability.Possible,
                                RiskProbability.Unlikely, RiskProbability.Rare)))

        @JvmField
        val p_consequence = CacheParamMapper.register(ProjectTypeConstants.RISK, RiskI18nEnum.FORM_CONSEQUENCE,
                I18nStringListParam("consequence", "m_prj_risk", "consequence",
                        Arrays.asList(RiskConsequence.Catastrophic, RiskConsequence.Critical, RiskConsequence.Marginal,
                                RiskConsequence.Negligible)))

        @JvmField
        val p_createdtime = CacheParamMapper.register(ProjectTypeConstants.RISK, GenericI18Enum.FORM_CREATED_TIME,
                DateParam("createdtime", "m_prj_risk", "createdTime"))

        @JvmField
        val p_lastupdatedtime = CacheParamMapper.register(ProjectTypeConstants.RISK, GenericI18Enum.FORM_LAST_UPDATED_TIME,
                DateParam("lastupdatedtime", "m_prj_risk", "lastUpdatedTime"))
    }
}

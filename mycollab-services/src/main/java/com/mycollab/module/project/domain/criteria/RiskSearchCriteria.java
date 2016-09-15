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
package com.mycollab.module.project.domain.criteria;

import com.mycollab.common.i18n.GenericI18Enum;
import com.mycollab.common.i18n.OptionI18nEnum.StatusI18nEnum;
import com.mycollab.db.arguments.NumberSearchField;
import com.mycollab.db.arguments.SearchCriteria;
import com.mycollab.db.arguments.StringSearchField;
import com.mycollab.module.project.ProjectTypeConstants;
import com.mycollab.module.project.i18n.BugI18nEnum;
import com.mycollab.module.project.i18n.RiskI18nEnum;
import com.mycollab.db.query.*;

import java.util.Arrays;

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
public class RiskSearchCriteria extends SearchCriteria {
    private static final long serialVersionUID = 1L;

    public static final Param p_assignee = CacheParamMapper.register(ProjectTypeConstants.RISK, GenericI18Enum.FORM_ASSIGNEE,
            new PropertyListParam("assignuser", "m_prj_risk", "assigntouser"));

    public static final Param p_raisedUser = CacheParamMapper.register(ProjectTypeConstants.RISK, RiskI18nEnum.FORM_RAISED_BY,
            new PropertyListParam("raiseduser", "m_prj_risk", "raisedbyuser"));

    public static final Param p_duedate = CacheParamMapper.register(ProjectTypeConstants.RISK, GenericI18Enum.FORM_DUE_DATE,
            new DateParam("duedate", "m_prj_risk", "datedue"));

    public static final Param p_raiseddate = CacheParamMapper.register(ProjectTypeConstants.RISK, GenericI18Enum.FORM_CREATED_TIME,
            new DateParam("raiseddate", "m_prj_risk", "dateraised"));

    public static final Param p_status = CacheParamMapper.register(ProjectTypeConstants.RISK, GenericI18Enum.FORM_STATUS,
            new I18nStringListParam("status", "m_prj_risk", "status", Arrays.asList(
                    StatusI18nEnum.Open, StatusI18nEnum.Closed)));

    public static final PropertyListParam<Integer> p_milestones = CacheParamMapper.register(ProjectTypeConstants.RISK,
            RiskI18nEnum.FORM_PHASE, new PropertyListParam<Integer>("milestones", "m_prj_risk", "milestoneId"));

    public static final Param p_probalitity = CacheParamMapper.register(ProjectTypeConstants.RISK, RiskI18nEnum.FORM_PROBABILITY,
            new StringListParam("probalitity", "m_prj_risk", "probalitity",
                    Arrays.asList("Certain", "Likely", "Possible", "Unlikely", "Rare")));

    public static final Param p_consequence = CacheParamMapper.register(ProjectTypeConstants.RISK, RiskI18nEnum.FORM_CONSEQUENCE,
            new StringListParam("consequence", "m_prj_risk", "consequence",
            Arrays.asList("Catastrophic", "Critical", "Marginal", "Negligible")));

    public static final Param p_createdtime = CacheParamMapper.register(ProjectTypeConstants.RISK, GenericI18Enum.FORM_CREATED_TIME,
            new DateParam("createdtime", "m_prj_risk", "createdTime"));

    public static final Param p_lastupdatedtime = CacheParamMapper.register(ProjectTypeConstants.RISK, GenericI18Enum.FORM_LAST_UPDATED_TIME,
            new DateParam("lastupdatedtime", "m_prj_risk", "lastUpdatedTime"));

    private StringSearchField riskname;

    private StringSearchField raisedByUser;

    private StringSearchField assignToUser;

    private NumberSearchField projectId;

    private NumberSearchField id;

    public StringSearchField getRiskname() {
        return riskname;
    }

    public void setRiskname(StringSearchField riskname) {
        this.riskname = riskname;
    }

    public NumberSearchField getProjectId() {
        return projectId;
    }

    public void setProjectId(NumberSearchField projectId) {
        this.projectId = projectId;
    }

    public StringSearchField getRaisedByUser() {
        return raisedByUser;
    }

    public void setRaisedByUser(StringSearchField raisedByUser) {
        this.raisedByUser = raisedByUser;
    }

    public StringSearchField getAssignToUser() {
        return assignToUser;
    }

    public void setAssignToUser(StringSearchField assignToUser) {
        this.assignToUser = assignToUser;
    }

    public void setId(NumberSearchField id) {
        this.id = id;
    }

    public NumberSearchField getId() {
        return id;
    }
}

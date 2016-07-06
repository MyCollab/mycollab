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
import com.mycollab.common.i18n.OptionI18nEnum;
import com.mycollab.db.arguments.NumberSearchField;
import com.mycollab.db.arguments.SearchCriteria;
import com.mycollab.db.arguments.SetSearchField;
import com.mycollab.db.arguments.StringSearchField;
import com.mycollab.module.project.ProjectTypeConstants;
import com.mycollab.db.query.*;

import java.util.Arrays;

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
public class ProjectSearchCriteria extends SearchCriteria {
    private static final long serialVersionUID = 1L;

    public static final NumberParam p_template = CacheParamMapper.register(ProjectTypeConstants.PROJECT, GenericI18Enum.FORM_NAME,
            new NumberParam("template", "m_prj_project", "istemplate"));

    public static final StringParam p_name = CacheParamMapper.register(ProjectTypeConstants.PROJECT, GenericI18Enum.FORM_NAME,
            new StringParam("name", "m_prj_project", "name"));

    public static final DateParam p_startdate = CacheParamMapper.register(ProjectTypeConstants.PROJECT, GenericI18Enum.FORM_START_DATE,
            new DateParam("startdate", "m_prj_project", "planStartDate"));

    public static final DateParam p_enddate = CacheParamMapper.register(ProjectTypeConstants.PROJECT, GenericI18Enum.FORM_END_DATE,
            new DateParam("enddate", "m_prj_project", "planEndDate"));

    public static final DateParam p_createdtime = CacheParamMapper.register(ProjectTypeConstants.PROJECT, GenericI18Enum.FORM_CREATED_TIME,
            new DateParam("createdtime", "m_prj_project", "createdTime"));

    public static final StringListParam p_status = CacheParamMapper.register(ProjectTypeConstants.PROJECT, GenericI18Enum.FORM_STATUS,
            new StringListParam("status", "m_prj_project", "projectStatus", Arrays.asList(OptionI18nEnum.StatusI18nEnum.Open.name(),
                    OptionI18nEnum.StatusI18nEnum.Closed.name(), OptionI18nEnum.StatusI18nEnum.Archived.name())));

    private SetSearchField<Integer> projectKeys;
    private SetSearchField<String> projectStatuses;
    private StringSearchField involvedMember;
    private StringSearchField projectName;
    private NumberSearchField accountId;

    public SetSearchField<String> getProjectStatuses() {
        return projectStatuses;
    }

    public void setProjectStatuses(SetSearchField<String> projectStatuses) {
        this.projectStatuses = projectStatuses;
    }

    public StringSearchField getInvolvedMember() {
        return involvedMember;
    }

    public void setInvolvedMember(StringSearchField involvedMember) {
        this.involvedMember = involvedMember;
    }

    public StringSearchField getProjectName() {
        return projectName;
    }

    public void setProjectName(StringSearchField projectName) {
        this.projectName = projectName;
    }

    public SetSearchField<Integer> getProjectKeys() {
        return projectKeys;
    }

    public void setProjectKeys(SetSearchField<Integer> projectKeys) {
        this.projectKeys = projectKeys;
    }

    public NumberSearchField getAccountId() {
        return accountId;
    }

    public void setAccountId(NumberSearchField accountId) {
        this.accountId = accountId;
    }
}

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
import com.mycollab.db.arguments.*;
import com.mycollab.db.query.CacheParamMapper;
import com.mycollab.db.query.DateParam;
import com.mycollab.db.query.PropertyListParam;
import com.mycollab.module.project.ProjectTypeConstants;
import com.mycollab.module.project.i18n.TimeTrackingI18nEnum;
import com.mycollab.module.user.accountsettings.localization.UserI18nEnum;

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
public class ItemTimeLoggingSearchCriteria extends SearchCriteria {
    private static final long serialVersionUID = 1L;

    private SetSearchField<Integer> projectIds;
    private SetSearchField<String> logUsers;
    private StringSearchField type;
    private NumberSearchField typeId;
    private BooleanSearchField isBillable;

    public static final DateParam p_logDates = CacheParamMapper.register(ProjectTypeConstants.TIME,
            TimeTrackingI18nEnum.LOG_FOR_DATE, new DateParam("logdate", "m_prj_time_logging", "logForDay"));

    public static final PropertyListParam<String> p_logUsers = CacheParamMapper.register(ProjectTypeConstants.TIME,
            UserI18nEnum.LIST, new PropertyListParam<String>("loguser", "m_prj_time_logging", "loguser"));

    public SetSearchField<Integer> getProjectIds() {
        return projectIds;
    }

    public void setProjectIds(SetSearchField<Integer> projectIds) {
        this.projectIds = projectIds;
    }

    public SetSearchField<String> getLogUsers() {
        return logUsers;
    }

    public void setLogUsers(SetSearchField<String> logUsers) {
        this.logUsers = logUsers;
    }

    public void setType(StringSearchField type) {
        this.type = type;
    }

    public StringSearchField getType() {
        return type;
    }

    public void setTypeId(NumberSearchField typeId) {
        this.typeId = typeId;
    }

    public NumberSearchField getTypeId() {
        return typeId;
    }

    public BooleanSearchField getIsBillable() {
        return isBillable;
    }

    public void setIsBillable(BooleanSearchField isBillable) {
        this.isBillable = isBillable;
    }
}

package com.mycollab.module.project.domain.criteria;

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

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

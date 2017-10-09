package com.mycollab.module.project.ui

import com.mycollab.module.project.ProjectTypeConstants
import com.mycollab.module.project.i18n.*
import com.mycollab.vaadin.UserUIContext

/**
 * @author MyCollab Ltd
 * @since 6.0.0
 */
object ProjectLocalizationTypeMap {
    @JvmStatic fun getType(key: String): String {
        return when (key) {
            ProjectTypeConstants.PROJECT -> UserUIContext.getMessage(ProjectI18nEnum.SINGLE)
            ProjectTypeConstants.MESSAGE -> UserUIContext.getMessage(MessageI18nEnum.SINGLE)
            ProjectTypeConstants.MILESTONE -> UserUIContext.getMessage(MilestoneI18nEnum.SINGLE)
            ProjectTypeConstants.TASK -> UserUIContext.getMessage(TaskI18nEnum.SINGLE)
            ProjectTypeConstants.BUG -> UserUIContext.getMessage(BugI18nEnum.SINGLE)
            ProjectTypeConstants.BUG_COMPONENT -> UserUIContext.getMessage(ComponentI18nEnum.SINGLE)
            ProjectTypeConstants.BUG_VERSION -> UserUIContext.getMessage(VersionI18nEnum.SINGLE)
            ProjectTypeConstants.PAGE -> UserUIContext.getMessage(PageI18nEnum.SINGLE)
            ProjectTypeConstants.STANDUP -> UserUIContext.getMessage(StandupI18nEnum.SINGLE)
            ProjectTypeConstants.RISK -> UserUIContext.getMessage(RiskI18nEnum.SINGLE)
            else -> ""
        }
    }
}
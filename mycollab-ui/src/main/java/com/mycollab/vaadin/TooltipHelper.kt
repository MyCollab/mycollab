package com.mycollab.vaadin

/**
 * @author MyCollab Ltd.
 * @since 4.6.0
 */
object TooltipHelper {
    @JvmField
    val TOOLTIP_ID = "mycollabtip"

    @JvmStatic
    fun userHoverJsFunction(username: String): String {
        val uidVal = "'$TOOLTIP_ID'"
        val usernameVal = "'$username'"
        val urlVal = "'" + AppUI.siteUrl + "tooltip/'"
        val siteUrlVal = "'" + AppUI.siteUrl + "'"
        val timezoneVal = "'" + UserUIContext.getUser().timezone + "'"
        val accountIdVal = "'" + AppUI.accountId + "'"
        val localeVal = "'" + UserUIContext.getUserLocale().toLanguageTag() + "'"

        return "return showUserTooltip($uidVal,$usernameVal,$urlVal,$siteUrlVal,$timezoneVal,$accountIdVal,$localeVal);"
    }

    @JvmStatic
    fun projectHoverJsFunction(type: String, typeId: String): String {
        val uidVal = "'$TOOLTIP_ID'"
        val typeVal = "'$type'"
        val typeIdVal = "'$typeId'"
        val urlVal = "'" + AppUI.siteUrl + "tooltip/'"
        val accountIdVal = "'" + AppUI.accountId + "'"
        val siteUrlVal = "'" + AppUI.siteUrl + "'"
        val timezoneVal = "'" + AppUI.accountId + "'"
        val localeVal = "'" + UserUIContext.getUserLocale().toLanguageTag() + "'"
        val dateFormatVal = "'" + AppUI.dateFormat + "'"

        return "return overIt($uidVal,$typeVal,$typeIdVal,$urlVal,$accountIdVal,$siteUrlVal,$timezoneVal,$localeVal,$dateFormatVal);"
    }

    @JvmStatic
    fun crmHoverJsFunction(type: String, typeId: String): String {
        val uidVal = "'$TOOLTIP_ID'"
        val typeVal = "'$type'"
        val typeIdVal = "'$typeId'"
        val urlVal = "'" + AppUI.siteUrl + "tooltip/'"
        val accountIdVal = "'" + AppUI.accountId + "'"
        val siteUrlVal = "'" + AppUI.siteUrl + "'"
        val timezoneVal = "'" + AppUI.accountId + "'"
        val localeVal = "'" + UserUIContext.getUserLocale().toLanguageTag() + "'"
        val dateFormatVal = "'" + AppUI.dateFormat + "'"
        return "return crmActivityOverIt($uidVal,$typeVal,$typeIdVal,$urlVal,$accountIdVal,$siteUrlVal,$timezoneVal,$localeVal,$dateFormatVal);";
    }

    @JvmStatic
    fun itemMouseLeaveJsFunction(): String = "hideTooltip('$TOOLTIP_ID')"
}

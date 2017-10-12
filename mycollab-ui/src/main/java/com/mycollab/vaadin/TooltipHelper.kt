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

/**
 * This file is part of mycollab-ui.
 *
 * mycollab-ui is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * mycollab-ui is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with mycollab-ui.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.esofthead.mycollab.vaadin;

/**
 * @author MyCollab Ltd.
 * @since 4.6.0
 */
public class TooltipHelper {
    public static final String TOOLTIP_ID = "mycollabtip";

    public static String userHoverJsFunction(String username) {
        String uidVal = "'" + TOOLTIP_ID + "'";
        String usernameVal = "'" + username + "'";
        String urlVal = "'" + AppContext.getSiteUrl() + "tooltip/'";
        String siteUrlVal = "'" + AppContext.getSiteUrl() + "'";
        String timezoneVal = "'" + AppContext.getUser().getTimezone() + "'";
        String accountIdVal = "'" + AppContext.getAccountId() + "'";
        String localeVal = "'" + AppContext.getUserLocale().toString() + "'";

        return String.format("return showUserTooltip(%s,%s,%s,%s,%s,%s,%s);", uidVal, usernameVal, urlVal,
                siteUrlVal, timezoneVal, accountIdVal, localeVal);
    }

    public static String projectHoverJsFunction(String type, String typeId) {
        String uidVal = "'" + TOOLTIP_ID + "'";
        String typeVal = "'" + type + "'";
        String typeIdVal = "'" + typeId + "'";
        String urlVal = "'" + AppContext.getSiteUrl() + "tooltip/'";
        String accountIdVal = "'" + AppContext.getAccountId() + "'";
        String siteUrlVal = "'" + AppContext.getSiteUrl() + "'";
        String timezoneVal = "'" + AppContext.getAccountId() + "'";
        String localeVal = "'" + AppContext.getUserLocale().toString() + "'";
        String dateFormatVal = "'" + AppContext.getDateFormat() + "'";

        return String.format("return overIt(%s,%s,%s,%s,%s,%s,%s,%s,%s);", uidVal, typeVal, typeIdVal,
                urlVal, accountIdVal, siteUrlVal, timezoneVal, localeVal, dateFormatVal);
    }

    public static String crmHoverJsFunction(String type, String typeId) {
        String uidVal = "'" + TOOLTIP_ID + "'";
        String typeVal = "'" + type + "'";
        String typeIdVal = "'" + typeId + "'";
        String urlVal = "'" + AppContext.getSiteUrl() + "tooltip/'";
        String accountIdVal = "'" + AppContext.getAccountId() + "'";
        String siteUrlVal = "'" + AppContext.getSiteUrl() + "'";
        String timezoneVal = "'" + AppContext.getAccountId() + "'";
        String localeVal = "'" + AppContext.getUserLocale().toString() + "'";
        String dateFormatVal = "'" + AppContext.getDateFormat() + "'";
        return String.format("return crmActivityOverIt(%s,%s,%s,%s,%s,%s,%s,%s,%s);",
                uidVal, typeVal, typeIdVal, urlVal, accountIdVal, siteUrlVal, timezoneVal, localeVal, dateFormatVal);
    }

    public static String itemMouseLeaveJsFunction() {
        return String.format("hideTooltip('%s')", TOOLTIP_ID);
    }
}

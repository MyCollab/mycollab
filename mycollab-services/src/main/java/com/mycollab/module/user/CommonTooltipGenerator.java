/**
 * Copyright © MyCollab
 * <p>
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * <p>
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 * <p>
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.mycollab.module.user;

import com.hp.gagawa.java.elements.*;
import com.mycollab.common.i18n.GenericI18Enum;
import com.mycollab.core.utils.DateTimeUtils;
import com.mycollab.core.utils.StringUtils;
import com.mycollab.core.utils.TimezoneVal;
import com.mycollab.i18n.LocalizationHelper;
import com.mycollab.module.file.service.AbstractStorageService;
import com.mycollab.module.user.accountsettings.localization.UserI18nEnum;
import com.mycollab.module.user.domain.SimpleUser;
import com.mycollab.spring.AppContextUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.ZoneId;
import java.time.format.TextStyle;
import java.util.Locale;

/**
 * @author MyCollab Ltd.
 * @since 4.3.1
 */
public class CommonTooltipGenerator {
    private static final Logger LOG = LoggerFactory.getLogger(CommonTooltipGenerator.class);

    public static String generateTooltipUser(Locale locale, SimpleUser user, String siteURL, ZoneId timeZone) {
        try {
            if (user == null) {
                return generateTooltipNull(locale);
            }

            Div div = new Div();
            H3 userFullName = new H3().setStyle("line-height: normal;");
            userFullName.setStyle("padding-left:10px;").appendText(user.getDisplayName());
            div.appendChild(userFullName);

            Table table = new Table();
            table.setStyle("padding-left:10px; width:380px; color: #5a5a5a; font-size:12px;");

            String userEmail = (Boolean.TRUE.equals(user.getShowEmailPublicly())) ? user.getEmail() : "&lt;&lt;Hidden&gt;&gt;";
            Tr trRow1 = new Tr().appendChild(new Td().setStyle("width: 110px; vertical-align: top; text-align: right;color:#999")
                    .appendText(LocalizationHelper.getMessage(locale, GenericI18Enum.FORM_EMAIL)))
                    .appendChild(new Td().setStyle("vertical-align: top;").appendChild(
                            new A().setHref("mailto:" + user.getEmail()).appendText(userEmail)));

            Td trRow1_value = new Td().setStyle("width:150px;text-align: right; vertical-align: top;")
                    .appendChild(new Img("", getAvatarPath(user.getAvatarid(), 100))
                            .setCSSClass("circle-box"));
            trRow1_value.setAttribute("rowspan", "5");
            trRow1.appendChild(new Td().setStyle("width: 0px; vertical-align: top; text-align: right;").appendChild(trRow1_value));

            Tr trRow2 = new Tr().appendChild(new Td().setStyle("width: 110px; vertical-align: top; text-align: right;color:#999")
                    .appendText(LocalizationHelper.getMessage(locale, UserI18nEnum.FORM_TIMEZONE)))
                    .appendChild(new Td().setStyle("vertical-align: top;").appendText(
                            TimezoneVal.valueOf(user.getTimezone()).getDisplayName(TextStyle.FULL, locale)));
            Tr trRow3 = new Tr().appendChild(new Td().setStyle("width: 110px; vertical-align: top; text-align: right;color:#999")
                    .appendText(LocalizationHelper.getMessage(locale, UserI18nEnum.FORM_COUNTRY)))
                    .appendChild(new Td().setStyle("vertical-align: top;").appendText(
                            StringUtils.trimHtmlTags(user.getCountry())));

            Tr trRow4 = new Tr().appendChild(new Td().setStyle("width: 110px; vertical-align: top; text-align: right;color:#999")
                    .appendText(LocalizationHelper.getMessage(locale, UserI18nEnum.FORM_WORK_PHONE)))
                    .appendChild(new Td().setStyle("vertical-align: top;")
                            .appendText(StringUtils.trimHtmlTags(user.getWorkphone())));

            Tr trRow5 = new Tr().appendChild(
                    new Td().setStyle("width: 110px; vertical-align: top; text-align: right;color:#999")
                            .appendText(LocalizationHelper.getMessage(locale, GenericI18Enum.FORM_LAST_ACCESSED_TIME)))
                    .appendChild(new Td().setStyle("word-wrap: break-word; white-space: normal;vertical-align: top;")
                            .appendText(DateTimeUtils.getPrettyDateValue(user.getLastaccessedtime(), timeZone, locale)));
            table.appendChild(trRow1, trRow2, trRow3, trRow4, trRow5);
            div.appendChild(table);
            return div.write();
        } catch (Exception e) {
            LOG.error("Error while generate tooltip for servlet project-task tooltip", e);
            return null;
        }
    }

    private static String generateTooltipNull(Locale locale) {
        Div div = new Div();
        Table table = new Table();
        table.setStyle("padding-left:10px;  color: #5a5a5a; font-size:12px;");

        Tr trRow1 = new Tr().appendChild(new Td().setStyle("vertical-align: top; text-align: left;").appendText(
                LocalizationHelper.getMessage(locale, GenericI18Enum.TOOLTIP_NO_ITEM_EXISTED)));

        table.appendChild(trRow1);
        div.appendChild(table);

        return div.write();
    }

    private static String getAvatarPath(String userAvatarId, Integer size) {
        AbstractStorageService abstractStorageService = AppContextUtil.getSpringBean(AbstractStorageService.class);
        return abstractStorageService.getAvatarPath(userAvatarId, size);
    }
}

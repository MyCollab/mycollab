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
package com.mycollab.vaadin.ui.formatter;

import com.mycollab.common.i18n.GenericI18Enum;
import com.mycollab.core.utils.StringUtils;
import com.mycollab.vaadin.UserUIContext;

/**
 * @author MyCollab Ltd
 * @since 5.3.4
 */
public class LocalizationHistoryFieldFormat implements HistoryFieldFormat {
    private Class<? extends Enum> enumCls;

    public LocalizationHistoryFieldFormat(Class<? extends Enum> enumCls) {
        this.enumCls = enumCls;
    }

    @Override
    public String toString(String value) {
        return toString(value, true, UserUIContext.getMessage(GenericI18Enum.FORM_EMPTY));
    }

    @Override
    public String toString(String value, Boolean displayAsHtml, String msgIfBlank) {
        String content;
        if (StringUtils.isNotBlank(value)) {
            content = UserUIContext.getMessage(enumCls, value);
            content = (content.length() > 150) ? (content.substring(0, 150) + "...") : content;
        } else {
            content = msgIfBlank;
        }

        return content;
    }
}

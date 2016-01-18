/**
 * This file is part of mycollab-dao.
 *
 * mycollab-dao is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * mycollab-dao is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with mycollab-dao.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.esofthead.mycollab.core.arguments;

import com.esofthead.mycollab.core.utils.StringUtils;

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
public class StringSearchField extends SearchField {
    private static final long serialVersionUID = 1L;

    private String value;

    public StringSearchField() {
        this(AND, "");
    }

    private StringSearchField(String oper, String value) {
        this.operation = oper;
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public static StringSearchField and(String value) {
        if (StringUtils.isNotBlank(value)) {
            return new StringSearchField(AND, value);
        } else {
            return null;
        }
    }
}

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
package com.mycollab.common.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.mycollab.core.arguments.ValuedBean;
import org.apache.commons.lang.StringUtils;

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
public class MailRecipientField extends ValuedBean {
    private static final long serialVersionUID = 1L;

    private String email;
    private String name;

    @JsonCreator
    public MailRecipientField(String email, String name) {
        this.email = email;
        this.name = StringUtils.isNotBlank(name) ? name : email;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

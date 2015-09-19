/**
 * This file is part of mycollab-core.
 *
 * mycollab-core is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * mycollab-core is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with mycollab-core.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.esofthead.mycollab.core;

/**
 * @author MyCollab Ltd
 * @since 5.1.3
 */
public abstract class AbstractNotification {
    private String type;
    private String scope;

    public static final String WARNING = "warning";
    public static final String NEWS = "news";

    public static final String SCOPE_GLOBAL = "global";
    public static final String SCOPE_USER = "user";

    public AbstractNotification(String scope, String type) {
        this.scope = scope;
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public String getScope() {
        return scope;
    }

    public boolean isGlobalScope() {
        return SCOPE_GLOBAL.equals(scope);
    }
}

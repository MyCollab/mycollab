/**
 * This file is part of mycollab-services.
 *
 * mycollab-services is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * mycollab-services is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with mycollab-services.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.mycollab.common.interceptor.aspect;

import java.util.ArrayList;
import java.util.List;

/**
 * @author MyCollab Ltd.
 * @since 5.0.1
 */
public class ClassInfo {
    private String module;
    private String type;
    private List<String> excludeHistoryFields;

    public ClassInfo(String module, String type) {
        this.module = module;
        this.type = type;
        excludeHistoryFields = new ArrayList<>();
        excludeHistoryFields.add("id");
        excludeHistoryFields.add("lastupdatedtime");
        excludeHistoryFields.add("createdtime");
    }

    public String getModule() {
        return module;
    }

    public void setModule(String module) {
        this.module = module;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void addExcludeHistoryField(String field) {
        excludeHistoryFields.add(field);
    }

    public List<String> getExcludeHistoryFields() {
        return excludeHistoryFields;
    }
}

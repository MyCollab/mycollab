/**
 * mycollab-web - Parent pom providing dependency and plugin management for applications
		built with Maven
 * Copyright © ${project.inceptionYear} MyCollab (support@mycollab.com)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.mycollab.vaadin.reporting;

import com.mycollab.form.view.builder.type.DynaForm;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * @author MyCollab Ltd
 * @since 5.2.11
 */
public class FormReportLayout {
    private String moduleName;
    private DynaForm dynaForm;
    private String titleField;
    private Set<String> excludeFields;

    public FormReportLayout(String moduleName, String titleField, DynaForm defaultForm, String... excludeFieldArr) {
        this.moduleName = moduleName;
        this.dynaForm = defaultForm;
        this.titleField = titleField;
        if (excludeFieldArr.length > 0) {
            this.excludeFields = new HashSet<>(Arrays.asList(excludeFieldArr));
        } else {
            this.excludeFields = new HashSet<>();
        }
    }

    public String getModuleName() {
        return moduleName;
    }

    public DynaForm getDynaForm() {
        return dynaForm;
    }

    public String getTitleField() {
        return titleField;
    }

    public Set<String> getExcludeFields() {
        return excludeFields;
    }
}

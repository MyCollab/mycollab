/**
 * This file is part of mycollab-web.
 *
 * mycollab-web is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * mycollab-web is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with mycollab-web.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.mycollab.reporting;

import com.mycollab.core.reporting.NotInReport;
import net.sf.dynamicreports.report.definition.datatype.DRIDataType;
import net.sf.dynamicreports.report.exception.DRException;

import java.lang.reflect.Field;

import static net.sf.dynamicreports.report.builder.DynamicReports.type;

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
class DRIDataTypeFactory {

    static <T extends DRIDataType<?, ?>> T detectType(Field field) throws DRException {
        if (field.getAnnotation(NotInReport.class) != null) {
            return null;
        }

        String dataType = field.getType().getName();
        return type.detectType(dataType);
    }
}
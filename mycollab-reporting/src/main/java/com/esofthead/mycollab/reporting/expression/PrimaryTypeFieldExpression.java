/**
 * This file is part of mycollab-reporting.
 *
 * mycollab-reporting is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * mycollab-reporting is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with mycollab-reporting.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.esofthead.mycollab.reporting.expression;

import com.esofthead.mycollab.core.SimpleLogging;
import net.sf.dynamicreports.report.definition.ReportParameters;

/**
 * @author MyCollab Ltd.
 * @since 4.1.2
 */
public class PrimaryTypeFieldExpression<T> extends SimpleFieldExpression<T> implements MValue {
    private static final long serialVersionUID = 1L;

    public PrimaryTypeFieldExpression(String field) {
        super(field);
    }

    @Override
    public T evaluate(ReportParameters param) {
        try {
            return param.getFieldValue(field);
        } catch (Exception e) {
            SimpleLogging.error("Error while do report", e);
            return null;
        }
    }
}
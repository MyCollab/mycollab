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
package com.mycollab.reporting.expression;

import com.mycollab.core.utils.HumanTime;
import net.sf.dynamicreports.report.definition.ReportParameters;

/**
 * @author MyCollab Ltd
 * @since 5.1.4
 */
public class HumanTimeExpression extends SimpleFieldExpression {
    public HumanTimeExpression(String field) {
        super(field);
    }

    @Override
    public String evaluate(ReportParameters reportParameters) {
        Double longValue = reportParameters.getFieldValue(field);
        if (longValue != null) {
            Double milis = longValue * 1000 * 60 * 60;
            return HumanTime.exactly(milis.longValue());
        } else {
            return "O d";
        }
    }
}

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
package com.esofthead.mycollab.reporting.generator;

import com.esofthead.mycollab.reporting.ReportStyles;
import net.sf.dynamicreports.report.base.expression.AbstractSimpleExpression;
import net.sf.dynamicreports.report.builder.component.ComponentBuilder;

import static net.sf.dynamicreports.report.builder.DynamicReports.cmp;

/**
 * @author MyCollab Ltd
 * @since 5.2.12
 */
public class SimpleExpressionBuilderGenerator implements ComponentBuilderGenerator {
    private AbstractSimpleExpression expression;

    public SimpleExpressionBuilderGenerator(AbstractSimpleExpression expression) {
        this.expression = expression;
    }

    @Override
    public ComponentBuilder getCompBuilder(ReportStyles reportStyles) {
        return cmp.text(expression).setStyle(reportStyles.getRootStyle());
    }
}

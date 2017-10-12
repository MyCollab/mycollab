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
package com.mycollab.reporting.expression;

import net.sf.dynamicreports.report.definition.ReportParameters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author MyCollab Ltd.
 * @since 4.1.2
 */
public class PrimaryTypeFieldExpression<T> extends SimpleFieldExpression<T> {
    private static Logger LOG = LoggerFactory.getLogger(PrimaryTypeFieldExpression.class);
    private static final long serialVersionUID = 1L;

    public PrimaryTypeFieldExpression(String field) {
        super(field);
    }

    @Override
    public T evaluate(ReportParameters param) {
        try {
            return param.getFieldValue(field);
        } catch (Exception e) {
            LOG.error("Error while do report", e);
            return null;
        }
    }
}
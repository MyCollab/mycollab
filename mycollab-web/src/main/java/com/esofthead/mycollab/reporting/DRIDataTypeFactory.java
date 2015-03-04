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
package com.esofthead.mycollab.reporting;

import static net.sf.dynamicreports.report.builder.DynamicReports.type;

import java.lang.reflect.Field;

import net.sf.dynamicreports.report.base.datatype.AbstractDataType;
import net.sf.dynamicreports.report.definition.datatype.DRIDataType;
import net.sf.dynamicreports.report.exception.DRException;

import com.esofthead.mycollab.common.domain.Currency;
import com.esofthead.mycollab.core.reporting.NotInReport;

/**
 * 
 * @author MyCollab Ltd.
 * @since 1.0
 * 
 */
class DRIDataTypeFactory {
	private static final CurrencyType currencyType = new CurrencyType();

	@SuppressWarnings("unchecked")
	static <T extends DRIDataType<?, ?>> T detectType(Field field)
			throws DRException {
		if (field.getAnnotation(NotInReport.class) != null) {
			return null;
		}

		String dataType = field.getType().getName();

		String dataTypeLC = dataType.toLowerCase().trim();
		if (dataTypeLC.equals("currency") || dataType.equals(Currency.class.getName())) {
			return (T) currencyType;
		} else {
			return type.detectType(dataType);
		}
	}

	static class CurrencyType extends AbstractDataType<Currency, Currency> {
		private static final long serialVersionUID = 1L;
	}
}
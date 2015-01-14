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
package com.esofthead.mycollab.reporting;

import java.util.ArrayList;
import java.util.List;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRField;

import org.apache.commons.beanutils.PropertyUtils;

/**
 * 
 * @author MyCollab Ltd.
 * @since 1.0
 * 
 */
public class BeanDataSource<T> implements JRDataSource {

	private List<T> data = new ArrayList<>();
	private int currentIndex = 0;
	private T currentRecord;

	public BeanDataSource(List<T> data) {
		this.data = data;
	}

	@Override
	public Object getFieldValue(JRField jrField) throws JRException {
		try {
			String fieldName = jrField.getName();
			return PropertyUtils.getProperty(currentRecord, fieldName);
		} catch (Exception e) {
			throw new JRException(e);
		}
	}

	@Override
	public boolean next() throws JRException {
		boolean result = (currentIndex < data.size());
		if (result) {
			currentRecord = data.get(currentIndex);
			currentIndex++;
		}

		return result;
	}

}

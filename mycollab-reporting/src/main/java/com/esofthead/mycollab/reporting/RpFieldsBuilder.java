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

import com.esofthead.mycollab.common.TableViewField;

/**
 * 
 * @author MyCollab Ltd.
 * @since 1.0
 * 
 */
public class RpFieldsBuilder {
	private List<TableViewFieldDecorator> viewFields;

	public RpFieldsBuilder(List<TableViewField> fields) {
		viewFields = new ArrayList<>();

		for (TableViewField field : fields) {
			TableViewFieldDecorator fieldDecorator = new TableViewFieldDecorator(field);
			viewFields.add(fieldDecorator);
		}
	}

	public List<TableViewFieldDecorator> getFields() {
		return viewFields;
	}

}

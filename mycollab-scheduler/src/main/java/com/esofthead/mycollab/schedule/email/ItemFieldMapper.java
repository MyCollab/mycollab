/**
 * This file is part of mycollab-scheduler.
 *
 * mycollab-scheduler is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * mycollab-scheduler is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with mycollab-scheduler.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.esofthead.mycollab.schedule.email;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import com.esofthead.mycollab.schedule.email.format.DefaultFieldFormat;
import com.esofthead.mycollab.schedule.email.format.FieldFormat;

/**
 * 
 * @author MyCollab Ltd.
 * @since 4.0
 * 
 */
public class ItemFieldMapper {
	protected Map<String, FieldFormat> fieldNameMap = new LinkedHashMap<String, FieldFormat>();

	public void put(String fieldname, String displayName) {
		fieldNameMap.put(fieldname, new DefaultFieldFormat(fieldname,
				displayName));
	}

	public void put(String fieldname, String displayName, boolean isColSpan) {
		fieldNameMap.put(fieldname, new DefaultFieldFormat(fieldname,
				displayName, isColSpan));
	}

	public void put(String fieldname, FieldFormat format) {
		fieldNameMap.put(fieldname, format);
	}

	public boolean hasField(String fieldName) {
		return fieldNameMap.containsKey(fieldName);
	}

	public FieldFormat getFieldLabel(String fieldName) {
		return fieldNameMap.get(fieldName);
	}

	public Set<String> keySet() {
		return fieldNameMap.keySet();
	}
}

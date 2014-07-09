/**
 * This file is part of mycollab-services.
 *
 * mycollab-services is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * mycollab-services is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with mycollab-services.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.esofthead.mycollab.common;

import java.io.Serializable;

/**
 * 
 * @author MyCollab Ltd.
 * @since 2.0
 * 
 */
public class TableViewField implements Serializable {
	private static final long serialVersionUID = 1L;

	private Enum<?> descKey;

	private String field;

	private int defaultWidth;

	public TableViewField() {
		this(null, "");
	}

	public TableViewField(Enum<?> desc, String field) {
		this(desc, field, -1);
	}

	public TableViewField(Enum<?> descKey, String field, int defaultWidth) {
		this.descKey = descKey;
		this.field = field;
		this.defaultWidth = defaultWidth;
	}

	public Enum<?> getDescKey() {
		return descKey;
	}

	public void setDescKey(Enum<?> descKey) {
		this.descKey = descKey;
	}

	public String getField() {
		return field;
	}

	public void setField(String field) {
		this.field = field;
	}

	public int getDefaultWidth() {
		return defaultWidth;
	}

	public void setDefaultWidth(int defaultWidth) {
		this.defaultWidth = defaultWidth;
	}
}

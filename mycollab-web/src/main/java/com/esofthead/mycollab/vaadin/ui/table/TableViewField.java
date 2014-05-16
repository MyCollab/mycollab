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
package com.esofthead.mycollab.vaadin.ui.table;

import java.io.Serializable;

import com.esofthead.mycollab.vaadin.AppContext;

/**
 * 
 * @author MyCollab Ltd.
 * @since 2.0
 * 
 */
public class TableViewField implements Serializable {
	private static final long serialVersionUID = 1L;

	private Enum descKey;

	private String desc;

	private String field;

	private int defaultWidth;

	public TableViewField() {
		this("", "");
	}

	public TableViewField(Enum desc, String field) {
		this(desc, field, -1);
	}

	@Deprecated
	public TableViewField(String desc, String field) {
		this(desc, field, -1);
	}

	@Deprecated
	public TableViewField(String desc, String field, int defaultWidth) {
		this.desc = desc;
		this.field = field;
		this.defaultWidth = defaultWidth;
	}

	public TableViewField(Enum descKey, String field, int defaultWidth) {
		this.descKey = descKey;
		this.field = field;
		this.defaultWidth = defaultWidth;
	}

	public Enum getDescKey() {
		return descKey;
	}

	public void setDescKey(Enum descKey) {
		this.descKey = descKey;
	}

	public String getDesc() {
		if (desc != null) {
			return desc;
		} else {
			return AppContext.getMessage(descKey);
		}
	}

	public void setDesc(String key) {
		this.desc = key;
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

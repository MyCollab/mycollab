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
package com.esofthead.mycollab.module.file.resource;

/**
 * 
 * @author MyCollab Ltd.
 * @since 1.0
 * 
 */
public class FieldExportColumn {

	private String name;
	private String displayName;
	private int columnWidth;

	public FieldExportColumn(String headerName, String displayName) {
		this(headerName, displayName, 0);
	}

	public FieldExportColumn(String headerName, String displayName,
			int columnWidth) {
		this.name = headerName;
		this.displayName = displayName;
		this.columnWidth = columnWidth;
	}

	public String getName() {
		return name;
	}

	public void setName(String headerName) {
		this.name = headerName;
	}

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	public int getColumnWidth() {
		return columnWidth;
	}

	public void setColumnWidth(int columnWidth) {
		this.columnWidth = columnWidth;
	}
}

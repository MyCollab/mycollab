/**
 * This file is part of mycollab-ui.
 *
 * mycollab-ui is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * mycollab-ui is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with mycollab-ui.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.esofthead.mycollab.vaadin.events;

import com.vaadin.server.StreamResource;

/**
 * Popup action handler of table
 * 
 * @author MyCollab Ltd.
 * @since 2.0
 * 
 */
public interface MassItemActionHandler {
	public static final String MAIL_ACTION = "mail";

	public static final String EXPORT_CSV_ACTION = "exportCsv";

	public static final String EXPORT_PDF_ACTION = "exportPdf";

	public static final String EXPORT_EXCEL_ACTION = "exportExcel";

	public static final String DELETE_ACTION = "delete";

	public static final String MASS_UPDATE_ACTION = "massUpdate";

	/**
	 * 
	 * @param id
	 */
	void onSelect(String id);

	/**
	 * 
	 * @param id
	 * @return
	 */
	StreamResource buildStreamResource(String id);
}

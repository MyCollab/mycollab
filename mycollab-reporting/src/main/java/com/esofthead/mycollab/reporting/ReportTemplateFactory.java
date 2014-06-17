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

import java.util.Locale;

/**
 * 
 * @author MyCollab Ltd.
 * @since 1.0
 * 
 */
public class ReportTemplateFactory {
	private static final AbstractReportTemplate jpReport = new ReportTemplateJp();
	private static final AbstractReportTemplate enReport = new ReportTemplateEn();

	public static AbstractReportTemplate getTemplate() {
		return getTemplate(null);
	}

	public static AbstractReportTemplate getTemplate(Locale language) {
		if (Locale.JAPANESE.equals(language)) {
			return jpReport;
		} else {
			return enReport;
		}
	}
}

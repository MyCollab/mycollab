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
package com.esofthead.mycollab.module.project.reporting;

import static net.sf.dynamicreports.report.builder.DynamicReports.col;
import static net.sf.dynamicreports.report.builder.DynamicReports.sbt;
import static net.sf.dynamicreports.report.builder.DynamicReports.type;

import java.util.Date;

import net.sf.dynamicreports.report.builder.column.TextColumnBuilder;

import com.esofthead.mycollab.module.file.resource.ExportItemsStreamResource;
import com.esofthead.mycollab.module.project.domain.SimpleItemTimeLogging;
import com.esofthead.mycollab.module.project.domain.criteria.ItemTimeLoggingSearchCriteria;
import com.esofthead.mycollab.module.project.service.ItemTimeLoggingService;
import com.esofthead.mycollab.reporting.GroupIteratorDataSource;
import com.esofthead.mycollab.reporting.ReportExportType;
import com.esofthead.mycollab.vaadin.AppContext;

/**
 * 
 * @author MyCollab Ltd.
 * @since 1.0
 * 
 */
@SuppressWarnings("serial")
public class ExportTimeLoggingStreamResource extends
		ExportItemsStreamResource<SimpleItemTimeLogging> {

	private ItemTimeLoggingService searchService;
	private ItemTimeLoggingSearchCriteria searchCriteria;

	public ExportTimeLoggingStreamResource(String title,
			ReportExportType outputForm, ItemTimeLoggingService searchService,
			ItemTimeLoggingSearchCriteria searchCriteria) {
		super(AppContext.getUserLocale(), title, outputForm);

		this.searchService = searchService;
		this.searchCriteria = searchCriteria;
	}

	@Override
	protected void initReport() throws Exception {
		TextColumnBuilder<String> summaryColumn = col.column("Summary",
				"summary", type.stringType()).setWidth(400);

		TextColumnBuilder<String> logUserColumn = col.column("Logged User",
				"logUserFullName", type.stringType());

		TextColumnBuilder<Double> logValueColumn = col.column("Hours",
				"logvalue", type.doubleType()).setWidth(150);

		TextColumnBuilder<Date> createdTimeColumn = col.column("Created Time",
				"createdtime", type.dateType()).setWidth(100);

		reportBuilder
				.columns(summaryColumn, logUserColumn, logValueColumn,
						createdTimeColumn).groupBy(logUserColumn)
				.subtotalsAtSummary(sbt.sum(logValueColumn));

	}

	@Override
	protected void fillReport() throws Exception {
		reportBuilder.setDataSource(new GroupIteratorDataSource(searchService,
				searchCriteria));

	}

}

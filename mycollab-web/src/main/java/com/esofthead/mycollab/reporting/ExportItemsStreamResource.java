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

import static net.sf.dynamicreports.report.builder.DynamicReports.cmp;
import static net.sf.dynamicreports.report.builder.DynamicReports.export;
import static net.sf.dynamicreports.report.builder.DynamicReports.report;

import java.awt.GraphicsEnvironment;
import java.io.IOException;
import java.io.InputStream;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.util.Arrays;
import java.util.Locale;

import net.sf.dynamicreports.jasper.builder.JasperReportBuilder;
import net.sf.dynamicreports.jasper.builder.export.JasperCsvExporterBuilder;
import net.sf.dynamicreports.jasper.builder.export.JasperXlsxExporterBuilder;
import net.sf.dynamicreports.jasper.constant.JasperProperty;
import net.sf.dynamicreports.report.constant.PageOrientation;
import net.sf.dynamicreports.report.constant.PageType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.esofthead.mycollab.core.MyCollabException;
import com.esofthead.mycollab.core.MyCollabThread;
import com.esofthead.mycollab.eventmanager.EventBusFactory;
import com.esofthead.mycollab.shell.events.ShellEvent;
import com.vaadin.server.StreamResource;

/**
 * 
 * @author MyCollab Ltd.
 * @since 2.0
 *
 */
public abstract class ExportItemsStreamResource implements StreamResource.StreamSource {
	private static final long serialVersionUID = 1L;

	private static final Logger LOG = LoggerFactory.getLogger(ExportItemsStreamResource.class);

	protected AbstractReportTemplate reportTemplate;

	private String reportTitle;

	private ReportExportType outputForm;

	protected JasperReportBuilder reportBuilder;

	private Locale language;

	public ExportItemsStreamResource(Locale languageSupport,
			String reportTitle, ReportExportType outputForm) {
		this.language = languageSupport;
		this.reportTemplate = ReportTemplateFactory.getTemplate(languageSupport);
		this.reportTitle = reportTitle;
		this.outputForm = outputForm;
	}

	public static String getDefaultExportFileName(ReportExportType type) {
		if (type == ReportExportType.PDF) {
			return "export.pdf";
		} else if (type == ReportExportType.CSV) {
			return "export.csv";
		} else if (type == ReportExportType.EXCEL) {
			return "export.xlsx";
		} else {
			throw new MyCollabException("Do not support report output " + type);
		}
	}

	@Override
	public InputStream getStream() {
		final PipedInputStream inStream = new PipedInputStream();
		final PipedOutputStream outStream;

		try {
			outStream = new PipedOutputStream(inStream);
		} catch (IOException ex) {
			LOG.error("Can not create outstream file", ex);
			return null;
		}

		Thread threadExport = new MyCollabThread(new Runnable() {
			@Override
			public void run() {
				try {
					reportBuilder = createReport();

					initReport();
					fillReport();

					if (outputForm == ReportExportType.PDF) {
						reportBuilder.toPdf(outStream);
					} else if (outputForm == ReportExportType.CSV) {
						JasperCsvExporterBuilder csvExporter = export
								.csvExporter(outStream);
						reportBuilder.ignorePageWidth();
						reportBuilder.toCsv(csvExporter);
					} else if (outputForm == ReportExportType.EXCEL) {
						JasperXlsxExporterBuilder xlsExporter = export
								.xlsxExporter(outStream)
								.setDetectCellType(true)
								.setIgnorePageMargins(true)
								.setWhitePageBackground(false)
								.setRemoveEmptySpaceBetweenColumns(true);
						reportBuilder.toXlsx(xlsExporter);
					} else {
						throw new IllegalArgumentException(
								"Do not support output type " + outputForm);
					}

				} catch (Exception e) {
					EventBusFactory.getInstance().post(
							new ShellEvent.NotifyErrorEvent(
									ExportItemsStreamResource.this, e));
				} finally {
					try {
						outStream.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		});
		threadExport.start();
		return inStream;
	}

	abstract protected void initReport() throws Exception;

	abstract protected void fillReport() throws Exception;

	protected JasperReportBuilder createReport() {
		JasperReportBuilder reportBuilder = report();
		if (outputForm == ReportExportType.PDF) {
			reportBuilder
					.title(reportTemplate.createTitleComponent(reportTitle))
					.noData(reportTemplate.createTitleComponent(reportTitle),
							cmp.text("There is no data"))
					.setPageFormat(PageType.A3, PageOrientation.LANDSCAPE)
					.setColumnTitleStyle(reportTemplate.getColumnTitleStyle())
					.highlightDetailEvenRows()
					.pageFooter(
							cmp.pageXofY().setStyle(
									reportTemplate.getBoldCenteredStyle()))
					.setLocale(language);

		} else if (outputForm == ReportExportType.CSV) {
			reportBuilder.setIgnorePagination(true);
		} else if (outputForm == ReportExportType.EXCEL) {
			reportBuilder
					.setColumnTitleStyle(reportTemplate.getColumnTitleStyle())
					.addProperty(JasperProperty.EXPORT_XLS_FREEZE_ROW, "2")
					.ignorePageWidth().ignorePagination();

		} else {
			throw new IllegalArgumentException("Do not support output type "
					+ outputForm);
		}

		return reportBuilder;
	}
}
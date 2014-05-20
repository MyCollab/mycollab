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

import static net.sf.dynamicreports.report.builder.DynamicReports.cmp;
import static net.sf.dynamicreports.report.builder.DynamicReports.export;
import static net.sf.dynamicreports.report.builder.DynamicReports.report;

import java.awt.GraphicsEnvironment;
import java.io.IOException;
import java.io.InputStream;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.util.Arrays;

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
import com.esofthead.mycollab.reporting.ReportExportType;
import com.esofthead.mycollab.reporting.Templates;
import com.vaadin.server.StreamResource;

/**
 * 
 * @author MyCollab Ltd.
 * @since 2.0
 * 
 * @param <T>
 */
public abstract class ExportItemsStreamResource<T> implements
		StreamResource.StreamSource {
	private static final long serialVersionUID = 1L;

	private static Logger log = LoggerFactory
			.getLogger(ExportItemsStreamResource.class);

	protected String reportTitle;

	protected ReportExportType outputForm;

	protected JasperReportBuilder reportBuilder;

	public ExportItemsStreamResource(String reportTitle,
			ReportExportType outputForm) {
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
			log.error("Can not create outstream file", ex);
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
					log.error("Exception while generating report ", e);
					throw new MyCollabException(e);
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
					.title(Templates.createTitleComponent(reportTitle))
					.noData(Templates.createTitleComponent(reportTitle),
							cmp.text("There is no data"))
					.setPageFormat(PageType.A3, PageOrientation.LANDSCAPE)
					.setColumnTitleStyle(Templates.columnTitleStyle)
					.highlightDetailEvenRows()
					.pageFooter(
							cmp.pageXofY()
									.setStyle(Templates.boldCenteredStyle));
		} else if (outputForm == ReportExportType.CSV) {
			reportBuilder.setIgnorePagination(true);
		} else if (outputForm == ReportExportType.EXCEL) {
			reportBuilder.setColumnTitleStyle(Templates.columnTitleStyle)
					.addProperty(JasperProperty.EXPORT_XLS_FREEZE_ROW, "2")
					.ignorePageWidth().ignorePagination();

		} else {
			throw new IllegalArgumentException("Do not support output type "
					+ outputForm);
		}

		return reportBuilder;
	}

	public static void main(String[] args) throws Exception {
		System.out.println(Arrays.asList(GraphicsEnvironment
				.getLocalGraphicsEnvironment().getAvailableFontFamilyNames()));
	}

}

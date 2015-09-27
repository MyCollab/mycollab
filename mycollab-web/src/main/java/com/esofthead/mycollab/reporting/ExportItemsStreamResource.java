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

import com.esofthead.mycollab.core.MyCollabException;
import com.esofthead.mycollab.core.utils.DateTimeUtils;
import com.esofthead.mycollab.eventmanager.EventBusFactory;
import com.esofthead.mycollab.shell.events.ShellEvent;
import com.vaadin.server.StreamResource;
import net.sf.dynamicreports.jasper.builder.JasperReportBuilder;
import net.sf.dynamicreports.jasper.builder.export.JasperCsvExporterBuilder;
import net.sf.dynamicreports.jasper.builder.export.JasperXlsxExporterBuilder;
import net.sf.dynamicreports.jasper.constant.JasperProperty;
import net.sf.dynamicreports.report.builder.HyperLinkBuilder;
import net.sf.dynamicreports.report.builder.component.ComponentBuilder;
import net.sf.dynamicreports.report.constant.HorizontalTextAlignment;
import net.sf.dynamicreports.report.constant.PageOrientation;
import net.sf.dynamicreports.report.constant.PageType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

import static net.sf.dynamicreports.report.builder.DynamicReports.*;

/**
 * @author MyCollab Ltd.
 * @since 2.0
 */
public abstract class ExportItemsStreamResource implements StreamResource.StreamSource {
    private static final long serialVersionUID = 1L;

    private static Logger LOG = LoggerFactory.getLogger(ExportItemsStreamResource.class);

    protected AbstractReportTemplate reportTemplate;
    protected JasperReportBuilder reportBuilder;
    private String reportTitle;
    private ReportExportType outputForm;
    protected Locale locale;
    protected TimeZone timeZone;
    protected Map<String, Object> parameters;

    public ExportItemsStreamResource(TimeZone timezone, Locale languageSupport, String reportTitle, ReportExportType
            outputForm, Map<String, Object> parameters) {
        this.locale = languageSupport;
        this.timeZone = timezone;
        this.reportTemplate = ReportTemplateFactory.getTemplate(languageSupport);
        this.reportTitle = reportTitle;
        this.outputForm = outputForm;
        this.parameters = parameters;
    }

    @Override
    public InputStream getStream() {
        final PipedInputStream inStream = new PipedInputStream();
        final PipedOutputStream outStream = new PipedOutputStream();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    reportBuilder = createReport();

                    initReport();
                    fillReport();

                    if (outputForm == ReportExportType.PDF) {
                        reportBuilder.toPdf(outStream);
                    } else if (outputForm == ReportExportType.CSV) {
                        JasperCsvExporterBuilder csvExporter = export.csvExporter(outStream);
                        reportBuilder.ignorePageWidth();
                        reportBuilder.toCsv(csvExporter);
                    } else if (outputForm == ReportExportType.EXCEL) {
                        JasperXlsxExporterBuilder xlsExporter = export.xlsxExporter(outStream)
                                .setDetectCellType(true).setIgnorePageMargins(true)
                                .setWhitePageBackground(false).setRemoveEmptySpaceBetweenColumns(true);
                        reportBuilder.toXlsx(xlsExporter);
                    } else {
                        throw new IllegalArgumentException(
                                "Do not support output type " + outputForm);
                    }
                } catch (Exception e) {
                    EventBusFactory.getInstance().post(new ShellEvent.NotifyErrorEvent(ExportItemsStreamResource.this, e));
                } finally {
                    try {
                        outStream.close();
                    } catch (IOException e) {
                        LOG.error("Try to close reporting stream error", e);
                    }
                }
            }
        }).start();
        try {
            outStream.connect(inStream);
        } catch (IOException e) {
            throw new MyCollabException(e);
        }
        return inStream;
    }

    abstract protected void initReport() throws Exception;

    abstract protected void fillReport() throws Exception;

    protected JasperReportBuilder createReport() {
        JasperReportBuilder reportBuilder = report();
        reportBuilder.setParameters(parameters);
        if (outputForm == ReportExportType.PDF) {
            reportBuilder
                    .title(createTitleComponent(reportTitle))
                    .noData(createTitleComponent(reportTitle), cmp.text("There is no data"))
                    .setPageFormat(PageType.A3, PageOrientation.LANDSCAPE)
                    .setColumnTitleStyle(reportTemplate.getColumnTitleStyle())
                    .highlightDetailEvenRows().pageFooter(cmp.pageXofY().setStyle(reportTemplate.getBoldCenteredStyle()))
                    .setLocale(locale);

        } else if (outputForm == ReportExportType.CSV) {
            reportBuilder.setIgnorePagination(true);
        } else if (outputForm == ReportExportType.EXCEL) {
            reportBuilder.title(createTitleComponent(reportTitle)).setColumnTitleStyle(reportTemplate.getColumnTitleStyle())
                    .addProperty(JasperProperty.EXPORT_XLS_FREEZE_ROW, "2").ignorePageWidth().ignorePagination();

        } else {
            throw new IllegalArgumentException("Do not support output type " + outputForm);
        }

        return reportBuilder;
    }

    /**
     * Creates custom component which is possible to add to any report band
     * component
     */
    private ComponentBuilder<?, ?> createTitleComponent(String label) {
        HyperLinkBuilder link = hyperLink("https://www.mycollab.com");
        ComponentBuilder<?, ?> dynamicReportsComponent = cmp.horizontalList(
                cmp.image(
                        ReportTemplateFactory.class.getClassLoader().getResourceAsStream("images/logo.png"))
                        .setFixedDimension(150, 28), cmp.horizontalGap(10), cmp.verticalList(
                        cmp.text(label).setStyle(reportTemplate.bold22CenteredStyle)
                                .setHorizontalTextAlignment(HorizontalTextAlignment.LEFT),
                        cmp.text("https://www.mycollab.com").setStyle(reportTemplate.italicStyle).setHyperLink(link)),
                cmp.horizontalGap(20),
                cmp.text(String.format("Generated at: %s",
                        DateTimeUtils.formatDate(new GregorianCalendar().getTime(), "yyyy-MM-dd'T'HH:mm:ss", timeZone))));

        return cmp.horizontalList().add(dynamicReportsComponent).newRow().add(cmp.line()).newRow().add(cmp.verticalGap(10));
    }
}
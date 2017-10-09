package com.mycollab.reporting;

import net.sf.dynamicreports.jasper.builder.export.JasperCsvExporterBuilder;
import net.sf.dynamicreports.report.exception.DRException;

import java.io.OutputStream;
import java.util.Locale;
import java.util.Map;

import static net.sf.dynamicreports.report.builder.DynamicReports.cmp;
import static net.sf.dynamicreports.report.builder.DynamicReports.export;

/**
 * @author MyCollab Ltd
 * @since 5.4.7
 */
public class CsvReportBuilder extends AbstractReportBuilder {

    CsvReportBuilder(String reportTitle, Locale locale, RpFieldsBuilder fieldBuilder,
                     Class classType, Map parameters) {
        super(reportTitle, locale, fieldBuilder, classType, parameters);
    }

    @Override
    protected void initReport() {
    }

    @Override
    protected void setTitle(String title) {
        reportBuilder.title(cmp.horizontalList());
    }

    @Override
    public void toStream(OutputStream outputStream) throws DRException {
        reportBuilder.ignorePageWidth();
        reportBuilder.ignorePagination();
        JasperCsvExporterBuilder csvExporter = export.csvExporter(outputStream);
        reportBuilder.toCsv(csvExporter);
    }
}

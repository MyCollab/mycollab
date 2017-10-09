package com.mycollab.reporting;

import net.sf.dynamicreports.jasper.builder.export.JasperXlsxExporterBuilder;
import net.sf.dynamicreports.jasper.constant.JasperProperty;
import net.sf.dynamicreports.report.exception.DRException;

import java.io.OutputStream;
import java.util.Locale;
import java.util.Map;

import static net.sf.dynamicreports.report.builder.DynamicReports.export;

/**
 * @author MyCollab Ltd
 * @since 5.4.7
 */
public class XlsReportBuilder extends AbstractReportBuilder {

    XlsReportBuilder(String reportTitle, Locale locale, RpFieldsBuilder fieldBuilder, Class classType, Map parameters) {
        super(reportTitle, locale, fieldBuilder, classType, parameters);
    }

    @Override
    protected void initReport() {
        reportBuilder
                .setColumnTitleStyle(ReportStyles.instance().getColumnTitleStyle())
                .addProperty(JasperProperty.EXPORT_XLS_FREEZE_ROW, "2").ignorePageWidth()
                .ignorePagination();
    }

    @Override
    public void toStream(OutputStream outputStream) throws DRException {
        JasperXlsxExporterBuilder xlsExporter = export.xlsxExporter(outputStream)
                .setDetectCellType(true).setIgnorePageMargins(true)
                .setWhitePageBackground(false).setRemoveEmptySpaceBetweenColumns(true);
        reportBuilder.toXlsx(xlsExporter);
    }
}

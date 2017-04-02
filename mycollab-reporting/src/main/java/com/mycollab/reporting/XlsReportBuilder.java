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

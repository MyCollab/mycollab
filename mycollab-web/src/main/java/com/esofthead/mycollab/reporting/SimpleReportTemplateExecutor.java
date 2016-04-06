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

import com.esofthead.mycollab.core.arguments.SearchCriteria;
import com.esofthead.mycollab.core.persistence.service.ISearchableService;
import com.esofthead.mycollab.core.utils.ClassUtils;
import com.esofthead.mycollab.reporting.expression.MValue;
import com.esofthead.mycollab.vaadin.AppContext;
import net.sf.dynamicreports.jasper.builder.JasperReportBuilder;
import net.sf.dynamicreports.jasper.builder.export.JasperCsvExporterBuilder;
import net.sf.dynamicreports.jasper.builder.export.JasperXlsxExporterBuilder;
import net.sf.dynamicreports.jasper.constant.JasperProperty;
import net.sf.dynamicreports.report.builder.column.ComponentColumnBuilder;
import net.sf.dynamicreports.report.builder.component.HorizontalListBuilder;
import net.sf.dynamicreports.report.constant.PageOrientation;
import net.sf.dynamicreports.report.constant.PageType;
import net.sf.dynamicreports.report.constant.VerticalTextAlignment;
import net.sf.dynamicreports.report.definition.datatype.DRIDataType;
import net.sf.dynamicreports.report.exception.DRException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.*;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

import static net.sf.dynamicreports.report.builder.DynamicReports.*;

/**
 * @author MyCollab Ltd
 * @since 5.1.4
 */
public abstract class SimpleReportTemplateExecutor<T> extends ReportTemplateExecutor {
    private static Logger LOG = LoggerFactory.getLogger(SimpleReportTemplateExecutor.class);
    public static final String CRITERIA = "criteria";

    protected JasperReportBuilder reportBuilder;
    protected Class<T> classType;
    protected RpFieldsBuilder fieldBuilder;

    public SimpleReportTemplateExecutor(String reportTitle, RpFieldsBuilder fieldBuilder, ReportExportType outputForm,
                                        Class<T> classType) {
        super(AppContext.getUserTimezone(), AppContext.getUserLocale(), reportTitle, outputForm);
        this.fieldBuilder = fieldBuilder;
        this.classType = classType;
    }

    @Override
    protected void initReport() throws Exception {
        reportBuilder = createReport();
        LOG.debug("Init report: " + classType);
        // Add field of report
        Field[] clsFields = ClassUtils.getAllFields(classType);
        for (Field objField : clsFields) {
            if ("selected".equals(objField.getName()) || "extraData".equals(objField.getName())) {
                continue;
            }

            DRIDataType<Object, ? extends Object> jrType = DRIDataTypeFactory.detectType(objField);
            if (jrType != null) {
                LOG.debug("  Add field: " + objField.getName());
                reportBuilder.addField(objField.getName(), jrType);
            }
        }
        List<TableViewFieldDecorator> fields = fieldBuilder.getFields();

        Map<String, MValue> lstFieldBuilder = ColumnBuilderClassMapper.getListFieldBuilder(classType);
        if (lstFieldBuilder != null) {
            // build columns of report
            for (TableViewFieldDecorator field : fields) {
                MValue columnFieldBuilder = lstFieldBuilder.get(field.getField());
                if (columnFieldBuilder != null) {
                    field.setComponentBuilder(reportTemplate.buildCompBuilder(columnFieldBuilder));
                }

                LOG.debug("Construct component builder {} and width {}", field.getField(), field.getDefaultWidth());
                ComponentColumnBuilder columnBuilder = col.componentColumn(AppContext.getMessage(field.getDescKey()),
                        field.getComponentBuilder()).setWidth(field.getDefaultWidth());

                reportBuilder.addColumn(columnBuilder);
            }
        }
        LOG.debug("Accomplish init report");
    }

    @Override
    protected void outputReport(OutputStream outputStream) throws DRException, IOException {
        if (outputForm == ReportExportType.PDF) {
            reportBuilder.toPdf(outputStream);
        } else if (outputForm == ReportExportType.CSV) {
            JasperCsvExporterBuilder csvExporter = export.csvExporter(outputStream);
            reportBuilder.ignorePageWidth();
            reportBuilder.toCsv(csvExporter);
        } else if (outputForm == ReportExportType.EXCEL) {
            JasperXlsxExporterBuilder xlsExporter = export.xlsxExporter(outputStream)
                    .setDetectCellType(true).setIgnorePageMargins(true)
                    .setWhitePageBackground(false).setRemoveEmptySpaceBetweenColumns(true);
            reportBuilder.toXlsx(xlsExporter);
        } else {
            throw new IllegalArgumentException("Do not support output type " + outputForm);
        }
    }

    private JasperReportBuilder createReport() {
        JasperReportBuilder reportBuilder = report();
        reportBuilder.setParameters(parameters);
        if (outputForm == ReportExportType.PDF) {
            reportBuilder
                    .title(defaultTitleComponent())
                    .setPageFormat(PageType.A3, PageOrientation.LANDSCAPE)
                    .setHighlightDetailEvenRows(true)
                    .setColumnStyle(stl.style(stl.style().setLeftPadding(4)))
                    .setColumnTitleStyle(reportTemplate.getColumnTitleStyle())
                    .pageFooter(cmp.pageXofY())
                    .setLocale(locale);

        } else if (outputForm == ReportExportType.CSV) {
            reportBuilder.setIgnorePagination(true);
        } else if (outputForm == ReportExportType.EXCEL) {
            reportBuilder.title(defaultTitleComponent()).setColumnTitleStyle(reportTemplate.getColumnTitleStyle())
                    .addProperty(JasperProperty.EXPORT_XLS_FREEZE_ROW, "2").ignorePageWidth().ignorePagination();

        } else {
            throw new IllegalArgumentException("Do not support output type " + outputForm);
        }

        HorizontalListBuilder historyHeader = cmp.horizontalList().add(cmp.text(reportTitle)
                .setStyle(reportTemplate.getH2Style()));
        reportBuilder.title(historyHeader, cmp.verticalGap(10));

        return reportBuilder;
    }

    public static class AllItems<S extends SearchCriteria, T> extends SimpleReportTemplateExecutor<T> {
        private ISearchableService<S> searchService;

        public AllItems(String reportTitle, RpFieldsBuilder fieldBuilder, ReportExportType outputForm,
                        Class<T> classType, ISearchableService<S> searchService) {
            super(reportTitle, fieldBuilder, outputForm, classType);
            this.searchService = searchService;
        }

        @Override
        protected void fillReport() {
            S searchCriteria = (S) parameters.get(CRITERIA);
            reportBuilder.setDataSource(new GroupIteratorDataSource(searchService, searchCriteria));
        }
    }

    public static class ListData<T> extends SimpleReportTemplateExecutor<T> {
        private List<T> data;

        public ListData(String reportTitle, RpFieldsBuilder fieldBuilder, ReportExportType outputForm, List<T> data, Class<T> classType) {
            super(reportTitle, fieldBuilder, outputForm, classType);
            this.data = data;
        }

        @SuppressWarnings({"unchecked", "rawtypes"})
        @Override
        protected void fillReport() {
            BeanDataSource ds = new BeanDataSource(data);
            reportBuilder.setDataSource(ds);
        }
    }
}

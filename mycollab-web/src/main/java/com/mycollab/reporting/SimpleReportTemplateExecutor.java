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
package com.mycollab.reporting;

import com.mycollab.core.arguments.NotBindable;
import com.mycollab.db.arguments.SearchCriteria;
import com.mycollab.db.persistence.service.ISearchableService;
import com.mycollab.core.utils.ClassUtils;
import com.mycollab.reporting.generator.ComponentBuilderGenerator;
import com.mycollab.vaadin.AppContext;
import net.sf.dynamicreports.jasper.builder.JasperReportBuilder;
import net.sf.dynamicreports.jasper.builder.export.JasperCsvExporterBuilder;
import net.sf.dynamicreports.jasper.builder.export.JasperXlsxExporterBuilder;
import net.sf.dynamicreports.jasper.constant.JasperProperty;
import net.sf.dynamicreports.report.builder.column.ComponentColumnBuilder;
import net.sf.dynamicreports.report.builder.component.ComponentBuilder;
import net.sf.dynamicreports.report.constant.PageOrientation;
import net.sf.dynamicreports.report.constant.PageType;
import net.sf.dynamicreports.report.definition.datatype.DRIDataType;
import net.sf.dynamicreports.report.exception.DRException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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

    public SimpleReportTemplateExecutor(String reportTitle, RpFieldsBuilder fieldBuilder, ReportExportType outputForm, Class<T> classType) {
        super(AppContext.getUser(), AppContext.getUserTimeZone(), AppContext.getUserLocale(), reportTitle, outputForm);
        this.fieldBuilder = fieldBuilder;
        this.classType = classType;
    }

    @Override
    protected void initReport() throws Exception {
        reportBuilder = createReport();
        // Add field of report
        Field[] clsFields = ClassUtils.getAllFields(classType);
        for (Field objField : clsFields) {
            if ("selected".equals(objField.getName()) || "extraData".equals(objField.getName()) || objField.getAnnotation(NotBindable.class) != null) {
                continue;
            }

            DRIDataType<Object, ?> jrType = DRIDataTypeFactory.detectType(objField);
            if (jrType != null) {
                LOG.debug("  Add field: " + objField.getName());
                reportBuilder.addField(objField.getName(), jrType);
            }
        }
        List<TableViewFieldDecorator> fields = fieldBuilder.getFields();

        Map<String, ComponentBuilderGenerator> lstFieldBuilder = ColumnBuilderClassMapper.getListFieldBuilder(classType);
        if (lstFieldBuilder != null) {
            // build columns of report
            for (TableViewFieldDecorator field : fields) {
                ComponentBuilderGenerator columnFieldBuilder = lstFieldBuilder.get(field.getField());
                if (columnFieldBuilder != null) {
                    field.setComponentBuilder(reportStyles.buildCompBuilder(columnFieldBuilder));
                }

                LOG.debug("Construct component builder {} and width {}", field.getField(), field.getDefaultWidth());
                ComponentColumnBuilder columnBuilder = col.componentColumn(AppContext.getMessage(field.getDescKey()),
                        field.getComponentBuilder()).setWidth(field.getDefaultWidth());

                reportBuilder.addColumn(columnBuilder);
            }
        }
    }

    @Override
    protected void outputReport(OutputStream outputStream) throws DRException, IOException {
        if (outputForm == ReportExportType.PDF) {
            reportBuilder.toPdf(outputStream);
        } else if (outputForm == ReportExportType.CSV) {
            reportBuilder.ignorePageWidth();
            reportBuilder.ignorePagination();
            JasperCsvExporterBuilder csvExporter = export.csvExporter(outputStream);
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
                    .title(headerCreator(reportTitle), cmp.verticalGap(10))
                    .setPageFormat(PageType.A3, PageOrientation.LANDSCAPE)
                    .setHighlightDetailEvenRows(true)
                    .setColumnStyle(stl.style(stl.style().setLeftPadding(4)))
                    .setColumnTitleStyle(reportStyles.getColumnTitleStyle())
                    .pageFooter(cmp.pageXofY())
                    .setLocale(locale);
        } else if (outputForm == ReportExportType.CSV) {
            reportBuilder.title(headerCreator(reportTitle));
        } else if (outputForm == ReportExportType.EXCEL) {
            reportBuilder.title(headerCreator(reportTitle), cmp.verticalGap(10)).setColumnTitleStyle(reportStyles.getColumnTitleStyle())
                    .addProperty(JasperProperty.EXPORT_XLS_FREEZE_ROW, "2").ignorePageWidth().ignorePagination();

        } else {
            throw new IllegalArgumentException("Do not support output type " + outputForm);
        }

        return reportBuilder;
    }

    protected ComponentBuilder headerCreator(String title) {
        return cmp.horizontalList().add(cmp.text(title).setStyle(reportStyles.getH2Style()));
    }

    public static class AllItems<S extends SearchCriteria, T> extends SimpleReportTemplateExecutor<T> {
        private ISearchableService<S> searchService;

        private int totalItems;

        public AllItems(String reportTitle, RpFieldsBuilder fieldBuilder, ReportExportType outputForm,
                        Class<T> classType, ISearchableService<S> searchService) {
            super(reportTitle, fieldBuilder, outputForm, classType);
            this.searchService = searchService;
        }

        @Override
        protected ComponentBuilder headerCreator(String title) {
            S searchCriteria = (S) parameters.get(CRITERIA);
            totalItems = searchService.getTotalCount(searchCriteria);
            return super.headerCreator(title + "(" + totalItems + ")");
        }

        @Override
        protected void fillReport() {
            S searchCriteria = (S) parameters.get(CRITERIA);
            reportBuilder.setDataSource(new GroupIteratorDataSource(searchService, searchCriteria, totalItems));
        }
    }

    public static class ListData<T> extends SimpleReportTemplateExecutor<T> {
        private List<T> data;

        public ListData(String reportTitle, RpFieldsBuilder fieldBuilder, ReportExportType outputForm, List<T> data, Class<T> classType) {
            super(reportTitle, fieldBuilder, outputForm, classType);
            this.data = data;
        }

        @Override
        protected ComponentBuilder headerCreator(String title) {
            int totalItems = data.size();
            return super.headerCreator(title + "(" + totalItems + ")");
        }

        @SuppressWarnings({"unchecked", "rawtypes"})
        @Override
        protected void fillReport() {
            BeanDataSource ds = new BeanDataSource(data);
            reportBuilder.setDataSource(ds);
        }
    }
}

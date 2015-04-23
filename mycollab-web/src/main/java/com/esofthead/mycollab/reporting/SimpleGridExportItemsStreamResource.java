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
/**
 * This file is part of mycollab-web.
 * <p>
 * mycollab-web is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * <p>
 * mycollab-web is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * <p>
 * You should have received a copy of the GNU General Public License
 * along with mycollab-web.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.esofthead.mycollab.reporting;

import static net.sf.dynamicreports.report.builder.DynamicReports.col;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

import net.sf.dynamicreports.report.builder.column.ComponentColumnBuilder;
import net.sf.dynamicreports.report.definition.datatype.DRIDataType;
import net.sf.dynamicreports.report.exception.DRException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.esofthead.mycollab.core.arguments.SearchCriteria;
import com.esofthead.mycollab.core.persistence.service.ISearchableService;
import com.esofthead.mycollab.core.utils.ClassUtils;
import com.esofthead.mycollab.reporting.expression.MValue;
import com.esofthead.mycollab.vaadin.AppContext;

/**
 *
 * @author MyCollab Ltd.
 * @since 2.0
 *
 * @param <T>
 */
public abstract class SimpleGridExportItemsStreamResource<T> extends ExportItemsStreamResource {
    private static final long serialVersionUID = 1L;

    private static final Logger LOG = LoggerFactory.getLogger(SimpleGridExportItemsStreamResource.class);

    private Class<T> classType;
    private RpParameterBuilder parameters;

    SimpleGridExportItemsStreamResource(String reportTitle, RpParameterBuilder parameters,
                                        ReportExportType outputForm, Class<T> classType) {
        super(AppContext.getTimezone(), AppContext.getUserLocale(), reportTitle, outputForm);
        this.parameters = parameters;
        this.classType = classType;
    }

    @Override
    protected void initReport() throws DRException {
        LOG.debug("Init report: " + classType);
        // Add field of report
        Field[] clsFields = ClassUtils.getAllFields(classType);
        for (Field objField : clsFields) {
            if ("selected".equals(objField.getName())
                    || "extraData".equals(objField.getName())) {
                continue;
            }

            DRIDataType<Object, ? extends Object> jrType = DRIDataTypeFactory
                    .detectType(objField);
            if (jrType != null) {
                reportBuilder.addField(objField.getName(), jrType);
            }
        }
        List<TableViewFieldDecorator> fields = parameters.getFields();

        Map<String, MValue> lstFieldBuilder = ColumnBuilderClassMapper
                .getListFieldBuilder(classType);
        if (lstFieldBuilder != null) {
            // build columns of report
            for (TableViewFieldDecorator field : fields) {
                MValue columnFieldBuilder = lstFieldBuilder.get(field.getField());
                if (columnFieldBuilder != null) {
                    field.setComponentBuilder(reportTemplate
                            .buildCompBuilder(columnFieldBuilder));
                }

                LOG.debug("Construct component builder {} and width {}",
                        field.getField(), field.getDefaultWidth());
                ComponentColumnBuilder columnBuilder = col.componentColumn(
                        AppContext.getMessage(field.getDescKey()),
                        field.getComponentBuilder()).setWidth(
                        field.getDefaultWidth());

                reportBuilder.addColumn(columnBuilder);
            }
        }

        LOG.debug("Accomplish init report");
    }

    public static class AllItems<S extends SearchCriteria, T> extends SimpleGridExportItemsStreamResource<T> {
        private static final long serialVersionUID = 1L;
        private ISearchableService<S> searchService;
        private S searchCriteria;

        public AllItems(String reportTitle, RpParameterBuilder parameters, ReportExportType outputForm,
                        ISearchableService<S> searchService, S searchCriteria, Class<T> classType) {
            super(reportTitle, parameters, outputForm, classType);
            this.searchService = searchService;
            this.searchCriteria = searchCriteria;
        }

        @SuppressWarnings({"unchecked", "rawtypes"})
        @Override
        protected void fillReport() {
            reportBuilder.setDataSource(new GroupIteratorDataSource(
                    searchService, searchCriteria));
        }

    }

    public static class ListData<T> extends SimpleGridExportItemsStreamResource<T> {
        private static final long serialVersionUID = 1L;
        private List<T> data;

        public ListData(String reportTitle, RpParameterBuilder parameters,
                        ReportExportType outputForm, List<T> data, Class<T> classType) {
            super(reportTitle, parameters, outputForm, classType);
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
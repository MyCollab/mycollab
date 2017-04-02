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

import com.mycollab.core.MyCollabException;
import com.mycollab.core.arguments.NotBindable;
import com.mycollab.core.utils.ClassUtils;
import com.mycollab.i18n.LocalizationHelper;
import com.mycollab.reporting.generator.ComponentBuilderGenerator;
import net.sf.dynamicreports.jasper.builder.JasperReportBuilder;
import net.sf.dynamicreports.report.builder.column.ComponentColumnBuilder;
import net.sf.dynamicreports.report.definition.datatype.DRIDataType;
import net.sf.dynamicreports.report.exception.DRException;
import net.sf.jasperreports.engine.JRDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.OutputStream;
import java.lang.reflect.Field;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import static net.sf.dynamicreports.report.builder.DynamicReports.*;

/**
 * @author MyCollab Ltd
 * @since 5.4.7
 */
public abstract class AbstractReportBuilder<T> {
    private static Logger LOG = LoggerFactory.getLogger(AbstractReportBuilder.class);

    protected String reportTitle;
    protected JasperReportBuilder reportBuilder;
    protected Locale locale;

    public AbstractReportBuilder(String reportTitle, Locale locale, RpFieldsBuilder fieldBuilder, Class<T> classType, Map<String, Object> parameters) {
        this.reportTitle = reportTitle;
        this.locale = locale;
        reportBuilder = report();
        reportBuilder.setParameters(parameters);

        initReport();

        Field[] clsFields = ClassUtils.getAllFields(classType);
        try {
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
        } catch (DRException e) {
            throw new MyCollabException(e);
        }
        List<TableViewFieldDecorator> fields = fieldBuilder.getFields();

        Map<String, ComponentBuilderGenerator> lstFieldBuilder = ColumnBuilderClassMapper.getListFieldBuilder(classType);
        if (lstFieldBuilder != null) {
            // build columns of report
            for (TableViewFieldDecorator field : fields) {
                ComponentBuilderGenerator columnFieldBuilder = lstFieldBuilder.get(field.getField());
                if (columnFieldBuilder != null) {
                    field.setComponentBuilder(ReportStyles.instance().buildCompBuilder(columnFieldBuilder));
                }

                LOG.debug("Construct component builder {} and width {}", field.getField(), field.getDefaultWidth());
                ComponentColumnBuilder columnBuilder = col.componentColumn(LocalizationHelper.getMessage(locale, field.getDescKey()),
                        field.getComponentBuilder()).setWidth(field.getDefaultWidth());

                reportBuilder.addColumn(columnBuilder);
            }
        }
    }

    protected void setTitle(String title) {
        reportBuilder.title(cmp.horizontalList().add(cmp.text(title)
                .setStyle(ReportStyles.instance().getH2Style())));
    }

    protected abstract void initReport();

    public abstract void toStream(OutputStream outputStream) throws DRException;

    void setDataSource(BeanDataSource<T> ds) {
        reportBuilder.setDataSource(ds);
    }

    void setDataSource(JRDataSource ds) {
        reportBuilder.setDataSource(ds);
    }
}

/**
 * Copyright © MyCollab
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http:></http:>//www.gnu.org/licenses/>.
 */
package com.mycollab.reporting

import com.mycollab.core.MyCollabException
import com.mycollab.core.arguments.NotBindable
import com.mycollab.core.utils.ClassUtils
import com.mycollab.i18n.LocalizationHelper
import net.sf.dynamicreports.jasper.builder.JasperReportBuilder
import net.sf.dynamicreports.report.builder.DynamicReports.*
import net.sf.dynamicreports.report.definition.datatype.DRIDataType
import net.sf.dynamicreports.report.exception.DRException
import net.sf.jasperreports.engine.JRDataSource
import org.slf4j.LoggerFactory
import java.io.OutputStream
import java.util.*

/**
 * @author MyCollab Ltd
 * @since 5.4.7
 */
abstract class AbstractReportBuilder(protected var locale: Locale, fieldBuilder: RpFieldsBuilder, classType: Class<*>, parameters: Map<String, Any>) {

    var reportBuilder: JasperReportBuilder = report()

    init {
        reportBuilder.setParameters(parameters)

        initReport()

        val clsFields = ClassUtils.getAllFields(classType)
        try {
            for (objField in clsFields) {
                if ("selected" == objField.name || "extraData" == objField.name || objField.getAnnotation(NotBindable::class.java) != null) {
                    continue
                }

                val jrType = DRIDataTypeFactory.detectType<DRIDataType<Any, *>>(objField)
                if (jrType != null) {
                    LOG.debug("  Add field: " + objField.name)
                    reportBuilder.addField(objField.name, jrType)
                }
            }
        } catch (e: DRException) {
            throw MyCollabException(e)
        }

        val fields = fieldBuilder.fields

        val lstFieldBuilder = ColumnBuilderClassMapper.getListFieldBuilder(classType)
        if (lstFieldBuilder != null) {
            // build columns of report
            for (field in fields) {
                val columnFieldBuilder = lstFieldBuilder[field.field]
                if (columnFieldBuilder != null) {
                    field.componentBuilder = ReportStyles.instance().buildCompBuilder(columnFieldBuilder)
                }

                LOG.debug("Construct component builder {} and width {}", field.field, field.defaultWidth)
                val columnBuilder = col.componentColumn(LocalizationHelper.getMessage(locale, field.descKey),
                        field.componentBuilder).setWidth(field.defaultWidth)

                reportBuilder.addColumn(columnBuilder)
            }
        }
    }

    open fun setTitle(title: String) {
        reportBuilder.title(cmp.horizontalList().add(cmp.text(title).setStyle(ReportStyles.instance().h2Style)))
    }

    protected abstract fun initReport()

    @Throws(DRException::class)
    abstract fun toStream(outputStream: OutputStream)

    fun setDataSource(ds: BeanDataSource<*>) {
        reportBuilder.dataSource = ds
    }

    fun setDataSource(ds: JRDataSource) {
        reportBuilder.dataSource = ds
    }

    companion object {
        private val LOG = LoggerFactory.getLogger(AbstractReportBuilder::class.java)
    }
}

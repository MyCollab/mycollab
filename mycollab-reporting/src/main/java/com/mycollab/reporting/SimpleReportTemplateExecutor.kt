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

import com.mycollab.db.arguments.SearchCriteria
import com.mycollab.db.persistence.service.ISearchableService
import net.sf.dynamicreports.report.exception.DRException
import org.slf4j.LoggerFactory
import java.io.IOException
import java.io.OutputStream
import java.util.*

/**
 * @author MyCollab Ltd
 * @since 5.1.4
 */
abstract class SimpleReportTemplateExecutor<T>(timeZone: TimeZone, locale: Locale, reportTitle: String, protected var fieldBuilder: RpFieldsBuilder, outputForm: ReportExportType, protected var classType: Class<T>) : ReportTemplateExecutor(timeZone, locale, reportTitle, outputForm) {

    protected lateinit var reportBuilder: AbstractReportBuilder

    @Throws(Exception::class)
    override fun initReport() {
        LOG.info("Export document: $outputForm")
        reportBuilder = when {
            outputForm === ReportExportType.PDF -> PdfReportBuilder(locale, fieldBuilder, classType, parameters)
            outputForm === ReportExportType.CSV -> CsvReportBuilder(locale, fieldBuilder, classType, parameters)
            outputForm === ReportExportType.EXCEL -> XlsReportBuilder(locale, fieldBuilder, classType, parameters)
            else -> throw IllegalArgumentException("Do not support output type $outputForm")
        }
    }

    @Throws(DRException::class, IOException::class)
    override fun outputReport(outputStream: OutputStream) {
        reportBuilder.toStream(outputStream)
    }

    class AllItems<S : SearchCriteria, T>(timeZone: TimeZone, locale: Locale, reportTitle: String, fieldBuilder: RpFieldsBuilder, outputForm: ReportExportType,
                                          classType: Class<T>, private val searchService: ISearchableService<S>) : SimpleReportTemplateExecutor<T>(timeZone, locale, reportTitle, fieldBuilder, outputForm, classType) {

        private var totalItems: Int = 0

        override fun fillReport() {
            val searchCriteria = parameters[CRITERIA] as S
            totalItems = searchService.getTotalCount(searchCriteria)
            reportBuilder.setTitle("$reportTitle($totalItems)")
            reportBuilder.setDataSource(GroupIteratorDataSource(searchService, searchCriteria, totalItems))
            LOG.info("Fill report $totalItems items and criteria $searchCriteria")
        }
    }

    class ListData<T>(timeZone: TimeZone, locale: Locale, reportTitle: String, fieldBuilder: RpFieldsBuilder, outputForm: ReportExportType, private val data: List<T>, classType: Class<T>) : SimpleReportTemplateExecutor<T>(timeZone, locale, reportTitle, fieldBuilder, outputForm, classType) {

        override fun fillReport() {
            val ds = BeanDataSource(data)
            val totalItems = data.size
            reportBuilder.setTitle("$reportTitle($totalItems)")
            reportBuilder.setDataSource(ds)
            LOG.info(String.format("Fill report %d items", totalItems))
        }
    }

    companion object {
        @JvmField
        val CRITERIA = "criteria"
        private val LOG = LoggerFactory.getLogger(SimpleReportTemplateExecutor::class.java)
    }
}

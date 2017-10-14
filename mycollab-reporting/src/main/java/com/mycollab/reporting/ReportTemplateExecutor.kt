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
import com.mycollab.core.utils.DateTimeUtils
import net.sf.dynamicreports.report.builder.DynamicReports.cmp
import net.sf.dynamicreports.report.builder.DynamicReports.hyperLink
import net.sf.dynamicreports.report.builder.component.ComponentBuilder
import net.sf.dynamicreports.report.constant.HorizontalTextAlignment
import net.sf.dynamicreports.report.exception.DRException
import org.slf4j.LoggerFactory
import java.io.*
import java.util.*
import java.util.concurrent.CountDownLatch

/**
 * @author MyCollab Ltd
 * @since 5.1.4
 */
abstract class ReportTemplateExecutor(private var timeZone: TimeZone, protected var locale: Locale,
                                      protected var reportTitle: String,
                                      protected var outputForm: ReportExportType) {

    lateinit var parameters: MutableMap<String, Any>
    protected var reportStyles: ReportStyles = ReportStyles.instance()

    val defaultExportFileName: String
        get() = outputForm.defaultFileName

    @Throws(Exception::class)
    abstract fun initReport()

    @Throws(DRException::class)
    abstract fun fillReport()

    @Throws(IOException::class, DRException::class)
    abstract fun outputReport(outputStream: OutputStream)

    protected fun defaultTitleComponent(): ComponentBuilder<*, *> {
        val link = hyperLink("https://www.mycollab.com")
        val dynamicReportsComponent = cmp.horizontalList(
                cmp.image(ReportTemplateExecutor::class.java.classLoader.getResourceAsStream("images/logo.png"))
                        .setFixedDimension(150, 28), cmp.horizontalGap(20),
                cmp.verticalList(
                        cmp.text("https://www.mycollab.com").setStyle(reportStyles.italicStyle).setHyperLink(link)
                                .setHorizontalTextAlignment(HorizontalTextAlignment.RIGHT), cmp.text(String.format("Generated at: %s",
                        DateTimeUtils.formatDate(GregorianCalendar().time, "yyyy-MM-dd'T'HH:mm:ss",
                                Locale.US, timeZone)))
                        .setHorizontalTextAlignment(HorizontalTextAlignment.RIGHT))
        )

        return cmp.horizontalList().add(dynamicReportsComponent).newRow().add(reportStyles.line()).newRow().add(cmp.verticalGap(10))
    }

    fun exportStream(): InputStream {
        val latch = CountDownLatch(1)
        val inStream = PipedInputStream()

        val `in` = object : InputStream() {

            @Throws(IOException::class)
            override fun read(b: ByteArray): Int {
                return inStream.read(b)
            }

            @Throws(IOException::class)
            override fun read(): Int {
                return inStream.read()
            }

            @Throws(IOException::class)
            override fun close() {
                super.close()
                latch.countDown()
            }
        }

        val outputStream = PipedOutputStream()
        Thread(Runnable {
            val out = object : OutputStream() {
                @Throws(IOException::class)
                override fun write(b: Int) {
                    outputStream.write(b)
                }

                @Throws(IOException::class)
                override fun close() {
                    while (latch.count != 0L) {
                        try {
                            latch.await()
                        } catch (e: InterruptedException) {
                            e.printStackTrace()
                            break
                        }

                    }
                    super.close()
                }
            }

            try {
                parameters = parameters
                initReport()
                fillReport()
                outputReport(out)
            } catch (e: Exception) {
                LOG.error("Error report", e)
            } finally {
                try {
                    outputStream.close()
                    out.close()
                } catch (e: IOException) {
                    LOG.error("Try to close reporting stream error", e)
                }

            }
        }).start()
        try {
            outputStream.connect(inStream)
        } catch (e: IOException) {
            throw MyCollabException(e)
        }

        return `in`
    }

    companion object {
        private val LOG = LoggerFactory.getLogger(ReportTemplateExecutor::class.java)
    }
}

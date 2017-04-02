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
import com.mycollab.core.utils.DateTimeUtils;
import net.sf.dynamicreports.report.builder.HyperLinkBuilder;
import net.sf.dynamicreports.report.builder.component.ComponentBuilder;
import net.sf.dynamicreports.report.constant.HorizontalTextAlignment;
import net.sf.dynamicreports.report.exception.DRException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;
import java.util.concurrent.CountDownLatch;

import static net.sf.dynamicreports.report.builder.DynamicReports.cmp;
import static net.sf.dynamicreports.report.builder.DynamicReports.hyperLink;

/**
 * @author MyCollab Ltd
 * @since 5.1.4
 */
public abstract class ReportTemplateExecutor {
    private static Logger LOG = LoggerFactory.getLogger(ReportTemplateExecutor.class);

    protected Map<String, Object> parameters;
    protected ReportStyles reportStyles;
    protected String reportTitle;
    protected ReportExportType outputForm;
    protected Locale locale;
    protected TimeZone timeZone;

    public ReportTemplateExecutor(TimeZone timezone, Locale languageSupport, String reportTitle,
                                  ReportExportType outputForm) {
        this.locale = languageSupport;
        this.timeZone = timezone;
        this.reportStyles = ReportStyles.instance();
        this.reportTitle = reportTitle;
        this.outputForm = outputForm;
    }

    public void setParameters(Map<String, Object> parameters) {
        this.parameters = parameters;
    }

    public String getDefaultExportFileName() {
        return outputForm.getDefaultFileName();
    }

    public Map<String, Object> getParameters() {
        return parameters;
    }

    abstract public void initReport() throws Exception;

    abstract public void fillReport() throws DRException;

    abstract public void outputReport(OutputStream outputStream) throws IOException, DRException;

    protected ComponentBuilder<?, ?> defaultTitleComponent() {
        HyperLinkBuilder link = hyperLink("https://www.mycollab.com");
        ComponentBuilder<?, ?> dynamicReportsComponent = cmp.horizontalList(
                cmp.image(ReportTemplateExecutor.class.getClassLoader().getResourceAsStream("images/logo.png"))
                        .setFixedDimension(150, 28), cmp.horizontalGap(20),
                cmp.verticalList(
                        cmp.text("https://www.mycollab.com").setStyle(reportStyles.getItalicStyle()).setHyperLink(link)
                                .setHorizontalTextAlignment(HorizontalTextAlignment.RIGHT), cmp.text(String.format("Generated at: %s",
                                DateTimeUtils.formatDate(new GregorianCalendar().getTime(), "yyyy-MM-dd'T'HH:mm:ss",
                                        Locale.US, timeZone)))
                                .setHorizontalTextAlignment(HorizontalTextAlignment.RIGHT))
        );

        return cmp.horizontalList().add(dynamicReportsComponent).newRow().add(reportStyles.line()).newRow().add(cmp.verticalGap(10));
    }

    public InputStream exportStream() {
        final CountDownLatch latch = new CountDownLatch(1);
        final PipedInputStream inStream = new PipedInputStream();

        InputStream in = new InputStream() {

            @Override
            public int read(byte[] b) throws IOException {
                return inStream.read(b);
            }

            @Override
            public int read() throws IOException {
                return inStream.read();
            }

            @Override
            public void close() throws IOException {
                super.close();
                latch.countDown();
            }
        };

        final PipedOutputStream outputStream = new PipedOutputStream();
        new Thread(new Runnable() {
            @Override
            public void run() {
                final OutputStream out = new OutputStream() {
                    @Override
                    public void write(int b) throws IOException {
                        outputStream.write(b);
                    }

                    @Override
                    public void close() throws IOException {
                        while (latch.getCount() != 0) {
                            try {
                                latch.await();
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                                break;
                            }
                        }
                        super.close();
                    }
                };

                try {
                    setParameters(parameters);
                    initReport();
                    fillReport();
                    outputReport(out);
                } catch (Exception e) {
                    LOG.error("Error report", e);
                } finally {
                    try {
                        outputStream.close();
                        out.close();
                    } catch (IOException e) {
                        LOG.error("Try to close reporting stream error", e);
                    }
                }
            }
        }).start();
        try {
            outputStream.connect(inStream);
        } catch (IOException e) {
            throw new MyCollabException(e);
        }
        return in;
    }
}

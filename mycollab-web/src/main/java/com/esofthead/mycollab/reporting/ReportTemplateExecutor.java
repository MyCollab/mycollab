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

import com.esofthead.mycollab.core.utils.DateTimeUtils;
import net.sf.dynamicreports.report.builder.HyperLinkBuilder;
import net.sf.dynamicreports.report.builder.component.ComponentBuilder;
import net.sf.dynamicreports.report.constant.HorizontalTextAlignment;
import net.sf.dynamicreports.report.exception.DRException;

import java.io.IOException;
import java.io.OutputStream;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

import static net.sf.dynamicreports.report.builder.DynamicReports.cmp;
import static net.sf.dynamicreports.report.builder.DynamicReports.hyperLink;

/**
 * @author MyCollab Ltd
 * @since 5.1.4
 */
public abstract class ReportTemplateExecutor {
    protected Map<String, Object> parameters;
    protected AbstractReportTemplate reportTemplate;
    protected String reportTitle;
    protected ReportExportType outputForm;
    protected Locale locale;
    protected TimeZone timeZone;

    public ReportTemplateExecutor(TimeZone timezone, Locale languageSupport, String reportTitle, ReportExportType outputForm) {
        this.locale = languageSupport;
        this.timeZone = timezone;
        this.reportTemplate = ReportTemplateFactory.getTemplate(languageSupport);
        this.reportTitle = reportTitle;
        this.outputForm = outputForm;
    }

    void setParameters(Map<String, Object> parameters) {
        this.parameters = parameters;
    }

    public Map<String, Object> getParameters() {
        return parameters;
    }

    abstract protected void initReport() throws Exception;

    abstract protected void fillReport() throws DRException;

    abstract protected void outputReport(OutputStream outputStream) throws IOException, DRException;

    protected ComponentBuilder<?, ?> defaultTitleComponent() {
        HyperLinkBuilder link = hyperLink("https://www.mycollab.com");
        ComponentBuilder<?, ?> dynamicReportsComponent = cmp.horizontalList(
                cmp.image(ReportTemplateFactory.class.getClassLoader().getResourceAsStream("images/logo.png"))
                        .setFixedDimension(150, 28), cmp.horizontalGap(20),
                cmp.verticalList(
                        cmp.text("https://www.mycollab.com").setStyle(reportTemplate.getItalicStyle())
                                .setHyperLink(link).setHorizontalTextAlignment(HorizontalTextAlignment.RIGHT), cmp.text(String.format("Generated at: %s",
                                DateTimeUtils.formatDate(new GregorianCalendar().getTime(), "yyyy-MM-dd'T'HH:mm:ss",
                                        timeZone))).setHorizontalTextAlignment(HorizontalTextAlignment.RIGHT))
        );

        return cmp.horizontalList().add(dynamicReportsComponent).newRow().add(reportTemplate.line()).newRow().add(cmp
                .verticalGap(10));
    }
}

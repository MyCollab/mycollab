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
package com.esofthead.mycollab.reporting;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
public class ReportTemplateFactory {
    private static String baseCls = "com.esofthead.mycollab.reporting.ReportTemplate_%s";

    private static final AbstractReportTemplate enReport = new ReportTemplate_en_US();

    private static final Map<Locale, AbstractReportTemplate> reportMap;

    static {
        reportMap = new HashMap<>();
        reportMap.put(Locale.US, enReport);
    }

    @SuppressWarnings("unchecked")
    public static AbstractReportTemplate getTemplate(Locale language) {
        AbstractReportTemplate reportTemplate = reportMap.get(language);
        if (reportTemplate != null) {
            return reportTemplate;
        } else {
            String intendedCls = String.format(baseCls, language.toString());
            try {
                Class<AbstractReportTemplate> cls = (Class<AbstractReportTemplate>) Class.forName(intendedCls);
                reportTemplate = cls.newInstance();
                reportMap.put(language, reportTemplate);
                return reportTemplate;
            } catch (Exception e) {
                return enReport;
            }
        }
    }
}

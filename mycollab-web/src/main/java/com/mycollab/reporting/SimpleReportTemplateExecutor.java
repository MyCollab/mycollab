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

import com.mycollab.db.arguments.SearchCriteria;
import com.mycollab.db.persistence.service.ISearchableService;
import com.mycollab.vaadin.UserUIContext;

import net.sf.dynamicreports.report.exception.DRException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

/**
 * @author MyCollab Ltd
 * @since 5.1.4
 */
public abstract class SimpleReportTemplateExecutor<T> extends ReportTemplateExecutor {
    public static final String CRITERIA = "criteria";
    private static Logger LOG = LoggerFactory.getLogger(SimpleReportTemplateExecutor.class);

    protected AbstractReportBuilder reportBuilder;
    protected Class<T> classType;
    protected RpFieldsBuilder fieldBuilder;

    public SimpleReportTemplateExecutor(String reportTitle, RpFieldsBuilder fieldBuilder, ReportExportType outputForm, Class<T> classType) {
        super(UserUIContext.getUser(), UserUIContext.getUserTimeZone(), UserUIContext.getUserLocale(), reportTitle, outputForm);
        this.fieldBuilder = fieldBuilder;
        this.classType = classType;
    }

    @Override
    protected void initReport() throws Exception {
        LOG.info("Export document: " + outputForm);
        if (outputForm == ReportExportType.PDF) {
            reportBuilder = new PdfReportBuilder(reportTitle, fieldBuilder, classType, parameters);
        } else if (outputForm == ReportExportType.CSV) {
            reportBuilder = new CsvReportBuilder(reportTitle, fieldBuilder, classType, parameters);
        } else if (outputForm == ReportExportType.EXCEL) {
            reportBuilder = new XlsReportBuilder(reportTitle, fieldBuilder, classType, parameters);
        } else {
            throw new IllegalArgumentException("Do not support output type " + outputForm);
        }
    }

    @Override
    protected void outputReport(OutputStream outputStream) throws DRException, IOException {
        reportBuilder.toStream(outputStream);
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
        protected void fillReport() {
            S searchCriteria = (S) parameters.get(CRITERIA);
            totalItems = searchService.getTotalCount(searchCriteria);
            reportBuilder.setTitle(reportTitle + "(" + totalItems + ")");
            reportBuilder.setDataSource(new GroupIteratorDataSource(searchService, searchCriteria, totalItems));
            LOG.info(String.format("Fill report %d items", totalItems));
        }
    }

    public static class ListData<T> extends SimpleReportTemplateExecutor<T> {
        private List<T> data;

        public ListData(String reportTitle, RpFieldsBuilder fieldBuilder, ReportExportType outputForm, List<T> data, Class<T> classType) {
            super(reportTitle, fieldBuilder, outputForm, classType);
            this.data = data;
        }

        @Override
        protected void fillReport() {
            BeanDataSource ds = new BeanDataSource(data);
            int totalItems = data.size();
            reportBuilder.setTitle(reportTitle + "(" + totalItems + ")");
            reportBuilder.setDataSource(ds);
            LOG.info(String.format("Fill report %d items", totalItems));
        }
    }
}

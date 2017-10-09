package com.mycollab.reporting;

import com.mycollab.core.utils.BeanUtility;
import com.mycollab.db.arguments.SearchCriteria;
import com.mycollab.db.persistence.service.ISearchableService;
import net.sf.dynamicreports.report.exception.DRException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

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

    public SimpleReportTemplateExecutor(TimeZone timeZone, Locale locale, String reportTitle, RpFieldsBuilder fieldBuilder, ReportExportType outputForm, Class<T> classType) {
        super(timeZone, locale, reportTitle, outputForm);
        this.fieldBuilder = fieldBuilder;
        this.classType = classType;
    }

    @Override
    public void initReport() throws Exception {
        LOG.info("Export document: " + outputForm);
        if (outputForm == ReportExportType.PDF) {
            reportBuilder = new PdfReportBuilder(reportTitle, locale, fieldBuilder, classType, parameters);
        } else if (outputForm == ReportExportType.CSV) {
            reportBuilder = new CsvReportBuilder(reportTitle, locale, fieldBuilder, classType, parameters);
        } else if (outputForm == ReportExportType.EXCEL) {
            reportBuilder = new XlsReportBuilder(reportTitle, locale, fieldBuilder, classType, parameters);
        } else {
            throw new IllegalArgumentException("Do not support output type " + outputForm);
        }
    }

    @Override
    public void outputReport(OutputStream outputStream) throws DRException, IOException {
        reportBuilder.toStream(outputStream);
    }

    public static class AllItems<S extends SearchCriteria, T> extends SimpleReportTemplateExecutor<T> {
        private ISearchableService<S> searchService;

        private int totalItems;

        public AllItems(TimeZone timeZone, Locale locale, String reportTitle, RpFieldsBuilder fieldBuilder, ReportExportType outputForm,
                        Class<T> classType, ISearchableService<S> searchService) {
            super(timeZone, locale, reportTitle, fieldBuilder, outputForm, classType);
            this.searchService = searchService;
        }

        @Override
        public void fillReport() {
            S searchCriteria = (S) parameters.get(CRITERIA);
            totalItems = searchService.getTotalCount(searchCriteria);
            reportBuilder.setTitle(reportTitle + "(" + totalItems + ")");
            reportBuilder.setDataSource(new GroupIteratorDataSource(searchService, searchCriteria, totalItems));
            LOG.info(String.format("Fill report %d items and criteria %s", totalItems, BeanUtility.printBeanObj(searchCriteria)));
        }
    }

    public static class ListData<T> extends SimpleReportTemplateExecutor<T> {
        private List<T> data;

        public ListData(TimeZone timeZone, Locale locale, String reportTitle, RpFieldsBuilder fieldBuilder, ReportExportType outputForm, List<T> data, Class<T> classType) {
            super(timeZone, locale, reportTitle, fieldBuilder, outputForm, classType);
            this.data = data;
        }

        @Override
        public void fillReport() {
            BeanDataSource ds = new BeanDataSource(data);
            int totalItems = data.size();
            reportBuilder.setTitle(reportTitle + "(" + totalItems + ")");
            reportBuilder.setDataSource(ds);
            LOG.info(String.format("Fill report %d items", totalItems));
        }
    }
}

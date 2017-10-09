package com.mycollab.reporting;

import com.mycollab.db.arguments.BasicSearchRequest;
import com.mycollab.db.arguments.SearchCriteria;
import com.mycollab.db.persistence.service.ISearchableService;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRField;
import org.apache.commons.beanutils.PropertyUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
public class GroupIteratorDataSource<T, SearchService extends ISearchableService<S>, S extends SearchCriteria> implements JRDataSource {
    private static final int ITEMS_PER_PAGE = 20;
    private static final Logger LOG = LoggerFactory.getLogger(GroupIteratorDataSource.class);

    private int currentIndex = 0;
    private int currentPage = 0;

    private SearchService searchService;
    private S searchCriteria;

    private List<?> currentData;
    private Object currentItem;

    private int totalItems;

    public GroupIteratorDataSource(SearchService searchService, S searchCriteria, int totalItems) {
        this.searchService = searchService;
        this.searchCriteria = searchCriteria;

        this.totalItems = totalItems;
        BasicSearchRequest<S> searchRequest = new BasicSearchRequest<>(searchCriteria, currentPage, ITEMS_PER_PAGE);
        currentData = searchService.findPageableListByCriteria(searchRequest);
    }

    @Override
    public boolean next() throws JRException {
        boolean result = (currentIndex < totalItems);
        if (result) {
            if (currentIndex == (currentPage + 1) * ITEMS_PER_PAGE) {
                currentPage = currentPage + 1;
                BasicSearchRequest<S> searchRequest = new BasicSearchRequest<>(searchCriteria, currentPage, ITEMS_PER_PAGE);
                currentData = searchService.findPageableListByCriteria(searchRequest);
                LOG.debug("Current data {}", currentData.size());
            }

            LOG.debug("Current index {} - {} - {} - {}", currentIndex, currentPage, currentData.size(), totalItems);
            if (currentIndex % ITEMS_PER_PAGE < currentData.size()) {
                currentItem = currentData.get(currentIndex % ITEMS_PER_PAGE);
            }

            currentIndex = currentIndex + 1;
        }

        return result;
    }

    @Override
    public Object getFieldValue(JRField jrField) throws JRException {
        try {
            String fieldName = jrField.getName();
            return PropertyUtils.getProperty(currentItem, fieldName);
        } catch (Exception e) {
            throw new JRException(e);
        }
    }
}

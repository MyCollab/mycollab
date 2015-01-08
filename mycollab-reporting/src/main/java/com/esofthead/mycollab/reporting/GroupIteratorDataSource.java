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

import java.util.List;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRField;

import org.apache.commons.beanutils.PropertyUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.esofthead.mycollab.core.arguments.SearchCriteria;
import com.esofthead.mycollab.core.arguments.SearchRequest;
import com.esofthead.mycollab.core.persistence.service.ISearchableService;

/**
 * 
 * @author MyCollab Ltd.
 * @since 1.0
 * 
 */
public class GroupIteratorDataSource<SearchService extends ISearchableService<S>, S extends SearchCriteria>
		implements JRDataSource {
	private static final int ITEMS_PER_PAGE = 20;
	private static final Logger LOG = LoggerFactory
			.getLogger(GroupIteratorDataSource.class);

	private int currentIndex = 0;
	private int currentPage = 0;

	private SearchService searchService;
	private S searchCriteria;

	private List<?> currentData;
	private Object currentItem;

	private int totalItems;

	public GroupIteratorDataSource(SearchService searchService, S searchCriteria) {
		this.searchService = searchService;
		this.searchCriteria = searchCriteria;

		totalItems = searchService.getTotalCount(searchCriteria);
		SearchRequest<S> searchRequest = new SearchRequest<S>(searchCriteria,
				currentPage, ITEMS_PER_PAGE);
		currentData = searchService.findPagableListByCriteria(searchRequest);
	}

	@Override
	public boolean next() throws JRException {
		boolean result = (currentIndex < totalItems);
		if (result) {
			if (currentIndex == (currentPage + 1) * ITEMS_PER_PAGE) {
				currentPage = currentPage + 1;
				SearchRequest<S> searchRequest = new SearchRequest<S>(
						searchCriteria, currentPage, ITEMS_PER_PAGE);
				currentData = searchService
						.findPagableListByCriteria(searchRequest);
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

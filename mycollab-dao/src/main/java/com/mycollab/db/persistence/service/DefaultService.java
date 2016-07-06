/**
 * This file is part of mycollab-dao.
 *
 * mycollab-dao is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * mycollab-dao is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with mycollab-dao.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.mycollab.db.persistence.service;

import com.mycollab.db.arguments.SearchCriteria;
import com.mycollab.db.arguments.SearchField;
import com.mycollab.db.arguments.BasicSearchRequest;
import com.mycollab.db.persistence.IMassUpdateDAO;
import com.mycollab.db.persistence.ISearchableDAO;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.ibatis.session.RowBounds;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.beans.PropertyDescriptor;
import java.io.Serializable;
import java.util.List;

/**
 * @param <K>
 * @param <T>
 * @param <S>
 * @author MyCollab Ltd.
 * @since 1.0
 */
public abstract class DefaultService<K extends Serializable, T, S extends SearchCriteria>
        extends DefaultCrudService<K, T> implements IDefaultService<K, T, S> {
    private static final Logger LOG = LoggerFactory.getLogger(DefaultService.class);

    public abstract ISearchableDAO<S> getSearchMapper();

    @Override
    public Integer getTotalCount(S criteria) {
        return getSearchMapper().getTotalCount(criteria);
    }

    @Override
    public List findPagableListByCriteria(BasicSearchRequest<S> searchRequest) {
        return getSearchMapper().findPagableListByCriteria(searchRequest.getSearchCriteria(),
                new RowBounds((searchRequest.getCurrentPage() - 1) * searchRequest.getNumberOfItems(),
                        searchRequest.getNumberOfItems()));
    }

    @Override
    public List findAbsoluteListByCriteria(S searchCriteria, Integer firstIndex, Integer numberOfItems) {
        return getSearchMapper().findPagableListByCriteria(searchCriteria,
                new RowBounds(firstIndex, numberOfItems));
    }

    @Override
    public void removeByCriteria(S criteria, Integer accountId) {
        boolean isValid = false;
        try {
            PropertyDescriptor[] propertyDescriptors = PropertyUtils.getPropertyDescriptors(criteria);

            for (PropertyDescriptor descriptor : propertyDescriptors) {
                String propName = descriptor.getName();
                if ((descriptor.getPropertyType().getGenericSuperclass() == SearchField.class)
                        && (PropertyUtils.getProperty(criteria, propName) != null)) {
                    isValid = true;
                    break;
                }

            }
        } catch (Exception e) {
            LOG.debug("Error while validating criteria", e);
        }
        if (isValid) {
            getSearchMapper().removeByCriteria(criteria);
        }

    }

    @Override
    public Integer getNextItemKey(S criteria) {
        return getSearchMapper().getNextItemKey(criteria);
    }

    @Override
    public Integer getPreviousItemKey(S criteria) {
        return getSearchMapper().getPreviousItemKey(criteria);
    }

    @Override
    public void updateBySearchCriteria(T record, S searchCriteria) {
        ISearchableDAO<S> searchMapper = getSearchMapper();
        if (searchMapper != null && searchMapper instanceof IMassUpdateDAO) {
            ((IMassUpdateDAO) searchMapper).updateBySearchCriteria(record, searchCriteria);
        }
    }
}
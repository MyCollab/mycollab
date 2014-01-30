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
/**
 * This file is part of mycollab-core.
 *
 * mycollab-core is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * mycollab-core is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with mycollab-core.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.esofthead.mycollab.core.persistence.service;

import java.beans.PropertyDescriptor;
import java.io.Serializable;
import java.util.List;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.ibatis.session.RowBounds;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.esofthead.mycollab.core.arguments.SearchCriteria;
import com.esofthead.mycollab.core.arguments.SearchField;
import com.esofthead.mycollab.core.arguments.SearchRequest;
import com.esofthead.mycollab.core.persistence.ICrudGenericDAO;
import com.esofthead.mycollab.core.persistence.IMassUpdateDAO;
import com.esofthead.mycollab.core.persistence.ISearchableDAO;

public abstract class DefaultService<K extends Serializable, T, S extends SearchCriteria>
		implements IDefaultService<K, T, S> {

	private static Logger log = LoggerFactory.getLogger(DefaultService.class);

	public abstract ICrudGenericDAO getCrudMapper();

	public abstract ISearchableDAO<S> getSearchMapper();

	@Override
	public T findByPrimaryKey(K primaryKey, int accountId) {
		return (T) getCrudMapper().selectByPrimaryKey(primaryKey);
	}

	@Override
	public int saveWithSession(T record, String username) {
		ICrudGenericDAO<K, T> crudMapper = getCrudMapper();
		Class<? extends ICrudGenericDAO> crudMapperClass = crudMapper
				.getClass();

		getCrudMapper().insertAndReturnKey(record);
		try {
			return (Integer) PropertyUtils.getProperty(record, "id");
		} catch (Exception e) {
			return 1;
		}
	}

	@Override
	public int updateWithSession(T record, String username) {
		if (username == null) {
			return getCrudMapper().updateByPrimaryKeySelective(record);
		} else {
			return internalUpdateWithSession(record, username);
		}
	}

	protected int internalUpdateWithSession(T record, String username) {
		return getCrudMapper().updateByPrimaryKeySelective(record);
	}

	@Override
	public int removeWithSession(K primaryKey, String username, int accountId) {
		if (username == null) {
			return getCrudMapper().deleteByPrimaryKey(primaryKey);
		} else {
			return internalRemoveWithSession(primaryKey, username);
		}
	}

	protected int internalRemoveWithSession(K primaryKey, String username) {
		return getCrudMapper().deleteByPrimaryKey(primaryKey);
	}

	@Override
	public int getTotalCount(S criteria) {
		return getSearchMapper().getTotalCount(criteria);
	}

	@Override
	public List findPagableListByCriteria(SearchRequest<S> searchRequest) {
		return getSearchMapper().findPagableListByCriteria(
				searchRequest.getSearchCriteria(),
				new RowBounds((searchRequest.getCurrentPage() - 1)
						* searchRequest.getNumberOfItems(), searchRequest
						.getNumberOfItems()));
	}

	@Override
	public void removeByCriteria(S criteria, int accountId) {
		boolean isValid = false;
		try {
			PropertyDescriptor[] propertyDescriptors = PropertyUtils
					.getPropertyDescriptors(criteria);

			for (PropertyDescriptor descriptor : propertyDescriptors) {
				String propName = descriptor.getName();
				if ((descriptor.getPropertyType().getGenericSuperclass() == SearchField.class)
						&& (PropertyUtils.getProperty(criteria, propName) != null)) {
					isValid = true;
					break;
				}

			}
		} catch (Exception e) {
			log.debug("Error while validating criteria", e);
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
	public void massRemoveWithSession(List<K> primaryKeys, String username,
			int accountId) {
		getCrudMapper().removeKeysWithSession(primaryKeys);
	}

	@Override
	public void massUpdateWithSession(T record, List<K> primaryKeys,
			int accountId) {
		getCrudMapper().massUpdateWithSession(record, primaryKeys);
	}

	@Override
	public void updateBySearchCriteria(T record, S searchCriteria) {
		ISearchableDAO<S> searchMapper = getSearchMapper();
		if (searchMapper != null && searchMapper instanceof IMassUpdateDAO) {
			((IMassUpdateDAO) searchMapper).updateBySearchCriteria(record,
					searchCriteria);
		}
	}

}

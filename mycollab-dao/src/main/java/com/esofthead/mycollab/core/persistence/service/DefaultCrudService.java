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
package com.esofthead.mycollab.core.persistence.service;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.GregorianCalendar;
import java.util.List;

import org.apache.commons.beanutils.PropertyUtils;

import com.esofthead.mycollab.core.MyCollabException;
import com.esofthead.mycollab.core.cache.CacheKey;
import com.esofthead.mycollab.core.persistence.ICrudGenericDAO;

/**
 * The generic class that serves the basic operations in data access layer:
 * Create, Retrieve, Update and Delete.
 * 
 * @author MyCollab Ltd.
 * @since 1.0
 * 
 * @param <T>
 * @param <K>
 */
public abstract class DefaultCrudService<K extends Serializable, T> implements
		ICrudService<K, T> {

	public abstract ICrudGenericDAO<K, T> getCrudMapper();

	private Method cacheUpdateMethod;

	@Override
	public T findByPrimaryKey(K primaryKey, int accountId) {
		return (T) getCrudMapper().selectByPrimaryKey(primaryKey);
	}

	@Override
	public int saveWithSession(T record, String username) {
		if (username != null && !username.trim().equals("")) {
			try {
				PropertyUtils.setProperty(record, "createduser", username);
			} catch (Exception e) {

			}
		}

		try {
			PropertyUtils.setProperty(record, "createdtime",
					new GregorianCalendar().getTime());
			PropertyUtils.setProperty(record, "lastupdatedtime",
					new GregorianCalendar().getTime());
		} catch (Exception e) {
		}

		getCrudMapper().insertAndReturnKey(record);
		try {
			return (Integer) PropertyUtils.getProperty(record, "id");
		} catch (Exception e) {
			return 0;
		}
	}

	@Override
	public int updateWithSession(T record, String username) {
		try {
			PropertyUtils.setProperty(record, "lastupdatedtime",
					new GregorianCalendar().getTime());
		} catch (Exception e) {
		}

		if (cacheUpdateMethod == null) {
			findCacheUpdateMethod();
		}
		try {
			cacheUpdateMethod.invoke(getCrudMapper(), record);
			return 1;
		} catch (IllegalAccessException | IllegalArgumentException
				| InvocationTargetException e) {
			throw new MyCollabException(e);
		}
	}

	private void findCacheUpdateMethod() {
		ICrudGenericDAO<K, T> crudMapper = getCrudMapper();
		Class<? extends ICrudGenericDAO> crudMapperCls = crudMapper.getClass();
		for (Method method : crudMapperCls.getMethods()) {
			if ("updateByPrimaryKeyWithBLOBs".equals(method.getName())) {
				cacheUpdateMethod = method;
				return;
			} else if ("updateByPrimaryKey".equals(method.getName())) {
				cacheUpdateMethod = method;
			}
		}
	}

	public int updateSelectiveWithSession(@CacheKey T record,
			String username) {
		try {
			PropertyUtils.setProperty(record, "lastupdatedtime",
					new GregorianCalendar().getTime());
		} catch (Exception e) {
		}
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
	public void massRemoveWithSession(List<K> primaryKeys, String username,
			int accountId) {
		getCrudMapper().removeKeysWithSession(primaryKeys);
	}

	@Override
	public void massUpdateWithSession(T record, List<K> primaryKeys,
			int accountId) {
		getCrudMapper().massUpdateWithSession(record, primaryKeys);
	}
}

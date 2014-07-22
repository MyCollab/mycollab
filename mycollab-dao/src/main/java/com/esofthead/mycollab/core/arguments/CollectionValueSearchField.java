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
package com.esofthead.mycollab.core.arguments;

import java.util.Collection;

/**
 * 
 * @author MyCollab Ltd.
 * @since 4.0
 * 
 */
public class CollectionValueSearchField extends SearchField {
	private static final long serialVersionUID = 1L;

	private String queryCount;

	private String querySelect;

	private Collection<?> value;

	public CollectionValueSearchField(String oper, String expression,
			Collection<?> value) {
		this.operation = oper;
		this.queryCount = expression;
		this.querySelect = expression;
		this.value = value;
	}

	public String getQueryCount() {
		return queryCount;
	}

	public void setQueryCount(String queryCount) {
		this.queryCount = queryCount;
	}

	public String getQuerySelect() {
		return querySelect;
	}

	public void setQuerySelect(String querySelect) {
		this.querySelect = querySelect;
	}

	public Collection<?> getValue() {
		return value;
	}

	public void setValue(Collection<?> value) {
		this.value = value;
	}
}

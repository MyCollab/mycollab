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

import com.esofthead.mycollab.core.utils.BeanUtility;

/**
 * 
 * @author MyCollab Ltd.
 * @since 1.0
 */
public class BitSearchField extends NumberSearchField {
	private static final long serialVersionUID = 1L;

	public static final BitSearchField TRUE = new BitSearchField(AND, 1);
	public static final BitSearchField FALSE = new BitSearchField(AND, 0);

	public BitSearchField() {
		this(AND, 0);
	}

	public BitSearchField(Number value) {
		this(SearchField.AND, value, EQUAL);
	}

	public BitSearchField(String oper, Number value) {
		this(oper, value, EQUAL);
	}

	public BitSearchField(Number value, String compareOperator) {
		this(SearchField.AND, value, compareOperator);
	}

	public BitSearchField(String oper, Number value, String compareOperator) {
		super(oper, value, compareOperator);
	}

	public String toString() {
		return BeanUtility.printBeanObj(this);
	}
}

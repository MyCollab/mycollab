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
package com.mycollab.db.arguments;

import com.mycollab.core.utils.BeanUtility;

import java.io.Serializable;

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
public class SearchField implements Serializable {
    private static final long serialVersionUID = 1L;

    public static final String OR = "OR";
    public static final String AND = "AND";

    protected String operation = AND;

    public SearchField() {
    }

    public SearchField(String operation) {
        this.operation = operation;
    }

    public String getOperation() {
        return operation;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }

    @Override
    public String toString() {
        return BeanUtility.printBeanObj(this);
    }
}

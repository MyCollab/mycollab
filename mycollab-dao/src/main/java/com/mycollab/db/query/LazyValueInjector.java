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
package com.mycollab.db.query;

import java.util.Collection;

/**
 * @author MyCollab Ltd
 * @since 5.3.1
 */
public abstract class LazyValueInjector implements VariableInjector {
    private boolean isEval = false;
    private boolean isArray = false;
    private boolean isCollection = false;
    private Class type;

    @Override
    public Object eval() {
        Object value = doEval();
        isEval = true;
        type = (type != null) ? type : value.getClass();

        if (this.type.isArray()) {
            isArray = true;
        } else if (Collection.class.isAssignableFrom(this.type)) {
            isCollection = true;
        }
        return value;
    }

    abstract protected Object doEval();

    @Override
    public Class getType() {
        return type;
    }

    @Override
    public boolean isArray() {
        return isArray;
    }

    @Override
    public boolean isCollection() {
        return isCollection;
    }
}

/**
 * Copyright © MyCollab
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.mycollab.db.query;

import java.lang.reflect.Array;
import java.util.Collection;

/**
 * @author MyCollab Ltd
 * @since 5.2.1
 */
public class ConstantValueInjector implements VariableInjector {
    private Object value;
    private boolean isArray = false;
    private boolean isCollection = false;
    private Class type;

    private ConstantValueInjector(Object value) {
        this(null, value);
    }

    private ConstantValueInjector(Class type, Object value) {
        this.value = value;
        if (type != null) {
            this.type = type;
        } else {
           if (value instanceof Collection) {
               Collection collection = (Collection)value;
               if (collection.size() > 0) {
                   Object item = collection.iterator().next();
                   this.type = item.getClass();
               }
           } else if (value.getClass().isArray()) {
               if (Array.getLength(value) > 0) {
                   Object item = Array.get(value, 0);
                   this.type = item.getClass();
               }
           } else {
               this.type = value.getClass();
           }
        }

        if (value.getClass().isArray()) {
            isArray = true;
        } else if (Collection.class.isAssignableFrom(value.getClass())) {
            isCollection = true;
        }
    }

    public static ConstantValueInjector valueOf(Object value) {
        return new ConstantValueInjector(value);
    }

    public static ConstantValueInjector valueOf(Class type, Object value) {
        return new ConstantValueInjector(type, value);
    }

    @Override
    public Object eval() {
        return value;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    @Override
    public boolean isArray() {
        return isArray;
    }

    @Override
    public boolean isCollection() {
        return isCollection;
    }

    @Override
    public Class getType() {
        return type;
    }

    public void setType(Class type) {
        this.type = type;
    }
}

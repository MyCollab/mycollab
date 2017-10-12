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
package com.mycollab.db.arguments;

import com.mycollab.core.IgnoreException;
import com.mycollab.core.utils.ArrayUtils;
import org.apache.commons.collections.CollectionUtils;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * @param <T>
 * @author MyCollab Ltd.
 * @since 1.0
 */
public class SetSearchField<T> extends SearchField {
    private static final long serialVersionUID = 1L;
    private Set<T> values = new HashSet<>();

    public SetSearchField() {
    }

    public SetSearchField(T... vals) {
        if (ArrayUtils.isNotEmpty(vals)) {
            CollectionUtils.addAll(values, vals);
        }

        this.setOperation(SearchField.AND);
    }

    public SetSearchField(String oper, Collection<T> vals) {
        super(oper);
        values.addAll(vals);
    }

    public SetSearchField(Collection<T> items) {
        if (CollectionUtils.isNotEmpty(items)) {
            values.addAll(items);
        }
        this.setOperation(SearchField.AND);
    }

    public Set<T> getValues() {
        if (values == null || values.size() == 0) {
            throw new IgnoreException("You must select one option");
        }
        return values;
    }

    public void setValues(Set<T> values) {
        this.values = values;
    }

    public void addValue(T value) {
        values.add(value);
    }

    public void removeValue(T value) {
        values.remove(value);
    }
}

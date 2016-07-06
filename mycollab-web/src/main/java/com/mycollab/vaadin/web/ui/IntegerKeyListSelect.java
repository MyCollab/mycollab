/**
 * This file is part of mycollab-web.
 *
 * mycollab-web is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * mycollab-web is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with mycollab-web.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.mycollab.vaadin.web.ui;

import com.vaadin.data.Property;
import com.vaadin.ui.ListSelect;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

/**
 * @author MyCollab Ltd
 * @since 5.3.1
 */
public class IntegerKeyListSelect extends ListSelect {
    @Override
    public void setValue(Object newValue) throws Property.ReadOnlyException {
        if (newValue != null) {
            Class valueCls = newValue.getClass();
            ArrayList<Integer> wrappedList = new ArrayList<>();
            if (Collection.class.isAssignableFrom(valueCls)) {
                Iterator iter = ((Collection) newValue).iterator();
                while (iter.hasNext()) {
                    try {
                        Integer value = Integer.parseInt(iter.next().toString());
                        wrappedList.add(value);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                super.setValue(wrappedList);
            } else if (valueCls.isArray()) {
                int length = Array.getLength(newValue);
                for (int i = 0; i < length; i++) {
                    try {
                        Integer value = Integer.parseInt(Array.get(newValue, i).toString());
                        wrappedList.add(value);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                super.setValue(wrappedList);
            } else {
                super.setValue(newValue);
                return;
            }
        } else {
            super.setValue(null);
        }
    }
}

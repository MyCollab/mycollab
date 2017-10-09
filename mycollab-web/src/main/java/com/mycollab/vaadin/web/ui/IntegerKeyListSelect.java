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
                for (Object o : ((Collection) newValue)) {
                    try {
                        Integer value = Integer.parseInt(o.toString());
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
            }
        } else {
            super.setValue(null);
        }
    }
}

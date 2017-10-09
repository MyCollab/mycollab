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

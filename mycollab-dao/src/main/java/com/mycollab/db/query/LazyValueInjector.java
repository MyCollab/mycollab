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

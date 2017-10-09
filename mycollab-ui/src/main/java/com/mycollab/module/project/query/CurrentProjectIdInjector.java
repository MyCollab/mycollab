package com.mycollab.module.project.query;

import com.mycollab.db.query.VariableInjector;
import com.mycollab.module.project.CurrentProjectVariables;

import java.util.Collection;
import java.util.Collections;

/**
 * @author MyCollab Ltd
 * @since 5.2.1
 */
public class CurrentProjectIdInjector implements VariableInjector<Integer> {
    @Override
    public Collection<Integer> eval() {
        return Collections.singletonList(CurrentProjectVariables.getProjectId());
    }

    @Override
    public Class<Integer> getType() {
        return Integer.class;
    }

    @Override
    public boolean isArray() {
        return false;
    }

    @Override
    public boolean isCollection() {
        return true;
    }
}

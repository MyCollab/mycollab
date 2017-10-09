package com.mycollab.common.domain.criteria;

import com.mycollab.db.arguments.SearchCriteria;
import com.mycollab.db.arguments.SetSearchField;
import com.mycollab.db.arguments.StringSearchField;

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
public class ActivityStreamSearchCriteria extends SearchCriteria {
    private static final long serialVersionUID = 1L;

    private SetSearchField<String> moduleSet;
    private SetSearchField<Integer> extraTypeIds;
    private StringSearchField createdUser;
    private SetSearchField<String> types;

    public StringSearchField getCreatedUser() {
        return createdUser;
    }

    public void setCreatedUser(StringSearchField createdUser) {
        this.createdUser = createdUser;
    }

    public SetSearchField<String> getModuleSet() {
        return moduleSet;
    }

    public void setModuleSet(SetSearchField<String> moduleSet) {
        this.moduleSet = moduleSet;
    }

    public SetSearchField<Integer> getExtraTypeIds() {
        return extraTypeIds;
    }

    public void setExtraTypeIds(SetSearchField<Integer> extraTypeIds) {
        this.extraTypeIds = extraTypeIds;
    }

    public SetSearchField<String> getTypes() {
        return types;
    }

    public void setTypes(SetSearchField<String> types) {
        this.types = types;
    }
}

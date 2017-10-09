package com.mycollab.common.domain.criteria;

import com.mycollab.db.arguments.SearchCriteria;
import com.mycollab.db.arguments.SetSearchField;
import com.mycollab.db.arguments.StringSearchField;

/**
 * @author MyCollab Ltd
 * @since 5.2.2
 */
public class TimelineTrackingSearchCriteria extends SearchCriteria {

    private SetSearchField<String> types;
    private StringSearchField fieldgroup;
    private SetSearchField<Integer> extraTypeIds;

    public SetSearchField<String> getTypes() {
        return types;
    }

    public void setTypes(SetSearchField<String> types) {
        this.types = types;
    }

    public SetSearchField<Integer> getExtraTypeIds() {
        return extraTypeIds;
    }

    public void setExtraTypeIds(SetSearchField<Integer> extraTypeIds) {
        this.extraTypeIds = extraTypeIds;
    }

    public StringSearchField getFieldgroup() {
        return fieldgroup;
    }

    public void setFieldgroup(StringSearchField fieldgroup) {
        this.fieldgroup = fieldgroup;
    }
}

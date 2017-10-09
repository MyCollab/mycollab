package com.mycollab.common.domain.criteria;

import com.mycollab.db.arguments.SearchCriteria;
import com.mycollab.db.arguments.StringSearchField;

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
public class SaveSearchResultCriteria extends SearchCriteria {
    private static final long serialVersionUID = 1L;

    private StringSearchField type;

    private StringSearchField createUser;

    public StringSearchField getType() {
        return type;
    }

    public void setType(StringSearchField type) {
        this.type = type;
    }

    public StringSearchField getCreateUser() {
        return createUser;
    }

    public void setCreateUser(StringSearchField createUser) {
        this.createUser = createUser;
    }
}

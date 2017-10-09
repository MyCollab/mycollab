package com.mycollab.module.user.domain.criteria;

import com.mycollab.core.utils.DateTimeUtils;
import com.mycollab.db.arguments.*;

import java.util.Date;
import java.util.Locale;

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
public class UserSearchCriteria extends SearchCriteria {
    private static final long serialVersionUID = 1L;

    private StringSearchField displayName;
    private StringSearchField username;
    private SetSearchField<String> registerStatuses;
    private StringSearchField subdomain;
    private SetSearchField<String> statuses;
    private BooleanSearchField isAccountOwner;

    public StringSearchField getDisplayName() {
        return displayName;
    }

    public void setDisplayName(StringSearchField displayName) {
        this.displayName = displayName;
    }

    public StringSearchField getUsername() {
        return username;
    }

    public void setUsername(StringSearchField username) {
        this.username = username;
    }

    public SetSearchField<String> getRegisterStatuses() {
        return registerStatuses;
    }

    public void setRegisterStatuses(SetSearchField<String> registerStatuses) {
        this.registerStatuses = registerStatuses;
    }

    public StringSearchField getSubdomain() {
        return subdomain;
    }

    public void setSubdomain(StringSearchField subdomain) {
        this.subdomain = subdomain;
    }

    public SetSearchField<String> getStatuses() {
        return statuses;
    }

    public void setStatuses(SetSearchField<String> statuses) {
        this.statuses = statuses;
    }

    // @NOTE: Only works with method find... not getTotalCount(...)
    public void setLastAccessTimeRange(Date from, Date to) {
        String expr = String.format("s_user_account.lastAccessedTime >= '%s' AND s_user_account.lastAccessedTime <='%s'",
                        DateTimeUtils.formatDate(from, "yyyy-MM-dd", Locale.US),
                        DateTimeUtils.formatDate(to, "yyyy-MM-dd", Locale.US));
        NoValueSearchField searchField = new NoValueSearchField(SearchField.AND, expr);
        this.addExtraField(searchField);
    }
}

package com.mycollab.core;

/**
 * @author MyCollab Ltd
 * @since 5.1.3
 */
public abstract class AbstractNotification {
    private String type;
    private String scope;

    public static final String WARNING = "warning";
    public static final String NEWS = "news";

    public static final String SCOPE_GLOBAL = "global";
    public static final String SCOPE_USER = "user";

    public AbstractNotification(String scope, String type) {
        this.scope = scope;
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public String getScope() {
        return scope;
    }

    public boolean isGlobalScope() {
        return SCOPE_GLOBAL.equals(scope);
    }
}

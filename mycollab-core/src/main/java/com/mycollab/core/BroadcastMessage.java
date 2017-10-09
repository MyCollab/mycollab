package com.mycollab.core;

/**
 * @author MyCollab Ltd
 * @since 5.3.5
 */
public class BroadcastMessage {

    private Integer sAccountId;

    private String clientId;

    private Object wrapObj;

    public BroadcastMessage(Object wrapObj) {
        this(null, null, wrapObj);
    }

    public BroadcastMessage(Integer sAccountId, String clientId, Object wrapObj) {
        this.sAccountId = sAccountId;
        this.clientId = clientId;
        this.wrapObj = wrapObj;
    }

    public Integer getsAccountId() {
        return sAccountId;
    }

    public String getClientId() {
        return clientId;
    }

    public Object getWrapObj() {
        return wrapObj;
    }
}

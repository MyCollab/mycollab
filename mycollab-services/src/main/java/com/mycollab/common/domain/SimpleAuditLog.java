package com.mycollab.common.domain;

import com.mycollab.core.utils.JsonDeSerializer;
import com.mycollab.core.utils.StringUtils;
import com.fasterxml.jackson.core.type.TypeReference;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
public class SimpleAuditLog extends AuditLog {
    private static final long serialVersionUID = 1L;

    private List<AuditChangeItem> changeItems;

    private String postedUserFullName;

    private String postedUserAvatarId;

    public List<AuditChangeItem> getChangeItems() {
        if (changeItems == null) {
            changeItems = parseChangeItems();
        }
        if (changeItems == null) {
            changeItems = new ArrayList<>();
        }
        return changeItems;
    }

    public Date getCreatedtime() {
        return getPosteddate();
    }

    public String getPostedUserFullName() {
        if (StringUtils.isBlank(postedUserFullName)) {
            return StringUtils.extractNameFromEmail(getPosteduser());
        }
        return postedUserFullName;
    }

    public void setPostedUserFullName(String postedUserFullName) {
        this.postedUserFullName = postedUserFullName;
    }

    public String getPostedUserAvatarId() {
        return postedUserAvatarId;
    }

    public void setPostedUserAvatarId(String postedUserAvatarId) {
        this.postedUserAvatarId = postedUserAvatarId;
    }

    private List<AuditChangeItem> parseChangeItems() {
        return JsonDeSerializer.fromJson(
                this.getChangeset(), new TypeReference<List<AuditChangeItem>>() {
                });
    }
}

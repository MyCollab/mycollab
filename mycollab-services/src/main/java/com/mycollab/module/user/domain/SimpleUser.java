package com.mycollab.module.user.domain;

import com.mycollab.core.arguments.NotBindable;
import com.mycollab.core.utils.StringUtils;
import com.mycollab.security.PermissionMap;
import com.google.common.base.MoreObjects;

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
public class SimpleUser extends User {
    private static final long serialVersionUID = 1L;

    private Integer roleid;

    private String roleName;

    @NotBindable
    private PermissionMap permissionMaps;

    @NotBindable
    private Boolean isAccountOwner;

    private String subdomain;
    private Integer accountId;
    private String registerstatus;
    private String inviteUser;
    private String lastModuleVisit;
    private String inviteUserFullName;
    private String displayName;
    private String dateFormat;
    private String shortDateFormat;
    private String longDateFormat;
    private Boolean showEmailPublicly;

    @NotBindable
    private Boolean canSendEmail;

    public PermissionMap getPermissionMaps() {
        return permissionMaps;
    }

    public void setPermissionMaps(PermissionMap permissionMaps) {
        this.permissionMaps = permissionMaps;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public Integer getRoleid() {
        return roleid;
    }

    public void setRoleid(Integer roleid) {
        this.roleid = roleid;
    }

    public Boolean getIsAccountOwner() {
        return isAccountOwner;
    }

    public void setIsAccountOwner(Boolean isAccountOwner) {
        this.isAccountOwner = isAccountOwner;
    }

    public String getSubdomain() {
        return subdomain;
    }

    public void setSubdomain(String subdomain) {
        this.subdomain = subdomain;
    }

    public Integer getAccountId() {
        return accountId;
    }

    public void setAccountId(Integer accountId) {
        this.accountId = accountId;
    }

    public String getRegisterstatus() {
        return registerstatus;
    }

    public void setRegisterstatus(String registerstatus) {
        this.registerstatus = registerstatus;
    }

    public String getInviteUser() {
        return inviteUser;
    }

    public String getLastModuleVisit() {
        return lastModuleVisit;
    }

    public void setLastModuleVisit(String lastModuleVisit) {
        this.lastModuleVisit = lastModuleVisit;
    }

    public String getInviteUserFullName() {
        return inviteUserFullName;
    }

    public void setInviteUserFullName(String inviteUserFullName) {
        this.inviteUserFullName = inviteUserFullName;
    }

    public void setInviteUser(String inviteUser) {
        this.inviteUser = inviteUser;
    }

    public String getDateFormat() {
        return MoreObjects.firstNonNull(dateFormat, "MM/dd/yyyy");
    }

    public void setDateFormat(String dateFormat) {
        this.dateFormat = dateFormat;
    }

    public String getShortDateFormat() {
        return MoreObjects.firstNonNull(shortDateFormat, "MM/dd");
    }

    public void setShortDateFormat(String shortDateFormat) {
        this.shortDateFormat = shortDateFormat;
    }

    public String getLongDateFormat() {
        return MoreObjects.firstNonNull(longDateFormat, "E, dd MMM yyyy");
    }

    public void setLongDateFormat(String longDateFormat) {
        this.longDateFormat = longDateFormat;
    }

    public Boolean getShowEmailPublicly() {
        return showEmailPublicly;
    }

    public void setShowEmailPublicly(Boolean showEmailPublicly) {
        this.showEmailPublicly = showEmailPublicly;
    }

    public Boolean getCanSendEmail() {
        return canSendEmail;
    }

    public void setCanSendEmail(Boolean canSendEmail) {
        this.canSendEmail = canSendEmail;
    }

    public String getDisplayName() {
        if (StringUtils.isBlank(displayName)) {
            String result = getFirstname() + " " + getLastname();
            if (StringUtils.isBlank(result)) {
                String displayName = getUsername();
                return StringUtils.extractNameFromEmail(displayName);
            }
            return result;
        }
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public enum Field {
        displayName, roleName, roleid;

        public boolean equalTo(Object value) {
            return name().equals(value);
        }
    }
}
